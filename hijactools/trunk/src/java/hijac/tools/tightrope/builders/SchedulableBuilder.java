package hijac.tools.tightrope.builders;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;

import hijac.tools.analysis.SCJAnalysis;
import hijac.tools.tightrope.environments.AperiodicEventHandlerEnv;
import hijac.tools.tightrope.environments.EventHandlerEnv;
import hijac.tools.tightrope.environments.ManagedThreadEnv;
import hijac.tools.tightrope.environments.MethodEnv;
import hijac.tools.tightrope.environments.ObjectEnv;
import hijac.tools.tightrope.environments.ProgramEnv;
import hijac.tools.tightrope.environments.VariableEnv;
import hijac.tools.tightrope.utils.TightRopeTransUtils;
import hijac.tools.tightrope.visitors.MethodVisitor;

public class SchedulableBuilder extends ParadigmBuilder
{

  private ObjectEnv schedulableEnv;

  public SchedulableBuilder(SCJAnalysis analysis, ProgramEnv programEnv,
      ObjectEnv schedulableEnv, EnvironmentBuilder environmentBuilder)
  {
    super(analysis, programEnv, environmentBuilder);
    this.schedulableEnv = schedulableEnv;
  }

  @SuppressWarnings("unchecked")
  @Override
  public ArrayList<Name> build(TypeElement paradigmTypeElement)
  {

    ClassTree ct = analysis.TREES.getTree(paradigmTypeElement);

    getVariables(paradigmTypeElement, schedulableEnv);
    
    if (schedulableEnv instanceof AperiodicEventHandlerEnv)
    {
      String extendsClause = ct.getExtendsClause().toString();
      AperiodicEventHandlerEnv apehEnv = (AperiodicEventHandlerEnv) schedulableEnv;
      if (extendsClause.equals("AperiodicEventHandler"))
      {
        apehEnv.setHandlerType(AperiodicEventHandlerEnv.HandlerType.aperiodic);

      }
      else if (extendsClause.equals("AperiodicLongEventHandler"))
      {
        apehEnv.setHandlerType(AperiodicEventHandlerEnv.HandlerType.aperiodicLong);
      }
    }

    List<StatementTree> members = (List<StatementTree>) ct.getMembers();

    Iterator<StatementTree> i = members.iterator();

    while (i.hasNext())
    {
      Tree tlst = i.next();

      if (tlst instanceof MethodTree)
      {
        MethodTree mt = (MethodTree) tlst;
        MethodVisitor methodVisitor = new MethodVisitor(analysis, schedulableEnv);

        if (mt.getModifiers().getFlags().contains(Modifier.SYNCHRONIZED))
        {

          final String schedulableEnvName = schedulableEnv.getName().toString();
          schedulableEnv.setObjectId(schedulableEnvName);
          MethodEnv m = methodVisitor.visitMethod(mt, false);
          schedulableEnv.getClassEnv().addSyncMeth(m);
        }
        else if ((mt.getName().contentEquals("<init>")))
        {
          extractProcessParameters(mt, (ObjectEnv) schedulableEnv);
        }
        else
        {
          if ((mt.getName().contentEquals("run")))
          {
            ((ManagedThreadEnv) schedulableEnv).addRunMethod(methodVisitor.visitMethod(
                mt, false));
          }
          else if ((mt.getName().contentEquals("handleAsyncEvent")))
          {
            ((EventHandlerEnv) schedulableEnv).addHandleAsyncMethod(methodVisitor
                .visitMethod(mt, false));
          }
          else
          {
            schedulableEnv.addMeth(methodVisitor.visitMethod(mt, false));
          }
        }
      }
    }

    
    return null;
  }

  @Override
  public void addParents()
  {
    
  }

  protected void extractProcessParameters(MethodTree methodTree, ObjectEnv object)
  {
    for (VariableTree vt : methodTree.getParameters())
    {

      VariableEnv parameter = new VariableEnv();

      parameter.setName(vt.getName().toString());
      parameter.setType(TightRopeTransUtils.encodeType(vt.getType()));
      parameter.setProgramType(TightRopeTransUtils.encodeType(vt.getType()));

      final boolean ignoredParameter = parameter.getType().endsWith("Parameters")
          || parameter.getType().equals("String") || parameter.getType().endsWith("Time");

      if (!ignoredParameter)
      {
        object.addProcParameter(parameter);
      }

    }
  }

}
package hijac.tools.tightrope.builders;

import hijac.tools.analysis.SCJAnalysis;
import hijac.tools.application.TightRope;
import hijac.tools.tightrope.environments.MethodEnv;
import hijac.tools.tightrope.environments.NonParadigmEnv;
import hijac.tools.tightrope.environments.ObjectEnv;
import hijac.tools.tightrope.environments.ProgramEnv;
import hijac.tools.tightrope.environments.VariableEnv;
import hijac.tools.tightrope.utils.Debugger;
import hijac.tools.tightrope.utils.MethodDestinationE;
import hijac.tools.tightrope.utils.TightRopeString;
import hijac.tools.tightrope.utils.TightRopeTransUtils;
import hijac.tools.tightrope.visitors.MethodLocationVisitor;
import hijac.tools.tightrope.visitors.MethodVisitor;
import hijac.tools.tightrope.visitors.ReturnVisitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.PrimitiveTypeTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.Trees;

public class NonParadigmBuilder extends ParadigmBuilder
{
  


  public NonParadigmBuilder(SCJAnalysis analysis, ProgramEnv programEnv,
      EnvironmentBuilder environmentBuilder)
  {
    super(analysis, programEnv, environmentBuilder);
  }

  @Override
  public ArrayList<Name> build(TypeElement te)
  {
    Trees trees = analysis.TREES;

    NonParadigmEnv nonParaEnv = new NonParadigmEnv();
    nonParaEnv.setName(te.getSimpleName());

    MethodVisitor methodVisitor = new MethodVisitor(analysis, nonParaEnv);
    ClassTree ct = trees.getTree(te);

    HashMap<Name, Tree> varMap = getVariables(te, nonParaEnv);

    List<StatementTree> members = (List<StatementTree>) ct.getMembers();
    Iterator<StatementTree> i = members.iterator();

    while (i.hasNext())
    {
      Object obj = i.next();

      if (obj instanceof MethodTree)
      {
        MethodTree mt = (MethodTree) obj;

        Tree returnType = mt.getReturnType();

        final boolean isSyncMethod = mt.getModifiers().getFlags()
            .contains(Modifier.SYNCHRONIZED);

        if (returnType instanceof PrimitiveTypeTree)
        {
          ((PrimitiveTypeTree) mt.getReturnType()).getPrimitiveTypeKind();
        }
        // ArrayList<Name> returns =

        mt.accept(new ReturnVisitor(varMap), null);

        @SuppressWarnings("rawtypes")
        Map paramMap = new HashMap();
        for (VariableTree vt : mt.getParameters())
        {
          paramMap.put(vt.getName().toString(), vt.getType());
        }

        if (mt.getName().contentEquals("<init>"))
        {
          extractProcessParameters(mt, nonParaEnv);
        }
        else if (mt.getName().contentEquals("main"))
        {

        }
        else if (isSyncMethod)
        {
          nonParaEnv.setObjectId(nonParaEnv.getName().toString());
          MethodEnv m = methodVisitor.visitMethod(mt, false);
          setMethodAccess(m, mt);

          nonParaEnv.addSyncMeth(m);
        }
        else
        // if (notIgnoredMethod)
        {
          MethodEnv m = methodVisitor.visitMethod(mt, false);
          setMethodAccess(m, mt);
//          methodVisitor.setNonPBuilder(this);

          
          // TODO Needs some logic to check where to put it. Bufer is wrong,
//          Debugger.log("NonP Meth: " + m.getName() + " isCalledOutside="+isCalledOutside() +" and isAccessesVariable=" + isAccessesVariable());
          MethodDestinationE methodDestination = new MethodLocationVisitor(varMap).visitMethod(mt, null);
          
          Debugger.log("Non P Method: " + m.getName() + " destination is " + methodDestination );
          
          if (methodDestination == MethodDestinationE.PROCESS)
          {
            nonParaEnv.addMeth(m);
          }
          else if(methodDestination == MethodDestinationE.CLASS)
          {
            nonParaEnv.getClassEnv().addMeth(m);
          }
          
        }

      }
    }

    //
    nonParaEnv.setId(nonParaEnv.getName().toString() + TightRopeString.Name.ID_STR);
    // Debugger.log("Adding Non-P Obj: " + nonParaEnv.getName() + " with ID = "
    // + nonParaEnv.getId() );

    programEnv.addNonParadigmObjectEnv(nonParaEnv);

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
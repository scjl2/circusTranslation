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

import hijac.tools.analysis.SCJAnalysis;
import hijac.tools.tightrope.environments.AperiodicEventHandlerEnv;
import hijac.tools.tightrope.environments.EventHandlerEnv;
import hijac.tools.tightrope.environments.ManagedThreadEnv;
import hijac.tools.tightrope.environments.MethodEnv;
import hijac.tools.tightrope.environments.ObjectEnv;
import hijac.tools.tightrope.environments.ParadigmEnv;
import hijac.tools.tightrope.environments.ProgramEnv;
import hijac.tools.tightrope.visitors.MethodVisitor;

public class SchedulableObjectBuilder extends ParadigmBuilder
{

	private ParadigmEnv schedulableEnv;

	public SchedulableObjectBuilder(SCJAnalysis analysis,
			ProgramEnv programEnv, ParadigmEnv schedulableEnv,
			EnvironmentBuilder environmentBuilder)
	{
		super(analysis, programEnv, environmentBuilder);
		this.schedulableEnv = schedulableEnv;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Name> build(TypeElement paradigmTypeElement)
	{

		ClassTree ct = analysis.TREES.getTree(paradigmTypeElement);

		
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

				MethodVisitor methodVisitor = new MethodVisitor(analysis,
						schedulableEnv);
				if (mt.getModifiers().getFlags()
						.contains(Modifier.SYNCHRONIZED))
				{

					schedulableEnv.setObjectId(schedulableEnv.getName().toString());
					
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
						((ManagedThreadEnv) schedulableEnv)
								.addRunMethod(methodVisitor.visitMethod(mt,
										false));
					}
					else if ((mt.getName().contentEquals("handleAsyncEvent")))
					{
						((EventHandlerEnv) schedulableEnv)
								.addHandleAsyncMethod(methodVisitor
										.visitMethod(mt, false));
					}
					else
					{
						schedulableEnv.addMeth(methodVisitor.visitMethod(mt,
								false));
					}
				}
			}
		}
		
		getVariables(paradigmTypeElement, schedulableEnv);
		return null;
	}

	@Override
	public void addParents()
	{
		// TODO Auto-generated method stub
		
	}

}
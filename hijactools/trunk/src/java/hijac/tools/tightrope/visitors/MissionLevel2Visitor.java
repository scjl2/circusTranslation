package hijac.tools.tightrope.visitors;

import hijac.tools.analysis.SCJAnalysis;
import hijac.tools.tightrope.environments.MethodEnv;
import hijac.tools.tightrope.environments.MissionEnv;

import hijac.tools.tightrope.environments.ProgramEnv;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.ModifiersTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.Tree;

import com.sun.source.util.Trees;

public class MissionLevel2Visitor
{
	// private static ProgramEnv programEnv;
	private static SCJAnalysis analysis;
	private static RegistersVisitor registersVisitor;
	private static Trees trees;

	private MissionEnv missionEnv;

	public MissionLevel2Visitor(ProgramEnv programEnv, MissionEnv missionEnv,
			SCJAnalysis analysis)
	{
		MissionLevel2Visitor.analysis = analysis;
		// MissionLevel2Visitor.programEnv = programEnv;
		this.missionEnv = missionEnv;

		trees = analysis.TREES;
		analysis.getCompilationUnits();
		analysis.getTypeElements();

		registersVisitor = new RegistersVisitor(missionEnv, analysis);

	}

	@SuppressWarnings("unchecked")
	public ArrayList<Name> visitType(TypeElement arg0, Void arg1)
	{
		ArrayList<Name> schedulables = new ArrayList<Name>();
		ClassTree ct = trees.getTree(arg0);

		List<Tree> members = (List<Tree>) ct.getMembers();
		Iterator<Tree> i = members.iterator();
		while (i.hasNext())
		{
			System.out.println("Mission Visitor: I Iterator");

			Tree tlst = i.next();
			System.out.println("Mission Visistor: tlst = " + tlst.getKind());

			//

			// if (tlst instanceof VariableTree)
			// {
			// VariableTree vt = (VariableTree) tlst;
			// System.out.println("Mission Visitor: Variable Tree Found");
			// // System.out.println("-> " + vt.toString());
			// // System.out.println("-> Name:" + vt.getName());
			// // System.out.println("-> Type: " + vt.getType());
			//
			//
			// }

			if (tlst instanceof MethodTree)
			{
				// capture the method
				MethodTree mt = (MethodTree) tlst;
				final boolean notIgnoredMethod = !(mt.getName().contentEquals(
						"<init>") || mt.getName().contentEquals(
						"missionMemorySize"));
				final boolean syncMethod = mt.getModifiers().getFlags()
						.contains(Modifier.SYNCHRONIZED);

				if (mt.getName().contentEquals("initialize"))
				{
					System.out.println("Mission Visitor: J iterator");
					List<StatementTree> s = (List<StatementTree>) mt.getBody()
							.getStatements();

					Iterator<StatementTree> j = s.iterator();

					// iterate through the statements in the Init method
					while (j.hasNext())
					{
						StatementTree st = j.next();

						System.out.println("Mission Visistor: j = "
								+ st.getKind());

						// ArrayList<Name> name =
						Name name = st.accept(registersVisitor, null);

						if (name != null)
						{
							schedulables.add(name);
						}
					}
				}
				else
				{
					// ADD METHOD TO MISSION ENV
					MethodVisitor methodVisitor = new MethodVisitor(analysis,
							missionEnv);

					if (syncMethod)
					{

						// missionEnv.addSyncMeth(mt.getName(), typeKind,
						// returns,
						// paramMap);
						MethodEnv m = methodVisitor.visitMethod(mt, null);
						setMethodAccess(mt, m);
						missionEnv.addSyncMeth(m);
						
						System.out.println("/// method params =" + m.getParameters());
					}
					else
					{
						if (notIgnoredMethod)
						{
							MethodEnv m = methodVisitor.visitMethod(mt, null);
								
									
							setMethodAccess(mt, m);
							missionEnv.getClassEnv().addMeth(m);

							MethodEnv m2 = methodVisitor.visitMethod(mt, null);
							
							System.out.println("/// method params 2 =" + m2.getParameters());
							StringBuilder body;

							if (m2.getReturnType() == "null")
							{
								body = new StringBuilder();
							}
							else
							{
								body = new StringBuilder("ret := ");
							}

							StringBuilder parametersString = new StringBuilder();

							if (!m2.getParameters().isEmpty())
							{
								for (String s : m2.getParameters().keySet())
								{
									parametersString.append(s);
								}
							}

							body.append("this~.~");
							body.append(m2.getMethodName()); 
							body.append("(");
							body.append(parametersString.toString());
							body.append(")");

							m2.setBody(body);
							missionEnv.addMeth(m2);
						}
					}
				}
			}
		}
		return schedulables;

	}

	private void setMethodAccess(MethodTree mt, MethodEnv m)
	{
		ModifiersTree modTree = mt.getModifiers();
		Set<Modifier> flags = modTree.getFlags();

		// m.setSynchronised(flags.contains(Modifier.SYNCHRONIZED));

		if (flags.contains(Modifier.PUBLIC))
		{
			m.setAccess(MethodEnv.AccessMod.PUBLIC);
		}
		else if (flags.contains(Modifier.PRIVATE))
		{
			m.setAccess(MethodEnv.AccessMod.PRIVATE);
		}
		else if (flags.contains(Modifier.PROTECTED))
		{
			m.setAccess(MethodEnv.AccessMod.PROTECTED);
		}
	}

}

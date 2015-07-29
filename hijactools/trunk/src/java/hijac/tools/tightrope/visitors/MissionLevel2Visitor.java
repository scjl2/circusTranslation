package hijac.tools.tightrope.visitors;

import hijac.tools.analysis.SCJAnalysis;
import hijac.tools.tightrope.environments.MissionEnv;
import hijac.tools.tightrope.environments.ParadigmEnv;
import hijac.tools.tightrope.environments.ProgramEnv;

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
import com.sun.source.util.Trees;

public class MissionLevel2Visitor 
{
	private static ProgramEnv programEnv;
	private static SCJAnalysis analysis;
	private static RegistersVisitor registersVisitor;
	private static Trees trees;
	
	private MissionEnv missionEnv;	

	public MissionLevel2Visitor(ProgramEnv programEnv, MissionEnv missionEnv,
			SCJAnalysis analysis)
	{
		MissionLevel2Visitor.analysis = analysis;
		MissionLevel2Visitor.programEnv = programEnv;
		this.missionEnv = missionEnv;

		trees = analysis.TREES;
		analysis.getCompilationUnits();
		analysis.getTypeElements();

		registersVisitor = new RegistersVisitor(programEnv, analysis);

	}

	@SuppressWarnings("unchecked")
	public ArrayList<Name> visitType(TypeElement arg0, Void arg1)
	{
		// System.out.println(arg0);

		ArrayList<Name> schedulables = new ArrayList<Name>();
		ClassTree ct = trees.getTree(arg0);

		List<Tree> members = (List<Tree>) ct.getMembers();
		Iterator<Tree> i = members.iterator();
		while (i.hasNext())
		{
			System.out.println("Mission Visitor: I Iterator");

			Tree tlst = i.next();
			System.out.println("Mission Visistor: tlst = " + tlst.getKind());

			// Name name = tlst.accept(registersVisitor,null);
			//
			// if (name != null)
			// {
			// schedulables.add(name);
			// }
			//

			if (tlst instanceof VariableTree)
			{
				VariableTree vt = (VariableTree) tlst;
				System.out.println("Mission Visitor: Variable Tree Found");
				System.out.println("-> " + vt.toString());
				System.out.println("-> Name:" + vt.getName());
				System.out.println("-> Type: " + vt.getType());

				// TODO refactor this. Variables should be kept in the relevent
				// env
				programEnv.addVariable(vt.getName(), vt.getType());

			}

			if (tlst instanceof MethodTree)
			{
				//capture the method
				MethodTree mt = (MethodTree) tlst;
			

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
					MethodVisitor methodVisitor = new MethodVisitor(analysis, missionEnv);
					if (mt.getModifiers().getFlags()
							.contains(Modifier.SYNCHRONIZED))
					{

//						missionEnv.addSyncMeth(mt.getName(), typeKind, returns,
//								paramMap);
						missionEnv.addSyncMeth((methodVisitor.visitMethod(mt, null)));
					}
					else if(! (mt.getName().contentEquals("<init>") || mt.getName().contentEquals("missionMemorySize") ))
					{
//						missionEnv.addMeth(mt.getName(), typeKind, returns,
//								paramMap);
						missionEnv.addMeth((methodVisitor.visitMethod(mt, null)));
					}
				}
			}
		}
		return schedulables;

	}




}

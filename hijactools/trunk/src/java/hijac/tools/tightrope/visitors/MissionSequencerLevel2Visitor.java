package hijac.tools.tightrope.visitors;

import hijac.tools.analysis.SCJAnalysis;
import hijac.tools.tightrope.environments.MethodEnv;
import hijac.tools.tightrope.environments.ParadigmEnv;
import hijac.tools.tightrope.environments.ProgramEnv;
import hijac.tools.tightrope.generators.NewSCJApplication;
import java.util.ArrayList;
import java.util.HashMap;
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

public class MissionSequencerLevel2Visitor
{
	ProgramEnv programEnv;
	SCJAnalysis analysis;

	private Trees trees;
	private ReturnVisitor returnVisitor;
	private ParadigmEnv sequencerEnv;
	private HashMap<Name, Tree> varMap = new HashMap<Name, Tree>();
	private MethodVisitor methodVisitor; 

	public MissionSequencerLevel2Visitor(ProgramEnv programEnv,
			ParadigmEnv sequencerEnv, SCJAnalysis analysis)
	{
		this.analysis = analysis;
		this.programEnv = programEnv;
		this.sequencerEnv = sequencerEnv;

		trees = analysis.TREES;

		returnVisitor = new ReturnVisitor();
		methodVisitor = new MethodVisitor(analysis, sequencerEnv);

	}

	// TODO Tuning: have this method accept an empty ArrayList to fill
	public ArrayList<Name> visitType(TypeElement arg0, Void arg1)
	{
		System.out.println();
		System.out.println("+++ Mission Sequencer Variables +++");
		System.out.println();

		for (Name n : varMap.keySet())
		{
			System.out.println("+++ Variable " + n + " = " + varMap.get(n));
		}

		ArrayList<Name> missions = new ArrayList<Name>();

//		System.out.println("In MS Visitor for " + arg0);

		ClassTree ct = trees.getTree(arg0);
//		System.out.println("MS Visitor class tree: " + ct);

		@SuppressWarnings("unchecked")
		List<StatementTree> members = (List<StatementTree>) ct.getMembers();
//		System.out.println("MS Visitor members: " + members);

		Iterator<StatementTree> i = members.iterator();

		returnVisitor = new ReturnVisitor(varMap);
		MethodEnv m;
		while (i.hasNext())
		{
			m = null;
			Tree tlst = i.next();

			if (tlst instanceof VariableTree)
			{
				System.out.println("MS VIsitor: Variable Tree Found");

				// VariableTree vt = (VariableTree) tlst;
				//
				// System.out.println("-> " + vt.toString());
				// System.out.println("-> Name:" + vt.getName());
				// System.out.println("-> Type: " + vt.getType());
				//
				// programEnv.addVariable(vt.getName(), vt.getType());
			}

			if (tlst instanceof MethodTree)
			{

				MethodTree o = (MethodTree) tlst;

				Name methodName = o.getName();
				System.out.println("MS Visitor Method Tree = " + methodName);

				if (methodName.contentEquals("<init>"))
				{
					System.out.println("Release the Visitor!");

					o.accept(returnVisitor, false);

				} else
				{
					
					if (methodName.contentEquals("getNextMission"))
					{

						ArrayList<Name> getNextReturns = null;

						getNextReturns = o.accept(returnVisitor, false);

						if (getNextReturns != null)
						{
							missions.addAll(getNextReturns);
						}

						m = methodVisitor.visitMethod(o, null);

						sequencerEnv.addMeth(m);
					} else
					{// ADD METHOD TO MISSION ENV
						m = methodVisitor.visitMethod(o, null);

						if (o.getModifiers().getFlags()
								.contains(Modifier.SYNCHRONIZED))
						{
							sequencerEnv.addSyncMeth(m);
						} else if (!(methodName.contentEquals("<init>")))
						{
							sequencerEnv.addMeth(m);
						}
					}
				}
			}
		}

		System.out.println(" +++ MissionSequencerVissitor has "
				+ missions.size() + " missions +++");
		return missions;
	}
}
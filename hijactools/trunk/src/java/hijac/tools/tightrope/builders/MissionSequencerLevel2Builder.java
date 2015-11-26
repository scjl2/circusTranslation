package hijac.tools.tightrope.builders;

import hijac.tools.analysis.SCJAnalysis;
import hijac.tools.tightrope.environments.MethodEnv;
import hijac.tools.tightrope.environments.MissionSequencerEnv;
import hijac.tools.tightrope.environments.ObjectEnv;
import hijac.tools.tightrope.environments.ProgramEnv;
import hijac.tools.tightrope.environments.VariableEnv;
import hijac.tools.tightrope.visitors.MethodVisitor;
import hijac.tools.tightrope.visitors.ReturnVisitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.ModifiersTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.Trees;

public class MissionSequencerLevel2Builder extends ParadigmBuilder
{
	ProgramEnv programEnv;
	SCJAnalysis analysis;

	private Trees trees;
	private ReturnVisitor returnVisitor;
	private MissionSequencerEnv sequencerEnv;
	private List<VariableEnv> varMap ;
	private MethodVisitor methodVisitor;

	public MissionSequencerLevel2Builder(ProgramEnv programEnv,
			MissionSequencerEnv sequencerEnv, SCJAnalysis analysis, EnvironmentBuilder environmentBuilder)
	{
		super(analysis, programEnv, environmentBuilder);
		
		this.analysis = analysis;
		this.programEnv = programEnv;
		this.sequencerEnv = sequencerEnv;

		trees = analysis.TREES;
		
		
		methodVisitor = new MethodVisitor(analysis, sequencerEnv);

	}
	
//	public void setVarMap(Map<Name, Tree> varMap)
//	{
//		this.varMap = varMap;
//		returnVisitor = new ReturnVisitor(varMap);
//	}

	// TODO Tuning: have this method accept an empty ArrayList to fill
	public ArrayList<String> build(TypeElement arg0)
	{
		System.out.println();
		System.out.println("+++ Mission Sequencer Variables +++");
		System.out.println();

		assert(sequencerEnv != null);
		
		varMap = getVariables(arg0, sequencerEnv);
		
//		for (Name n : varMap.keySet())
//		{
//			System.out.println("+++ Variable " + n + " = " + varMap.get(n));
//		}

		ArrayList<String> missions = new ArrayList<String>();

		// System.out.println("In MS Visitor for " + arg0);

		ClassTree ct = trees.getTree(arg0);
		// System.out.println("MS Visitor class tree: " + ct);

		@SuppressWarnings("unchecked")
		List<StatementTree> members = (List<StatementTree>) ct.getMembers();
		// System.out.println("MS Visitor members: " + members);

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

				 VariableTree vt = (VariableTree) tlst;
//				//
				 System.out.println("-> " + vt.toString());
				 System.out.println("-> Name:" + vt.getName());
				 System.out.println("-> Type: " + vt.getType());
//				//
				 
				 
				 
//				 programEnv.addVariable(vt.getName(), vt.getType());
			}

			if (tlst instanceof MethodTree)
			{

				MethodTree o = (MethodTree) tlst;

				Name methodName = o.getName();
				System.out.println("MS Visitor Method Tree = " + methodName);

				if (methodName.contentEquals("<init>"))
				{
					System.out.println("Release the Visitor!");

					methodVisitor.visitMethod(o, true);
					
					o.accept(returnVisitor, false);

				}
				else
				{

					final boolean isGetNextMissionMethod = methodName
							.contentEquals("getNextMission");

					final boolean notIgnoredMethod = !(methodName
							.contentEquals("<init>"));
					
					if (isGetNextMissionMethod)
					{

						
						missions = o.accept(returnVisitor, false);

						

						m = methodVisitor.visitMethod(o, false);
						setMethodAccess(m, o);

						sequencerEnv.getClassEnv().addMeth(m);
					}
					else
					{// ADD METHOD TO MISSION ENV
						m = methodVisitor.visitMethod(o, false);
						setMethodAccess(m, o);

						final boolean isSyncMethod = o.getModifiers()
								.getFlags().contains(Modifier.SYNCHRONIZED);
						if (isSyncMethod)
						{
							sequencerEnv.addSyncMeth(m);
						}
						else
						{
							if (notIgnoredMethod)
							{
								sequencerEnv.addMeth(m);
							}
						}
					}
				}
			}
		}

		return missions;
	}

	private void setMethodAccess(MethodEnv m, MethodTree o)
	{
		ModifiersTree modTree = o.getModifiers();
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
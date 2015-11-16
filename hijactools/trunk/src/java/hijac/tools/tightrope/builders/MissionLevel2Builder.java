package hijac.tools.tightrope.builders;

import hijac.tools.analysis.SCJAnalysis;
import hijac.tools.tightrope.environments.MethodEnv;
import hijac.tools.tightrope.environments.MissionEnv;
import hijac.tools.tightrope.environments.ProgramEnv;
import hijac.tools.tightrope.visitors.MethodVisitor;
import hijac.tools.tightrope.visitors.RegistersVisitor;
import hijac.tools.tightrope.visitors.VariableVisitor;

import java.util.ArrayList;
import java.util.HashMap;
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

public class MissionLevel2Builder extends ParadigmBuilder
{
	// private static ProgramEnv programEnv;
	private static SCJAnalysis analysis;
	private static RegistersVisitor registersVisitor;
	private static Trees trees;

	private MissionEnv missionEnv;
	private ArrayList<Name> schedulables;
	private ProgramEnv programEnv;

	public MissionLevel2Builder(ProgramEnv programEnv, MissionEnv missionEnv,
			SCJAnalysis analysis, EnvironmentBuilder environmentBuilder)
	{
		super(analysis, programEnv, environmentBuilder);
		MissionLevel2Builder.analysis = analysis;
		this.programEnv = programEnv;
		this.missionEnv = missionEnv;

		trees = analysis.TREES;
		analysis.getCompilationUnits();
		analysis.getTypeElements();

		schedulables = new ArrayList<Name>();
		registersVisitor = new RegistersVisitor(missionEnv, analysis);

	}

	@SuppressWarnings("unchecked")
	public ArrayList<Name> build(TypeElement missionTypeElement)
	{
		ClassTree missionClassTree = trees.getTree(missionTypeElement);

		getVariables(missionTypeElement, missionEnv);

		List<Tree> missionMembers = (List<Tree>) missionClassTree.getMembers();
		Iterator<Tree> missionMembersIterator = missionMembers.iterator();

		while (missionMembersIterator.hasNext())
		{
			System.out.println("Mission Visitor: Mission Members Iterator");

			Tree missionMemberTree = missionMembersIterator.next();
			System.out.println("Mission Visistor: mission member tree = "
					+ missionMemberTree.getKind());

			// getParameters(missionMemberTree);

			// //TODO This doesn't work
			// if (missionMemberTree instanceof VariableTree )
			// {
			// VariableTree vt = (VariableTree) missionMemberTree;

			// programEnv.getObjectEnv(vt.getName())

			// }

			// else
			if (missionMemberTree instanceof MethodTree)
			{
				// capture the method
				MethodTree missionMethodTree = (MethodTree) missionMemberTree;

				final boolean notIgnoredMethod = !(missionMethodTree.getName()
						.contentEquals("<init>") || missionMethodTree.getName()
						.contentEquals("missionMemorySize"));
				final boolean syncMethod = missionMethodTree.getModifiers()
						.getFlags().contains(Modifier.SYNCHRONIZED);

				final boolean currentMethodIsInitialize = missionMethodTree
						.getName().contentEquals("initialize");

				if (currentMethodIsInitialize)
				{
					buildMissionInitialise(missionMethodTree);
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
						MethodEnv m = methodVisitor.visitMethod(
								missionMethodTree, false);
						setMethodAccess(missionMethodTree, m);
						missionEnv.addSyncMeth(m);

						System.out.println("/// method params ="
								+ m.getParameters());
					}
					else
					{
						if (notIgnoredMethod)
						{
							MethodEnv m = methodVisitor.visitMethod(
									missionMethodTree, false);

							setMethodAccess(missionMethodTree, m);
							missionEnv.getClassEnv().addMeth(m);

							MethodEnv m2 = methodVisitor.visitMethod(
									missionMethodTree, false);

							System.out.println("/// method params 2 ="
									+ m2.getParameters());
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

	private void buildMissionInitialise(MethodTree missionMethodTree)
	{
		{
			System.out.println("+++ Mission Initialise +++");

			System.out.println("Mission Visitor: methodStatementIterator");
			List<StatementTree> methodStatements = (List<StatementTree>) missionMethodTree
					.getBody().getStatements();

			Iterator<StatementTree> methodStatementsIterator = methodStatements
					.iterator();

			VariableVisitor varVisitor = new VariableVisitor(programEnv,
					missionEnv);

			HashMap<Name, Tree> varMap = new HashMap<Name, Tree>();
			StatementTree methodStatementTree;

			while (methodStatementsIterator.hasNext())
			{
				methodStatementTree = methodStatementsIterator.next();

				//TODO Should only add the right trees I suppose..
				environmentBuilder.addDeferredParam(methodStatementTree);
//				getParameters(methodStatementTree);

				HashMap<Name, Tree> m = (HashMap<Name, Tree>) methodStatementTree
						.accept(varVisitor, false);
				// assert (m != null);
				if (m != null)
				{

					// TODO this is a bit of a hack...

					for (Name n : m.keySet())
					{
						// System.out.println("\t*** Name = " + n +
						// " Type = "
						// + m.get(n) + " Kind = " +
						// m.get(n).getKind());
						varMap.putIfAbsent(n, m.get(n));
					}
				}

			}
			methodStatementsIterator = methodStatements.iterator();

			while (methodStatementsIterator.hasNext())
			{
				methodStatementTree = methodStatementsIterator.next();
				findSchedulables(schedulables, methodStatementTree, varMap);
			}
		}
	}

	private void findSchedulables(ArrayList<Name> schedulables,
			StatementTree methodStatementTree, HashMap<Name, Tree> varMap)
	{

		System.out.println("Mission Visistor: methodStatementTree = "
				+ methodStatementTree.getKind());

		Name schedulableName = methodStatementTree.accept(new RegistersVisitor(
				missionEnv, analysis, varMap), null);

		if (schedulableName != null)
		{
			schedulables.add(schedulableName);
		}
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

//	private void getParameters( Tree tree)
//	{
//		System.out.println("*** Get Params *** ");
//
//		List<? extends ExpressionTree> args = new ArrayList();
//
////		ObjectEnv objectWithParams = null;
//
//		if (tree instanceof VariableTree)
//		{
//			System.out.println("Tree: " + tree + " instance of VairableTree ");
//
//			ExpressionTree et = ((VariableTree) tree).getInitializer();
//			if (et instanceof NewClassTree)
//			{
//				args = ((NewClassTree) et).getArguments();
//			}
//
//			System.out.println("trying to get objectEnv for " + ((VariableTree) tree)
//					.getType().toString());
////			objectWithParams = programEnv.getObjectEnv(((VariableTree) tree)
////					.getType().toString());
//
//		}
//		else if (tree instanceof NewClassTree)
//		{
//			System.out.println("Tree: " + tree + " instance of NewClassTree ");
//
//			args = ((NewClassTree) tree).getArguments();
//
//			ExpressionTree identifier = ((NewClassTree) tree).getIdentifier();
//
//			System.out.println("trying to get objectEnv for " + identifier);
////			objectWithParams = programEnv.getObjectEnv(identifier.toString());
//
//		}
//
//		System.out.println("args = " + args.toString());
//
//		if (!args.isEmpty())
//		{
//			ParametersVisitor paramVisitor = new ParametersVisitor(programEnv,
//					missionEnv, null);
//
//			List<VariableEnv> params = new ArrayList();
//
//			for (ExpressionTree et : args)
//			{
//				System.out.println("visiting " + et.toString());
//
//				VariableEnv returns = et.accept(paramVisitor, null);
//
//				if (returns != null)
//				{
//					System.out
//							.println("returns = " + returns.getVariableName());
//					if (objectWithParams != null)
//					{
//						objectWithParams.addParameter(returns);
//					}
//					else
//					{
//						System.out.println("objectWithParams was null");
//					}
//				}
//				else
//				{
//					System.out.println("returns = null");
//				}
//			}
//
//		}
//
//		// if (tree instanceof NewClassTree)
//		// {
//		// List<? extends ExpressionTree> args = ((NewClassTree) tree)
//		// .getArguments();
//		//
//		// for (ExpressionTree et : args)
//		// {
//		// System.out.println(et);
//		// VariableEnv varEnv = new VariableEnv();
//		//
//		// params = et.accept(paramVisitor, null);
//		//
//		// }
//		//
//		// }
//		//
//		// // System.out.println("/*/* Params = " + params);
//		//
//		// if (params != null)
//		// {
//		// for (VariableEnv v : params)
//		// {
//		// missionEnv.addParameter(v);
//		// System.out.println("/*/* Param for "
//		// + missionEnv.getName().toString() + " is "
//		// + v.getVariableName());
//		// }
//		// }
//	}
}

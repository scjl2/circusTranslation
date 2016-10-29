package hijac.tools.tightrope.builders;

import hijac.tools.analysis.SCJAnalysis;
import hijac.tools.tightrope.environments.MethodEnv;
import hijac.tools.tightrope.environments.MissionEnv;
import hijac.tools.tightrope.environments.ObjectEnv;
import hijac.tools.tightrope.environments.ProgramEnv;
import hijac.tools.tightrope.environments.VariableEnv;
import hijac.tools.tightrope.utils.Debugger;
import hijac.tools.tightrope.utils.TightRopeTransUtils;
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
import com.sun.source.tree.VariableTree;
import com.sun.source.util.Trees;

public class MissionBuilder extends ParadigmBuilder
{
	private static SCJAnalysis analysis;

	private static Trees trees;

	private MissionEnv missionEnv;
	private ArrayList<Name> schedulables;
	private ProgramEnv programEnv;


	public MissionBuilder(ProgramEnv programEnv, MissionEnv missionEnv,
			SCJAnalysis analysis, EnvironmentBuilder environmentBuilder)
	{
		super(analysis, programEnv, environmentBuilder);
		MissionBuilder.analysis = analysis;
		this.programEnv = programEnv;
		this.missionEnv = missionEnv;

		trees = analysis.TREES;
		analysis.getCompilationUnits();
		analysis.getTypeElements();

		schedulables = new ArrayList<Name>();

	}

	@SuppressWarnings("unchecked")
	public ArrayList<Name> build(TypeElement missionTypeElement)
	{
		Debugger.log("missionTypeElem="+missionTypeElement.toString());
		ClassTree missionClassTree = trees.getTree(missionTypeElement);

		getVariables(missionTypeElement, missionEnv);

		List<Tree> missionMembers = (List<Tree>) missionClassTree.getMembers();
		Iterator<Tree> missionMembersIterator = missionMembers.iterator();
		
		MethodVisitor methodVisitor = new MethodVisitor(analysis, missionEnv, IDType.MissionID);

		while (missionMembersIterator.hasNext())
		{
			Debugger.log("Mission Visitor: Mission Members Iterator");

			Tree missionMemberTree = missionMembersIterator.next();
			Debugger.log("Mission Visistor: mission member tree = "
					+ missionMemberTree.getKind());


			if (missionMemberTree instanceof MethodTree)
			{
				// capture the method
				MethodTree missionMethodTree = (MethodTree) missionMemberTree;

				final boolean notIgnoredMethod = !(missionMethodTree.getName()
						.contentEquals("<init>") || missionMethodTree.getName()
						.contentEquals("missionMemorySize"));
				final boolean syncMethod = missionMethodTree.getModifiers().getFlags()
						.contains(Modifier.SYNCHRONIZED);

				final boolean currentMethodIsInitialize = missionMethodTree.getName()
						.contentEquals("initialize");

				final boolean currentMethodIsConstructor = missionMethodTree.getName()
						.contentEquals("<init>");

				if (currentMethodIsConstructor)
				{
					
					extractProcessParameters(missionMethodTree, missionEnv);
				}
				else if (currentMethodIsInitialize)
				{
					buildMissionInitialise(missionMethodTree);
				}
				else
				{
					// ADD METHOD TO MISSION ENV
					

					if (syncMethod)
					{	
						final String missionName = missionEnv.getName().toString();
						
						missionEnv.setObjectId(missionName);
						MethodEnv m = methodVisitor.visitMethod(missionMethodTree, false);
															
						setMethodAccess(missionMethodTree, m);
						missionEnv.addSyncMeth(m);
					
						
//						programEnv.addObjectIdName(missionName);

						Debugger.log("/// method params =" + m.getParameters());
					}
					else
					{
						if (notIgnoredMethod)
						{
							MethodEnv m = methodVisitor.visitMethod(missionMethodTree,
									false);

							
							
							setMethodAccess(missionMethodTree, m);
						
							
							missionEnv.getClassEnv().addMeth(m);

							MethodEnv m2 = methodVisitor.visitMethod(missionMethodTree,	false);

//						
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
							body.append(m2.getName());
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

	@SuppressWarnings("unchecked")
	private void buildMissionInitialise(MethodTree missionMethodTree)
	{
		{
			Debugger.log("+++ Mission Initialise +++");

			Debugger.log("Mission Visitor: methodStatementIterator");
			List<StatementTree> methodStatements = (List<StatementTree>) missionMethodTree
					.getBody().getStatements();

			Iterator<StatementTree> methodStatementsIterator = methodStatements
					.iterator();

			VariableVisitor varVisitor = new VariableVisitor(programEnv, missionEnv);

			HashMap<Name, Tree> varMap = new HashMap<Name, Tree>();
			StatementTree methodStatementTree;

			while (methodStatementsIterator.hasNext())
			{
				methodStatementTree = methodStatementsIterator.next();

				HashMap<Name, Tree> m = (HashMap<Name, Tree>) methodStatementTree.accept(
						varVisitor, false);

				if (m != null)
				{

					// TODO this is a bit of a hack...

					for (Name n : m.keySet())
					{
					
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

			addDeferredParameters(methodStatements, varMap, missionEnv);
		}
	}

	private void findSchedulables(ArrayList<Name> schedulables,
			StatementTree methodStatementTree, HashMap<Name, Tree> varMap)
	{

		Debugger.log("Mission Visistor: methodStatementTree = "
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
			m.setAccess(MethodEnv.AccessModifier.PUBLIC);
		}
		else if (flags.contains(Modifier.PRIVATE))
		{
			m.setAccess(MethodEnv.AccessModifier.PRIVATE);
		}
		else if (flags.contains(Modifier.PROTECTED))
		{
			m.setAccess(MethodEnv.AccessModifier.PROTECTED);
		}
	}

	@Override
	public void addParents()
	{
		// TODO Auto-generated method stub
		
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
  				|| parameter.getType().equals("String");
  
  		if (!ignoredParameter)
  		{
  			object.addProcParameter(parameter);
  		}
  
  	}
  }	
}

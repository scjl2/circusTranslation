package hijac.tools.tightrope.builders;

import hijac.tools.analysis.SCJAnalysis;
import hijac.tools.compiler.SCJCompilationTask;
import hijac.tools.tightrope.environments.AperiodicEventHandlerEnv;
import hijac.tools.tightrope.environments.ClassEnv;
import hijac.tools.tightrope.environments.ManagedThreadEnv;
import hijac.tools.tightrope.environments.MethodEnv;
import hijac.tools.tightrope.environments.MissionEnv;
import hijac.tools.tightrope.environments.NestedMissionSequencerEnv;
import hijac.tools.tightrope.environments.ObjectEnv;
import hijac.tools.tightrope.environments.OneShotEventHandlerEnv;
import hijac.tools.tightrope.environments.ParadigmEnv;
import hijac.tools.tightrope.environments.PeriodicEventHandlerEnv;
import hijac.tools.tightrope.environments.ProgramEnv;
import hijac.tools.tightrope.environments.SafeletEnv;
import hijac.tools.tightrope.environments.SchedulableTypeE;
import hijac.tools.tightrope.environments.TopLevelMissionSequencerEnv;
import hijac.tools.tightrope.environments.VariableEnv;
import hijac.tools.tightrope.utils.Debugger;
import hijac.tools.tightrope.utils.TightRopeString;
import hijac.tools.tightrope.visitors.ParametersVisitor;
import hijac.tools.tightrope.visitors.VariableVisitor;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.ExpressionStatementTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;

public class EnvironmentBuilder
{
	private static final String FINDING_PROCESS_PARAMETERS = "+++ Finding Process Parameters +++";

	private static final String BUILDING_TOP_LEVEL_SEQUENCER = "+++ Building Top Level Sequencer +++";

	private static final String BUILD_MISSION = "+++ Build Mission: ";

	private static final String NO_MISSIONS = "+++ No Missions +++";

	private static final String BUILDING_SCHEDULABLE_MISSION_SEQUENCERS = "+++ Building Schedulable Mission Sequencers +++";

	private static final String BUILDING_SCHEDULABLE = "+++ Building Schedulable ";

	private static final String NO_SCHEDULABLES = "Has No Schedulables +++";

	private static final String CIRCNEW = "\\circnew ";

	private static final String CLASS = "Class";

	private static final String CLASS_BRACKETS = "Class()";

	private static final String CIRCREFTYPE = "\\circreftype ";

	private static final String THIS = "this";

	private static final String BUILDING_MISSION = "+++ Building Mission ";

	private static final String END_PLUSES = " +++";

	private static final String ONE_SHOT_EVENT_HANDLER_QUALIFIED_NAME = "javax.safetycritical.OneShotEventHandler";

	private static final String APERIODIC_EVENT_HANDLER_QUALIFIED_NAME = "javax.safetycritical.AperiodicEventHandler";

	private static final String PERIODIC_EVENT_HANDLER_QUALIFIED_NAME = "javax.safetycritical.PeriodicEventHandler";

	private static final String MISSION_SEQUENCER_QUALIFIED_NAME = "javax.safetycritical.MissionSequencer";

	private static final String MANAGED_THREAD_QUALIFIED_NAME = "javax.safetycritical.ManagedThread";

	public static SCJAnalysis analysis;

	private List<DeferredParamter> deferredParamsList;

	public class DeferredParamter
	{
		Tree tree;
		String originClass;
		public HashMap<Name, Tree> varMap;

		public String toString()
		{
			return "Origin: " + originClass + " Tree:" + tree;
		}
	}

	// private Trees trees;
	// private Set<CompilationUnitTree> units;
	private Set<TypeElement> type_elements;
	private Elements elems;

	private ProgramEnv programEnv;

	private String packagePrefix;

	Map<String, List<String>> classTypeMap;

	Map<String, List<String>> classMethodsMap;

	private String programName;

	public EnvironmentBuilder(SCJAnalysis analysis)
	{
		EnvironmentBuilder.analysis = analysis;
		this.programEnv = new ProgramEnv(analysis);

		// trees = analysis.TREES;
		// units = analysis.getCompilationUnits();
		type_elements = analysis.getTypeElements();
		elems = analysis.ELEMENTS;
		deferredParamsList = new ArrayList<DeferredParamter>();
		classTypeMap = new HashMap<String, List<String>>();
		classMethodsMap = new HashMap<String, List<String>>();

	}

	public ProgramEnv getProgramEnv()
	{
		return programEnv;
	}

	public SchedulableTypeE getSchedulableType(Name s)
	{
		Debugger.log("+++ getSchedulableType: Name = " + s + " +++");
		for (TypeElement elem : type_elements)
		{
			Debugger.log("+++  simpleName = " + elem.getSimpleName() + " +++");
			if (elem.getSimpleName().contentEquals(s))
			{
				TypeMirror superclass = elem.getSuperclass();

				if (superclass.toString().contains(MANAGED_THREAD_QUALIFIED_NAME))
				{
					return SchedulableTypeE.MT;
				}

				if (superclass.toString().contains(MISSION_SEQUENCER_QUALIFIED_NAME))
				{
					return SchedulableTypeE.SMS;
				}

				if (superclass.toString().contains(PERIODIC_EVENT_HANDLER_QUALIFIED_NAME))
				{
					return SchedulableTypeE.PEH;
				}

				if (superclass.toString()
						.contains(APERIODIC_EVENT_HANDLER_QUALIFIED_NAME))
				{
					return SchedulableTypeE.APEH;
				}

				if (superclass.toString().contains(ONE_SHOT_EVENT_HANDLER_QUALIFIED_NAME))
				{
					return SchedulableTypeE.OSEH;
				}
			}

		}

		return null;

	}

	/**
	 * Start the exploration of the program represented by the
	 * <code>SCJAnalysis</code> supplied to this class's constructor. It begins
	 * by getting the Safelet and building it, and continues from there down the
	 * tiers.
	 * 
	 * @return <code>ProgramEnv</code> the program environment for this program
	 */
	public ProgramEnv explore()
	{
		System.out.println();
		System.out.println("+++ Building Environments +++");
		System.out.println();

		preprocess();

		TypeElement safeletType = findSafelet();
		ArrayList<Name> topLevelMissionSequners = buildSafelet(safeletType);

		for (Name n : topLevelMissionSequners)
		{
			// Which will call buildMission etc
			buildTopLevelMissionSequencer(n);
		}

		findParameters();

		return programEnv;
	}

	private void preprocess()
	{
		System.out.println();
		System.out.println("+++ Build Pre Processing +++");
		System.out.println();

		for (TypeElement elem : type_elements)
		{					
			String className = elem.getSimpleName().toString();
			
			grabSuperTypes(elem, className);
			
			
			ClassTree ct = analysis.TREES.getTree(elem);
			
			grabClassMethods(className, ct);			
		}

		Debugger.log("Class Super Type Map");
		for (String s : classTypeMap.keySet())
		{
			Debugger.log(s + " = " + classTypeMap.get(s));
		}
		Debugger.log("");

		Debugger.log("Method Location Map");
		for (String s : classMethodsMap.keySet())
		{
			Debugger.log(s + " = " + classMethodsMap.get(s));
		}
		
		exploreNonParadigmObjects(grabNonParadigmObjects());
	}

	private void exploreNonParadigmObjects(List<TypeElement> grabNonParadigmObjects)
	{
		// TODO Makes this build the envs for this list.
		
	}

	private List<TypeElement> grabNonParadigmObjects()
	{
		List<TypeElement> nonParadigmObjects = new ArrayList<TypeElement>();
		
		for (TypeElement elem : type_elements)
		{
			
			boolean notParadigm = (! (elem.getInterfaces().toString().contains(TightRopeString.ParadigmName.SAFELET))) &&
				(!(elem.getSuperclass().toString().contains(TightRopeString.ParadigmName.MISSION_SEQUENCER))) &&
				(!(elem.getSuperclass().toString().contains(TightRopeString.ParadigmName.MISSION))) &&
				(!(elem.getSuperclass().toString().contains(TightRopeString.ParadigmName.PERIODIC_EVENT_HANDLER))) &&
				(!(elem.getSuperclass().toString().contains(TightRopeString.ParadigmName.APERIODIC_EVENT_HANDLER))) &&
				(!(elem.getSuperclass().toString().contains(TightRopeString.ParadigmName.ONE_SHOT_EVENT_HANDLER))) &&
				(!(elem.getSuperclass().toString().contains(TightRopeString.ParadigmName.MANAGED_THREAD))) ;
			
			
			
			if (notParadigm)
			{
				Debugger.log(elem.toString() + " is not paradigm");
				nonParadigmObjects.add(elem);
			}
		}
		return nonParadigmObjects;
	}

	/**
	 * Builds a map of Class names to their super types. For use in MCB
	 * generation
	 */
	private void grabSuperTypes(TypeElement elem, String className)
	{
		String superClass = elem.getSuperclass().toString();
		List<String> superInterfaces = new ArrayList<String>();
	
		for (TypeMirror tm : elem.getInterfaces())
		{
			superInterfaces.add(tm.toString());
		}
	
		List<String> superTypes = new ArrayList<String>();
		superTypes.addAll(superInterfaces);
		superTypes.add(superClass);
	
		classTypeMap.put(className, superTypes);
	}

	@SuppressWarnings("unchecked")
	private void grabClassMethods(String className, ClassTree ct)
	{
		List<StatementTree> members = (List<StatementTree>) ct.getMembers();
		Iterator<StatementTree> i = members.iterator();
		ArrayList<String> locs = new ArrayList<String>();
		
		while (i.hasNext())
		{
			Object obj = i.next();

			if (obj instanceof MethodTree)
			{
				MethodTree mt = (MethodTree) obj;
				final String methodName = mt.getName().toString();
				final boolean notIgnoredMethod = !((methodName.endsWith("<init>"))
						|| (methodName.contains("handleAsyncEvent"))
						|| (methodName.contains("run"))
						|| (methodName.contains("initialize"))
						|| (methodName.contains("missionMemorySize"))
						|| (methodName.contains("main"))
						|| (methodName.contains("initializeApplication"))
						|| (methodName.contains("cleanUp"))
						|| (methodName.contains("immortalMemorySize"))
						|| (methodName.contains("getSequencer")) || (methodName
						.contains("getNextMission"))

				);

				
				if (notIgnoredMethod)
				{
					locs.add(mt.getName().toString());
					
				}
			}
			
		}
		
		if(! locs.isEmpty())
		{
			classMethodsMap.put(className, locs);
		}
	}

	private TypeElement findSafelet()
	{
		System.out.println();
		System.out.println("+++ Finding Safelet +++");
		System.out.println();

		// TypeElement safelet = null;
		for (TypeElement elem : type_elements)
		{
			// TODO needs to be made safer. I think this might fall over if
			// presented with multiple interfaces
			if (elem.getInterfaces().toString().contains(TightRopeString.ParadigmName.SAFELET))
			{
				final Name safeletName = elem.getSimpleName();

				System.out.println("+++ Found Safelet " + safeletName + END_PLUSES);

				packagePrefix = findPackagePrefix(elem);

				programEnv.addSafelet(safeletName);

				// getVariables(elem, programEnv.getSafelet());
			

				return elem;
			}
		}
		return null;
	}

	private ArrayList<Name> buildSafelet(TypeElement safelet)
	{
		System.out.println();
		System.out.println("+++ Building Saflet +++");
		System.out.println();

		// init return list
		ArrayList<Name> topLevelMissionSequencers = null;

		// init Safelet visitor
		SafeletLevel2Builder safeletLevel2Visitor = new SafeletLevel2Builder(programEnv,
				analysis, this);

		// get TLMS list from visitor
		topLevelMissionSequencers = safeletLevel2Visitor.build(safelet);

		// explore TLMSs
		for (Name n : topLevelMissionSequencers)
		{
			programEnv.addTopLevelMissionSequencer(n);
		}

		return topLevelMissionSequencers;
	}

	private void buildTopLevelMissionSequencer(Name tlms)
	{
		System.out.println();
		System.out.println(BUILDING_TOP_LEVEL_SEQUENCER);
		System.out.println();

		String tlmsStr = "" + Character.toUpperCase(tlms.charAt(0))
				+ tlms.subSequence(1, tlms.length());

		Debugger.log("packagePrefix=" + packagePrefix + " and tlms=" + tlmsStr);

		TypeElement tlmsElement = null;// =
										// analysis.getTypeElement(packagePrefix
										// + tlms);

		for (TypeElement te : type_elements)
		{
			Debugger.log(te.toString() + " and " + tlmsStr);
			if (te.toString().contains(tlmsStr))
			{
				Debugger.log("found");
				tlmsElement = te;
			}
		}

		TopLevelMissionSequencerEnv topLevelMissionSequencer = programEnv
				.getTopLevelMissionSequencer(tlms);
		ClassEnv tlmsClassEnv = new ClassEnv();
		tlmsClassEnv.setName(tlms);
		topLevelMissionSequencer.addClassEnv(tlmsClassEnv);

		ParadigmBuilder msl2Visitor = new MissionSequencerLevel2Builder(programEnv,
				topLevelMissionSequencer, analysis, this);

		// msl2Visitor.setVarMap(getVariables(tlmsElement, tlmsClassEnv));

		Debugger.log("tlmselement=" + tlmsElement);
		ArrayList<Name> missions = msl2Visitor.build(tlmsElement);

		topLevelMissionSequencer.addVariable(THIS, CIRCREFTYPE + tlms.toString() + CLASS,
				CIRCNEW + tlms.toString() + CLASS_BRACKETS, false);

		assert (missions != null);
		if (missions.isEmpty())
		{
			System.out.println(NO_MISSIONS);
		}
		else
		{
			programEnv.newTier();
			final String output = "+++ Exploring Mission ";

			for (Name n : missions)
			{
				programEnv.newCluster(tlms);

				System.out.println(output + n + END_PLUSES);
				topLevelMissionSequencer.addMission(n);

				buildMission(n);
			}
		}
	}

	private void buildMission(Name n)
	{
		System.out.println();
		System.out.println(BUILDING_MISSION + n + END_PLUSES);
		System.out.println();

		programEnv.addMission(n);

		MissionEnv missionEnv = programEnv.getFrameworkEnv().getMission(n);

		ClassEnv missionClassEnv = new ClassEnv();
		missionClassEnv.setName(n);
		missionEnv.addClassEnv(missionClassEnv);

		String fullName = packagePrefix + n;
	

		TypeElement missionTypeElem = elems.getTypeElement(fullName);

		// HashMap<Name, Tree> variables =
		// getVariables(missionTypeElem, missionClassEnv);

		missionEnv.addVariable(THIS, CIRCREFTYPE + n.toString() + CLASS,
				CIRCNEW + n.toString() + CLASS_BRACKETS, true);

		ArrayList<Name> schedulables = new MissionLevel2Builder(programEnv, missionEnv,
				analysis, this).build(missionTypeElem);

		assert (schedulables != null);
		if (schedulables.isEmpty())
		{
			System.out.println("+++ Mission " + n + NO_SCHEDULABLES);
		}
		else
		{
			Debugger.log("*** Schedulables = " + schedulables.toString());

			buildSchedulables(missionEnv, schedulables);
		}

	}

	private void buildSchedulables(MissionEnv missionEnv, ArrayList<Name> schedulables)
	{

		ArrayList<Name> nestedSequencers = new ArrayList<Name>();
		for (Name s : schedulables)
		{
			SchedulableTypeE type = getSchedulableType(s);

			// TODO need to get rid of this stupid method call. I add to the
			// Cluster and to the Mission envs
			Debugger.log("*** Scheudlable name = " + s + " type = " + type);

			programEnv.addSchedulable(type, s);

			assert (programEnv.containsScheudlable(s));

			if (type == SchedulableTypeE.SMS)
			{
				nestedSequencers.add(s);
			}
			else
			{
				buildShedulable(s);
			}

		}

		if (!nestedSequencers.isEmpty())
		{
			buildSchedulableMissionSequencer(nestedSequencers);
		}
	}

	@SuppressWarnings("unchecked")
	private void buildShedulable(Name s)
	{
		System.out.println();
		System.out.println(BUILDING_SCHEDULABLE + s + END_PLUSES);
		System.out.println();

		ClassEnv classEnv = new ClassEnv();
		classEnv.setName(s);

		String fullName = packagePrefix + s;
		Elements elems = analysis.ELEMENTS;

		TypeElement schedulableType = elems.getTypeElement(fullName);

		ParadigmEnv schedulableEnv = programEnv.getSchedulable(s);
		schedulableEnv.addClassEnv(classEnv);

		new SchedulableObjectBuilder(analysis, programEnv, schedulableEnv, this)
				.build(schedulableType);

	}

	private void buildSchedulableMissionSequencer(ArrayList<Name> nestedSequencers)
	{
		System.out.println();
		System.out.println(BUILDING_SCHEDULABLE_MISSION_SEQUENCERS);
		System.out.println();

		TypeElement tlmsElement;
		ArrayList<Name> missions;
		NestedMissionSequencerEnv nestedMissionSequencer;

		for (Name sequencer : nestedSequencers)
		{
			tlmsElement = analysis.ELEMENTS.getTypeElement(packagePrefix + sequencer);

			nestedMissionSequencer = programEnv.getNestedMissionSequencer(sequencer);

			ClassEnv smsClassEnv = new ClassEnv();
			smsClassEnv.setName(sequencer);
			nestedMissionSequencer.addClassEnv(smsClassEnv);

			Debugger.log("nestedMissionSequencer = " + nestedMissionSequencer);

			ParadigmBuilder msl2Visitor = new MissionSequencerLevel2Builder(programEnv,
					nestedMissionSequencer, analysis, this);

			// msl2Visitor.setVarMap(getVariables(tlmsElement,
			// nestedMissionSequencer));

			missions = msl2Visitor.build(tlmsElement);

			assert (missions != null);
			if (missions.isEmpty())
			{
				System.out.println(NO_MISSIONS);
			}
			else
			{
				
				programEnv.newTier();

				for (Name n : missions)
				{
					programEnv.newCluster(sequencer);

					nestedMissionSequencer.addMission(n);
					System.out.println(BUILD_MISSION + n + END_PLUSES);
					buildMission(n);
				}
			}
		}
	}

	private String findPackagePrefix(TypeElement elem)
	{
		String packagePrefix;

		packagePrefix = elem.getQualifiedName().toString();
		int firstIndex = packagePrefix.indexOf(elem.getSimpleName().toString());
		packagePrefix = packagePrefix.substring(0, firstIndex);
		setProgramName(packagePrefix);
		return packagePrefix;
	}

	private void setProgramName(String packagePrefix)
	{
		programName = packagePrefix.substring(0, packagePrefix.length()-1);
	}

	private HashMap<Name, Tree> getVariables(TypeElement arg0, ObjectEnv objectEnv)
	{
		HashMap<Name, Tree> varMap = new HashMap<Name, Tree>();

		VariableVisitor varVisitor;

		if (objectEnv != null)
		{
			varVisitor = new VariableVisitor(programEnv, objectEnv);
		}
		else
		{
			varVisitor = new VariableVisitor(programEnv);
		}

		ClassTree ct = analysis.TREES.getTree(arg0);
		List<? extends Tree> members = ct.getMembers();
		Iterator<? extends Tree> i = members.iterator();

		while (i.hasNext())
		{
			Tree s = i.next();
			// TODO if this is only ever going to return one value at a time
			// then it shouldn't be a map
			HashMap<Name, Tree> m = (HashMap<Name, Tree>) s.accept(varVisitor, true);
			assert (m != null);
			Debugger.log("getVariables m = " + m);
			// TODO this is a bit of a hack...

			for (Name n : m.keySet())
			{
				
				varMap.putIfAbsent(n, m.get(n));
			}
		}
		Debugger.log("getVariables varMap = " + varMap);
		return varMap;

	}

	private void findParameters()
	{
		System.out.println();
		System.out.println(FINDING_PROCESS_PARAMETERS);
		System.out.println();

		Debugger.log("Deferred Params = " + deferredParamsList.toString());
		for (DeferredParamter deferred : deferredParamsList)
		{
			Debugger.log("*** start of Loop for " + deferred.toString() + "*** ");
			List<? extends ExpressionTree> args = new ArrayList<ExpressionTree>();

			ObjectEnv objectWithParams = null;

			Tree tree = deferred.tree;
			String nameOfClassBeingTranslated = "";
			Debugger.log("Tree kind = " + tree.getKind());
			if (tree instanceof VariableTree)
			{
				Debugger.log("Tree: " + tree + " instance of VairableTree ");

				ExpressionTree et = ((VariableTree) tree).getInitializer();
				if (et instanceof NewClassTree)
				{
					args = ((NewClassTree) et).getArguments();
				}

				Debugger.log("trying to get objectEnv for "
						+ ((VariableTree) tree).getType().toString());
				objectWithParams = programEnv.getObjectEnv(((VariableTree) tree)
						.getType().toString());

				nameOfClassBeingTranslated = ((VariableTree) tree).getType().toString();

			}
			else if (tree instanceof NewClassTree)
			{
				Debugger.log("Tree: " + tree + " instance of NewClassTree ");

				args = ((NewClassTree) tree).getArguments();

				ExpressionTree identifierTree = ((NewClassTree) tree).getIdentifier();
				//
				Debugger.log("trying to get objectEnv for " + identifierTree);

				objectWithParams = programEnv.getObjectEnv(identifierTree.toString());

				nameOfClassBeingTranslated = ((NewClassTree) tree).getIdentifier()
						.toString();
			}
			else if (tree instanceof ExpressionStatementTree)
			{
				Debugger.log("Tree: " + tree + " instance of ExpressionStatement ");

				ExpressionTree et = ((ExpressionStatementTree) tree).getExpression();

				Debugger.log("et kind = " + et.getKind());

				if (et instanceof NewClassTree)
				{
					System.out
							.println("Tree: "
									+ tree
									+ " instance of ExpressionStatement->Expression->NewClassTree ");

					args = ((NewClassTree) tree).getArguments();

					ExpressionTree identifierTree = ((NewClassTree) tree).getIdentifier();
					//
					Debugger.log("trying to get objectEnv for " + identifierTree);

					objectWithParams = programEnv.getObjectEnv(identifierTree.toString());

					nameOfClassBeingTranslated = ((NewClassTree) tree).getIdentifier()
							.toString();
				}

			}

			Debugger.log("args = " + args.toString());

			if (!args.isEmpty())
			{
				ParametersVisitor paramVisitor = new ParametersVisitor(programEnv,
						objectWithParams, null, deferred.originClass, deferred.varMap);

				paramVisitor.setName(nameOfClassBeingTranslated);

				List<VariableEnv> params = new ArrayList<VariableEnv>();

				for (ExpressionTree et : args)
				{
					Debugger.log("visiting " + et.toString());

					VariableEnv returns = et.accept(paramVisitor, null);

					if (returns != null)
					{
						Debugger.log("returns = " + returns.getName());
						if (objectWithParams != null)
						{
							String type = returns.getType();

							if (type.equals("PriorityParameters"))
							{
								objectWithParams.addThreadParameter(returns);
							}
							else if (type.equals("ID"))
							{
								Debugger.log("Is ID");
								MethodEnv me = objectWithParams.getConstructor();
								if (me != null)
								{
									Map<String, Type> m = me.getParameters();

									for (String s : m.keySet())
									{
										Type t = m.get(s);

										final int length = returns.getProgramType()
												.length();
										Debugger.log("t=" + t.toString());
										Debugger.log("returns type ="
												+ returns.getProgramType().substring(0,
														length - 2));

										if (t.toString().equals(
												returns.getProgramType().substring(0,
														length - 2)))
										{
											returns.setName(s);
										}
									}
								}
								objectWithParams.addAppParameter(returns);

							}
							else
							{
								if (objectWithParams instanceof SafeletEnv)
								{

									objectWithParams.addAppParameter(returns);
								}
								else if (objectWithParams instanceof TopLevelMissionSequencerEnv)
								{
									// if (type.equals("SchedulableID"))
									// {
									// objectWithParams.addFWdParameter(returns);
									// }
									// else
									{
										objectWithParams.addAppParameter(returns);
									}

								}
								else if (objectWithParams instanceof MissionEnv)
								{

									// if (type.equals("MissionID"))
									// {
									// objectWithParams.addFWdParameter(returns);
									// }
									// else
									{
										objectWithParams.addAppParameter(returns);
									}

								}
								else if (objectWithParams instanceof PeriodicEventHandlerEnv)
								{
									if (type.equals("PeriodicParameters")
											|| type.equals("SchedulableID"))
									{
										objectWithParams.addFWdParameter(returns);
									}
									else
									{
										objectWithParams.addAppParameter(returns);
									}
								}
								else if (objectWithParams instanceof OneShotEventHandlerEnv)
								{
									if (type.equals("AperiodicParameters")
											|| type.equals("JTime")
											|| type.equals("SchedulableID"))
									{
										objectWithParams.addFWdParameter(returns);
									}
									else
									{
										objectWithParams.addAppParameter(returns);
									}
								}
								else if (objectWithParams instanceof AperiodicEventHandlerEnv)
								{
									if (type.equals("AperiodicParameters")
											|| type.equals("AperiodicType")
											|| type.equals("SchedulableID"))
									{
										objectWithParams.addFWdParameter(returns);
									}
									else
									{
										objectWithParams.addAppParameter(returns);
									}
								}
								else if (objectWithParams instanceof NestedMissionSequencerEnv)
								{

									// if(type.equals("SchedulableID"))
									// {
									//
									// }
									// else
									{
										objectWithParams.addAppParameter(returns);
									}
								}
								else if (objectWithParams instanceof ManagedThreadEnv)
								{
									// if(type.equals("SchedulableID"))
									// {
									//
									// }
									// else
									{
										objectWithParams.addAppParameter(returns);
									}
								}

							}

							Debugger.log("Adding " + returns.toString() + " to "
									+ objectWithParams.getName());
						}
						else
						{
							Debugger.log("objectWithParams was null");
						}
					}
					else
					{
						Debugger.log("returns = null");
					}
				}
			}
		}
	}

	public void addDeferredParam(Tree tree, String originClass, HashMap<Name, Tree> varMap)
	{
		DeferredParamter d = new DeferredParamter();
		d.tree = tree;
		d.originClass = originClass;
		d.varMap = varMap;

		deferredParamsList.add(d);
	}

	public String getProgramName()
	{
		// TODO Auto-generated method stub
		return programName;
	}
}
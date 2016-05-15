package hijac.tools.tightrope.builders;

import hijac.tools.analysis.SCJAnalysis;
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
import hijac.tools.tightrope.visitors.ParametersVisitor;

import hijac.tools.tightrope.visitors.VariableVisitor;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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

	Map<String, List<String>> methodLocationMap;

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
		methodLocationMap = new HashMap<String, List<String>>();

	}

	public ProgramEnv getProgramEnv()
	{
		return programEnv;
	}

	public SchedulableTypeE getSchedulableType(Name s)
	{
		System.out.println("+++ getSchedulableType: Name = " + s + " +++");
		for (TypeElement elem : type_elements)
		{
			System.out.println("+++  simpleName = " + elem.getSimpleName() + " +++");
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
		System.out.println("+++ Pre Processing +++");
		System.out.println();

		buildClassTypeMap();

		buildMethodLocationMap();

		System.out.println("Class Type Map");
		for (String s : classTypeMap.keySet())
		{
			System.out.println(s + " = " + classTypeMap.get(s));
		}
		System.out.println();

		System.out.println("Method Location Map");
		for (String s : methodLocationMap.keySet())
		{
			System.out.println(s + " = " + methodLocationMap.get(s));
		}
	}

	private void buildMethodLocationMap()
	{
		Map<String, List<String>> duplicates;

		duplicates = firstPassMethodLocationMap();

		secondPassMethodLocationMap(duplicates);
	}

	@SuppressWarnings("unchecked")
	private Map<String, List<String>> firstPassMethodLocationMap()
	{
		Map<String, List<String>> duplicates = new HashMap<String, List<String>>();
		Set<String> flagged = new HashSet<String>();

		for (TypeElement elem : type_elements)
		{
			String className = elem.getSimpleName().toString();
			ClassTree ct = analysis.TREES.getTree(elem);

			List<StatementTree> members = (List<StatementTree>) ct.getMembers();
			Iterator<StatementTree> i = members.iterator();

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
						if (methodLocationMap.containsKey(methodName))
						{

							// This method name is already in the
							// methodLocationMap, so add it and it's location
							// list
							// to the duplicates list for the Second Pass to
							// deal with.

							flagged.add(methodName);
							List<String> currentLocations = methodLocationMap
									.get(methodName);

							currentLocations.add(className);

							methodLocationMap.put(methodName, currentLocations);

						}
						else
						{
							// This method name is new, add it to the
							// methodLocationMap
							List<String> locations = new ArrayList<String>();
							locations.add(className);

							methodLocationMap.put(methodName, locations);
						}
					}

				}
			}
		}

		for (String flaggedMethod : flagged)
		{
			List<String> currentLocations = methodLocationMap.get(flaggedMethod);

			methodLocationMap.remove(flaggedMethod);

			duplicates.put(flaggedMethod, currentLocations);

		}

		return duplicates;
	}

	private void secondPassMethodLocationMap(Map<String, List<String>> duplicates)
	{
		System.out.println("** Duplicates **");
		for (String s : duplicates.keySet())
		{
			System.out.println(s + " = " + duplicates.get(s));
		}
		System.out.println();
		System.out.println();

		Set<String> toRemove = new HashSet<String>();
		
		for (String methodName : duplicates.keySet())
		{
			// inheritedMethod = false;
			List<String> sources = duplicates.get(methodName);

			for (String source : sources)
			{
				List<String> sourceSuperTypes = classTypeMap.get(source);

				for (String superType : sourceSuperTypes)
				{
					String strippedSuperType = superType;
					int lastIndexOfDot = superType.lastIndexOf('.');
					if(lastIndexOfDot != -1)
					{
						lastIndexOfDot ++;
						strippedSuperType = superType.substring(lastIndexOfDot);
					}
					System.out.println("*** trying to strip: " + superType + " and got: " + strippedSuperType);
					
					if (sources.contains(strippedSuperType))
					{
						
								// Then the supertype is in the method sources

								// This is an inherited method, so just add it
								// to the
								// methodLocationMap
						
						//Assumes that this is true for all the sources here...

								System.out.println("*** Inherited, putting + "
										+ methodName + " = " + sources);
								methodLocationMap.put(methodName, sources);
								toRemove.add(methodName);
							
					}

				}
			}

		}
		
		
		for(String s : toRemove)
		{
			duplicates.remove(s);
		}

		// This is a duplicate method name, so mangle the method name
		// with the class name and
		// add the, with only the single class as the location, to the
		// methodLocationMap

		String mangledName = "";

		for (String duplicateName : duplicates.keySet())
		{
			System.out.println("*** Not Inherited, + " + duplicateName);
			for (String sourceName : duplicates.get(duplicateName))
			{

				mangledName = duplicateName.concat(sourceName);
				List<String> newSources = new ArrayList<String>();
				newSources.add(sourceName);
				System.out.println("\t... putting " + duplicateName + " = " + newSources);
				methodLocationMap.put(mangledName, newSources);

			}
		}

		// Then remove all the duplicates from the methodLocationMap
		// for(String duplicate : duplicates.keySet())
		// {
		// methodLocationMap.remove(duplicate);
		// }

	}

	/**
	 * Builds a map of Class names to their super types. For use in MCB
	 * generation
	 */
	private void buildClassTypeMap()
	{
		for (TypeElement elem : type_elements)
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

			classTypeMap.put(elem.getSimpleName().toString(), superTypes);
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
			if (elem.getInterfaces().toString().contains("Safelet"))
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

		System.out.println("packagePrefix=" + packagePrefix + " and tlms=" + tlmsStr);

		TypeElement tlmsElement = null;// =
										// analysis.getTypeElement(packagePrefix
										// + tlms);

		for (TypeElement te : type_elements)
		{
			System.out.println(te.toString() + " and " + tlmsStr);
			if (te.toString().contains(tlmsStr))
			{
				System.out.println("found");
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

		System.out.println("tlmselement=" + tlmsElement);
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

		// System.out.println("+++ Building Mission: Full Name = " + fullName
		// + END_PLUSES);

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
			System.out.println("*** Schedulables = " + schedulables.toString());

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
			System.out.println("*** Scheudlable name = " + s + " type = " + type);

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

			System.out.println("nestedMissionSequencer = " + nestedMissionSequencer);

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
				// System.out.println(" +++ I have " + missions.size()
				// + " missions +++ ");
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
		return packagePrefix;
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
			System.out.println("getVariables m = " + m);
			// TODO this is a bit of a hack...

			for (Name n : m.keySet())
			{
				// System.out.println("\t*** Name = " + n + " Type = "
				// + m.get(n) + " Kind = " + m.get(n).getKind());
				varMap.putIfAbsent(n, m.get(n));
			}
		}
		System.out.println("getVariables varMap = " + varMap);
		return varMap;

	}

	private void findParameters()
	{
		System.out.println();
		System.out.println(FINDING_PROCESS_PARAMETERS);
		System.out.println();

		System.out.println("Deferred Params = " + deferredParamsList.toString());
		for (DeferredParamter deferred : deferredParamsList)
		{
			System.out.println("*** start of Loop for " + deferred.toString() + "*** ");
			List<? extends ExpressionTree> args = new ArrayList<ExpressionTree>();

			ObjectEnv objectWithParams = null;

			Tree tree = deferred.tree;
			String nameOfClassBeingTranslated = "";
			System.out.println("Tree kind = " + tree.getKind());
			if (tree instanceof VariableTree)
			{
				System.out.println("Tree: " + tree + " instance of VairableTree ");

				ExpressionTree et = ((VariableTree) tree).getInitializer();
				if (et instanceof NewClassTree)
				{
					args = ((NewClassTree) et).getArguments();
				}

				System.out.println("trying to get objectEnv for "
						+ ((VariableTree) tree).getType().toString());
				objectWithParams = programEnv.getObjectEnv(((VariableTree) tree)
						.getType().toString());

				nameOfClassBeingTranslated = ((VariableTree) tree).getType().toString();

			}
			else if (tree instanceof NewClassTree)
			{
				System.out.println("Tree: " + tree + " instance of NewClassTree ");

				args = ((NewClassTree) tree).getArguments();

				ExpressionTree identifierTree = ((NewClassTree) tree).getIdentifier();
				//
				System.out.println("trying to get objectEnv for " + identifierTree);

				objectWithParams = programEnv.getObjectEnv(identifierTree.toString());

				nameOfClassBeingTranslated = ((NewClassTree) tree).getIdentifier()
						.toString();
			}
			else if (tree instanceof ExpressionStatementTree)
			{
				System.out.println("Tree: " + tree + " instance of ExpressionStatement ");

				ExpressionTree et = ((ExpressionStatementTree) tree).getExpression();

				System.out.println("et kind = " + et.getKind());

				if (et instanceof NewClassTree)
				{
					System.out
							.println("Tree: "
									+ tree
									+ " instance of ExpressionStatement->Expression->NewClassTree ");

					args = ((NewClassTree) tree).getArguments();

					ExpressionTree identifierTree = ((NewClassTree) tree).getIdentifier();
					//
					System.out.println("trying to get objectEnv for " + identifierTree);

					objectWithParams = programEnv.getObjectEnv(identifierTree.toString());

					nameOfClassBeingTranslated = ((NewClassTree) tree).getIdentifier()
							.toString();
				}

			}

			System.out.println("args = " + args.toString());

			if (!args.isEmpty())
			{
				ParametersVisitor paramVisitor = new ParametersVisitor(programEnv,
						objectWithParams, null, deferred.originClass, deferred.varMap);

				paramVisitor.setName(nameOfClassBeingTranslated);

				List<VariableEnv> params = new ArrayList<VariableEnv>();

				for (ExpressionTree et : args)
				{
					System.out.println("visiting " + et.toString());

					VariableEnv returns = et.accept(paramVisitor, null);

					if (returns != null)
					{
						System.out.println("returns = " + returns.getName());
						if (objectWithParams != null)
						{
							String type = returns.getType();

							if (type.equals("PriorityParameters"))
							{
								objectWithParams.addThreadParameter(returns);
							}
							else if (type.equals("ID"))
							{
								System.out.println("Is ID");
								MethodEnv me = objectWithParams.getConstructor();
								if (me != null)
								{
									Map<String, Type> m = me.getParameters();

									for (String s : m.keySet())
									{
										Type t = m.get(s);

										final int length = returns.getProgramType()
												.length();
										System.out.println("t=" + t.toString());
										System.out.println("returns type ="
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

							// objectWithParams.addParameter(returns);

							System.out.println("Adding " + returns.toString() + " to "
									+ objectWithParams.getName());
						}
						else
						{
							System.out.println("objectWithParams was null");
						}
					}
					else
					{
						System.out.println("returns = null");
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
}
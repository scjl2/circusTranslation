package hijac.tools.tightrope.environments;

import hijac.tools.analysis.SCJAnalysis;
import hijac.tools.tightrope.visitors.MethodVisitor;
import hijac.tools.tightrope.visitors.MissionLevel2Visitor;
import hijac.tools.tightrope.visitors.MissionSequencerLevel2Visitor;
import hijac.tools.tightrope.visitors.SafeletLevel2Visitor;
import hijac.tools.tightrope.visitors.VariableVisitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.Tree;

public class EnvironmentBuilder
{
	private static final String ONE_SHOT_EVENT_HANDLER_QUALIFIED_NAME = "javax.safetycritical.OneShotEventHandler";

	private static final String APERIODIC_EVENT_HANDLER_QUALIFIED_NAME = "javax.safetycritical.AperiodicEventHandler";

	private static final String PERIODIC_EVENT_HANDLER_QUALIFIED_NAME = "javax.safetycritical.PeriodicEventHandler";

	private static final String MISSION_SEQUENCER_QUALIFIED_NAME = "javax.safetycritical.MissionSequencer";

	private static final String MANAGED_THREAD_QUALIFIED_NAME = "javax.safetycritical.ManagedThread";

	public static SCJAnalysis analysis;

	// private Trees trees;
	// private Set<CompilationUnitTree> units;
	private Set<TypeElement> type_elements;

	private ProgramEnv programEnv;

	public ProgramEnv getProgramEnv()
	{
		return programEnv;
	}

	private String packagePrefix;

	public EnvironmentBuilder(SCJAnalysis analysis)
	{
		EnvironmentBuilder.analysis = analysis;
		this.programEnv = new ProgramEnv(analysis);

		// trees = analysis.TREES;
		// units = analysis.getCompilationUnits();
		type_elements = analysis.getTypeElements();

	}

	public SchedulableTypeE getSchedulableType(Name s)
	{
		// System.out.println("+++ getSchedulableType: Name = " + s + " +++");
		for (TypeElement elem : type_elements)
		{
			// System.out.println("+++  simpleName = "+elem.getSimpleName()
			// +" +++");
			if (elem.getSimpleName().contentEquals(s))
			{
				TypeMirror superclass = elem.getSuperclass();

				if (superclass.toString().contains(
						MANAGED_THREAD_QUALIFIED_NAME))
				{
					return SchedulableTypeE.MT;
				}

				if (superclass.toString().contains(
						MISSION_SEQUENCER_QUALIFIED_NAME))
				{
					return SchedulableTypeE.SMS;
				}

				if (superclass.toString().contains(
						PERIODIC_EVENT_HANDLER_QUALIFIED_NAME))
				{
					return SchedulableTypeE.PEH;
				}

				if (superclass.toString().contains(
						APERIODIC_EVENT_HANDLER_QUALIFIED_NAME))
				{
					return SchedulableTypeE.APEH;
				}

				if (superclass.toString().contains(
						ONE_SHOT_EVENT_HANDLER_QUALIFIED_NAME))
				{
					return SchedulableTypeE.OSEH;
				}
			}

		}

		return null;

	}

	public ProgramEnv explore()
	{
		System.out.println("+++ Building Environments +++");
		System.out.println();
		ArrayList<Name> topLevelMissionSequners = buildSafelet(getSafelet());

		for (Name n : topLevelMissionSequners)
		{
			// System.out.println("+++ top Level Sequencer: " + n + " ***");

			// programEnv.addMission(n);

			buildTopLevelMissionSequencer(n);
		}

		return programEnv;
	}

	private TypeElement getSafelet()
	{

		TypeElement safelet = null;
		for (TypeElement elem : type_elements)
		{
			// TODO needs to be made safer. I think this might fall over if
			// presented with multiple interfaces
			if (elem.getInterfaces().toString().contains("Safelet"))
			{
				System.out.println("Found Safelet");
				safelet = elem;
				packagePrefix = findPackagePrefix(elem);

				programEnv.addSafelet(safelet.getSimpleName());

				getVariables(safelet, programEnv.getSafelet());

				// add methods etc here
				// programEnv.a

				// TODO Hack, needs to return all the tlms

				// programEnv.addTopLevelMissionSequencers(topLevelMissionSequencer);

				// programEnv.getSafelet().setTLMSNames(names);
				//
				// for (int i = 0; i < names.length; i++)
				// {
				// // framework.put("TopLevelMissionSequencer", names[i]);
				// programEnv.addTopLevelMissionSequencer(names[i]);
				// }

				// System.out.println(names == null);

			}

		}
		return safelet;
	}

	private ArrayList<Name> buildSafelet(TypeElement safelet)
	{

		ArrayList<Name> topLevelMissionSequencers = null;
		topLevelMissionSequencers = safelet.accept(new SafeletLevel2Visitor(
				programEnv, analysis), null);

		for (Name n : topLevelMissionSequencers)
		{
			System.out.println();
			System.out.println("+++ Exploring Top Level Sequencer " + n
					+ " +++");
			System.out.println();
			programEnv.addTopLevelMissionSequencer(n);
		}
		return topLevelMissionSequencers;
	}

	private void buildTopLevelMissionSequencer(Name tlms)
	{
		System.out.println();
		System.out.println("+++ Building Top Level Sequencer +++");
		System.out.println();

		TypeElement tlmsElement = analysis.ELEMENTS
				.getTypeElement(packagePrefix + tlms);

		TopLevelMissionSequencerEnv topLevelMissionSequencer = programEnv
				.getTopLevelMissionSequencer(tlms);
		ClassEnv tlmsClassEnv = new ClassEnv();
		tlmsClassEnv.setName(tlms);
		topLevelMissionSequencer.addClassEnv(tlmsClassEnv);

		ArrayList<Name> missions = new MissionSequencerLevel2Visitor(
				programEnv, tlmsClassEnv, analysis).visitType(
				tlmsElement, null);

		topLevelMissionSequencer.addVariable("this",
				"\\circreftype " + tlms.toString() + "Class", "\\circnew "
						+ tlms.toString() + "Class()");

		getVariables(tlmsElement, tlmsClassEnv);

		if (missions == null)
		{
			System.out.println("+++ No Missions +++");
		} else
		{
			programEnv.newTier();

			for (Name n : missions)
			{
				programEnv.newCluster(tlms);
				System.out.println("+++ Exploring Mission " + n + " +++");
				topLevelMissionSequencer.addMission(n);

				buildMission(n);
			}
		}
	}

	private void buildMission(Name n)
	{
		System.out.println();
		System.out.println("+++ Building Mission " + n + " +++");
		System.out.println();

		programEnv.addMission(n);

		MissionEnv missionEnv = programEnv.getFrameworkEnv().getMission(n);

		String fullName = packagePrefix + n;
		Elements elems = analysis.ELEMENTS;
		System.out.println("Building Mission: Full Name = " + fullName);

		TypeElement missionType = elems.getTypeElement(fullName);

		HashMap<Name, Tree> variables = getVariables(missionType, missionEnv);

		ArrayList<Name> schedulables = missionType.accept(
				new MissionLevel2Visitor(programEnv, missionEnv, analysis),
				null);

		if (schedulables == null)
		{
			System.out.println("+++ No Schedulables +++");
		} else
		{
			buildSchedulables(missionEnv, schedulables);
		}

	}

	private void buildSchedulables(MissionEnv missionEnv, ArrayList<Name> schedulables)
	{

		ArrayList<Name> nestedSequencers = new ArrayList<Name>();
		for (Name s : schedulables)
		{
			SchedulableTypeE type = getSchedulableType(s);

			//TODO need to get rid of this stupid method call. I add to the Cluster and to the Mission envs
			programEnv.addSchedulable(type, s);

			assert (programEnv.containsScheudlable(s));

			if (type == SchedulableTypeE.SMS)
			{
				nestedSequencers.add(s);
			}

			buildShedulable(s);
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
		System.out.println("+++ Building Schedulable " + s + " +++");
		System.out.println();

		ClassEnv classEnv = new ClassEnv();
		classEnv.setName(s);

		String fullName = packagePrefix + s;
		Elements elems = analysis.ELEMENTS;
		System.out.println("Building Schedulable: Full Name = " + fullName);
		TypeElement schedulableType = elems.getTypeElement(fullName);

		ClassTree ct = analysis.TREES.getTree(schedulableType);

		List<StatementTree> members = (List<StatementTree>) ct.getMembers();

		Iterator<StatementTree> i = members.iterator();

		ArrayList<Name> sycnMeths = new ArrayList<Name>();

		HashMap<Name, Tree> variables;

		ParadigmEnv schedulableEnv = programEnv.getSchedulable(s);
		schedulableEnv.addClassEnv(classEnv);

		variables = getVariables(schedulableType, classEnv);

		schedulableEnv.addVariable("this", "\\circreftype " + s + "Class",
				"\\circnew " + s + "Class()");

		while (i.hasNext())
		{

			// ArrayList<Name> tmp = new ArrayList<Name>();

			Tree tlst = i.next();

			if (tlst instanceof MethodTree)
			{
				MethodTree mt = (MethodTree) tlst;

				System.out
						.println("*** Method Name = " + mt.getName() + " ***");

				// tmp = tlst.accept(new ManagedThreadVisitor(programEnv,
				// analysis, variables, packagePrefix), null);

				if (mt.getModifiers().getFlags()
						.contains(Modifier.SYNCHRONIZED))
				{

					classEnv.addSyncMeth((new MethodVisitor(analysis, classEnv)
							.visitMethod(mt, null)));

					// TODO Add to schedulableEnv but...need a different visitor
					// because otherwie it'll be outputting too much

				} else if (!(mt.getName().contentEquals("<init>")))
				{
					if (!(mt.getName().contentEquals("run")))
					{
						// TODO Add to schedulableEnv but...need a different
						// visitor because otherwie it'll be outputting too much

						// schedulableEnv.addSyncMeth(
						// (new MethodVisitor(analysis,
						// classEnv).visitMethod(mt, null)));
					}
					classEnv.addMeth(new MethodVisitor(analysis, classEnv)
							.visitMethod(mt, null));
				}

			}

			// if (tmp != null)
			// {
			// sycnMeths.addAll(tmp);
			// }

		}
		// System.out.println();
		// System.out.println("+++ syncMethds empty = " + (sycnMeths.size() <=
		// 0)
		// + " +++");
		// if (sycnMeths.size() > 0)
		// {
		// System.out.println("*** SyncMeths for " + s + " ***");
		// for (Name n : sycnMeths)
		// {
		// System.out.println("\t*** " + n + " is synchronised");
		// }
		// }
		// System.out.println();

	}

	private void buildSchedulableMissionSequencer(
			ArrayList<Name> nestedSequencers)
	{
		System.out.println();
		System.out.println("+++ Building Schedulable Mission Sequencers +++");
		System.out.println();

		TypeElement tlmsElement;
		ArrayList<Name> missions;
		NestedMissionSequencerEnv nestedMissionSequencer;

		for (Name sequencer : nestedSequencers)
		{
			tlmsElement = analysis.ELEMENTS.getTypeElement(packagePrefix
					+ sequencer);

			nestedMissionSequencer = programEnv
					.getNestedMissionSequencer(sequencer);

			missions = new MissionSequencerLevel2Visitor(programEnv,
					nestedMissionSequencer, analysis).visitType(tlmsElement,
					null);

			if (missions == null)
			{
				System.out.println("+++ No Missions +++");
			} else
			{
				System.out.println(" +++ I have " + missions.size()
						+ " missions +++ ");
				programEnv.newTier();

				for (Name n : missions)
				{
					programEnv.newCluster(sequencer);
					
					nestedMissionSequencer.addMission(n);
					System.out.println("+++ Build Mission: " + n + " +++");
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

	private HashMap<Name, Tree> getVariables(TypeElement arg0,
			ObjectEnv objectEnv)
	{
		HashMap<Name, Tree> varMap = new HashMap<Name, Tree>();

		VariableVisitor varVisitor;

		if (objectEnv != null)
		{
			varVisitor = new VariableVisitor(programEnv, objectEnv);
		} else
		{
			varVisitor = new VariableVisitor(programEnv);
		}

		ClassTree ct = analysis.TREES.getTree(arg0);
		List<? extends Tree> members = ct.getMembers();
		Iterator<? extends Tree> i = members.iterator();

		while (i.hasNext())
		{
			Tree s = i.next();
			System.out.println("\t *** Tree = " + s);
			// TODO if this is only ever going to return one value at a time
			// then it shouldn't be a map
			HashMap<Name, Tree> m = (HashMap<Name, Tree>) s.accept(varVisitor,
					true);

			// System.out.println("+++ m == null : " + m == null + " +++" );

			if (m == null)
			{
				System.out.println("+++ Variable Visitor Returned Null +++");

			} else
			{
				// System.out.println("+++ Variable Visitor Returned " + m);
				// if(s instanceof MethodTree)
				// {
				// MethodTree mt = (MethodTree)s;
				// if(mt.getName().contentEquals("<init>") )
				// {
				// System.out.println("*** it was init, continue ***");
				// continue;
				// }
				// else
				// {
				// System.out.println("*** Adding 1***");
				// varMap.putAll(m);
				// }
				//
				// }
				// else
				// {
				// System.out.println("*** Adding 2***");
				// varMap.putAll(m);
				// }

				// TODO this is a bit of a hack...

				for (Name n : m.keySet())
				{
					System.out.println("\t*** Name = " + n + " Type = "
							+ m.get(n) + " Kind = " + m.get(n).getKind());
					varMap.putIfAbsent(n, m.get(n));
				}

			}

		}

		return varMap;

	}

	public class ClassEnv extends ParadigmEnv
	{
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public Map toMap()
		{
			Map map = new HashMap();
			map.put("ProcessID", name.toString());
			// map.put("handlerType", "aperiodic");
			// map.put("importName", "Aperiodic");
			map.put("Methods", methsList());
			map.put("Variables", varsList());

			return map;
		}
	}

	// private Name[] buildMissionSequencers(Name[] names, String packagePrefix,
	// Name[] missionNames)
	// {
	// if (names != null)
	// {
	// System.out.println("Mission Sequencer Visiting");
	// for (int i = 0; i < names.length; i++)
	// {
	// programEnv.addTopLevelMissionSequencer(names[i]);
	// // System.out.println(packagePrefix +names[i]);
	// TypeElement elem = analysis.getTypeElement(packagePrefix
	// + names[i]);
	//
	// System.out.println("Visiting: " + elem);
	// // missionNames = elem.accept(new
	// // MissionSequencerLevel2Visitor(programEnv, analysis),
	// // null);
	// }
	// }
	// return missionNames;
	// }
	//
	// private void buildMissions(String packagePrefix, Name[] missionNames)
	// {
	// Name[] schedulables;
	// if (missionNames != null)
	// {
	// System.out.println("Mission Visiting");
	// for (int i = 0; i < missionNames.length; i++)
	// {
	// programEnv.addMission(missionNames[i]);
	// TypeElement elem = analysis.getTypeElement(packagePrefix
	// + missionNames[i]);
	// System.out.println("Visiting: " + elem);
	//
	// elem.accept(new MissionLevel2Visitor(programEnv, analysis),
	// null);
	// }
	// }
	// }
}
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
	private static final String VARIABLE_VISITOR_RETURNED_NULL = "+++ Variable Visitor Returned Null +++";

	private static final String BUILD_MISSION = "+++ Build Mission: ";

	private static final String NO_MISSIONS = "+++ No Missions +++";

	private static final String BUILDING_SCHEDULABLE_MISSION_SEQUENCERS = "+++ Building Schedulable Mission Sequencers +++";

	private static final String BUILDING_SCHEDULABLE = "+++ Building Schedulable ";

	private static final String NO_SCHEDULABLES = "+++ No Schedulables +++";

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

	// private Trees trees;
	// private Set<CompilationUnitTree> units;
	private Set<TypeElement> type_elements;
	private Elements elems;

	private ProgramEnv programEnv;

	private String packagePrefix;

	public EnvironmentBuilder(SCJAnalysis analysis)
	{
		EnvironmentBuilder.analysis = analysis;
		this.programEnv = new ProgramEnv(analysis);

		// trees = analysis.TREES;
		// units = analysis.getCompilationUnits();
		type_elements = analysis.getTypeElements();
		elems = analysis.ELEMENTS;

	}

	public ProgramEnv getProgramEnv()
	{
		return programEnv;
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
		// TypeElement safelet = null;
		for (TypeElement elem : type_elements)
		{
			// TODO needs to be made safer. I think this might fall over if
			// presented with multiple interfaces
			if (elem.getInterfaces().toString().contains("Safelet"))
			{
				System.out.println("Found Safelet");
				// safelet = elem;
				packagePrefix = findPackagePrefix(elem);

				programEnv.addSafelet(elem.getSimpleName());

				getVariables(elem, programEnv.getSafelet());

				return elem;
			}
		}
		return null;
	}

	private ArrayList<Name> buildSafelet(TypeElement safelet)
	{

		ArrayList<Name> topLevelMissionSequencers = null;

		SafeletLevel2Visitor safeletLevel2Visitor = new SafeletLevel2Visitor(
				programEnv, analysis);

		topLevelMissionSequencers = safeletLevel2Visitor.visitType(safelet,
				null);
		
		final String output = "+++ Exploring Top Level Sequencer ";

		for (Name n : topLevelMissionSequencers)
		{
			System.out.println();
			
			System.out.println(output + n
					+ END_PLUSES);
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
				programEnv, tlmsClassEnv, analysis)
				.visitType(tlmsElement, null);

		topLevelMissionSequencer.addVariable(THIS,
				CIRCREFTYPE + tlms.toString() + CLASS, CIRCNEW
						+ tlms.toString() + CLASS_BRACKETS, false);

		getVariables(tlmsElement, tlmsClassEnv);

		if (missions == null)
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

//		System.out.println("+++ Building Mission: Full Name = " + fullName
//				+ END_PLUSES);

		TypeElement missionTypeElem = elems.getTypeElement(fullName);

		// HashMap<Name, Tree> variables =
		getVariables(missionTypeElem, missionClassEnv);

		missionEnv.addVariable(THIS, CIRCREFTYPE + n.toString()
				+ CLASS, CIRCNEW + n.toString() + CLASS_BRACKETS, true);

		ArrayList<Name> schedulables = new MissionLevel2Visitor(programEnv,
				missionEnv, analysis).visitType(missionTypeElem, null);

		if (schedulables == null)
		{
			System.out.println(NO_SCHEDULABLES);
		}
		else
		{
			buildSchedulables(missionEnv, schedulables);
		}

	}

	private void buildSchedulables(MissionEnv missionEnv,
			ArrayList<Name> schedulables)
	{

		ArrayList<Name> nestedSequencers = new ArrayList<Name>();
		for (Name s : schedulables)
		{
			SchedulableTypeE type = getSchedulableType(s);

			// TODO need to get rid of this stupid method call. I add to the
			// Cluster and to the Mission envs
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
		System.out.println(BUILDING_SCHEDULABLE + s + END_PLUSES);
		System.out.println();

		ClassEnv classEnv = new ClassEnv();
		classEnv.setName(s);

		String fullName = packagePrefix + s;
		Elements elems = analysis.ELEMENTS;

		TypeElement schedulableType = elems.getTypeElement(fullName);

		ClassTree ct = analysis.TREES.getTree(schedulableType);

		List<StatementTree> members = (List<StatementTree>) ct.getMembers();

		Iterator<StatementTree> i = members.iterator();


		ParadigmEnv schedulableEnv = programEnv.getSchedulable(s);
		schedulableEnv.addClassEnv(classEnv);

		getVariables(schedulableType, schedulableEnv);
		
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

					schedulableEnv.getClassEnv().addSyncMeth(
						methodVisitor.visitMethod(mt, null));

				}
				else if (!(mt.getName().contentEquals("<init>")))
				{
					if ((mt.getName().contentEquals("run")))
					{						
						((ManagedThreadEnv) schedulableEnv).addRunMethod(methodVisitor.visitMethod(mt, null));					
					}
					else if ((mt.getName().contentEquals("handleAsyncEvent")))
					{
						((EventHandlerEnv) schedulableEnv).addHandleAsyncMethod(methodVisitor.visitMethod(mt, null));
					}
					else
					{
						schedulableEnv.addMeth(methodVisitor.visitMethod(mt, null));
					}
				}
			}			
		}
	}

	private void buildSchedulableMissionSequencer(
			ArrayList<Name> nestedSequencers)
	{
		System.out.println();
		System.out.println(BUILDING_SCHEDULABLE_MISSION_SEQUENCERS);
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
				System.out.println(NO_MISSIONS);
			}
			else
			{
//				System.out.println(" +++ I have " + missions.size()
//						+ " missions +++ ");
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

	private HashMap<Name, Tree> getVariables(TypeElement arg0,
			ObjectEnv objectEnv)
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
			System.out.println("\t *** Tree = " + s);
			// TODO if this is only ever going to return one value at a time
			// then it shouldn't be a map
			HashMap<Name, Tree> m = (HashMap<Name, Tree>) s.accept(varVisitor,
					true);
			
			if (m == null)
			{
				System.out.println(VARIABLE_VISITOR_RETURNED_NULL);

			}
			else
			{
				// TODO this is a bit of a hack...

				for (Name n : m.keySet())
				{
//					System.out.println("\t*** Name = " + n + " Type = "
//							+ m.get(n) + " Kind = " + m.get(n).getKind());
					varMap.putIfAbsent(n, m.get(n));
				}
			}
		}

		return varMap;

	}

	
}
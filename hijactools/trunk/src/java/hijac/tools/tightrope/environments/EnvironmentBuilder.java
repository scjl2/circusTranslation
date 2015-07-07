package hijac.tools.tightrope.environments;

import hijac.tools.analysis.SCJAnalysis;
import hijac.tools.tightrope.visitors.ManagedThreadVisitor;
import hijac.tools.tightrope.visitors.MethodVisitor;
import hijac.tools.tightrope.visitors.MissionLevel2Visitor;
import hijac.tools.tightrope.visitors.MissionSequencerLevel2Visitor;
import hijac.tools.tightrope.visitors.ReturnVisitor;
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
import com.sun.source.tree.VariableTree;

public class EnvironmentBuilder
{
	public SCJAnalysis analysis;

	// private Trees trees;
	// private Set<CompilationUnitTree> units;
	private Set<TypeElement> type_elements;

	private ProgramEnv programEnv;
	private String packagePrefix;

	public EnvironmentBuilder(SCJAnalysis analysis)
	{
		this.analysis = analysis;
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
						"javax.safetycritical.ManagedThread"))
				{
					return SchedulableTypeE.MT;
				}

				if (superclass.toString().contains(
						("javax.safetycritical.MissionSequencer")))
				{
					return SchedulableTypeE.SMS;
				}

				if (superclass.toString().contains(
						"javax.safetycritical.PeriodicEventHandler"))
				{
					return SchedulableTypeE.PEH;
				}

				if (superclass.toString().contains(
						"javax.safetycritical.AperiodicEventHandler"))
				{
					return SchedulableTypeE.APEH;
				}

				if (superclass.toString().contains(
						"javax.safetycritical.OneShotEventHandler"))
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
		// First Cluster
		// programEnv.newCluster(tlms);

		TypeElement tlmsElement = analysis.ELEMENTS
				.getTypeElement(packagePrefix + tlms);

		ArrayList<Name> missions = tlmsElement.accept(
				new MissionSequencerLevel2Visitor(programEnv, programEnv.getTopLevelMissionSequencer(tlms), analysis), null);
		
		getVariables(tlmsElement, programEnv.getTopLevelMissionSequencer(tlms));

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
				programEnv.addMissionSequencerMission(tlms, n);
			
				
				
				// System.out.println("buildMission:" + n); //
				buildMission(n);
				// if(newClusterNeeded)
				// {
				// programEnv.newCluster(tlms);
				// }
				// else
				// {
				// newClusterNeeded = true;
				// }
			}

		}

	}

	private void buildMission(Name n)
	{
		System.out.println();
		System.out.println("+++ Building Mission " + n + " +++");
		System.out.println();

		programEnv.addMission(n);

		ParadigmEnv missionEnv = programEnv.getFrameworkEnv().getMission(n);

		String fullName = packagePrefix + n;
		Elements elems = analysis.ELEMENTS;
		System.out.println("Building Mission: Full Name = " + fullName);

		TypeElement missionType = elems.getTypeElement(fullName);

		HashMap<Name, Tree> variables = getVariables(missionType, missionEnv);
		
//		//TODO Here be more hack
//		for (Name varName : variables.keySet())
//		{
//			//TODO This needs to have the TypeKind, so VariableVisitor needs to find the TypeKind of the var.
//			//TODO Ideally the VariableVisitor needs to retrun a VariableEnv
//			missionEnv.addVariable(varName, variables.get(varName), null);
//		}

		ArrayList<Name> schedulables = missionType.accept(
				new MissionLevel2Visitor(programEnv, missionEnv, analysis),
				null);

		if (schedulables == null)
		{
			System.out.println("+++ No Schedulables +++");
		} else
		{
			buildSchedulables(schedulables);
			// System.out.println("Build Schedulables");
		}

	}

	private void buildSchedulables(ArrayList<Name> schedulables)
	{

		ArrayList<Name> nestedSequencers = new ArrayList<Name>();
		for (Name s : schedulables)
		{
			SchedulableTypeE type = getSchedulableType(s);
			System.out.println("+++ Adding Schedulable " + s + " +++");
			System.out.println("+++ schedulableType = " + type + " +++");

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
			// System.out.println("Build SMS");
		}
	}

	private void buildShedulable(Name s)
	{
		System.out.println();
		System.out.println("+++ Building Schedulable " + s + " +++");
		System.out.println();

		String fullName = packagePrefix + s;
		Elements elems = analysis.ELEMENTS;
		System.out.println("Building Schedulable: Full Name = " + fullName);
		TypeElement schedulableType = elems.getTypeElement(fullName);

		ClassTree ct = analysis.TREES.getTree(schedulableType);

		// ReturnVisitor rv = new ReturnVisitor(ct);
		// System.out.println("Retrun Visitor says... " +rv.getReturns());

		List<StatementTree> members = (List<StatementTree>) ct.getMembers();

		Iterator<StatementTree> i = members.iterator();

		ArrayList<Name> sycnMeths = new ArrayList<Name>();

		HashMap<Name, Tree> variables;
		
		ParadigmEnv schedulableEnv = programEnv.getSchedulable(s);

		variables = getVariables(schedulableType, schedulableEnv);
		
		while (i.hasNext())
		{

			ArrayList<Name> tmp = new ArrayList<Name>();
			
			System.out.println("\t *** variables = " + variables);

			Tree tlst = i.next();
			// System.out.println("MS Visitor i=" + ((Tree) i).getKind());

			if (tlst instanceof MethodTree)
			{
				MethodTree mt = (MethodTree) tlst;

				System.out
						.println("*** Method Name = " + mt.getName() + " ***");

//				tmp = tlst.accept(new ManagedThreadVisitor(programEnv,
//						analysis, variables, packagePrefix), null);
				
				if (mt.getModifiers().getFlags()
						.contains(Modifier.SYNCHRONIZED))
				{

					schedulableEnv.addSyncMeth(
							mt.accept(new MethodVisitor(), null));
				} else if (!(mt.getName().contentEquals("<init>") || mt.getName().contentEquals("getSequencer") || mt.getName().contentEquals("getLevel")))
				{
					schedulableEnv.addMeth(mt.accept(new MethodVisitor(), null));
				}

			}

//			if (tmp != null)
//			{
//				sycnMeths.addAll(tmp);
//			}

		}
//		System.out.println();
//		System.out.println("+++ syncMethds empty = " + (sycnMeths.size() <= 0)
//				+ " +++");
//		if (sycnMeths.size() > 0)
//		{
//			System.out.println("*** SyncMeths for " + s + " ***");
//			for (Name n : sycnMeths)
//			{
//				System.out.println("\t*** " + n + " is synchronised");
//			}
//		}
//		System.out.println();

	}

	private void buildSchedulableMissionSequencer(
			ArrayList<Name> nestedSequencers)
	{
		System.out.println();
		System.out.println("+++ Building Schedulable Mission Sequencers +++");
		System.out.println();

		for (Name sequencer : nestedSequencers)
		{
			// programEnv.newTier();

			ArrayList<Name> missions = (analysis.ELEMENTS
					.getTypeElement(packagePrefix + sequencer).accept(
					new MissionSequencerLevel2Visitor(programEnv, programEnv.getNestedMissionSequencer(sequencer) ,analysis),
					null));

			if (missions == null)
			{
				System.out.println("+++ No Missions +++");
			} else
			{
				System.out.println(" +++ I have " + missions.size()
						+ " missions +++ ");
				programEnv.newTier();
				// boolean newClusterNeeded = false;
				for (Name n : missions)
				{
					programEnv.newCluster(sequencer);
					System.out.println("+++ Exploring Mission " + n + " +++");

					programEnv.addMissionSequencerMission(sequencer, n);
					System.out.println("Build Mission: " + n);
					buildMission(n);
					// if(newClusterNeeded)
					// {
					// programEnv.newCluster(sequencer);
					// }
					// else
					// {
					// newClusterNeeded = true;
					// // }
				}
			}

		}
	}

	// public ProgramEnv build()
	// {
	//
	// Name[] names = null;
	// String packagePrefix = null;
	//
	// // for all the types in the program
	// for (TypeElement elem : type_elements)
	// {
	// // System.out.println(elem.toString());
	// String elemID = elem.toString();
	// // if the type we have is the safelet
	//
	// TypeMirror safelet_type = (TypeMirror) analysis.get(Hijac
	// .key("SafeletType"));
	//
	// //
	// if (elem.getInterfaces().toString().contains("Safelet"))
	// {
	// System.out.println("Found Safelet");
	//
	// // packagePrefix = findPackagePrefix(elem);
	//
	// programEnv.addSafelet(elem.getSimpleName());
	//
	// // add methods etc here
	// programEnv.getSafelet();
	//
	// // names = elem.accept(new SafeletLevel2Visitor(programEnv,
	// // analysis), null);
	//
	// // programEnv.getSafelet().setTLMSNames(names);
	//
	// for (int i = 0; i < names.length; i++)
	// {
	// // framework.put("TopLevelMissionSequencer", names[i]);
	// programEnv.addTopLevelMissionSequencer(names[i]);
	// }
	//
	// System.out.println(names == null);
	//
	// }
	//
	// }
	//
	// Name[] missionNames = null;
	//
	// missionNames = buildMissionSequencers(names, packagePrefix,
	// missionNames);
	//
	// Name[][] clusters = null;
	//
	// buildMissions(packagePrefix, missionNames);
	//
	// return programEnv;
	// }

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
		
		if(objectEnv != null)
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
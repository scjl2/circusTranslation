package hijac.tools.tightrope.environments;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import hijac.tools.analysis.SCJAnalysis;

import hijac.tools.config.Hijac;
import hijac.tools.tightrope.visitors.MissionLevel2Visitor;
import hijac.tools.tightrope.visitors.MissionSequencerLevel2Visitor;
import hijac.tools.tightrope.visitors.SafeletLevel2Visitor;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.Trees;
import com.sun.tools.javac.code.Attribute.Array;

public class EnvironmentBuilder
{
	public SCJAnalysis analysis;

	private Trees trees;
	private Set<CompilationUnitTree> units;
	private Set<TypeElement> type_elements;

	private ProgramEnv programEnv;
	private String packagePrefix;

	public EnvironmentBuilder(SCJAnalysis analysis)
	{
		this.analysis = analysis;
		this.programEnv = new ProgramEnv(analysis);

		trees = analysis.TREES;
		units = analysis.getCompilationUnits();
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
			programEnv.addMission(n);

			buildTopLevelMissionSequencer(analysis.ELEMENTS
					.getTypeElement(packagePrefix + n));
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

	private void buildTopLevelMissionSequencer(TypeElement tlms)
	{
		ArrayList<Name> missions = tlms.accept(
				new MissionSequencerLevel2Visitor(programEnv, analysis), null);

		if (missions == null)
		{
			System.out.println("+++ No Missions +++");
		} else
		{
			boolean newClusterNeeded = false;
			for (Name n : missions)
			{
				System.out.println("+++ Exploring Mission " + n + " +++");
				buildMission(n);
				if(newClusterNeeded)
				{
					programEnv.newCluster();
				
				}
				else
				{
					newClusterNeeded = true;
				}
			}
		}

	}

	private void buildMission(Name n)
	{
		System.out.println();
		System.out.println("+++ Building Mission " + n + " +++");
		System.out.println();

		programEnv.addMission(n);

		
		String fullName = packagePrefix + n;
		Elements elems =analysis.ELEMENTS;	
		System.out.println("Building Mission: Full Name = " + fullName);
		
		ArrayList<Name> schedulables = elems.getTypeElement(fullName
				).accept(
				new MissionLevel2Visitor(programEnv, analysis), null);
		
		if (schedulables == null)
		{
			System.out.println("+++ No Schedulables +++");
		} else
		{
			buildSchedulables(schedulables);
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

			// schedulables's type element . accept(new
			// SchedulableLevel2Visitor(programEnv, analysis), null);

			// TODO check if its a mission sequencer, if it is then call
			// programEnv.newTier and translate it...which will call build
			// mission itself

		}

		if (!nestedSequencers.isEmpty())
		{
			buildSchedulableMissionSequencer(nestedSequencers);
		}
	}

	private void buildSchedulableMissionSequencer(
			ArrayList<Name> nestedSequencers)
	{
		System.out.println();
		System.out.println("+++ Building Schedulable Mission Sequencers +++");
		System.out.println();

		for (Name sequencer : nestedSequencers)
		{
			programEnv.newTier();

			ArrayList<Name> missions = (analysis.ELEMENTS
					.getTypeElement(packagePrefix + sequencer).accept(
					new MissionSequencerLevel2Visitor(programEnv, analysis),
					null));

			if (missions == null)
			{
				System.out.println("+++ No Missions +++");
			} else
			{
				boolean newClusterNeeded = false;
				for (Name n : missions)
				{
					System.out.println("+++ Exploring Mission " + n + " +++");
					buildMission(n);
					if(newClusterNeeded)
					{
						programEnv.newCluster();
					}
					else
					{
						newClusterNeeded = true;
					}
				}
			}

		}
	}

	public ProgramEnv build()
	{

		Name[] names = null;
		String packagePrefix = null;

		// for all the types in the program
		for (TypeElement elem : type_elements)
		{
			// System.out.println(elem.toString());
			String elemID = elem.toString();
			// if the type we have is the safelet

			TypeMirror safelet_type = (TypeMirror) analysis.get(Hijac
					.key("SafeletType"));

			//
			if (elem.getInterfaces()

			.toString().contains("Safelet"))
			{
				System.out.println("Found Safelet");

				// packagePrefix = findPackagePrefix(elem);

				programEnv.addSafelet(elem.getSimpleName());

				// add methods etc here
				programEnv.getSafelet();

				// names = elem.accept(new SafeletLevel2Visitor(programEnv,
				// analysis), null);

				programEnv.getSafelet().setTLMSNames(names);

				for (int i = 0; i < names.length; i++)
				{
					// framework.put("TopLevelMissionSequencer", names[i]);
					programEnv.addTopLevelMissionSequencer(names[i]);
				}

				System.out.println(names == null);

			}

		}

		Name[] missionNames = null;

		missionNames = buildMissionSequencers(names, packagePrefix,
				missionNames);

		Name[][] clusters = null;

		buildMissions(packagePrefix, missionNames);

		return programEnv;
	}

	private String findPackagePrefix(TypeElement elem)
	{
		String packagePrefix;

		packagePrefix = elem.getQualifiedName().toString();
		int firstIndex = packagePrefix.indexOf(elem.getSimpleName().toString());
		packagePrefix = packagePrefix.substring(0, firstIndex);
		return packagePrefix;
	}

	private Name[] buildMissionSequencers(Name[] names, String packagePrefix,
			Name[] missionNames)
	{
		if (names != null)
		{
			System.out.println("Mission Sequencer Visiting");
			for (int i = 0; i < names.length; i++)
			{
				programEnv.addTopLevelMissionSequencer(names[i]);
				// System.out.println(packagePrefix +names[i]);
				TypeElement elem = analysis.getTypeElement(packagePrefix
						+ names[i]);

				System.out.println("Visiting: " + elem);
				// missionNames = elem.accept(new
				// MissionSequencerLevel2Visitor(programEnv, analysis),
				// null);
			}
		}
		return missionNames;
	}

	private void buildMissions(String packagePrefix, Name[] missionNames)
	{
		Name[] schedulables;
		if (missionNames != null)
		{
			System.out.println("Mission Visiting");
			for (int i = 0; i < missionNames.length; i++)
			{
				programEnv.addMission(missionNames[i]);
				TypeElement elem = analysis.getTypeElement(packagePrefix
						+ missionNames[i]);
				System.out.println("Visiting: " + elem);

				elem.accept(new MissionLevel2Visitor(programEnv, analysis),
						null);
			}
		}
	}
}
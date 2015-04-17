package hijac.tools.tightrope.environments;

import java.util.Set;

import hijac.tools.analysis.SCJAnalysis;

import hijac.tools.config.Hijac;
import hijac.tools.tightrope.visitors.MissionLevel2Visitor;
import hijac.tools.tightrope.visitors.MissionSequencerLevel2Visitor;
import hijac.tools.tightrope.visitors.SafeletLevel2Visitor;

import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.Trees;

public class EnvironmentBuilder
{
	public  SCJAnalysis analysis;

	private  Trees trees;
	private  Set<CompilationUnitTree> units;
	private  Set<TypeElement> type_elements;

	private ProgramEnv programEnv;

	public EnvironmentBuilder(SCJAnalysis analysis)
	{
		this.analysis = analysis;
		this.programEnv = new ProgramEnv(analysis);
		
		trees = analysis.TREES;
		units = analysis.getCompilationUnits();
		type_elements = analysis.getTypeElements();
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
		if (elem.getInterfaces().toString().contains("Safelet"))
		{
			System.out.println("Found Safelet");

			packagePrefix = findPackagePrefix(elem);

			programEnv.addSafelet(elem.getSimpleName());

			// add methods etc here
			programEnv.getSafelet();

			names = elem.accept(new SafeletLevel2Visitor(programEnv, analysis), null);
			
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

	missionNames = buildMissionSequencers(names, packagePrefix, missionNames);

	Name[][] clusters = null;

	

	buildMissions(packagePrefix, missionNames);
	
	return programEnv;
	}

	private String findPackagePrefix(TypeElement elem)
	{
		String packagePrefix;
		packagePrefix = elem.getQualifiedName().toString();
		int firstIndex = packagePrefix.indexOf(elem.getSimpleName()
				.toString());
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
				missionNames = elem.accept(new MissionSequencerLevel2Visitor(programEnv, analysis),
						null);
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

				elem.accept(new MissionLevel2Visitor(programEnv, analysis), null);
			}
		}
	}
}
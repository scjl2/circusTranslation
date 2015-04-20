package hijac.tools.tightrope.environments;

import java.util.ArrayList;
import java.util.Set;

import hijac.tools.analysis.SCJAnalysis;

import hijac.tools.config.Hijac;
import hijac.tools.tightrope.environments.FrameworkEnv.schedulableType;
import hijac.tools.tightrope.visitors.MissionLevel2Visitor;
import hijac.tools.tightrope.visitors.MissionSequencerLevel2Visitor;
import hijac.tools.tightrope.visitors.SafeletLevel2Visitor;

import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.Trees;
import com.sun.tools.javac.code.Attribute.Array;

public class EnvironmentBuilder
{
	public  SCJAnalysis analysis;

	private  Trees trees;
	private  Set<CompilationUnitTree> units;
	private  Set<TypeElement> type_elements;

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
	

	
	public ProgramEnv explore()
	{		
		ArrayList<Name> topLevelMissionSequners = buildSafelet(getSafelet());
		
		for (Name n : topLevelMissionSequners)
		{
			programEnv.addMission(n);
	
			buildTopLevelMissionSequencer(analysis.ELEMENTS.getTypeElement(packagePrefix+n));
		}
		
		
		return programEnv;
	}
	
	
	private TypeElement getSafelet()
		{
			
			TypeElement safelet = null;
			for (TypeElement elem : type_elements)
			{	
				//TODO needs to be made safer. I think this might fall over if presented with multiple interfaces
				if (elem.getInterfaces().toString().contains("Safelet"))
				{
					System.out.println("Found Safelet");
					safelet = elem;
					packagePrefix = findPackagePrefix(elem);
	
					programEnv.addSafelet(safelet.getSimpleName());
	
					// add methods etc here
	//				programEnv.a
	
					//TODO Hack, needs to return all the tlms
							
					
					
					
//					programEnv.addTopLevelMissionSequencers(topLevelMissionSequencer);
					
	//				programEnv.getSafelet().setTLMSNames(names);
	//
	//				for (int i = 0; i < names.length; i++)
	//				{
	//					// framework.put("TopLevelMissionSequencer", names[i]);
	//					programEnv.addTopLevelMissionSequencer(names[i]);
	//				}
	
	//				System.out.println(names == null);
	
				
				}
	
			}
			return safelet;
		}



	private ArrayList<Name> buildSafelet(TypeElement safelet)
	{
		ArrayList<Name> topLevelMissionSequencers;
		topLevelMissionSequencers=	safelet.accept(new SafeletLevel2Visitor(programEnv, analysis), null);
		for(Name n : topLevelMissionSequencers )
		{
			
			programEnv.addTopLevelMissionSequencer(n);
		}
		return topLevelMissionSequencers;
	}

	private void buildTopLevelMissionSequencer(TypeElement tlms)
	{
		ArrayList<Name> missions = tlms.accept(new MissionSequencerLevel2Visitor(programEnv, analysis), null);
	
		if(missions == null )
		{
			System.out.println("No Missions");
		}
		else
		{
			for (Name n : missions)
			{
				buildMission(n);
				programEnv.newCluster();
			}
		}
	
		
		
		
	}

	private void buildMission(Name n)
	{
		programEnv.addMission(n);
		
		ArrayList<Name> schedulables = analysis.ELEMENTS.getTypeElement(packagePrefix+n).accept(new MissionLevel2Visitor(programEnv, analysis), null);
		
	
			
		buildSchedulables(schedulables);

	}

	private void buildSchedulables(ArrayList<Name> schedulables)
	{
		for(Name s : schedulables)
		{
			programEnv.addSchedulable(getSchedulableType(s), s);
//			schedulables's type element . accept(new SchedulableLevel2Visitor(programEnv, analysis), null);
			
			//TODO check if its a mission sequencer, if it is then call programEnv.newTier and translate it...which will call build mission itself
			
		}
	}

	private schedulableType getSchedulableType(Name s)
	{
		// TODO Auto-generated method stub
		return null;
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

//			packagePrefix = findPackagePrefix(elem);

			programEnv.addSafelet(elem.getSimpleName());

			// add methods etc here
			programEnv.getSafelet();

//			names = elem.accept(new SafeletLevel2Visitor(programEnv, analysis), null);
			
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
//				missionNames = elem.accept(new MissionSequencerLevel2Visitor(programEnv, analysis),
//						null);
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
package hijac.tools.tightrope.environments;

import hijac.tools.analysis.SCJAnalysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Name;

import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;

public class ProgramEnv
{
	FrameworkEnv structureEnv;
	List<NonParadigmEnv> nonParadigmObjectEnvs;
	private HashMap<Name, Tree> variables = new HashMap<Name, Tree>();

	MissionIdsEnv missionIds;
	SchedulableIdsEnv schedulableIds;

	public ProgramEnv(SCJAnalysis context)
	{
		this.structureEnv = new FrameworkEnv();
		this.nonParadigmObjectEnvs = new ArrayList<NonParadigmEnv>();

		missionIds = new MissionIdsEnv();
		schedulableIds = new SchedulableIdsEnv();
	}

	public void addSafelet(Name safelet)
	{
		structureEnv.addSafelet(safelet);
	}

	public void addTopLevelMissionSequencer(Name topLevelMissionSequencer)
	{
		structureEnv.addTopLevelMissionSequencer(topLevelMissionSequencer);
		schedulableIds.addTopLevelSequencer(topLevelMissionSequencer);
	}

	public void addMission(Name mission)
	{
		structureEnv.addMission(mission);
		missionIds.addMission(mission);
	}

	public FrameworkEnv getFrameworkEnv()
	{
		return structureEnv;
	}

	public List<NonParadigmEnv> getNonParadigmObjectEnvs()
	{
		return nonParadigmObjectEnvs;
	}

	public SafeletEnv getSafelet()
	{
		return structureEnv.getControlTier().getSafeletEnv();
	}

	public void output()
	{
		System.out.println("*******************");
		System.out.println("Program Environment");
		System.out.println("-------------------");
		System.out.println("Structure Environment");
		System.out.println("_____________________");
		System.out.println(structureEnv.toString());
		System.out.println("----------------------");
		System.out.println("Non-Framework Environments");
		System.out.println("___________________________");
		System.out.println(nonParadigmObjectEnvs.toString());
		System.out.println("--------------------------------");
		System.out.println("Variables");
		System.out.println("---------");
		for (Name n : variables.keySet())
		{
			System.out.println(n + " -> " + variables.get(n));
		}

	}

	public void addSchedulable(SchedulableTypeE type, Name name)
	{
		structureEnv.addSchedulable(type, name);
		schedulableIds.addSchedulable(name);
	}

	public boolean containsScheudlable(Name name)
	{
		return structureEnv.containsSchedulable(name);
	}

	public void addVariable(Name name, Tree type)
	{
		variables.put(name, type);
	}

	public Tree getVariable(Name name)
	{
		return variables.get(name);
	}

	public void newTier()
	{
		System.out.println("+++ New Tier +++");
		System.out.println();

		structureEnv.newTier();
	}

	public void newCluster(Name sequencer)
	{
		System.out.println("+++ New Cluster +++");
		System.out.println();

		structureEnv.newCluster(sequencer);
	}

	public ArrayList<TopLevelMissionSequencerEnv> getTopLevelMissionSequencers()
	{
		ArrayList<TopLevelMissionSequencerEnv> returnList = new ArrayList<TopLevelMissionSequencerEnv>();

		returnList.add(structureEnv.getControlTier()
				.getTopLevelMissionSequencerEnv());

		return returnList;
	}

	public ArrayList<MissionEnv> getMissions()
	{
		return structureEnv.getMissions();
	}

	public void addMissionSequencerMission(Name tlms, Name n)
	{
		structureEnv.addMissionSequencerMission(tlms, n);
	}

	public ArrayList<ManagedThreadEnv> getManagedThreads()
	{
		return structureEnv.getManagedThreads();
	}

	@SuppressWarnings("rawtypes")
	public Map geNetworkMap()
	{
		return structureEnv.getNetworkMap();
	}

	public ArrayList<NestedMissionSequencerEnv> getNestedMissionSequencers()
	{
		return structureEnv.getNestedMissionsequencers();
	}

	public ArrayList<OneShotEventHandlerEnv> getOneShotEventHandlers()
	{
		return structureEnv.getOneShotEventHandlers();
	}

	public ArrayList<PeriodicEventHandlerEnv> getPeriodicEventHandlers()
	{
		return structureEnv.getPeriodicEventHandlers();
	}

	public ArrayList<AperiodicEventHandlerEnv> getAperiodicEventHandlers()
	{
		return structureEnv.getAperiodicEventHandlers();
	}

	@SuppressWarnings("rawtypes")
	public Map getMissionIdsMap()
	{
		return missionIds.toMap();
	}

	@SuppressWarnings("rawtypes")
	public Map getSchedulableIdsMap()
	{
		return schedulableIds.toMap();
	}

	public ParadigmEnv getTopLevelMissionSequencer(Name tlms)
	{
		for (TopLevelMissionSequencerEnv tlmsEnv : getTopLevelMissionSequencers())
		{
			if (tlmsEnv.getName().contentEquals(tlms))
			{
				return tlmsEnv;
			}
		}

		return null;
	}

	public ParadigmEnv getSchedulable(Name s)
	{
		for(ParadigmEnv obj : getSchedulables())
		{
			if(obj.getName().contentEquals(s))
			{
				return obj;
			}
		}
		return null;
	}

	private List<ParadigmEnv> getSchedulables()
	{
		ArrayList<ParadigmEnv> schedulables = new ArrayList<ParadigmEnv>();
		
		schedulables.addAll(getPeriodicEventHandlers());
		schedulables.addAll(getAperiodicEventHandlers());
		schedulables.addAll(getOneShotEventHandlers());
		schedulables.addAll(getNestedMissionSequencers());
		schedulables.addAll(getManagedThreads());
				
		return schedulables;
	}

	public NestedMissionSequencerEnv getNestedMissionSequencer(Name sequencer)
	{
		for(NestedMissionSequencerEnv n : getNestedMissionSequencers())
		{
			if(n.getName().contentEquals(sequencer))
			{
				return n;
			}
		}
		
		return null;
	}

	public ObjectEnv getObjectEnv(String objectName)
	{
		ArrayList<ObjectEnv> objects = new ArrayList<ObjectEnv>();
		objects.addAll(	getMissions() );
		objects.addAll(getTopLevelMissionSequencers());
		objects.addAll(getNestedMissionSequencers());
		objects.addAll(getSchedulables());
		
		
		
		for(ObjectEnv o : objects)
		{
			if(o.getName().contentEquals(objectName))
			{
				return o;
			}
		}
		
		return null;
	}

}

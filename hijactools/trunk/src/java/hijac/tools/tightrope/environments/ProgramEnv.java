package hijac.tools.tightrope.environments;

import hijac.tools.analysis.SCJAnalysis;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;

import javax.lang.model.element.Name;

public class ProgramEnv
{
	private FrameworkEnv structureEnv;
	private List<NonParadigmEnv> nonParadigmObjectEnvs;

	private MissionIdsEnv missionIds;
	private SchedulableIdsEnv schedulableIds;
	private ThreadIdsEnv threadIds;
	private ObjectIdsEnv objectIds;

	public ProgramEnv(SCJAnalysis context)
	{
		this.structureEnv = new FrameworkEnv();
		this.nonParadigmObjectEnvs = new ArrayList<NonParadigmEnv>();

		missionIds = new MissionIdsEnv();
		schedulableIds = new SchedulableIdsEnv();
		threadIds = new ThreadIdsEnv();
		objectIds = new ObjectIdsEnv();
	}

	public void addSafelet(Name safelet)
	{
		structureEnv.addSafelet(safelet);
		objectIds.addIdNames(safelet.toString());
	}

	public void addTopLevelMissionSequencer(Name topLevelMissionSequencer)
	{
		structureEnv.addTopLevelMissionSequencer(topLevelMissionSequencer);
		schedulableIds.addTopLevelSequencer(topLevelMissionSequencer);
	}

	public void addMission(Name mission)
	{
		structureEnv.addMission(mission);
		final String missionString = mission.toString();
		missionIds.addIdNames(missionString);
		objectIds.addIdNames(missionString);
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

	}

	public void addSchedulable(SchedulableTypeE type, Name name)
	{
		structureEnv.addSchedulable(type, name);

		final String nameString = name.toString();

		schedulableIds.addIdNames(nameString);
		threadIds.addIdNames(nameString);
		objectIds.addIdNames(nameString);
	}

	public boolean containsScheudlable(Name name)
	{
		return structureEnv.containsSchedulable(name);
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map geNetworkMap()
	{
		Map returnMap = structureEnv.getNetworkMap();
		returnMap.put("Objects", getObjectIdsMap());
		returnMap.put("Threads", getThreadIdsMap());
		
		return returnMap;
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

	public TopLevelMissionSequencerEnv getTopLevelMissionSequencer(Name tlms)
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

	/**
	 * Gets the <code>ParadigmEnv</code> that represents the schedulable sharing
	 * a name with the parameter. May return <code>null</code>
	 * 
	 * @param name
	 *            The name of the schedulable that we're looking for, as a
	 *            <code>Name</code>
	 * @return The <code>ParadigmEnv</code> we're looking for, or
	 *         <code>null</code>
	 */
	public ParadigmEnv getSchedulable(Name name)
	{
		for (ParadigmEnv obj : getSchedulables())
		{
			if (obj.getName().contentEquals(name))
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
		for (NestedMissionSequencerEnv n : getNestedMissionSequencers())
		{
			if (n.getName().contentEquals(sequencer))
			{
				return n;
			}
		}

		return null;
	}

	/**
	 * Gets the object environment within this program environment that shares
	 * the same name as the parameter, if it does not exists this method will
	 * return <code>null</code>. Internally, this method calls
	 * <code>getObjectEnv(String objectName)</code>.
	 * 
	 * @param objectName
	 *            The name of the object we're looking for
	 * @return The <code>ObjectEnv</code> representing the object we're looking
	 *         for, or <code>null</code>
	 */
	public ObjectEnv getObjectEnv(Name objectName)
	{
		return getObjectEnv(objectName.toString());
	}

	/**
	 * Gets the object environment within this program environment that shares
	 * the same name as the parameter, if it does not exists this method will
	 * return <code>null</code>
	 * 
	 * @param objectName
	 *            The name of the object we're looking for
	 * @return The <code>ObjectEnv</code> representing the object we're looking
	 *         for, or <code>null</code>
	 */
	public ObjectEnv getObjectEnv(String objectName)
	{
		ArrayList<ObjectEnv> objects = new ArrayList<ObjectEnv>();
		objects.addAll(getMissions());
		objects.addAll(getTopLevelMissionSequencers());
		objects.addAll(getNestedMissionSequencers());
		objects.addAll(getSchedulables());

		for (ObjectEnv o : objects)
		{
			if (o.getName().contentEquals(objectName))
			{
				return o;
			}
		}

		return null;
	}

	@SuppressWarnings("rawtypes")
	public Map getThreadIdsMap()
	{
		return threadIds.toMap();
	}

	@SuppressWarnings({ "rawtypes"})
	public Map getObjectIdsMap()
	{
		return objectIds.toMap();
	}

};

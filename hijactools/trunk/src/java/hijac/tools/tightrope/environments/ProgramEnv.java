package hijac.tools.tightrope.environments;

import hijac.tools.analysis.SCJAnalysis;
import hijac.tools.tightrope.utils.Debugger;
import hijac.tools.tightrope.utils.TightRopeString;
import hijac.tools.tightrope.utils.TightRopeString.LATEX;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Name;

public class ProgramEnv
{
	private StructureEnv structureEnv;
	private List<NonParadigmEnv> nonParadigmObjectEnvs;
	private Map<String, MethodEnv> binderMethodEnvs;

	private List<MethodEnv> globalMethods;

	private IdEnv missionIds;
	private SchedulableIdsEnv schedulableIds;
	private ThreadIdsEnv threadIds;
	private ObjectIdsEnv objectIds;

	public ProgramEnv(SCJAnalysis context)
	{
		this.structureEnv = new StructureEnv();
		this.nonParadigmObjectEnvs = new ArrayList<NonParadigmEnv>();
		this.binderMethodEnvs = new HashMap<String, MethodEnv>();

		globalMethods = new ArrayList<MethodEnv>();

		missionIds = new MissionIdsEnv();
		schedulableIds = new SchedulableIdsEnv();
		threadIds = new ThreadIdsEnv();
		objectIds = new ObjectIdsEnv();
	}

	public void addSafelet(Name safelet)
	{
		structureEnv.addSafelet(safelet);
		// objectIds.addIdNames(safelet.toString());
	}

	public void addTopLevelMissionSequencer(Name topLevelMissionSequencer)
	{
		structureEnv.addTopLevelMissionSequencer(topLevelMissionSequencer);
		schedulableIds.addTopLevelSequencer(topLevelMissionSequencer);
		// objectIds.addIdNames(topLevelMissionSequencer.toString());
	}

	public void addMission(Name mission)
	{
		structureEnv.addMission(mission);
		final String missionString = mission.toString();
		missionIds.addIdNames(missionString);

	}

	public void addObjectIdName(String name)
	{
		objectIds.addIdNames(name);
	}

	public void addThreadIdName(String name)
	{
		threadIds.addIdNames(name);
	}

	public StructureEnv getStructureEnv()
	{
		return structureEnv;
	}

	public List<NonParadigmEnv> getNonParadigmObjectEnvs()
	{
		return nonParadigmObjectEnvs;
	}
	
	public void addNonParadigmObjectEnv(NonParadigmEnv npe)
	{
	  if(npe != null)
	  {
	    nonParadigmObjectEnvs.add(npe);
	  }
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
		System.out.println("Non-Paradigms Environments");
		System.out.println("___________________________");
		System.out.println(stringNonParadigmEnvs());
		System.out.println("--------------------------------");
		System.out.println("Variables");

	}
	
	public String stringNonParadigmEnvs()
	{
	  String npeString = "";
	  final String LINE_SEPARATOR = System.getProperty("line.separator");
	  
	  for(NonParadigmEnv npe : nonParadigmObjectEnvs)
	  {
	    npeString += "Name: " + npe.getName();
	    npeString += (LINE_SEPARATOR);
	    npeString += "Variables:";
	     npeString += (LINE_SEPARATOR);
	    for(VariableEnv v : npe.getVariables())
	    {
	      npeString+= "\tName: "+ v.getName() + " Type: " + v.getType();
	      npeString += (LINE_SEPARATOR);
	    }
	    npeString += "Methods:";
	     npeString += (LINE_SEPARATOR);
	    for(MethodEnv m : npe.getMeths())
	    {
	      npeString+="\tName: " + m.getName();
	      npeString += (LINE_SEPARATOR);
	    }
	    npeString += "Synchronised Methods:";
	     npeString += (LINE_SEPARATOR);
      for(MethodEnv m : npe.getSyncMeths())
      {
        npeString+="\tName: " + m.getName();
        npeString += (LINE_SEPARATOR);
      }
      npeString += (LINE_SEPARATOR);
	  }
	  
	  return npeString;
	}

	public void addSchedulable(SchedulableTypeE type, Name name)
	{
		structureEnv.addSchedulable(type, name);

		final String nameString = name.toString();

		schedulableIds.addIdNames(nameString);

		// TODO this should only happen if there is a Wait or Notify or
		// Synchronised method
		// if()

		// threadIds.addIdNames(nameString);
		// objectIds.addIdNames(nameString);

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

		returnList.add(structureEnv.getControlTier().getTopLevelMissionSequencerEnv());

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
		returnMap.put("NPE", getNonParadigmEnvList());
		

		returnMap.put("AppProcNames", getAppProcNamesList());

		returnMap.put("Objects", getObjectIdsMap());
		returnMap.put("Threads", getThreadIdsMap());
		returnMap.put("MethodCallBindings", getBinderMethodEnvsMapList());

		return returnMap;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
  private List getNonParadigmEnvList()
  {
    List npeList = new ArrayList();
    
    for(NonParadigmEnv npe : getNonParadigmObjectEnvs())
    {
      Map npeMap = new HashMap();
      Debugger.log("NonP " + npe.getName() +" into map");
      npeMap.put("Name", npe.getName());
//      npeMap.put();
      npeList.add(npeMap);
    }
    
    return npeList;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
	private List getAppProcNamesList()
	{
		List appProcNames = new ArrayList();
		String app = "App";

		appProcNames.add(getSafelet().getName() + app);

		for (ObjectEnv p : getTopLevelMissionSequencers())
		{
			appProcNames.add(p.getName() + app);
		}

		for (ObjectEnv p : getSchedulables())
		{
			appProcNames.add(p.getName() + app);
		}

		for (ObjectEnv p : getMissions())
		{
			appProcNames.add(p.getName() + app);
		}

		return appProcNames;
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
	private List getBinderMethodEnvsMapList()
	{
		List binderMethodEnvsMap = new ArrayList();

		for (MethodEnv b : binderMethodEnvs.values())
		{
			Map binderMethodMap = new HashMap();

			binderMethodMap.put("Name", b.getName());
			binderMethodMap.put(TightRopeString.Name.CHANNEL_NAME, b.getEventName());
			binderMethodMap.put("Locations", b.getLocations());
			binderMethodMap.put("Callers", b.getCallers());
			binderMethodMap.put("Return", b.hasReturn());
			binderMethodMap.put("ReturnType", b.getReturnType());
			binderMethodMap.put("Parameters", b.getParameters());
			binderMethodMap.put("ReturnValue", b.getReturnValue());
			binderMethodMap.put("LocType", b.getLocationType());
			binderMethodMap.put("CallerType", b.getCallerType());
			binderMethodMap.put("Sync", b.isSynchronised());

			binderMethodEnvsMap.add(binderMethodMap);
		}

		ArrayList<String> tempStrings = new ArrayList<String>();
		String methChan = "MethChan";

		for (Object o : binderMethodEnvsMap)
		{
			Map b = (Map) o;

			Set<String> locs = (Set<String>) b.get("Locations");

			// TODO Will break if the same method is in two locations?
			for (String l : locs)
			{
				String locParentString = l.substring(0, l.length() - 3) + methChan;

				if (!tempStrings.contains(locParentString))
				{
					b.put("LocParent", locParentString);
					tempStrings.add(locParentString);
				}
			}
		}

		return binderMethodEnvsMap;
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
	public ObjectEnv getSchedulable(Name name)
	{
		for (ObjectEnv obj : getSchedulables())
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
		objects.addAll(nonParadigmObjectEnvs);

		for (ObjectEnv o : objects)
		{
		  Debugger.log("Object Name = " +o.getName());
			if (o.getName().contentEquals(objectName))
			{
				return o;
			}
		}

		return null;
	}

	@SuppressWarnings({ "rawtypes" })
	public Map getThreadIdsMap()
	{
		return threadIds.toMap();
	}

	@SuppressWarnings({ "rawtypes" })
	public Map getObjectIdsMap()
	{
		return objectIds.toMap();
	}

	public void setThreadPriority(String threadID, String priority)
	{
		// if (getObjectEnv(threadID).hasSyncMeth())
		// {
		threadIds.setThreadPriority(threadID, priority);
		// }

	}

	public Map<String, MethodEnv> getBinderMethodEnvs()
	{
		return binderMethodEnvs;
	}

	@Deprecated
	public void addBinderMethodEnv(String name, String location, String caller)
	{
		addBinderMethodEnv(name, location, caller, null, null);
	}

	public IdEnv getMissionIdsEnv()
	{
		return missionIds;
	}

	// not adding anything to the list
	@Deprecated
	public void addBinderMethodEnv(String name, String location, String caller,
			String returnType, Map<String, Type> parameters)
	{
		String id = "";
		boolean existingBME = false;
		for (MethodEnv me : binderMethodEnvs.values())
		{
			if (me.getName().equals(name))
			{
				me.addLocation(location + id);
				me.addCaller(caller + id);
				me.setParameters(parameters);
				// me.setLocationType(location);

				existingBME = true;
			}
		}
	}

	public void addBinderMethodEnv(MethodEnv method, String location, String caller,
			String callerType)
	{
		String name = method.getName();

		if (binderMethodEnvs.containsKey(name))
		{
			MethodEnv existingmethod = binderMethodEnvs.get(name);

			existingmethod.addLocation(location);
			existingmethod.addCaller(caller);
			existingmethod.setCallerType(callerType);

			binderMethodEnvs.put(name, existingmethod);
		}
		else
		{
			method.addLocation(location);
			method.addCaller(caller);
			method.setCallerType(callerType);

			binderMethodEnvs.put(name, method);
		}
	}

	public List<MethodEnv> getCustomChannels()
	{
		return globalMethods;
	}

	public void addGlobalMethod(MethodEnv method)
	{

		for (MethodEnv m : globalMethods)
		{
			if (m.getName().equals(method.getName()))
			{
				method.setEventName(method.getEventName() + LATEX.UNDERSCORE
						+ method.getMethodLocation().getName().toString());

				m.setEventName(m.getEventName() + LATEX.UNDERSCORE
						+ m.getMethodLocation().getName().toString());
			}
		}
	}


}

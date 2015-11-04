package hijac.tools.tightrope.environments;

import java.util.ArrayList;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Name;

public class FrameworkEnv
{
	private static final String LINE_SEPARATOR = System
			.getProperty("line.separator");

	private ControlTierEnv controlTier;

	private List<TierEnv> tiers;

	private TierEnv currentTier;
	private ClusterEnv currentCluster;
	
	
	class ControlTierEnv
	{

		private SafeletEnv safeletEnv;
		private TopLevelMissionSequencerEnv topLevelMissionSequencerEnv;

		public ControlTierEnv()
		{
			safeletEnv = new SafeletEnv();
			topLevelMissionSequencerEnv = new TopLevelMissionSequencerEnv();
		}

		public SafeletEnv getSafeletEnv()
		{
			return safeletEnv;
		}

		public void setSafeletEnv(SafeletEnv safeletEnv)
		{
			this.safeletEnv = safeletEnv;
		}

		public TopLevelMissionSequencerEnv getTopLevelMissionSequencerEnv()
		{
			return topLevelMissionSequencerEnv;
		}

		public void setTopLevelMissionSequencerEnv(
				TopLevelMissionSequencerEnv topLevelMissionSequencerEnv)
		{
			this.topLevelMissionSequencerEnv = topLevelMissionSequencerEnv;
		}

		public void addSafelet(Name safelet)
		{
			this.safeletEnv.setName(safelet);
		}

		public void addTopLevelMissionSequencer(Name topLevelMissionSequencer)
		{
			this.topLevelMissionSequencerEnv.setName(topLevelMissionSequencer);
			safeletEnv.addTopLevelMissionSequencer(topLevelMissionSequencer);
		}

		public String toString()
		{
			String output = "";
			output +=  "\t Safelet: " + safeletEnv.getName();
			output += LINE_SEPARATOR;
			output += "\t Safelet Methods:";
			output += LINE_SEPARATOR;
			List<MethodEnv> methods = safeletEnv.getMeths();
			// TODO Output sync meths?
			output = outputMethods(output, methods);
			
			
			output += LINE_SEPARATOR;
			output += "\t Top-Level Mission Sequencer: "
					+ topLevelMissionSequencerEnv.getName();
			output += LINE_SEPARATOR;
			output += "\t Top Level Sequencer Methods:";
			output += LINE_SEPARATOR;

			methods = topLevelMissionSequencerEnv.getMeths();
			output = outputMethods(output, methods);

			return output;

		}

		

		@SuppressWarnings({ "rawtypes", "unchecked" })
		public Map toMap()
		{

			Map clusterMap = new HashMap();
			clusterMap.put("safelet", safeletEnv.getName());

			List topLevelSequencersList = new ArrayList();

			// for(TopLevelMissionSequencerEnv t : )
			topLevelSequencersList.add(topLevelMissionSequencerEnv.getName());

			clusterMap.put("topLevelSequencers", topLevelSequencersList);

			return clusterMap;
		}
	}

	private class TierEnv
	{
		private List<ClusterEnv> clusters;

		public TierEnv()
		{
			clusters = new ArrayList<ClusterEnv>();
			currentCluster = null;
		}

		public void addCluster(ClusterEnv cluster)
		{
			this.clusters.add(cluster);
		}

		public ArrayList<Name> getMissions()
		{
			ArrayList<Name> missions = new ArrayList<Name>();

			for (ClusterEnv c : clusters)
			{
				missions.add(c.getMissionEnv().getName());
			}

			return missions;
		}

		public ArrayList<Name> getMissionSequencers()
		{
			ArrayList<Name> misssionSequeners = new ArrayList<Name>();

			for (ClusterEnv c : clusters)
			{
				for (ObjectEnv p : c.getSchedulablesEnv()
						.getSchedulableMissionSequencerEnvs())
				{
					misssionSequeners.add(p.getName());
				}
			}

			if (misssionSequeners.isEmpty())
			{
				return null;
			}
			else
			{
				return misssionSequeners;
			}
		}

		public String toString()
		{
			if (clusters.isEmpty())
			{
				return "\tTier Empty";
			}
			else
			{
				String output = "";
				for (ClusterEnv c : clusters)
				{
					output += c.toString();
				}

				return output;
			}

		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		public List toList()
		{
			List clusterList = new ArrayList();

			for (ClusterEnv c : clusters)
			{
				Map clusterMap = new HashMap();
				clusterMap.put("Sequencer", c.getSequencer());

				Map missionMap = new HashMap();
				missionMap.put("Name", c.getMissionEnv().getName());
				missionMap.put("Parameters", c.getMissionEnv().getParameters());

				clusterMap.put("Mission", missionMap);
				clusterMap.put("Mission_HasClass", c.getMissionEnv().isHasClass());

				Map schedulablesMap = new HashMap();
				schedulablesMap.put("Periodics", c.getSchedulablesEnv()
						.getPeriodicsList());
				schedulablesMap.put("Aperiodics", c.getSchedulablesEnv()
						.getAperiodicsList());
				schedulablesMap.put("Oneshots", c.getSchedulablesEnv()
						.getOneshotsList());
				schedulablesMap.put("NestedSequencers", c.getSchedulablesEnv()
						.getNestedSequencersList());
				schedulablesMap.put("Threads", c.getSchedulablesEnv()
						.getThreadsList());

				clusterMap.put("Schedulables", schedulablesMap);

				clusterList.add(clusterMap);
			}

			return clusterList;
		}

	}

	private class ClusterEnv
	{
		private Name sequencer;
		private MissionEnv missionEnv;
		private SchedulablesEnv schedulablesEnv;

		public ClusterEnv(Name sequencer)
		{
			missionEnv = new MissionEnv();
			schedulablesEnv = new SchedulablesEnv();
			this.sequencer = sequencer;
		}

		public Name getSequencer()
		{
			return sequencer;
		}

		public MissionEnv getMissionEnv()
		{
			return missionEnv;
		}

		public SchedulablesEnv getSchedulablesEnv()
		{
			return schedulablesEnv;
		}

		public void addSchedulable(SchedulableTypeE type, Name name)
		{
			schedulablesEnv.addSchedulable(type, name);
		}

		public String toString()
		{
			String output = "\tCluster Environment:";
			output += LINE_SEPARATOR;
			output += "\t\t Mission: " + missionEnv.getName();
			output += LINE_SEPARATOR;
			output += "\t\t Mission Methods:";
			output += LINE_SEPARATOR;
			List<MethodEnv> methods = new ArrayList<MethodEnv>();
				methods.addAll(	missionEnv.getMeths());
				methods.addAll(missionEnv.getSyncMeths());			
			output = outputMethods(output, methods);
			
			output += schedulablesEnv.toString();

			return output;

		}
	}

	private class SchedulablesEnv
	{

		private List<PeriodicEventHandlerEnv> periodEventHandlerEnvs = new ArrayList<PeriodicEventHandlerEnv>();
		private List<AperiodicEventHandlerEnv> aperiodicEventHandlerEnvs = new ArrayList<AperiodicEventHandlerEnv>();
		private List<OneShotEventHandlerEnv> oneShotEventHandlerEnvs = new ArrayList<OneShotEventHandlerEnv>();
		private List<NestedMissionSequencerEnv> schedulableMissionSequencerEnvs = new ArrayList<NestedMissionSequencerEnv>();
		private List<ManagedThreadEnv> managedThreadEnvs = new ArrayList<ManagedThreadEnv>();

		public List<PeriodicEventHandlerEnv> getPeriodEventHandlerEnvs()
		{
			return periodEventHandlerEnvs;
		}

		@SuppressWarnings("rawtypes")
		public List getAperiodicsList()
		{
			return toList(SchedulableTypeE.APEH);
		}

		@SuppressWarnings("rawtypes")
		public List getThreadsList()
		{
			return toList(SchedulableTypeE.MT);
		}

		@SuppressWarnings("rawtypes")
		public List getNestedSequencersList()
		{
			return toList(SchedulableTypeE.SMS);
		}

		@SuppressWarnings("rawtypes")
		public List getOneshotsList()
		{
			return toList(SchedulableTypeE.OSEH);
		}

		@SuppressWarnings("rawtypes")
		public List getPeriodicsList()
		{
			return toList(SchedulableTypeE.PEH);
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		private List toList(SchedulableTypeE type)
		{
			List schedulablesList = new ArrayList();

			switch (type)
			{
				case PEH:
					for (ObjectEnv p : periodEventHandlerEnvs)
					{
						Map sMap = makeSchedulableMap(p);

						schedulablesList.add(sMap);
					}
					break;

				case APEH:
					for (ObjectEnv p : aperiodicEventHandlerEnvs)
					{
						Map sMap = makeSchedulableMap(p);

						schedulablesList.add(sMap);
					}
					break;

				case OSEH:
					for (ObjectEnv p : oneShotEventHandlerEnvs)
					{
						Map sMap = makeSchedulableMap(p);

						schedulablesList.add(sMap);
					}
					break;

				case SMS:
					for (ObjectEnv p : schedulableMissionSequencerEnvs)
					{
						Map sMap = makeSchedulableMap(p);

						schedulablesList.add(sMap);
					}
					break;

				case MT:
					for (ObjectEnv p : managedThreadEnvs)
					{
						Map sMap = makeSchedulableMap(p);

						schedulablesList.add(sMap);
					}
					break;
			}
			return schedulablesList;
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		private Map makeSchedulableMap(ObjectEnv p)
		{
			Map sMap = new HashMap();
			sMap.put("Name", p.getName());
			
			ArrayList<String> params = new ArrayList<String>();
			for(VariableEnv v : p.getParameters())
			{
				//This adds the type from the program E.G. FlatBufferMission not MissionID
				params.add(v.getProgramType());
			}
			
			sMap.put("Parameters", params);
			return sMap;
		}

		public List<AperiodicEventHandlerEnv> getAperiodicEventHandlerEnvs()
		{
			return aperiodicEventHandlerEnvs;
		}

		public List<OneShotEventHandlerEnv> getOneShotEventHandlerEnvs()
		{
			return oneShotEventHandlerEnvs;
		}

		public List<NestedMissionSequencerEnv> getSchedulableMissionSequencerEnvs()
		{
			return schedulableMissionSequencerEnvs;
		}

		public List<ManagedThreadEnv> getManagedThreadEnvs()
		{
			return managedThreadEnvs;
		}

		public void addSchedulable(SchedulableTypeE type, Name name)
		{
			if (type == SchedulableTypeE.MT)
			{
				ManagedThreadEnv mtEnv = new ManagedThreadEnv();
				mtEnv.setName(name);
				managedThreadEnvs.add(mtEnv);
			}
			if (type == SchedulableTypeE.PEH)
			{
				PeriodicEventHandlerEnv p = new PeriodicEventHandlerEnv();
				p.setName(name);
				periodEventHandlerEnvs.add(p);
			}
			if (type == SchedulableTypeE.APEH)
			{
				AperiodicEventHandlerEnv a = new AperiodicEventHandlerEnv();
				a.setName(name);
				aperiodicEventHandlerEnvs.add(a);
			}
			if (type == SchedulableTypeE.OSEH)
			{
				OneShotEventHandlerEnv osehEnv = new OneShotEventHandlerEnv();
				osehEnv.setName(name);
				oneShotEventHandlerEnvs.add(osehEnv);
			}
			if (type == SchedulableTypeE.SMS)
			{
				NestedMissionSequencerEnv nmsEnv = new NestedMissionSequencerEnv();
				nmsEnv.setName(name);
				schedulableMissionSequencerEnvs.add(nmsEnv);
			}
		}

		public String toString()
		{

			StringBuilder output = new StringBuilder("\t\tSchedulables:");
			output.append(LINE_SEPARATOR);

			if (!periodEventHandlerEnvs.isEmpty())
			{
				output.append("\t\t\tPeriodic Event Handlers:");
				output.append(LINE_SEPARATOR);

				for (PeriodicEventHandlerEnv p : periodEventHandlerEnvs)
				{
					output.append("\t\t\t\t" + p.getName());
					output.append(LINE_SEPARATOR);
				}
			}

			if (!aperiodicEventHandlerEnvs.isEmpty())
			{
				output.append("\t\t\tAperidic Event Handlers:");
				output.append(LINE_SEPARATOR);

				for (ObjectEnv p : aperiodicEventHandlerEnvs)
				{
					output.append("\t\t\t\t" + p.getName());
					output.append(LINE_SEPARATOR);
				}
			}

			if (!oneShotEventHandlerEnvs.isEmpty())
			{
				output.append("\t\t\tOneShot Event Handlers:");
				output.append(LINE_SEPARATOR);

				for (OneShotEventHandlerEnv p : oneShotEventHandlerEnvs)
				{
					output.append("\t\t\t\t" + p.getName());
					output.append(LINE_SEPARATOR);
				}
			}

			if (!schedulableMissionSequencerEnvs.isEmpty())
			{
				output.append("\t\t\tSchedulable Mission Sequencers:");
				output.append(LINE_SEPARATOR);

				for (NestedMissionSequencerEnv p : schedulableMissionSequencerEnvs)
				{
					output.append("\t\t\t\t" + p.getName());
					output.append(LINE_SEPARATOR);
				}
			}

			if (!managedThreadEnvs.isEmpty())
			{
				output.append("\t\t\tManaged Threads:");
				output.append(LINE_SEPARATOR);

				for (ManagedThreadEnv p : managedThreadEnvs)
				{
					output.append("\t\t\t\t" + p.getName());
					output.append(LINE_SEPARATOR);

				}
			}

			return output.toString();
		}

		// Could trigger false positive if two schedulable of different types
		// have the same name
		public boolean containsSchedulable(Name name)
		{
			for (ObjectEnv p : getAperiodicEventHandlerEnvs())
			{
				if (p.getName().contentEquals(name))
				{
					return true;
				}
			}

			for (ObjectEnv p : getPeriodEventHandlerEnvs())
			{
				if (p.getName().contentEquals(name))
				{
					return true;
				}
			}

			for (ObjectEnv p : getOneShotEventHandlerEnvs())
			{
				if (p.getName().contentEquals(name))
				{
					return true;
				}
			}

			for (ObjectEnv p : getManagedThreadEnvs())
			{
				if (p.getName().contentEquals(name))
				{
					return true;
				}
			}

			for (ObjectEnv p : getSchedulableMissionSequencerEnvs())
			{
				if (p.getName().contentEquals(name))
				{
					return true;
				}
			}

			return false;
		}
	}

	public FrameworkEnv()
	{
		super();
		this.controlTier = new ControlTierEnv();
		tiers = new ArrayList<TierEnv>();
		// tiers.add(new TierEnv());
	}

	public ControlTierEnv getControlTier()
	{
		return controlTier;
	}

	public void setControlTier(ControlTierEnv controlTier)
	{
		this.controlTier = controlTier;
	}

	public TierEnv getTier0()
	{
		return tiers.get(0);
	}

	public void addSafelet(Name safelet)
	{
		controlTier.addSafelet(safelet);
	}

	public void addTopLevelMissionSequencer(Name topLevelMissionSequencer)
	{
		controlTier.addTopLevelMissionSequencer(topLevelMissionSequencer);
	}

	public void addMission(Name mission)
	{
		currentCluster.getMissionEnv().setName(mission);
	}

	public void newCluster(Name sequencer)
	{
		currentCluster = new ClusterEnv(sequencer);

		// System.out.println("Current Tier " + currentTier);

		currentTier.addCluster(currentCluster);
	}

	public void newTier()
	{
		currentTier = new TierEnv();

		tiers.add(currentTier);
	}

	public String toString()
	{

		String top = "Control Tier:";
		top += (LINE_SEPARATOR);
		top += (controlTier.toString());
		top += (LINE_SEPARATOR);
		top += ("Number of Tiers = " +tiers.size());
		top += (LINE_SEPARATOR);
		top += ("____________________________________________________________________________________");
		top +=(LINE_SEPARATOR);
		
		StringBuilder output = new StringBuilder(top);

		int i = 0;
		for (TierEnv tier : tiers)
		{
			output.append("Tier ");
			output.append(i);
			output.append(":");
			output.append(LINE_SEPARATOR);
			output.append("\tNumber of Clusters in Tier ");
			output.append(i);
			output.append(" = ");
			output.append(tier.clusters.size());
			output.append(LINE_SEPARATOR);
			output.append(tier.toString());
			output.append("____________________________________________________________________________________");
			output.append(LINE_SEPARATOR);

			i++;

		}

		return output.toString();
	}

	public void addSchedulable(SchedulableTypeE type, Name name)
	{
		currentCluster.addSchedulable(type, name);
		currentCluster.missionEnv.addSchedulable(name);
	}

	public ArrayList<Name> missionsBelow(TierEnv tier)
	{
		if (tiers.contains(tier))
		{
			int tierIndex = tiers.indexOf(tier);
			if (tiers.size() != (tierIndex + 1))
			{
				TierEnv tierBelow = tiers.get(tierIndex + 1);
				return tierBelow.getMissions();
			}
		}
		return null;
	}

	public ArrayList<Name> sequencersAbove(TierEnv tier)
	{
		if (tiers.contains(tier))
		{
			int tierIndex = tiers.indexOf(tier);
			if (tierIndex != 0)
			{
				TierEnv tierAbove = tiers.get(tierIndex - 1);
				return tierAbove.getMissionSequencers();
			}

		}

		return null;
	}

	public boolean containsSchedulable(Name name)
	{
		boolean present = false;
		for (TierEnv t : tiers)
		{
			for (ClusterEnv c : t.clusters)
			{

				SchedulablesEnv s = c.schedulablesEnv;

				present = s.containsSchedulable(name);
			}
		}

		return present;
	}

	public ArrayList<MissionEnv> getMissions()
	{
		ArrayList<MissionEnv> missions = new ArrayList<MissionEnv>();

		for (TierEnv t : tiers)
		{
			for (ClusterEnv c : t.clusters)
			{
				missions.add(c.missionEnv);
			}
		}

		return missions;
	}

	public MissionEnv getMission(Name n)
	{
		for (TierEnv t : tiers)
		{
			for (ClusterEnv c : t.clusters)
			{
				if (c.getMissionEnv().getName().equals(n))
				{
					return c.getMissionEnv();
				}
			}

		}
		return null;
	}

	// Bit hacky
	public void addMissionSequencerMission(Name missionSequencerName,
			Name missionName)
	{
		// System.out.println("*** Adding " + n + " to " + tlms + " ***");
		if (controlTier.topLevelMissionSequencerEnv.getName() == missionSequencerName)
		{
			controlTier.topLevelMissionSequencerEnv.addMission(missionName);
		}

		for (TierEnv t : tiers)
		{
			for (ClusterEnv c : t.clusters)
			{
				if (!c.schedulablesEnv.schedulableMissionSequencerEnvs
						.isEmpty())
				{
					for (NestedMissionSequencerEnv p : c.schedulablesEnv.schedulableMissionSequencerEnvs)
					{
						if (p.getName() == missionSequencerName)
						{
							System.out
									.println("Trting to Add Mission to Nested Sequencer");
							p.addMission(missionName);
						}
					}
				}
			}
		}

	}

	public ArrayList<ManagedThreadEnv> getManagedThreads()
	{
		ArrayList<ManagedThreadEnv> mtEnvs = new ArrayList<ManagedThreadEnv>();

		for (TierEnv t : tiers)
		{
			for (ClusterEnv c : t.clusters)
			{
				mtEnvs.addAll(c.schedulablesEnv.managedThreadEnvs);
			}
		}
		return mtEnvs;
	}

	public ArrayList<NestedMissionSequencerEnv> getNestedMissionsequencers()
	{
		ArrayList<NestedMissionSequencerEnv> smsEnvs = new ArrayList<NestedMissionSequencerEnv>();

		for (TierEnv t : tiers)
		{
			for (ClusterEnv c : t.clusters)
			{
				smsEnvs.addAll(c.schedulablesEnv.schedulableMissionSequencerEnvs);
			}
		}
		return smsEnvs;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map getNetworkMap()
	{
		Map networkMap = new HashMap();

		Map safeletMap = new HashMap();
		safeletMap.put("Name", getControlTier().getSafeletEnv().getName());
		
		safeletMap.put("Parameters", getControlTier().getSafeletEnv()
				.getParameters());

		networkMap.put("Safelet", safeletMap);

		networkMap.put("Safelet_HasClass",
				getControlTier().getSafeletEnv().isHasClass());

		Map tlmsMap = new HashMap();
		tlmsMap.put("Name", getControlTier().getTopLevelMissionSequencerEnv()
				.getName());
		tlmsMap.put("Parameters", getControlTier()
				.getTopLevelMissionSequencerEnv().getParameters());

		networkMap.put("TopLevelSequencer", tlmsMap);

		networkMap.put("TopLevelSequencer_HasClass", getControlTier()
				.getTopLevelMissionSequencerEnv().isHasClass());

		List tierList = new ArrayList();

		for (TierEnv tier : tiers)
		{
			tierList.add(tier.toList());
		}
		networkMap.put("Tiers", tierList);
		

		

		return networkMap;
	}

	public ArrayList<OneShotEventHandlerEnv> getOneShotEventHandlers()
	{
		ArrayList<OneShotEventHandlerEnv> osehEnvs = new ArrayList<OneShotEventHandlerEnv>();

		for (TierEnv t : tiers)
		{
			for (ClusterEnv c : t.clusters)
			{
				osehEnvs.addAll(c.schedulablesEnv.oneShotEventHandlerEnvs);
			}
		}
		return osehEnvs;
	}

	public ArrayList<PeriodicEventHandlerEnv> getPeriodicEventHandlers()
	{
		ArrayList<PeriodicEventHandlerEnv> pehEnvs = new ArrayList<PeriodicEventHandlerEnv>();

		for (TierEnv t : tiers)
		{
			for (ClusterEnv c : t.clusters)
			{
				pehEnvs.addAll(c.schedulablesEnv.periodEventHandlerEnvs);
			}
		}
		return pehEnvs;
	}

	public ArrayList<AperiodicEventHandlerEnv> getAperiodicEventHandlers()
	{
		ArrayList<AperiodicEventHandlerEnv> apehEnvs = new ArrayList<AperiodicEventHandlerEnv>();

		for (TierEnv t : tiers)
		{
			for (ClusterEnv c : t.clusters)
			{
				apehEnvs.addAll(c.schedulablesEnv.aperiodicEventHandlerEnvs);
			}
		}
		return apehEnvs;
	}

	private String outputMethods(String output, List<MethodEnv> methods)
	{
		if (methods.size() <= 0)
		{
			output += "\t\t\t No Methods";
		}
		else
		{
	
			for (MethodEnv me : methods)
			{
				String params = "";
				Map<String, Type> parameterMap = me.getParameters();
				if (parameterMap != null)
				{
					params = parameterMap.toString();
				}
	
				output += "\t\t\t" + me.getReturnType() + ":" + me.getMethodName()
						+ "(" + params + ")";
				output += LINE_SEPARATOR;
			}
		}
		return  output;
	}

}
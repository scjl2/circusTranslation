package hijac.tools.tightrope.environments;

import hijac.tools.tightrope.utils.Debugger;
import hijac.tools.tightrope.utils.TightRopeString.LATEX;

import java.util.ArrayList;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Name;

public class FrameworkEnv
{
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	private ControlTierEnv controlTier;

	private List<TierEnv> tiers;

	private TierEnv currentTier;
	private ClusterEnv currentCluster;

	class ControlTierEnv
	{

		private SafeletEnv safeletEnv;
		private Set<String> safeletSync;
		private TopLevelMissionSequencerEnv topLevelMissionSequencerEnv;
		private Set<String> controlTierSync;

		public ControlTierEnv()
		{
			safeletEnv = new SafeletEnv();
			safeletSync = new HashSet<String>();
			topLevelMissionSequencerEnv = new TopLevelMissionSequencerEnv();
			controlTierSync = new HashSet<String>();

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
			this.topLevelMissionSequencerEnv.setId(topLevelMissionSequencer.toString());
			safeletEnv.addTopLevelMissionSequencer(topLevelMissionSequencer);
		}

		public Set<String> getSafeletSync()
		{
			return safeletSync;
		}

		public void setSafeletSync(Set<String> safeletSync)
		{
			this.safeletSync = safeletSync;
		}

		public String toString()
		{
			String output = "";
			output += "\t Safelet: " + safeletEnv.getName();
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

		public String getSafeletSyncString()
		{
			return toChannelSet(safeletSync);
		}

		public String getControlTierSync()
		{
			return toChannelSet(controlTierSync);
		}
	}

	private class TierEnv
	{
		private List<ClusterEnv> clusters;

		private Set<String> interfaceSync;

		public TierEnv()
		{
			clusters = new ArrayList<ClusterEnv>();
			currentCluster = null;
			interfaceSync = new HashSet<String>();
		}

		public TierEnv(boolean makeTier0)
		{
			if (makeTier0)
			{
				clusters = new ArrayList<ClusterEnv>();
				currentCluster = null;
				interfaceSync = null;
			}
			else
			{
				clusters = new ArrayList<ClusterEnv>();
				currentCluster = null;
				interfaceSync = new HashSet<String>();
			}
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

		public String getInterfaceSyncString()
		{
			if (interfaceSync == null)
			{
				return "";
			}
			else
			{
				return toChannelSet(interfaceSync);
			}

		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		public List toList()
		{
			List clusterList = new ArrayList();

			for (ClusterEnv c : clusters)
			{
				Map clusterMap = new HashMap();

				clusterMap.put("InterfaceSync", getInterfaceSyncString());

				clusterMap.put("Sequencer", c.getSequencer());

				Map missionMap = new HashMap();
				missionMap.put("Name", c.getMissionEnv().getName());

				List<String> params = new ArrayList<String>();

				for (VariableEnv v : c.getMissionEnv().getFWParameters())
				{
					params.add(v.toString());
				}

				missionMap.put("FWParameters", params);

				params = new ArrayList<String>();
				for (VariableEnv v : c.getMissionEnv().getAppParameters())
				{
					params.add(v.toString());
				}

				missionMap.put("AppParameters", params);

				params = new ArrayList<String>();
				for (VariableEnv v : c.getMissionEnv().getThreadParameters())

				{
					params.add(v.toString());
				}

				missionMap.put("ThreadParameters", params);

				clusterMap.put("Mission", missionMap);
				clusterMap.put("Mission_HasClass", c.getMissionEnv().hasClass());

				Map schedulablesMap = new HashMap();
				schedulablesMap.put("Periodics", c.getSchedulablesEnv()
						.getPeriodicsList());
				schedulablesMap.put("Aperiodics", c.getSchedulablesEnv()
						.getAperiodicsList());
				schedulablesMap.put("Oneshots", c.getSchedulablesEnv().getOneshotsList());
				schedulablesMap.put("NestedSequencers", c.getSchedulablesEnv()
						.getNestedSequencersList());
				schedulablesMap.put("Threads", c.getSchedulablesEnv().getThreadsList());

				clusterMap.put("Schedulables", schedulablesMap);

				clusterMap.put("MissionSync", c.getMissionSyncString());

				clusterList.add(clusterMap);
			}

			return clusterList;
		}
	}

	private class ClusterEnv
	{
		private Name sequencer;
		private MissionEnv missionEnv;
		private Set<String> missionSync;
		private SchedulablesEnv schedulablesEnv;

		public ClusterEnv(Name sequencer)
		{
			missionEnv = new MissionEnv();
			schedulablesEnv = new SchedulablesEnv();
			this.sequencer = sequencer;

			missionSync = new HashSet<String>();
		}

		public String getMissionSyncString()
		{
			return toChannelSet(missionSync);
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

		@SuppressWarnings("unused")
		public Set<String> getMissionSync()
		{
			return missionSync;
		}

		@SuppressWarnings("unused")
		public void setMissionSync(Set<String> missionSync)
		{
			this.missionSync = missionSync;
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
			methods.addAll(missionEnv.getMeths());
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
		
			for (VariableEnv v : p.getFWParameters())
			{
				// This adds the type from the program E.G. FlatBufferMission
				// not MissionID
				// params.add(v.getVariableName());
				params.add(v.getProgramType());
			}

			sMap.put("FWParameters", params);

			params = new ArrayList<String>();
			for (VariableEnv v : p.getAppParameters())
			{
				// This adds the type from the program E.G. FlatBufferMission
				// not MissionID
				// params.add(v.getVariableName());
				params.add(v.getProgramType());
			}

			sMap.put("AppParameters", params);

			params = new ArrayList<String>();
			for (VariableEnv v : p.getThreadParameters())
			{
				// This adds the type from the program E.G. FlatBufferMission
				// not MissionID
				// params.add(v.getVariableName());
				params.add(v.getProgramType());
			}

			sMap.put("ThreadParameters", params);

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
			String nameString = name.toString();
			
			switch (type)
			{
				case PEH:
					PeriodicEventHandlerEnv p = new PeriodicEventHandlerEnv();
					p.setName(name);
					p.setId(nameString);
					periodEventHandlerEnvs.add(p);
					break;

				case APEH:
					AperiodicEventHandlerEnv a = new AperiodicEventHandlerEnv();
					a.setName(name);
					a.setId(nameString);
					aperiodicEventHandlerEnvs.add(a);
					break;

				case OSEH:
					OneShotEventHandlerEnv osehEnv = new OneShotEventHandlerEnv();
					osehEnv.setName(name);
					osehEnv.setId(nameString);
					oneShotEventHandlerEnvs.add(osehEnv);
					break;

				case MT:
					ManagedThreadEnv mtEnv = new ManagedThreadEnv();
					mtEnv.setName(name);
					mtEnv.setId(nameString);
					managedThreadEnvs.add(mtEnv);
					break;

				case SMS:
					NestedMissionSequencerEnv nmsEnv = new NestedMissionSequencerEnv();
					nmsEnv.setName(name);
					nmsEnv.setId(nameString);
					schedulableMissionSequencerEnvs.add(nmsEnv);
					break;

				default:
					break;

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
		MissionEnv missionEnv = currentCluster.getMissionEnv();
		missionEnv.setName(mission);
		missionEnv.setId(mission.toString());
	}

	public void newCluster(Name sequencer)
	{
		currentCluster = new ClusterEnv(sequencer);


		currentTier.addCluster(currentCluster);
	}

	public void newTier()
	{
		if (tiers.isEmpty())
		{
			currentTier = new TierEnv(true);
		}
		else
		{
			currentTier = new TierEnv();
		}

		tiers.add(currentTier);
	}

	public String toString()
	{

		String top = "Control Tier:";
		top += (LINE_SEPARATOR);
		top += (controlTier.toString());
		top += (LINE_SEPARATOR);
		top += ("Number of Tiers = " + tiers.size());
		top += (LINE_SEPARATOR);
		top += ("____________________________________________________________________________________");
		top += (LINE_SEPARATOR);

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
	public void addMissionSequencerMission(Name missionSequencerName, Name missionName)
	{
		if (controlTier.topLevelMissionSequencerEnv.getName() == missionSequencerName)
		{
			controlTier.topLevelMissionSequencerEnv.addMission(missionName);
		}

		for (TierEnv t : tiers)
		{
			for (ClusterEnv c : t.clusters)
			{
				if (!c.schedulablesEnv.schedulableMissionSequencerEnvs.isEmpty())
				{
					for (NestedMissionSequencerEnv p : c.schedulablesEnv.schedulableMissionSequencerEnvs)
					{
						if (p.getName() == missionSequencerName)
						{
							Debugger.log("Trting to Add Mission to Nested Sequencer");
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

		safeletMap
				.put("FWParameters", getControlTier().getSafeletEnv().getFWParameters());
		safeletMap.put("AppParameters", getControlTier().getSafeletEnv()
				.getAppParameters());
		safeletMap.put("ThreadParameters", getControlTier().getSafeletEnv()
				.getThreadParameters());

		networkMap.put("Safelet", safeletMap);

		networkMap.put("SafeletSync", getControlTier().getSafeletSyncString());

		networkMap.put("Safelet_HasClass",

		getControlTier().getSafeletEnv().hasClass());

		Map tlmsMap = new HashMap();
		tlmsMap.put("Name", getControlTier().getTopLevelMissionSequencerEnv().getName());

		tlmsMap.put("FWParameters", getControlTier().getTopLevelMissionSequencerEnv()
				.getFWParameters());
		tlmsMap.put("AppParameters", getControlTier().getTopLevelMissionSequencerEnv()
				.getAppParameters());
		tlmsMap.put("ThreadParameters", getControlTier().getTopLevelMissionSequencerEnv()
				.getThreadParameters());

		networkMap.put("TopLevelSequencer", tlmsMap);

		networkMap.put("ControlTierSync", getControlTier().getControlTierSync());

		networkMap.put("TopLevelSequencer_HasClass", getControlTier()

		.getTopLevelMissionSequencerEnv().hasClass());

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

	/**
	 * Gets the ObjectEnv with the given name, or <code>null</code> if it does not exist.
	 * 
	 * @param nameOfObject The name of the ObjectEnv you're searching for
	 * @return The ObjectEnv or <code>null</code>
	 */
	public ObjectEnv getObjectEnv(String nameOfObject)
	{
		for(ObjectEnv o : getAllObjectEnvs())
		{
			if(o.getName().equals(nameOfObject))
			{
				return o;
			}
		}
		return null;
	}

	private ArrayList<ObjectEnv> getAllObjectEnvs()
	{
		ArrayList<ObjectEnv> objectEnvs = new ArrayList<ObjectEnv>();
		
		objectEnvs.add(controlTier.getSafeletEnv());
		objectEnvs.add(controlTier.getTopLevelMissionSequencerEnv());
		objectEnvs.addAll(getMissions());
		objectEnvs.addAll(getManagedThreads());
		objectEnvs.addAll(getAperiodicEventHandlers());
		objectEnvs.addAll(getNestedMissionsequencers());
		objectEnvs.addAll(getPeriodicEventHandlers());
		objectEnvs.addAll(getNestedMissionsequencers());
		
		return objectEnvs;
	}
	
	private String toChannelSet(Set<String> set)
	{
		String safeletSyncString = "";
		if (set.isEmpty())
		{
			safeletSyncString = LATEX.INTERLEAVE;
		}
		else
		{
			safeletSyncString = LATEX.LPAR +LATEX.LCHANSET;
			Iterator<String> safeletSyncIter = set.iterator();
	
			while (safeletSyncIter.hasNext())
			{
				String s = safeletSyncIter.next();
				safeletSyncString += s;
				if (safeletSyncIter.hasNext())
				{
					safeletSyncString += ", ";
				}
				safeletSyncString += LATEX.RCHANSET+LATEX.RPAR;
			}
		}
		return safeletSyncString;
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

				output += "\t\t\t" + me.getReturnType() + ":" + me.getName() + "("
						+ params + ")";
				output += LINE_SEPARATOR;
			}
		}
		return output;
	}

}
package hijac.tools.tightrope.environments;

import java.util.ArrayList;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Name;

import com.sun.source.tree.Tree;

public class FrameworkEnv
{
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
			output += "Safelet: " + safeletEnv.getName();
			output += System.getProperty("line.separator");
			output += "Safelet Methods:";
			output += System.getProperty("line.separator");

			List<MethodEnv> methods = safeletEnv.getMeths();
			//TODO Output sync meths?
			output = outputMethods(output, methods);
			output += System.getProperty("line.separator");
			output += "Top-Level Mission Sequencer: "
					+ topLevelMissionSequencerEnv.getName();
			output += System.getProperty("line.separator");
			output += "Top Level Sequencer Methods:";
			output += System.getProperty("line.separator");
			
			methods = topLevelMissionSequencerEnv.getMeths();
			output = outputMethods(output, methods);

			return output;

		}

		private String outputMethods(String output, List<MethodEnv> methods)
		{
			if (methods.size() <= 0)
			{
				output += "No Methods";
			} else
			{

				for (MethodEnv me : methods)
				{
					String params = "";
					Map<String, Type> parameterMap =me.getParameters();
					if(parameterMap != null)
					{
						params = parameterMap.toString();
					}
					
					output += me.getReturnType() + ":" + me.getMethodName()
							+ "(" + params + ")";
				}
			}
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
			// clusters.add(currentCluster);
		}

		// public List<ClusterEnv> getClusters()
		// {
		// return clusters;
		// }

		public void addCluster(ClusterEnv cluster)
		{
			this.clusters.add(cluster);
		}

		// public void addMission(Name mission)
		// {
		// currentCluster.getMissionEnv().setName(mission);
		// // currentCluster = new
		// // clusters.add(new ClusterEnv(missionEnv));
		// }

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
			} else
			{
				return misssionSequeners;
			}
		}

		public String toString()
		{
			if (clusters.isEmpty())
			{
				return "Tier Empty";
			} else
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
				clusterMap.put("Mission", c.getMissionEnv().getName());

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

		public ParadigmEnv getMissionEnv()
		{
			return missionEnv;
		}

		// public void setMissionEnv(MissionEnv missionEnv)
		// {
		// this.missionEnv = missionEnv;
		// }

		public SchedulablesEnv getSchedulablesEnv()
		{
			return schedulablesEnv;
		}

		//
		// public void setSchedulablesEnv(SchedulablesEnv schedulablesEnv)
		// {
		// this.schedulablesEnv = schedulablesEnv;
		// }

		public void addSchedulable(SchedulableTypeE type, Name name)
		{
			schedulablesEnv.addSchedulable(type, name);
		}

		public String toString()
		{
			String output = "Cluster Environment:";
			output += System.getProperty("line.separator");
			output += "\t Mission = " + missionEnv.getName();
			output += System.getProperty("line.separator");
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
						schedulablesList.add(p.getName());
					}
					break;

				case APEH:
					for (ObjectEnv p : aperiodicEventHandlerEnvs)
					{
						schedulablesList.add(p.getName());
					}
					break;

				case OSEH:
					for (ObjectEnv p : oneShotEventHandlerEnvs)
					{
						schedulablesList.add(p.getName());
					}
					break;

				case SMS:
					for (ObjectEnv p : schedulableMissionSequencerEnvs)
					{
						schedulablesList.add(p.getName());
					}
					break;

				case MT:
					for (ObjectEnv p : managedThreadEnvs)
					{
						schedulablesList.add(p.getName());
					}
					break;
			}
			return schedulablesList;
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

			String output = "Schedulables:";
			output += System.getProperty("line.separator");

			if (!periodEventHandlerEnvs.isEmpty())
			{
				output += "Periodic Event Handlers:";
				output += System.getProperty("line.separator");

				for (ObjectEnv p : periodEventHandlerEnvs)
				{
					output += "\t" + p.getName();
					output += System.getProperty("line.separator");
				}
			}

			if (!aperiodicEventHandlerEnvs.isEmpty())
			{
				output += "Aperidic Event Handlers:";
				output += System.getProperty("line.separator");

				for (ObjectEnv p : aperiodicEventHandlerEnvs)
				{
					output += "\t" + p.getName();
					output += System.getProperty("line.separator");
				}
			}

			if (!oneShotEventHandlerEnvs.isEmpty())
			{
				output = "OneShot Event Handlers:";
				output += System.getProperty("line.separator");

				for (ObjectEnv p : oneShotEventHandlerEnvs)
				{
					output += "\t" + p.getName();
					output += System.getProperty("line.separator");
				}
			}

			if (!schedulableMissionSequencerEnvs.isEmpty())
			{
				output += "Schedulable Mission Sequencers:";
				output += System.getProperty("line.separator");

				for (ObjectEnv p : schedulableMissionSequencerEnvs)
				{
					output += "\t" + p.getName();
					output += System.getProperty("line.separator");
				}
			}

			if (!managedThreadEnvs.isEmpty())
			{
				output += "Managed Threads:";
				output += System.getProperty("line.separator");

				for (ObjectEnv p : managedThreadEnvs)
				{
					output += "\t" + p.getName();
					output += System.getProperty("line.separator");

				}
			}

			return output;
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

		//
		// TierEnv tier = new TierEnv();
		// currentTier = tier;
		//
		// ClusterEnv cluster = new ClusterEnv();
		// currentCuster = cluster;
		//
		// tier.addCluster(cluster);
		//
		// tiers.add(tier);
	}

	public void addMission(Name mission)
	{
		currentCluster.getMissionEnv().setName(mission);
	}

	public void newCluster(Name sequencer)
	{
		currentCluster = new ClusterEnv(sequencer);

		System.out.println("Current Tier " + currentTier);

		currentTier.addCluster(currentCluster);
	}

	public void newTier()
	{
		currentTier = new TierEnv();

		tiers.add(currentTier);
	}

	public String toString()
	{
		String output = "";
		output += "Control Tier:";
		output += System.getProperty("line.separator");
		output += controlTier.toString();
		output += System.getProperty("line.separator");

		output += "Number of Tiers = " + tiers.size();
		output += System.getProperty("line.separator");

		int i = 0;
		for (TierEnv tier : tiers)
		{
			output += "Tier " + i + ":";
			output += System.getProperty("line.separator");
			output += "Number of Clusters in Tier " + i + " = "
					+ tier.clusters.size();
			output += System.getProperty("line.separator");
			output += tier.toString();
			// output += System.getProperty("line.separator");

			i++;

		}

		return output;
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

	public ParadigmEnv getMission(Name n)
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
	public void addMissionSequencerMission(Name tlms, Name n)
	{
		System.out.println("*** Adding " + n + " to " + tlms + " ***");
		if (controlTier.topLevelMissionSequencerEnv.getName() == tlms)
		{
			controlTier.topLevelMissionSequencerEnv.addMission(n);
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
						if (p.getName() == tlms)
						{
							System.out
									.println("Trting to Add Mission to Nested Sequencer");
							p.addMission(n);
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

		networkMap.put("SafeletName", getControlTier().getSafeletEnv()
				.getName());
		networkMap.put("TopLevelSequencer", getControlTier()
				.getTopLevelMissionSequencerEnv().getName());

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

}
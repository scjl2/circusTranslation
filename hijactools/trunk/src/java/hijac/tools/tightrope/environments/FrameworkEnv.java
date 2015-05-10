package hijac.tools.tightrope.environments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Name;

public class FrameworkEnv
{
	private ControlTierEnv controlTier;

	private List<TierEnv> tiers;

	private TierEnv currentTier;
	private ClusterEnv currentCluster;

	class ControlTierEnv
	{
		
		SafeletEnv safeletEnv;
		TopLevelMissionSequencerEnv topLevelMissionSequencerEnv;

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
			output += "Safelet Methods: " + safeletEnv.getMeths();
			output += System.getProperty("line.separator");
			output += "Top-Level Mission Sequencer: "
					+ topLevelMissionSequencerEnv.getName();

			return output;

		}

		public Map toMap()
		{
			Map clusterMap = new HashMap();
			clusterMap.put("safelet", safeletEnv.getName());
			
			List topLevelSequencersList = new ArrayList();
			
//			for(TopLevelMissionSequencerEnv t : )
			topLevelSequencersList.add(topLevelMissionSequencerEnv.getName());
			
			clusterMap.put("topLevelSequencers",topLevelSequencersList);
			
			
			return clusterMap;
		}
	}

	private class TierEnv
	{
		List<ClusterEnv> clusters;

		public TierEnv()
		{
			clusters = new ArrayList<ClusterEnv>();
			currentCluster = new ClusterEnv();
			clusters.add(currentCluster);
		}

		public List<ClusterEnv> getClusters()
		{
			return clusters;
		}

		public void addCluster(ClusterEnv cluster)
		{
			this.clusters.add(cluster);
		}

		public void addMission(Name mission)
		{
			currentCluster.getMissionEnv().setName(mission);

			// clusters.add(new ClusterEnv(missionEnv));
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
				for (ParadigmEnv p : c.getSchedulablesEnv()
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
		
		public List toList()
		{
			List clusterList = new ArrayList();
			
			for(ClusterEnv c : clusters)
			{
				Map clusterMap = new HashMap();
				clusterMap.put("Mission", c.getMissionEnv().getName());
				clusterMap.put("Schedulables", c.getSchedulablesEnv().toList());
				
				clusterList.add(clusterMap);
			}
			
			return clusterList;
		}

	}

	private class ClusterEnv
	{
		MissionEnv missionEnv;
		SchedulablesEnv schedulablesEnv;

		public ClusterEnv()
		{
			missionEnv = new MissionEnv();
			schedulablesEnv = new SchedulablesEnv();
		}

		public MissionEnv getMissionEnv()
		{
			return missionEnv;
		}

		public void setMissionEnv(MissionEnv missionEnv)
		{
			this.missionEnv = missionEnv;
		}

		public SchedulablesEnv getSchedulablesEnv()
		{
			return schedulablesEnv;
		}

		public void setSchedulablesEnv(SchedulablesEnv schedulablesEnv)
		{
			this.schedulablesEnv = schedulablesEnv;
		}

		public void addSchedulable(SchedulableTypeE type, Name name)
		{
			schedulablesEnv.addSchedulable(type, name);
		}

		public String toString()
		{
			String output = "Cluster Environment:";
			output += System.getProperty("line.separator");
			output += "Mission = " + missionEnv.getName();
			output += System.getProperty("line.separator");
			output += schedulablesEnv.toString();

			return output;

		}
	}

	private class SchedulablesEnv
	{

		List<ParadigmEnv> periodEventHandlerEnvs = new ArrayList<ParadigmEnv>();
		List<ParadigmEnv> aperiodicEventHandlerEnvs = new ArrayList<ParadigmEnv>();
		List<ParadigmEnv> oneShotEventHandlerEnvs = new ArrayList<ParadigmEnv>();
		List<NestedMissionSequencerEnv> schedulableMissionSequencerEnvs = new ArrayList<NestedMissionSequencerEnv>();
		List<ManagedThreadEnv> managedThreadEnvs = new ArrayList<ManagedThreadEnv>();

		public List<ParadigmEnv> getPeriodEventHandlerEnvs()
		{
			return periodEventHandlerEnvs;
		}

		public List toList()
		{
			List schedulablesList = new ArrayList();
			
			for(ParadigmEnv p : periodEventHandlerEnvs)
			{
				schedulablesList.add(p.getName());
			}
			
			for(ParadigmEnv p : aperiodicEventHandlerEnvs)
			{
				schedulablesList.add(p.getName());
			}
			for(ParadigmEnv p : oneShotEventHandlerEnvs)
			{
				schedulablesList.add(p.getName());
			}
			for(ParadigmEnv p : schedulableMissionSequencerEnvs)
			{
				schedulablesList.add(p.getName());
			}
			for(ParadigmEnv p : managedThreadEnvs)
			{
				schedulablesList.add(p.getName());
			}
			
			
			return schedulablesList;
		}

		public void setPeriodEventHandlerEnvs(
				List<ParadigmEnv> periodEventHandlerEnvs)
		{
			this.periodEventHandlerEnvs = periodEventHandlerEnvs;
		}

		public List<ParadigmEnv> getAperiodicEventHandlerEnvs()
		{
			return aperiodicEventHandlerEnvs;
		}

		public void setAperiodicEventHandlerEnvs(
				List<ParadigmEnv> aperiodicEventHandlerEnvs)
		{
			this.aperiodicEventHandlerEnvs = aperiodicEventHandlerEnvs;
		}

		public List<ParadigmEnv> getOneShotEventHandlerEnvs()
		{
			return oneShotEventHandlerEnvs;
		}

		public void setOneShotEventHandlerEnvs(
				List<ParadigmEnv> oneShotEventHandlerEnvs)
		{
			this.oneShotEventHandlerEnvs = oneShotEventHandlerEnvs;
		}

		public List<NestedMissionSequencerEnv> getSchedulableMissionSequencerEnvs()
		{
			return schedulableMissionSequencerEnvs;
		}

//		public void setSchedulableMissionSequencerEnvs(
//				List<NestedMissionSequencerEnv> schedulableMissionSequencerEnvs)
//		{
//			this.schedulableMissionSequencerEnvs = schedulableMissionSequencerEnvs;
//		}

		public List<ManagedThreadEnv> getManagedThreadEnvs()
		{
			return managedThreadEnvs;
		}

		public void setManagedThreadEnvs(List<ManagedThreadEnv> managedThreadEnvs)
		{
			this.managedThreadEnvs = managedThreadEnvs;
		}

		public void addSchedulable(SchedulableTypeE type, Name name)
		{
			ParadigmEnv p = new ParadigmEnv();
			p.setName(name);

			if (type == SchedulableTypeE.MT)
			{
				ManagedThreadEnv mtEnv = new ManagedThreadEnv();
				mtEnv.setName(name);
				managedThreadEnvs.add(mtEnv);
			}
			if (type == SchedulableTypeE.PEH)
			{
				periodEventHandlerEnvs.add(p);
			}
			if (type == SchedulableTypeE.APEH)
			{
				aperiodicEventHandlerEnvs.add(p);
			}
			if (type == SchedulableTypeE.OSEH)
			{
				oneShotEventHandlerEnvs.add(p);
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

				for (ParadigmEnv p : periodEventHandlerEnvs)
				{
					output += p.getName();
					output += System.getProperty("line.separator");
				}
			}

			if (!aperiodicEventHandlerEnvs.isEmpty())
			{
				output += "Aperidic Event Handlers:";
				output += System.getProperty("line.separator");

				for (ParadigmEnv p : aperiodicEventHandlerEnvs)
				{
					output += p.getName();
					output += System.getProperty("line.separator");
				}
			}

			if (!oneShotEventHandlerEnvs.isEmpty())
			{
				output = "OneShot Event Handlers:";
				output += System.getProperty("line.separator");

				for (ParadigmEnv p : oneShotEventHandlerEnvs)
				{
					output += p.getName();
					output += System.getProperty("line.separator");
				}
			}

			if (!schedulableMissionSequencerEnvs.isEmpty())
			{
				output += "Schedulable Mission Sequencers:";
				output += System.getProperty("line.separator");

				for (ParadigmEnv p : schedulableMissionSequencerEnvs)
				{
					output += p.getName();
					output += System.getProperty("line.separator");
				}
			}

			if (!managedThreadEnvs.isEmpty())
			{
				output += "Managed Threads:";
				output += System.getProperty("line.separator");

				for (ParadigmEnv p : managedThreadEnvs)
				{
					output += p.getName();
					output += System.getProperty("line.separator");

				}
			}

			return output;
		}

		//coiuld trigger false posotive if two schedulable of different types have the same name
		public boolean containsSchedulable(Name name)
		{
			for(ParadigmEnv p : getAperiodicEventHandlerEnvs())
			{
				if (p.getName().contentEquals(name))
				{
					return true;
				}
			}
			
			for(ParadigmEnv p : getPeriodEventHandlerEnvs())
			{
				if (p.getName().contentEquals(name))
				{
					return true;
				}
			}
			
			for(ParadigmEnv p : getOneShotEventHandlerEnvs())
			{
				if (p.getName().contentEquals(name))
				{
					return true;
				}
			}

		
			for(ParadigmEnv p : getManagedThreadEnvs())
			{
				if (p.getName().contentEquals(name))
				{
					return true;
				}
			}
		
			for(ParadigmEnv p : getSchedulableMissionSequencerEnvs())
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
		tiers.add(new TierEnv());
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

	public void newCluster()
	{
		currentCluster = new ClusterEnv();

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

		int i = 0;
		for (TierEnv tier : tiers)
		{
			output += "Tier " + i + ":";
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

				present= s.containsSchedulable(name);
			}
		}

		return present;
	}

	public ArrayList<MissionEnv> getMissions()
	{
		ArrayList<MissionEnv> missions = new ArrayList<MissionEnv>();
		
		for(TierEnv t : tiers)
		{
			for (ClusterEnv c : t.clusters)
			{
				missions.add(c.missionEnv);
			}	
				
		}
		
		return missions;
	}

	//Bit hacky
	public void addMissionSequencerMission(Name tlms, Name n)
	{
		System.out.println("*** Adding " + n + " to " + tlms + " ***");
		if(controlTier.topLevelMissionSequencerEnv.getName() == tlms )
		{
			controlTier.topLevelMissionSequencerEnv.addMission(n);
		}
		
		for(TierEnv t: tiers)
		{
			for(ClusterEnv c : t.clusters)
			{
				if(!c.schedulablesEnv.schedulableMissionSequencerEnvs.isEmpty())
				{
					for(NestedMissionSequencerEnv p : c.schedulablesEnv.schedulableMissionSequencerEnvs)
					{
						if(p.getName() == tlms)
						{
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
		
		for(TierEnv t : tiers)
		{
			for(ClusterEnv c : t.clusters)
			{
				mtEnvs.addAll(c.schedulablesEnv.managedThreadEnvs);
			}
		}
		return mtEnvs;
	}

	public Map getNetworkMap()
	{
		Map networkMap = new HashMap();
		
		networkMap.put("SafeletName", getControlTier().getSafeletEnv().getName());
		networkMap.put("TopLevelSequencer", getControlTier().getTopLevelMissionSequencerEnv().getName());

		List tierList = new ArrayList();
		int i = 0;
		for (TierEnv tier : tiers)
		{			
			tierList.add(tier.toList());
		}
		networkMap.put("Tiers", tierList);
		
		return networkMap;
	}

}

package hijac.tools.tightrope.environments;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.lang.model.element.Name;

public class FrameworkEnv
{
	private ControlTierEnv controlTier;
	// private TierEnv tier0;
	// private NestedTiersEnv nestedTiers;

	private List<TierEnv> tiers;

	private TierEnv currentTier;
	private ClusterEnv currentCluster;

	// List<TierEnv> nestedTiers;

	class ControlTierEnv
	{
		ParadigmEnv safeletEnv;
		ParadigmEnv topLevelMissionSequencerEnv;

		public ControlTierEnv()
		{
			safeletEnv = new ParadigmEnv();
			topLevelMissionSequencerEnv = new ParadigmEnv();
		}

		public ParadigmEnv getSafeletEnv()
		{
			return safeletEnv;
		}

		public void setSafeletEnv(ParadigmEnv safeletEnv)
		{
			this.safeletEnv = safeletEnv;
		}

		public ParadigmEnv getTopLevelMissionSequencerEnv()
		{
			return topLevelMissionSequencerEnv;
		}

		public void setTopLevelMissionSequencerEnv(
				ParadigmEnv topLevelMissionSequencerEnv)
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

	}

	// private class NestedTiersEnv
	// {
	// List<TierEnv> nestedTiers;
	//
	// public NestedTiersEnv()
	// {
	// nestedTiers = new ArrayList<TierEnv>();
	// }
	//
	// public String toString()
	// {
	// if (nestedTiers.isEmpty())
	// {
	// return "No Nested Tiers";
	// } else
	// {
	// return "Nested Tiers Exist";
	// }
	// }
	// }

	private class ClusterEnv
	{
		ParadigmEnv missionEnv;
		SchedulablesEnv schedulablesEnv;

		public ClusterEnv()
		{
			missionEnv = new ParadigmEnv();
			schedulablesEnv = new SchedulablesEnv();
		}

		public ParadigmEnv getMissionEnv()
		{
			return missionEnv;
		}

		public void setMissionEnv(ParadigmEnv missionEnv)
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
		List<ParadigmEnv> schedulableMissionSequencerEnvs = new ArrayList<ParadigmEnv>();
		List<ParadigmEnv> managedThreadEnvs = new ArrayList<ParadigmEnv>();

		public List<ParadigmEnv> getPeriodEventHandlerEnvs()
		{
			return periodEventHandlerEnvs;
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

		public List<ParadigmEnv> getSchedulableMissionSequencerEnvs()
		{
			return schedulableMissionSequencerEnvs;
		}

		public void setSchedulableMissionSequencerEnvs(
				List<ParadigmEnv> schedulableMissionSequencerEnvs)
		{
			this.schedulableMissionSequencerEnvs = schedulableMissionSequencerEnvs;
		}

		public List<ParadigmEnv> getManagedThreadEnvs()
		{
			return managedThreadEnvs;
		}

		public void setManagedThreadEnvs(List<ParadigmEnv> managedThreadEnvs)
		{
			this.managedThreadEnvs = managedThreadEnvs;
		}

		public void addSchedulable(SchedulableTypeE type, Name name)
		{
			ParadigmEnv p = new ParadigmEnv();
			p.setName(name);

			if (type == SchedulableTypeE.MT)
			{
				managedThreadEnvs.add(p);
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
				schedulableMissionSequencerEnvs.add(p);
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

}

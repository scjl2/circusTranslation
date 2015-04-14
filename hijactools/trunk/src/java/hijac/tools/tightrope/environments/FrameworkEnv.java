package hijac.tools.tightrope.environments;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.lang.model.element.Name;

public class FrameworkEnv
{
	ControlTierEnv controlTier;
	TierEnv tier0;
	NestedTiersEnv nestedTiers;
//	List<TierEnv> nestedTiers;

	private class ControlTierEnv
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
			output += "Top-Level Mission Sequencer: " + topLevelMissionSequencerEnv.getName();

			return output;

		}
	}

	private class TierEnv
	{
		List<ClusterEnv> clusters;
		
		public TierEnv()
		{
			clusters = new ArrayList<ClusterEnv>();
		}

		public List<ClusterEnv> getClusters()
		{
			return clusters;
		}

		public void setClusters(List<ClusterEnv> clusters)
		{
			this.clusters = clusters;
		}
		
		public String toString()
		{
			if(clusters.isEmpty())
			{
				return "Tier Empty";
			}
			else
			{
				return "Tier Present";
			}
			
		}
	}

	private class NestedTiersEnv
	{
		List<TierEnv> nestedTiers;
		
		public NestedTiersEnv()
		{
			nestedTiers = new ArrayList<TierEnv>();
		}
		
		public String toString()
		{
			if (nestedTiers.isEmpty())
			{
				return "No Nested Tiers";
			}
			else
			{
				return "Nested Tiers Exist";
			}
		}
	}
	
	private class ClusterEnv
	{
		ParadigmEnv missionEnv;
		SchedulablesEnv schedulablesEnv;

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
	}

	private class SchedulablesEnv
	{
		List<ParadigmEnv> periodEventHandlerEnvs;
		List<ParadigmEnv> aperiodicEventHandlerEnvs;
		List<ParadigmEnv> oneShotEventHandlerEnvs;
		List<ParadigmEnv> schedulableMissionSequencerEnvs;
		List<ParadigmEnv> managedThreadEnvs;

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
	}

	public FrameworkEnv()
	{
		super();
		this.controlTier = new ControlTierEnv();
		this.tier0 = new TierEnv();
		this.nestedTiers = new NestedTiersEnv();
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
		return tier0;
	}

	public void setTier0(TierEnv tier0)
	{
		this.tier0 = tier0;
	}

	public NestedTiersEnv getNestedTiers()
	{
		return nestedTiers;
	}

	public void setNestedTiers(NestedTiersEnv nestedTiers)
	{
		this.nestedTiers = nestedTiers;
	}

	public void addSafelet(Name safelet)
	{
		controlTier.addSafelet(safelet);
	}

	public void addTopLevelMissionSequencer(Name topLevelMissionSequencer)
	{
		controlTier.addTopLevelMissionSequencer(topLevelMissionSequencer);

	}

	public String toString()
	{
		String output = "";
		output += "Control Tier:";
		output += System.getProperty("line.separator");
		output += controlTier.toString();
		output += System.getProperty("line.separator");
		output += "Tier 0:";
		output += System.getProperty("line.separator");		
		output += tier0.toString();
		output += System.getProperty("line.separator");
		output += "Nested Tiers:";
		output += System.getProperty("line.separator");
		output += nestedTiers.toString();

		return output;
	}

}

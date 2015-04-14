package hijac.tools.tightrope.environments;

public class FrameworkEnv
{
	ControlTierEnv controlTier;
	TierEnv tier0;
	List<TierEnv> nestedTiers;


	private class ControlTier
	{
		ParadigmEnv safeletEnv;
		ParadigmEnv topLevelMissionSequencerEnv;
	}

	private class TierEnv
	{
		List<ClusterEnv> clusters;
	}

	private class ClusterEnv
	{
		ParadigmEnv missionEnv;
		SchedulablesEnv schedulablesEnv;  
	}

	private class SchedulablesEnv
	{
		List<ParadigmEnv> periodEventHandlerEnvs;
		List<ParadigmEnv> aperiodicEventHandlerEnvs;
		List<ParadigmEnv> oneShotEventHandlerEnvs;
		List<ParadigmEnv> schedulableMissionSequencerEnvs;
		List<ParadigmEnv>  managedThreadEnvs;
	}

		


}

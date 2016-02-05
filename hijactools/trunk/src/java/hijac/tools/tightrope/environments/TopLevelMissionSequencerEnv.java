package hijac.tools.tightrope.environments;

public class TopLevelMissionSequencerEnv extends MissionSequencerEnv
{
	public TopLevelMissionSequencerEnv()
	{
		super();
	}
	
	@Override
	public void setId(String name)
	{
		super.setId(name+IdEnv.SID);
	}
	
	
}

package hijac.tools.tightrope.environments;

public class NestedMissionSequencerEnv extends MissionSequencerEnv
{
	public NestedMissionSequencerEnv()
	{
		super();
	}
	
	@Override
	public void setId(String name)
	{
		super.setId(name+IdEnv.SID);
	}
	
}

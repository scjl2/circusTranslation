package hijac.tools.tightrope.environments;

import hijac.tools.tightrope.utils.TightRopeString.Name;

public class NestedMissionSequencerEnv extends MissionSequencerEnv
{
	public NestedMissionSequencerEnv()
	{
		super();
	}
	
	@Override
	public void setId(String name)
	{
		super.setId(name+Name.SID);
		//TODO THis will need to be changed when I streamline the S+S sections
//		setObjectId(name);
		setThreadID(name);
	}
	
}

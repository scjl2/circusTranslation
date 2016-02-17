package nestedSequencer2;

import javax.realtime.PriorityParameters;
import javax.safetycritical.Mission;

public class TopMission1 extends Mission 
{

	@Override
	protected void initialize() 
	{
		devices.Console.println("TopMission1" + ":init");
		
		FirstMissionSequencer FirstSeq = new FirstMissionSequencer(new PriorityParameters(5), MySCJ.storageParameters);
		FirstSeq.register();

		SecondMissionSequencer SecondSeq = new SecondMissionSequencer(new PriorityParameters(15), MySCJ.storageParameters);
		SecondSeq.register();

		ThirdMissionSequencer ThirdSeq = new ThirdMissionSequencer(new PriorityParameters(10), MySCJ.storageParameters);
		ThirdSeq.register();
	}

	@Override
	public long missionMemorySize() 
	{
		return 60000;
	}

}

package nestedSequencer2;


import javax.realtime.PriorityParameters;
import javax.safetycritical.Mission;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.StorageParameters;

public class FirstMissionSequencer extends MissionSequencer<Mission>
{
    private Mission myMission;
    private boolean done = false;
    public FirstMissionSequencer(PriorityParameters priority, StorageParameters storage)
	{
		super(priority, storage,"1ms");
		myMission = new MyMission1();
    }

    @Override
    protected Mission getNextMission()
    {
  //  	devices.Console.println(getName() + ":getNextMission");
		if (done == false)
		{
	   		done = true;
		    return myMission;
		}
		else
		{
			return null;
		}
    }
}

package nestedSequencer2;

import javax.realtime.PriorityParameters;
import javax.safetycritical.Mission;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.StorageParameters;

public class SecondMissionSequencer extends MissionSequencer<Mission>
{
   private Mission myMission;
    private boolean done = false;

    public  SecondMissionSequencer(PriorityParameters priority, StorageParameters storage)
    {
	    super(priority, storage,"2ms");
	    myMission = new MyMission2();
    }

    @Override
    protected Mission getNextMission()
    {
    //	devices.Console.println(getName() + ":getNextMission");
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

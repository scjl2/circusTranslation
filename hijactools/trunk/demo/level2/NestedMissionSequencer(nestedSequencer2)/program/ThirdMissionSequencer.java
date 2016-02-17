package nestedSequencer2;

import javax.realtime.PriorityParameters;
import javax.safetycritical.Mission;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.StorageParameters;

public class ThirdMissionSequencer extends MissionSequencer<Mission>
{
   private MyMission3 myMission;
    private boolean done = false;

    public  ThirdMissionSequencer(PriorityParameters priority, StorageParameters storage)
	{
		super(priority, storage,"3ms");
		myMission = new MyMission3();
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

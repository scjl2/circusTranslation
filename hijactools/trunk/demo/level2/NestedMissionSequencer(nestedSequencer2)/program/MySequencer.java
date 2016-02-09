package nestedSequencer2;

import javax.realtime.PriorityParameters;
import javax.safetycritical.Mission;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.StorageParameters;

public class MySequencer extends MissionSequencer<Mission>
{
    //private Mission myMission;
    private TopMission1 myMission;
    private boolean done = false;

    public MySequencer(PriorityParameters priority, StorageParameters storage)
    {
	    super(priority, storage,"OM--ms");

    	myMission = new TopMission1();
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

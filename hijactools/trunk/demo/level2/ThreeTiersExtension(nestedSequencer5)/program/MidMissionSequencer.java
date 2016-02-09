package nestedSequencer5;

import javax.realtime.PriorityParameters;
import javax.safetycritical.Mission;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.SCJAllowed;

import devices.Console;

class MidMissionSequencer extends MissionSequencer<Mission>
{
	private int releases = 0;

	public MidMissionSequencer(PriorityParameters priority, StorageParameters storage,
			String name) throws IllegalStateException
	{
		super(priority, storage, name);
	}

	@Override
	@SCJAllowed(Level.SUPPORT)
	protected Mission getNextMission()
	{
	//	Console.println(getName()+  " getNextMission");

		if (releases == 0 )
		{
			MidMissionA missionA = new MidMissionA();

			releases ++;
			return missionA;
		}
		else if (releases == 1)
		{
			MidMissionB missionB = new MidMissionB();

			releases ++;
			return missionB;
		}
		else
		{
			return null;
		}
	}

}

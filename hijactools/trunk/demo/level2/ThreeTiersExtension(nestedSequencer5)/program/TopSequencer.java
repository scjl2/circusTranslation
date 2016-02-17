package nestedSequencer5;

import javax.realtime.PriorityParameters;
import javax.safetycritical.Mission;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.SCJAllowed;

import devices.Console;

class TopSequencer extends MissionSequencer<Mission>
{
	private boolean notReleased = true;

	public TopSequencer(PriorityParameters priority, StorageParameters storage,
			String name) throws IllegalStateException
	{
		super(priority, storage, name);
	}

	@Override
	@SCJAllowed(Level.SUPPORT)
	protected Mission getNextMission()
	{
	//	Console.println(getName()+  " getNextMission");

		if (notReleased)
		{
			TopMission mission = new TopMission();

			notReleased = false;
			return mission;
		}
		else
		{
			return null;
		}
	}

}

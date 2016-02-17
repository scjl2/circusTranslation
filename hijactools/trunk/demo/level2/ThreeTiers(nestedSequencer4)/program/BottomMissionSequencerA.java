package nestedSequencer4;

import javax.realtime.PriorityParameters;
import javax.safetycritical.Mission;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.SCJAllowed;

import devices.Console;

class BottomMissionSequencerA extends MissionSequencer<Mission>
{
	private boolean notReleased = true;

	public BottomMissionSequencerA(PriorityParameters priority, StorageParameters storage,
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
			BottomMissionA mission = new BottomMissionA();

			notReleased = false;
			return mission;
		}
		else
		{
			return null;
		}
	}

}

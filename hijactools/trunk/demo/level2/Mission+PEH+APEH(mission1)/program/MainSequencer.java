package mission1;

import javax.realtime.PriorityParameters;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.SCJAllowed;

import devices.Console;

class MainSequencer extends MissionSequencer<MyMission>
{
	private boolean notReleased = true;

	public MainSequencer(PriorityParameters priority, StorageParameters storage,
			String name) throws IllegalStateException
	{
		super(priority, storage, name);
	}

	@Override
	@SCJAllowed(Level.SUPPORT)
	protected MyMission getNextMission()
	{
		//Console.println(getName()+  " getNextMission");
		
		if (notReleased)
		{
			 MyMission mission = new MyMission();
			notReleased = false;
			return mission;
		}
		else
		{
			return null;
		}
	}

}

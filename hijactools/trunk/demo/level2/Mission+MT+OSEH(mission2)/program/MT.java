package mission2;

import javax.realtime.PriorityParameters;
import javax.safetycritical.ManagedThread;
import javax.safetycritical.StorageParameters;

import devices.Console;

class MT extends ManagedThread
{
	private MissionA controllingMission;
	
	public MT(PriorityParameters pri, StorageParameters storage, MissionA controllingMission)
	{
		super(pri, storage);
		this.controllingMission = controllingMission;
	}

	@Override
	public void run()
	{
		Console.println("MT Release");
		controllingMission.systemAction();
		
	}
}
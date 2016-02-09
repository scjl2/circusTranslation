package nestedSequencer5;

import javax.realtime.AperiodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.AperiodicEventHandler;
import javax.safetycritical.Mission;
import javax.safetycritical.StorageParameters;

import devices.Console;

class APEH extends AperiodicEventHandler
{
	private Mission controllingMission = null;
	
	public APEH(PriorityParameters priority, AperiodicParameters release,
			StorageParameters storage, String name, Mission controllingMission)
	{
		super(priority, release, storage, name);	
		this.controllingMission = controllingMission;
	}

	@Override
	public void handleAsyncEvent()
	{
		Console.println("APEH Release");
		controllingMission.requestTermination();
	}
	
}

package threeOneShots;

import javax.realtime.AperiodicParameters;
import javax.realtime.HighResolutionTime;
import javax.realtime.PriorityParameters;
import javax.safetycritical.Mission;
import javax.safetycritical.OneShotEventHandler;
import javax.safetycritical.StorageParameters;

import devices.Console;

class OSEH2 extends OneShotEventHandler
{
	Mission controllingMision;
	
	public OSEH2(PriorityParameters pri, HighResolutionTime start,
			AperiodicParameters aParams, StorageParameters storage, Mission controllingMission)
	{
		super(pri, start, aParams, storage);
		
		this.controllingMision = controllingMission;
	}

	@Override
	public void handleAsyncEvent()
	{
		Console.println("OSEH2 Release");
		
		controllingMision.requestTermination();
	}
	
}

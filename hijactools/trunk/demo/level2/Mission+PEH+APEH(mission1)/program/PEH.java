package mission1;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.AperiodicEventHandler;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;

import devices.Console;

class PEH extends PeriodicEventHandler
{
	private AperiodicEventHandler apeh = null;

	PEH(PriorityParameters priority, PeriodicParameters release,
			StorageParameters storage, String name, AperiodicEventHandler apeh)
	{
		super(priority, release, storage);	
		if(apeh == null)
		{
			throw new IllegalArgumentException("apeh cannot be null");
		}
		this.apeh=apeh;
	}

	@Override
	public void handleAsyncEvent()
	{
		Console.println("PEH Release");
				
		apeh.release();
	}
}
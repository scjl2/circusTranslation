package nestedSequencer2;

import javax.safetycritical.Mission;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;
import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;

public class MyPEH3 extends PeriodicEventHandler
{
	int count = 0;
	Mission m;

	public MyPEH3(PriorityParameters priority, PeriodicParameters release, StorageParameters storage, Mission m)
	{
		super(priority, release, storage);
		this.m = m;
	}

	public void handleAsyncEvent()
	{
	//	devices.Console.println(getName() + ":handleAsyncEvent");
		count++;

		if (count == 10)
        {
			m.requestTermination();
		}
	}
}

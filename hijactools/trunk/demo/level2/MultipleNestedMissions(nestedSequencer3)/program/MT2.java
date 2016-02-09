package nestedSequencer3;

import javax.realtime.PriorityParameters;
import javax.safetycritical.ManagedThread;
import javax.safetycritical.StorageParameters;

import devices.Console;

class MT2 extends ManagedThread
{
	public MT2(PriorityParameters pri, StorageParameters storage)
	{
		super(pri, storage);
	}

	@Override
	public void run()
	{
		Console.println("MT2 Release");
		Console.println("MT2 Performing Actions");

	}
}
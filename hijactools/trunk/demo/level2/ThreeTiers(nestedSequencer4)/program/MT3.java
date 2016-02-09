package nestedSequencer4;

import javax.realtime.PriorityParameters;
import javax.safetycritical.ManagedThread;
import javax.safetycritical.StorageParameters;

import devices.Console;

class MT3 extends ManagedThread
{

	public MT3(PriorityParameters pri, StorageParameters storage)
	{
		super(pri, storage);
	}

	@Override
	public void run()
	{
		Console.println("MT3 Release");
	}
}
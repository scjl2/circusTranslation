package scjlevel2examples.flatbuffer;

import javax.realtime.PriorityParameters;
import javax.safetycritical.ManagedThread;
import javax.safetycritical.StorageParameters;

import devices.Console;

public class Reader extends ManagedThread
{
	private final FlatBufferMission fbMission;


	public Reader(PriorityParameters priority, StorageParameters storage,
			FlatBufferMission fbMission)
	{
		super(priority, storage);
		this.fbMission = fbMission;
	}

	public void run()
	{
		Console.println("Reader!");

		while (!fbMission.terminationPending())
		{
				int result=-1;

				try
				{
					result = fbMission.read();
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				Console.println("Reader Read " + result
						+ " from Buffer");
		}
	}
}

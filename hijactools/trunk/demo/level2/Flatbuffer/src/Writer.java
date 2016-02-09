package scjlevel2examples.flatbuffer;

import javax.realtime.PriorityParameters;
import javax.safetycritical.ManagedThread;
import javax.safetycritical.StorageParameters;

import devices.Console;

public class Writer extends ManagedThread
{
	private final FlatBufferMission fbMission;
	// private int i = 1;

	public Writer(PriorityParameters priority, StorageParameters storage,
			FlatBufferMission fbMission)
	{
		super(priority, storage);
		this.fbMission = fbMission;
	}

	public void run()
	{
		Console.println("Writer!");
		//Here, not as feild, becasue it goes to the class otherwise.
		int i = 1;

		while (!fbMission.terminationPending())
		{
					try
					{
						fbMission.write(i);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}

				i++;

				if(i >= 5)
				{
					fbMission.requestTermination();
				}
		}
	}
}

package scjlevel2examples.flatbuffer;

import javax.realtime.PriorityParameters;
import javax.safetycritical.ManagedThread;
import javax.safetycritical.StorageParameters;

import devices.Console;

public class Writer extends ManagedThread
{
	private final FlatBufferMission fbMission;
	private int i = 1;

	public Writer(PriorityParameters priority, StorageParameters storage,
			FlatBufferMission fbMission)
	{
		super(priority, storage);

		this.fbMission = fbMission;
	}

	public void run()
	{
		Console.println("Writer!");

		//boolean terminationPending = fbMission.terminationPending();
		while (!fbMission.terminationPending())
		{
		
//				while (!fbMission.buffer.bufferEmpty("Writer"))
//				{
					try
					{
						fbMission.write(i);
					}
					catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
//				}

				
				i++;
				
				boolean keepWriting = i >= 5 ;
				if(!keepWriting)
				{
					fbMission.requestTermination();
				}

				
			//terminationPending = fbMission.terminationPending();
		}

	}
}

package scjlevel2examples.flatbuffer;

import javax.realtime.PriorityParameters;
import javax.safetycritical.ManagedThread;
import javax.safetycritical.StorageParameters;

public class Reader extends ManagedThread
{
	private final Writer writer;
	private final FlatBufferMission fbMission;

	public Reader(PriorityParameters priority, StorageParameters storage,
			FlatBufferMission fbMission, Writer writer)
	{
		super(priority, storage);

		this.fbMission = fbMission;
		this.writer = writer;
	}

	
	public void run()
	{
		System.out.println("Reader!");

		while (!fbMission.terminationPending())
		{
			try
			{
				while (fbMission.bufferEmpty())
				{
					fbMission.waitOnMission(); //synchronised
				}

				System.out.println("I Read: " + fbMission.read()); // synchronised

				fbMission.notifyOnMission(); //synchronised
			}
			catch (InterruptedException ie)
			{
				//Handle Interruption
			}

		}
	}
}

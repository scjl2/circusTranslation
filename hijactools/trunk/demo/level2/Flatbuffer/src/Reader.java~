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

		//rewritten, only variables in boolean conditions
		boolean terminationPending = fbMission.terminationPending();
		while (!terminationPending)
		{
			try
			{
				//rewritten, only variables in boolean conditions
				boolean bufferEmpty = fbMission.bufferEmpty("Reader");
				while (bufferEmpty)
				{
					fbMission.waitOnMission("Reader");
				}
				
				int value = fbMission.read();
				Console.println("Reader Read " + value
						+ " from Buffer");	

			} catch (InterruptedException ie)
			{
				// Handle Interruption
			}

		}
	}
}

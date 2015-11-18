package scjlevel2examples.flatbuffer;

import javax.realtime.PriorityParameters;
import javax.safetycritical.Mission;
import javax.safetycritical.StorageParameters;
import javax.scj.util.Const;
import javax.safetycritical.Services;

import devices.Console;

public class FlatBufferMission extends Mission
{
	//Buffer buffer;
	private volatile int buffer;
	private testClass t;


	public FlatBufferMission()
	{
		Console.println("FlatBufferMission");
		buffer = 0;
		Services.setCeiling(this, 20);
		t = new testClass();
	}

	protected void initialize()
	{
		StorageParameters storageParameters = new StorageParameters(150 * 1000,
				new long[] { Const.HANDLER_STACK_SIZE },
				Const.PRIVATE_MEM_DEFAULT, Const.IMMORTAL_MEM_DEFAULT,
				Const.MISSION_MEM_DEFAULT - 100 * 1000);

		new Reader(new PriorityParameters(10), storageParameters, this).register();

		new Writer(new PriorityParameters(10), storageParameters, this).register();
		
		//buffer = new Buffer();

		Console.println("FlatBufferMission init");
	}

	public boolean bufferEmpty(String name)
	{
		Console.println(name + " Checking Buffer Empty");
		return buffer == 0;
	}

	public synchronized void write(int update) throws InterruptedException
	{
		//boolean bufferEmpty = bufferEmpty("Writer");
		while (!bufferEmpty("Writer"))
		{
			Console.println("Writer" + " Waiting on Buffer");
			
			this.wait();
			
			//bufferEmpty =  bufferEmpty("Writer");
		}

		Console.println("writing " + update + " to Buffer");
		buffer = update;
		this.notify();
	}

	public synchronized int read() throws InterruptedException
	{
		//boolean bufferEmptyCond = bufferEmpty("Reader");
		while(bufferEmpty("Reader"))
		{
			Console.println("Reader" + " Waiting on Buffer");
			
			
			this.wait();
			
			//bufferEmptyCond = bufferEmpty("Reader");
		}

		int out = buffer;
		Console.println("Reading " + out + " from Buffer");
		buffer = 0;
		this.notify();

		return out;
	}
	
	public boolean cleanUp()
	{
		Console.print("FlatBufferMission Cleanup");
		return false;
	}

	public long missionMemorySize()
	{
		return 1048576;
	}
}

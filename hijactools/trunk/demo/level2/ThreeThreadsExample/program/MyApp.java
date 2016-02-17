package threeThreads;

import javax.realtime.PriorityParameters;
import javax.safetycritical.Mission;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.Safelet;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.Phase;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;
import javax.scj.util.Const;

import devices.Console;

class MyApp implements Safelet<Mission>
{
	static PriorityParameters pri = new PriorityParameters(5);
	static StorageParameters storage = new StorageParameters(
			Const.OVERALL_BACKING_STORE_DEFAULT - 1000000,
			new long[] { Const.HANDLER_STACK_SIZE },
			Const.PRIVATE_MEM_DEFAULT, 10000 * 2, Const.MISSION_MEM_DEFAULT);
	

	@Override
	@SCJAllowed(Level.SUPPORT)
	@SCJRestricted(Phase.INITIALIZE)
	public MissionSequencer<Mission> getSequencer()
	{		
		MissionSequencer<Mission> mainSequencer = new MainSequencer(pri, storage, "Main Sequencer");
		return mainSequencer;
	}

	@Override
	@SCJAllowed(Level.SUPPORT)
	public long immortalMemorySize()
	{
		return Const.IMMORTAL_MEM_DEFAULT;
	}

	@Override
	@SCJAllowed(Level.SUPPORT)
	@SCJRestricted(Phase.INITIALIZE)
	public void initializeApplication()
	{
		Console.println("Init Application");		
	}
	
}

package nestedSequencer5;

import javax.safetycritical.Mission;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.SCJAllowed;

import devices.Console;

class TopMission extends Mission
{
	@Override
	@SCJAllowed(Level.SUPPORT)
	protected void initialize()
	{
		Console.println("Main Mission initialize");
		
		MT1 thread1 = new MT1(MyApp.pri, MyApp.storage);
		thread1.register();

		MidMissionSequencer nestedSequencer = new MidMissionSequencer(MyApp.pri, MyApp.storage, "Nested Mission Sequencer");
		nestedSequencer.register();
	}

	@Override
	@SCJAllowed(Level.SUPPORT)
	public long missionMemorySize()
	{
		return 1048576;
	}
}
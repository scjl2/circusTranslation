package sequentialMissions;

import javax.safetycritical.Mission;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.SCJAllowed;

import devices.Console;

class MissionB extends Mission
{
	@Override
	@SCJAllowed(Level.SUPPORT)
	protected void initialize()
	{
		Console.println("MissionA initialize");

		MT1 thread12 = new MT1(MyApp.pri, MyApp.storage);
		thread12.register();

		// MT2 thread22 = new MT2(MyApp.pri, MyApp.storage);
		// thread22.register();


	}

	@Override
	@SCJAllowed(Level.SUPPORT)
	public long missionMemorySize()
	{
		return 1048576;
	}
}

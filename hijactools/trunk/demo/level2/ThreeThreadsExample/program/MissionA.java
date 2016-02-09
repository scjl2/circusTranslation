package threeThreads;

import javax.safetycritical.Mission;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.SCJAllowed;

import devices.Console;

class MissionA extends Mission
{
	@Override
	@SCJAllowed(Level.SUPPORT)
	protected void initialize()
	{
		Console.println("MissionA initialize");
		
		MT1 thread1 = new MT1(MyApp.pri, MyApp.storage);
		thread1.register();

		MT2 thread2 = new MT2(MyApp.pri, MyApp.storage);
		thread2.register();

		MT3 thread3 = new MT3(MyApp.pri, MyApp.storage);
		thread3.register();
	}

	@Override
	@SCJAllowed(Level.SUPPORT)
	public long missionMemorySize()
	{
		return 1048576;
	}
}
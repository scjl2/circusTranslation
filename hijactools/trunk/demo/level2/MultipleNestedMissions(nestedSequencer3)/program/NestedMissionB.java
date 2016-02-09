package nestedSequencer3;

import javax.safetycritical.Mission;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.SCJAllowed;

import devices.Console;

class NestedMissionB extends Mission
{
	@Override
	@SCJAllowed(Level.SUPPORT)
	protected void initialize()
	{
		Console.println("NestedMissionB initialize");
		
		MT2 thread = new MT2(MyApp.pri, MyApp.storage);
		thread.register();
		
		
	}

	@Override
	@SCJAllowed(Level.SUPPORT)
	public long missionMemorySize()
	{
		return 1048576;
	}
	
}

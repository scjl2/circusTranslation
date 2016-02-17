package nestedSequencer5;

import javax.safetycritical.Mission;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.SCJAllowed;

import devices.Console;

class MidMissionA extends Mission
{
	@Override
	@SCJAllowed(Level.SUPPORT)
	protected void initialize()
	{
		Console.println("Nested Mission initialize");

		BottomMissionSequencerA nestedSequencerA = new BottomMissionSequencerA(MyApp.pri,
				MyApp.storage, "Nested Mission Sequencer A");
		nestedSequencerA.register();

	}

	@Override
	@SCJAllowed(Level.SUPPORT)
	public long missionMemorySize()
	{
		return 1048576;
	}
}
package nestedSequencer2;

import javax.realtime.Clock;
import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.RelativeTime;
import javax.safetycritical.Mission;

public class MyMission2 extends Mission 
{
	@Override
	protected void initialize() 
	{
		devices.Console.println("TopMission1" + ":init");
		MyPEH2 myPEH = new MyPEH2(new PriorityParameters(20), new PeriodicParameters(new RelativeTime(Clock.getRealtimeClock()),
				new RelativeTime(1000, 0, Clock.getRealtimeClock())), MySCJ.storageParameters_Handlers, this);
		myPEH.register();
	}

	@Override
	public long missionMemorySize() 
	{
		return 50000;
	}	
}
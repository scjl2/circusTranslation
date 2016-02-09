package nestedSequencer2;

import javax.realtime.Clock;
import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.RelativeTime;
import javax.safetycritical.Mission;

public class MyMission1 extends Mission 
{
	@Override
	protected void initialize() 
	{
		devices.Console.println("TopMission1" + ":init");
		MyPEH1 myPEH = new MyPEH1(new PriorityParameters(5), new PeriodicParameters(new RelativeTime(Clock.getRealtimeClock()),
				new RelativeTime(1000, 0, Clock.getRealtimeClock())), MySCJ.storageParameters_Handlers, this);
		myPEH.register();
	}

	@Override
	public long missionMemorySize() 
	{
		return 50000;
	}	
}
package nestedSequencer2;

import javax.realtime.PriorityParameters;
import javax.safetycritical.Mission;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.Safelet;

public class MyApp implements Safelet<Mission>
{	
	@Override
	public MissionSequencer<Mission> getSequencer() 
	{
		return new MySequencer(new PriorityParameters(15), MySCJ.storageParameters);
	}

	@Override
	public long immortalMemorySize() 
	{
		return 10000;
	}

	public void initializeApplication() 
	{	   	  
		devices.Console.println("Init Application");
	}
}
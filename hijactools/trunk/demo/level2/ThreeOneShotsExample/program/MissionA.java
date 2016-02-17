package threeOneShots;

import javax.realtime.AperiodicParameters;
import javax.realtime.RelativeTime;
import javax.safetycritical.Mission;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.SCJAllowed;

class MissionA extends Mission
{
	@Override
	@SCJAllowed(Level.SUPPORT)
	protected void initialize()
	{
		AperiodicParameters apParams = new AperiodicParameters(new RelativeTime(5, 0), null);
		OSEH1 OneShot = new OSEH1(MyApp.pri, new RelativeTime(60,0),apParams, MyApp.storage, this);
		OneShot.register();
		
		OSEH2 OneShot2 = new OSEH2(MyApp.pri, new RelativeTime(60,0),apParams, MyApp.storage, this);
		OneShot2.register();
		
		OSEH3 OneShot3 = new OSEH3(MyApp.pri, new RelativeTime(60,0),apParams, MyApp.storage, this);
		OneShot3.register();
		
	}

	@Override
	@SCJAllowed(Level.SUPPORT)
	public long missionMemorySize()
	{
		return 1048576;
	}
	
}

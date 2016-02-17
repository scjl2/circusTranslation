package mission1;

import javax.realtime.AperiodicParameters;
import javax.realtime.PeriodicParameters;
import javax.realtime.RelativeTime;
import javax.safetycritical.Mission;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.SCJAllowed;

class MyMission extends Mission
{
	@Override
	@SCJAllowed(Level.SUPPORT)
	protected void initialize()
	{
		AperiodicParameters apParams = new AperiodicParameters(new RelativeTime(5, 0), null);
		APEH apeh = new APEH(MyApp.pri, apParams, MyApp.storage, "APEH", this);
		apeh.register();
		
		PeriodicParameters pParams = new PeriodicParameters(new RelativeTime(60,0), new RelativeTime(5,0));
		PEH peh = new PEH(MyApp.pri, pParams ,MyApp.storage, "PEH", apeh);
		peh.register();
		
		
	}

	@Override
	@SCJAllowed(Level.SUPPORT)
	public long missionMemorySize()
	{
		return 1048576;
	}
	
}

/**
 * 
 */
package papabench.scj;

import javax.realtime.PriorityParameters;
import javax.safetycritical.CyclicExecutive;
import javax.safetycritical.CyclicSchedule;
import javax.safetycritical.Mission;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.SingleMissionSequencer;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.annotate.Level;

import papabench.scj.commons.data.FlightPlan;
import papabench.scj.commons.data.impl.Simple03FlightPlan;
import papabench.scj.schedule.SimplifiedPapabenchCyclicSchedule;

/**
 * The implementation of PapaBench based on SCJ Level 0.
 * 
 * The {@link CyclicExecutive} implements also a {@link Mission}.
 * 
 * @author Michal Malohlava
 *
 */
//@SCJAllowed(value=Level.LEVEL_0, members=true)
//@SCJAllowed(value=Level.LEVEL_0)
//@SCJAllowed
public class PapaBenchMission extends Mission {	
	
	public static final long MISSION_MEMORY_SIZE = 1000L;

	public PapaBenchMission() {

	}
	
	public CyclicSchedule getSchedule(PeriodicEventHandler[] pehs) {
		return SimplifiedPapabenchCyclicSchedule.generateSchedule(pehs);
	}

	@Override
	protected void initialize() {
		/*
		 * Creates an instance of PapaBench. 
		 */
		PapaBench papaBench = new PapaBenchSCJImpl();
		
		// setup flight plan for this mission
		papaBench.setFlightPlan(getFlightPlan());
		
		// initialize papabench 
		papaBench.init();
	}
	
	@Override
	public long missionMemorySize() {
		return MISSION_MEMORY_SIZE;
	}
	
	
   /* Modified by Frank Zeyda */
	//@Override
   /* End of Modification */
	protected void cleanup() {
	}

	private FlightPlan getFlightPlan() {
		return new Simple03FlightPlan();
	}
		
}

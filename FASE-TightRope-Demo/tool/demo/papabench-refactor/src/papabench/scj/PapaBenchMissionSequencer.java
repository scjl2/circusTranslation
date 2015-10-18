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

public class PapaBenchMissionSequencer extends MissionSequencer {	
	
	

    public PapaBenchMissionSequencer() {
        super(new PriorityParameters(
				javax.realtime.PriorityScheduler.instance().getNormPriority()),
				new StorageParameters(100000L, 1000, 1000));
    }

    public Mission getInitialMission() {
        PapaBenchMission mission = new PapaBenchMission();
        return mission;
    }

	public Mission getNextMission() {
		return null;
	}
	
}

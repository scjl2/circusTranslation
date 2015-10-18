/**
 * 
 */
package papabench.scj;

import javax.realtime.PriorityParameters;
import javax.safetycritical.CyclicExecutive;
import javax.safetycritical.CyclicSchedule;
import javax.safetycritical.Mission;
import javax.safetycritical.Safelet;
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
public class PapaBenchSCJLevel0Application implements Safelet {	
	
//	public PapaBenchSCJLevel0Application(StorageParameters storage) {
   /* Added by Frank Zeyda */
//   super(storage);
   /* End of Addition */
//		FIXME super(storage); ? should be called ?
//	}

	@Override
	public MissionSequencer getSequencer() {
        PapaBenchMissionSequencer sequencer = new PapaBenchMissionSequencer();
		return sequencer;
	}
	
	public void setUp() {		
	}

	public void tearDown() {		
	}
		
	public Level getLevel() {
		return Level.LEVEL_0;		
	}		
}

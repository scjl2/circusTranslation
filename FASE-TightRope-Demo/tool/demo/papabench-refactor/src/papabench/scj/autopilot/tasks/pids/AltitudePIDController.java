/**
 * 
 */
package papabench.scj.autopilot.tasks.pids;

import papabench.scj.autopilot.modules.AutopilotStatus;
import papabench.scj.autopilot.modules.Estimator;
import papabench.scj.autopilot.modules.Navigator;
import papabench.scj.utils.MathUtils;

/**
 * @author Michal Malohlava
 * 
 */
//@SCJAllowed
public class AltitudePIDController extends AbstractPIDController {

	private float altitudePGain = ALTITUDE_PGAIN;

	public void control(AutopilotStatus status, Estimator estimator, Navigator navigator) {		
		 float err = estimator.getPosition().z -  navigator.getDesiredAltitude();
		 
		 float desiredClimb = navigator.getPreClimb() + altitudePGain * err;
		 desiredClimb = MathUtils.symmetricalLimiter(desiredClimb, CLIMB_MAX);
		 
		 status.setClimb(desiredClimb);
	}
}

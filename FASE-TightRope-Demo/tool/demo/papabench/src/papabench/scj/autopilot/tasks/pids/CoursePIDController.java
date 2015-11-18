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
public class CoursePIDController extends AbstractPIDController {
	
	private float maxRoll 		= MAX_ROLL;
	private float coursePGain 	= COURSE_PGAIN; 
	
	public void control(AutopilotStatus status, Estimator estimator, Navigator navigator) {
		  float err = estimator.getHorizontalSpeed().direction - navigator.getDesiredCourse();
		  
		  err = MathUtils.normalizeRadAngle(err);
		  
		  float navDesiredRoll = coursePGain * err; //  * fspeed / AIR_SPEED;
		  
		  navDesiredRoll = MathUtils.symmetricalLimiter(navDesiredRoll, maxRoll);
		  
		  // propagate value
		  navigator.setDesiredRoll(navDesiredRoll);		  
	}
}

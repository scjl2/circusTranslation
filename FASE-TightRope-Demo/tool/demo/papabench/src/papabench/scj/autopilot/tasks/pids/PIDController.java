/**
 * 
 */
package papabench.scj.autopilot.tasks.pids;

import papabench.scj.autopilot.modules.AutopilotStatus;
import papabench.scj.autopilot.modules.Estimator;
import papabench.scj.autopilot.modules.Navigator;


/**
 * PID Controller interface
 * 
 *  Input: environment state (plane + sensors + estimator)
 *  Output: desired state 
 * 
 * @author Michal Malohlava
 *
 */
public interface PIDController {
	
	void control(AutopilotStatus status, Estimator estimator, Navigator navigator);

}

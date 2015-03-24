/**
 * 
 */
package papabench.scj.commons.status;

import papabench.scj.autopilot.modules.AutopilotStatus;

/**
 * @author Michal Malohlava
 *
 */
public interface AirplaneStatus {
	
	/**
	 * Returns mode of autopilot module.
	 * 
	 * @return
	 */
	AutopilotStatus getAutopilotStatus(); 

}

/**
 * 
 */
package papabench.scj.commons.data;

import papabench.scj.autopilot.modules.AutopilotStatus;
import papabench.scj.autopilot.modules.Estimator;
import papabench.scj.autopilot.modules.Navigator;


/**
 * @author Michal Malohlava
 *
 */
public interface FlightPlan {
	
	/**
	 * Initialize the flight plan.
	 * 
	 * Allocate desired memory (preferably in mission memory).
	 */
	void init();

	String getName();	
	
	void execute();
	
	void setEstimator(Estimator estimator);
	void setAutopilotStatus(AutopilotStatus status);
	void setNavigator(Navigator navigator);
	
	float getGroundAltitude();
	
	float getSecureAltitude();
	
}

/**
 * 
 */
package papabench.scj;

import papabench.scj.autopilot.modules.AutopilotModule;
import papabench.scj.commons.data.FlightPlan;
import papabench.scj.commons.modules.Module;
import papabench.scj.fbw.modules.FBWModule;

/**
 * Interface of PapaBench benchmark. 
 * 
 * It represents top-level module providing access to autopilot and fly-by-wire subsystems.
 * 
 * @author Michal Malohlava
 *
 */
public interface PapaBench extends Module {
	
	/**
	 * Returns autopilot module.
	 * 
	 * @return
	 */
	AutopilotModule getAutopilotModule();
	
	/**
	 * Returns fly-by-wire module.
	 * 
	 * @return
	 */
	FBWModule getFBWModule();
	
	/**
	 * Setup the mission flight-plan.
	 * 
	 * @param flightPlan
	 */
	void setFlightPlan(FlightPlan flightPlan);
}

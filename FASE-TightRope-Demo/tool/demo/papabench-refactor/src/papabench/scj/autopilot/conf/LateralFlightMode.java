/**
 * 
 */
package papabench.scj.autopilot.conf;

import papabench.scj.autopilot.modules.AutopilotModule;
import papabench.scj.autopilot.modules.Navigator;

/**
 * Lateral model - setup by {@link Navigator}.
 * 
 * @see AutopilotModule
 * @see Navigator
 * 
 * @author Michal Malohlava
 * TODO put here description for each mode
 */
public enum LateralFlightMode {
	MANUAL,
	ROLL_RATE,
	ROLL,
	COURSE,
	NB
}

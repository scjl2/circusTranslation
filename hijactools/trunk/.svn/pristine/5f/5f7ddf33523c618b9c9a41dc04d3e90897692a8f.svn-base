/**
 * 
 */
package papabench.scj.autopilot.tasks;

import static papabench.scj.utils.ParametersFactory.getPeriodicParameters;
import static papabench.scj.utils.ParametersFactory.getPriorityParameters;
import papabench.scj.autopilot.conf.AutopilotMode;
import papabench.scj.autopilot.conf.VerticalFlightMode;
import papabench.scj.autopilot.conf.PapaBenchAutopilotConf.AltitudeControlTaskConf;
import papabench.scj.autopilot.modules.AutopilotModule;
import papabench.scj.autopilot.tasks.pids.AltitudePIDController;
import papabench.scj.commons.tasks.PapaBenchPeriodicTask;

/**
 * Task controlling plane altitude.
 * 
 * f = 40Hz
 * 
 * @author Michal Malohlava
 *
 */
//@SCJAllowed
public class AltitudeControlTask extends PapaBenchPeriodicTask implements
		AltitudeControlTaskConf {

	// instantiated in mission memory to preserve PID specific attributes
	private AutopilotModule autopilotModule;
	
	// PID controller for altitude - it cannot be only a static method because it can 
	// have inner state (e.g., last error value)
	private AltitudePIDController pidController; 
	
	public AltitudeControlTask(AutopilotModule autopilotModuleArg) {
		super(getPriorityParameters(PRIORITY), 
				getPeriodicParameters(RELEASE_MS, PERIOD_MS), 
				null, // CHECKME 
				SIZE);
		
		autopilotModule = autopilotModuleArg;
		pidController = new AltitudePIDController();
	}

	@Override
	public void handlePeriod() {
		if (autopilotModule.getAutopilotMode() == AutopilotMode.AUTO2
			|| autopilotModule.getAutopilotMode() == AutopilotMode.HOME) {
			
			if (autopilotModule.getVerticalFlightMode() == VerticalFlightMode.AUTO_ALTITUDE) {
				pidController.control(autopilotModule, autopilotModule.getEstimator(), autopilotModule.getNavigator());								
			}
		}
	}
		
	public String getTaskName() {
		return NAME;
	}	
}

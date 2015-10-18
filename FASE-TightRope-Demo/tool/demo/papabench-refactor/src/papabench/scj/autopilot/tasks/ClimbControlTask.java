/**
 * 
 */
package papabench.scj.autopilot.tasks;

import static papabench.scj.utils.ParametersFactory.getPeriodicParameters;
import static papabench.scj.utils.ParametersFactory.getPriorityParameters;
import papabench.scj.autopilot.conf.AutopilotMode;
import papabench.scj.autopilot.conf.VerticalFlightMode;
import papabench.scj.autopilot.conf.PapaBenchAutopilotConf.ClimbControlTaskConf;
import papabench.scj.autopilot.modules.AutopilotModule;
import papabench.scj.autopilot.tasks.pids.ClimbPIDController;
import papabench.scj.commons.tasks.PapaBenchPeriodicTask;

/**
 * f = 40Hz
 * 
 * @author Michal Malohlava
 *
 */
//@SCJAllowed
public class ClimbControlTask extends PapaBenchPeriodicTask implements
		ClimbControlTaskConf {
	
	private AutopilotModule autopilotModule;
	
	private ClimbPIDController pidController;

	public ClimbControlTask(AutopilotModule autopilotModuleArg) {
		super(getPriorityParameters(PRIORITY), 
				getPeriodicParameters(RELEASE_MS, PERIOD_MS), 
				null, // FIXME 
				SIZE);	
		
		autopilotModule = autopilotModuleArg;
		pidController = new ClimbPIDController();
	}
	
	@Override
	public void handlePeriod() {
		AutopilotMode autopilotMode = autopilotModule.getAutopilotMode();
		VerticalFlightMode vfMode = autopilotModule.getVerticalFlightMode();
		
		if (autopilotMode == AutopilotMode.AUTO2
			|| autopilotMode == AutopilotMode.HOME) {
			
			if (vfMode == VerticalFlightMode.AUTO_CLIMB
				|| vfMode == VerticalFlightMode.AUTO_ALTITUDE
				|| vfMode == VerticalFlightMode.MODE_NB) {
				
				pidController.control(autopilotModule, autopilotModule.getEstimator(), autopilotModule.getNavigator());
			}
			
			if (vfMode == VerticalFlightMode.AUTO_GAZ) {
				autopilotModule.setGaz(autopilotModule.getNavigator().getDesiredGaz());
			}

			// switch off motor if the battery is to low
//			if (low_battery || (!estimator_flight_time && !launch)) {
//		   		 desired_gaz = 0.;
//		   	}  			
		}
	}
		
	public String getTaskName() {
		return NAME;
	}
}

/**
 * 
 */
package papabench.scj.autopilot.tasks;

import static papabench.scj.utils.ParametersFactory.getPeriodicParameters;
import static papabench.scj.utils.ParametersFactory.getPriorityParameters;
import papabench.scj.autopilot.conf.AutopilotMode;
import papabench.scj.autopilot.conf.LateralFlightMode;
import papabench.scj.autopilot.conf.PapaBenchAutopilotConf.NavigationTaskConf;
import papabench.scj.autopilot.modules.AutopilotModule;
import papabench.scj.autopilot.tasks.pids.CoursePIDController;
import papabench.scj.commons.tasks.PapaBenchPeriodicTask;
import papabench.scj.utils.LogUtils;

/**
 * @author Michal Malohlava
 *
 */
//@SCJAllowed
public class NavigationTask extends PapaBenchPeriodicTask implements NavigationTaskConf {
	
	private AutopilotModule autopilotModule;
	
	private CoursePIDController coursePIDController;
	
	public NavigationTask(AutopilotModule autopilotModuleArg) {
		super(getPriorityParameters(PRIORITY), 
				getPeriodicParameters(RELEASE_MS, PERIOD_MS), 
				null, 
				SIZE);
		
		autopilotModule = autopilotModuleArg;
		coursePIDController = new CoursePIDController();
	}

	@Override
	public void handlePeriod() {
		// FIXME 4Hz is a frequency of this task -> update time every 4th call
		autopilotModule.getEstimator().updateFlightTime();
		
		// FIXME following line should be in dedicated task: if (gps_msg_received) => update state
		autopilotModule.getEstimator().updatePosition();
		
		autopilotModule.setLateralFlightMode(LateralFlightMode.COURSE);
		
		if (autopilotModule.getAutopilotMode() == AutopilotMode.HOME) {
			//nav_home()
		} else {
			LogUtils.log(this, "calling navigator to compute the navigation parameters, autopilot status:");
			LogUtils.log(this, autopilotModule.toString());
			
			autopilotModule.getNavigator().autoNavigate();
		}
		
		LogUtils.log(this, "course recomputation");
		courseComputation();
		
		LogUtils.log(this, "Final autopilot status:");
		LogUtils.log(this, autopilotModule.toString());
	}
	
	protected void courseComputation() {
		AutopilotMode autopilotMode = autopilotModule.getAutopilotMode();
		if (autopilotMode == AutopilotMode.AUTO2 || autopilotMode ==AutopilotMode.HOME) {
			
			LateralFlightMode lateralFlightMode = autopilotModule.getLateralFlightMode();
			if (lateralFlightMode == LateralFlightMode.COURSE || lateralFlightMode == LateralFlightMode.NB) {
				coursePIDController.control(autopilotModule, autopilotModule.getEstimator(), autopilotModule.getNavigator());								
			}
			
			autopilotModule.setRoll(autopilotModule.getNavigator().getDesiredRoll());
		}
	}

	public String getTaskName() {
		return NAME;
	}
}

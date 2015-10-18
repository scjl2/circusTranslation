/**
 * 
 */
package papabench.scj.autopilot.tasks;

import static papabench.scj.utils.ParametersFactory.getPeriodicParameters;
import static papabench.scj.utils.ParametersFactory.getPriorityParameters;
import papabench.scj.autopilot.conf.PapaBenchAutopilotConf.ReportingTaskConf;
import papabench.scj.autopilot.modules.AutopilotModule;
import papabench.scj.commons.tasks.PapaBenchPeriodicTask;

/**
 * Reports airplane state to ground central.
 * 
 * Notes:
 *   - called as 'downlink'
 *   
 * @author Michal Malohlava
 *
 */
//@SCJAllowed
public class ReportingTask extends PapaBenchPeriodicTask implements ReportingTaskConf {
	
	private AutopilotModule autopilotModule;
	int counter = 0;
	
	public ReportingTask(AutopilotModule autopilotModule) {
		super(getPriorityParameters(PRIORITY), 
				getPeriodicParameters(RELEASE_MS, PERIOD_MS), 
				null, // FIXME 
				SIZE);
		
		this.autopilotModule = autopilotModule;
	}

	@Override
	public void handlePeriod() {
		// This task has period 100ms => count the number of executions and send 
		// different information according to a counter.
		
		// send attitude (500ms)		
		// send GPS position
		// send IR status
		// send ability to take-off
	}
	
	protected void sendGPS() {	
		
	}
	
	protected void sendIRStatus() {		
	}
	
	protected void sendTakeOff() {		
	}

	public String getTaskName() {
		return NAME;
	}

}

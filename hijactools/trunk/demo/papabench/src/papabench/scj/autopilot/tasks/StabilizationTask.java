/**
 * 
 */
package papabench.scj.autopilot.tasks;

import static papabench.scj.utils.ParametersFactory.getPeriodicParameters;
import static papabench.scj.utils.ParametersFactory.getPriorityParameters;
import papabench.scj.autopilot.conf.PapaBenchAutopilotConf.StabilizationTaskConf;
import papabench.scj.autopilot.modules.AutopilotModule;
import papabench.scj.autopilot.tasks.pids.RollPitchPIDController;
import papabench.scj.commons.conf.RadioConf;
import papabench.scj.commons.data.InterMCUMsg;
import papabench.scj.commons.data.RadioCommands;
import papabench.scj.commons.tasks.PapaBenchPeriodicTask;
import papabench.scj.utils.LogUtils;
import papabench.scj.utils.PPRZUtils;

/**
 * f = TODO
 * 
 * @author Michal Malohlava
 *
 */
//@SCJAllowed
public class StabilizationTask extends PapaBenchPeriodicTask implements
		StabilizationTaskConf {	

	private AutopilotModule autopilotModule;
	
	private RollPitchPIDController pidController;

	public StabilizationTask(AutopilotModule autopilotModule) {
		super(getPriorityParameters(PRIORITY), 
				getPeriodicParameters(RELEASE_MS, PERIOD_MS), 
				null, 
				SIZE);
		
		this.autopilotModule = autopilotModule;
		this.pidController = new RollPitchPIDController();
	}

	@Override
	public void handlePeriod() {

		autopilotModule.getIRDevice().update();
		autopilotModule.getEstimator().updateIRState();
		
		pidController.control(autopilotModule, autopilotModule.getEstimator(), autopilotModule.getNavigator());
		
		InterMCUMsg msg = new InterMCUMsg(true);
		RadioCommands radioCommands = msg.radioCommands;
		
		radioCommands.setPitch(autopilotModule.getElevator());
		radioCommands.setRoll(autopilotModule.getAileron());
		radioCommands.setThrottle(autopilotModule.getGaz());
		radioCommands.setGain1((int) PPRZUtils.trimPPRZ(RadioConf.MAX_PPRZ/0.75f*(autopilotModule.getEstimator().getAttitude().phi)));		
		msg.setValid(true);
		
		LogUtils.log(this, "Sending msg: " + msg);
		
		autopilotModule.getLinkToFBW().sendMessageToFBW(msg);
	}

	public String getTaskName() {
		return NAME;
	}
}

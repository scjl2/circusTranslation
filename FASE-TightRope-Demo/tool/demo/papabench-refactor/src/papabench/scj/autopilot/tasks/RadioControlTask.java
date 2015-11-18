package papabench.scj.autopilot.tasks;

import static papabench.scj.utils.ParametersFactory.getPeriodicParameters;
import static papabench.scj.utils.ParametersFactory.getPriorityParameters;
import papabench.scj.autopilot.conf.AutopilotMode;
import papabench.scj.autopilot.conf.PapaBenchAutopilotConf.RadioControlTaskConf;
import papabench.scj.autopilot.modules.AutopilotModule;
import papabench.scj.autopilot.modules.LinkToFBW;
import papabench.scj.commons.conf.RadioConf;
import papabench.scj.commons.data.InterMCUMsg;
import papabench.scj.commons.tasks.PapaBenchPeriodicTask;
import papabench.scj.utils.PPRZUtils;

/**
 * 
 * @author Michal Malohlava
 *
 */
//@SCJAllowed
public class RadioControlTask extends PapaBenchPeriodicTask implements RadioControlTaskConf {
	
	private AutopilotModule autopilotModule;
	
	public RadioControlTask(AutopilotModule autopilotModuleArg) {
		super(getPriorityParameters(PRIORITY), 
				getPeriodicParameters(RELEASE_MS, PERIOD_MS), 
				null, 
				SIZE);
		
		autopilotModule = autopilotModuleArg;
	}
	
	@Override
	public void handlePeriod() {
		LinkToFBW linkToFBW = autopilotModule.getLinkToFBW();
		
		InterMCUMsg msg = linkToFBW.getMessageFromFBW();
		AutopilotMode autopilotMode = autopilotModule.getAutopilotMode();
		
		// message is totally received
		if (msg.isValid()) {
			boolean modeChanged = false;
			
			if (msg.isRadioReallyLost() 
					&& 
				(autopilotMode == AutopilotMode.AUTO1 || autopilotMode == AutopilotMode.MANUAL)) {
				autopilotModule.setAutopilotMode(AutopilotMode.HOME);
				autopilotMode = AutopilotMode.HOME;
				modeChanged = true;				
			}
			
			if (msg.isAveragedChannelsSent()) {
				// TODO	setup modes, currently we are interested in in automatic mode AUTO2			
			}
			
			if (modeChanged) {
				// TODO sent telemetry data to the ground
			}
					
			if (autopilotMode == AutopilotMode.AUTO1) {
				autopilotModule.setRoll(PPRZUtils.floatOfPPRZ(msg.radioCommands.getRoll(), 0.0f, -0.6f));
				autopilotModule.setPitch(PPRZUtils.floatOfPPRZ(msg.radioCommands.getPitch(), 0.0f, 0.5f));
			} 
			
			if (autopilotMode == AutopilotMode.AUTO1 || autopilotMode == AutopilotMode.MANUAL) {
				autopilotModule.setGaz(msg.radioCommands.getThrottle());
			}
			
			autopilotModule.setMC1PpmCpt(msg.ppmCpt);
			autopilotModule.setVoltSupply(msg.voltSupply);
			
			if (autopilotModule.getEstimator().getFlightTime() == 0) {
				// TODO FIXME here we should put ground_calibration !
				if (autopilotMode == AutopilotMode.AUTO2
						&& msg.radioCommands.getThrottle() > RadioConf.GAZ_THRESHOLD_TAKEOFF) {
					autopilotModule.setLaunched(true);					
				}
			}
		}
	}
		
	public String getTaskName() {
		return NAME;
	}

}

/**
 * 
 */
package papabench.scj.fbw.tasks;

import static papabench.scj.utils.ParametersFactory.getPeriodicParameters;
import static papabench.scj.utils.ParametersFactory.getPriorityParameters;

import javax.safetycritical.StorageParameters;

import papabench.scj.commons.data.InterMCUMsg;
import papabench.scj.commons.tasks.PapaBenchPeriodicTask;
import papabench.scj.fbw.conf.PapaBenchFBWConf.SendDataToAutopilotTaskConf;
import papabench.scj.fbw.modules.FBWModule;

/**
 * T = 25ms
 *  
 * @author Michal Malohlava
 * 
 */
//@SCJAllowed
public class SendDataToAutopilotTask extends PapaBenchPeriodicTask implements SendDataToAutopilotTaskConf {
	private FBWModule fbwModule;
	
	public SendDataToAutopilotTask(FBWModule fbwModule) {
		super(getPriorityParameters(PRIORITY), 
				getPeriodicParameters(RELEASE_MS, PERIOD_MS), 
				null, // CHECKME 
				SIZE);
		
		this.fbwModule = fbwModule;
	}
	
	@Override
	public void handlePeriod() {	
		
		// send only valid 
		if (fbwModule.getPPMDevice().isValid()) {
			InterMCUMsg msg = new InterMCUMsg(); 
			msg.radioCommands = fbwModule.getPPMDevice().getLastRadioCommands().clone();
			
			msg.setRadioOK(fbwModule.isRadioOK());
			msg.setRadioReallyLost(fbwModule.isRadioReallyLost());
			msg.setAveragedChannelsSent(msg.radioCommands.containsAveragedChannels());
			
			msg.voltSupply = 0;
			msg.ppmCpt = 0; // FIXME <---this value should be computed
			
			fbwModule.getLinkToAutopilot().sendMessageToAutopilot(msg);			
		}
	}

	public void cleanUp() {
	}

	public void register() {
	}

	public StorageParameters getThreadConfigurationParameters() {
		return null;
	}

	public String getTaskName() {
		return NAME;
	}
}

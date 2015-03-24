/**
 * 
 */
package papabench.scj.fbw.tasks;

import static papabench.scj.utils.ParametersFactory.getPeriodicParameters;
import static papabench.scj.utils.ParametersFactory.getPriorityParameters;
import papabench.scj.commons.conf.FBWMode;
import papabench.scj.commons.conf.RadioConf;
import papabench.scj.commons.data.RadioCommands;
import papabench.scj.commons.tasks.PapaBenchPeriodicTask;
import papabench.scj.fbw.conf.PapaBenchFBWConf.TestPPMTaskConf;
import papabench.scj.fbw.devices.PPMDevice;
import papabench.scj.fbw.modules.FBWModule;

/**
 * Task receiving 
 * 
 * T = 25ms
 *  
 * @author Michal Malohlava
 * 
 */
//@SCJAllowed
public class TestPPMTask extends PapaBenchPeriodicTask implements TestPPMTaskConf {
	private FBWModule fbwModule;
	
	private int counterFromLastPPM;
	
	public TestPPMTask(FBWModule fbwModuleArg) {
		super(getPriorityParameters(PRIORITY), 
				getPeriodicParameters(RELEASE_MS, PERIOD_MS), 
				null, // CHECKME 
				SIZE);
		
		fbwModule = fbwModuleArg;
	}
	
	@Override
	public void handlePeriod() {
		PPMDevice ppmDevice = fbwModule.getPPMDevice();
		
		if (ppmDevice.isValid()) {
			fbwModule.setRadioOK(true);
			fbwModule.setRadioReallyLost(false);
			counterFromLastPPM++;
			
			ppmDevice.lastRadioFromPpm();
			
			RadioCommands radioCommands = ppmDevice.getLastRadioCommands();
			FBWMode mode = fbwModule.getFBWMode();
			if (radioCommands.containsAveragedChannels()) {
				mode = radioCommands.getMode();
			}
			
			if (mode == FBWMode.MANUAL) {
				fbwModule.getServosController().setServos(radioCommands);
			}
		} else if (fbwModule.getFBWMode() == FBWMode.MANUAL && fbwModule.isRadioReallyLost()) {
			fbwModule.setFBWMode(FBWMode.AUTO);
		}
		
		if (counterFromLastPPM > RadioConf.STALLED_TIME) {
				fbwModule.setRadioOK(false);
		}
		if (counterFromLastPPM > RadioConf.REALLY_STALLED_TIME) {
			fbwModule.setRadioReallyLost(true);
		}
	}

	public String getTaskName() {
		return NAME;
	}
}

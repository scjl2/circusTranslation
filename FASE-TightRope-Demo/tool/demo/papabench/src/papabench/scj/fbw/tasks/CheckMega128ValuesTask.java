package papabench.scj.fbw.tasks;

import static papabench.scj.utils.ParametersFactory.getPeriodicParameters;
import static papabench.scj.utils.ParametersFactory.getPriorityParameters;
import papabench.scj.commons.conf.FBWMode;
import papabench.scj.commons.conf.RadioConf;
import papabench.scj.commons.data.InterMCUMsg;
import papabench.scj.commons.tasks.PapaBenchPeriodicTask;
import papabench.scj.fbw.conf.PapaBenchFBWConf.CheckMega128ValuesTaskConf;
import papabench.scj.fbw.modules.FBWModule;

/**
 * T = 50ms
 * @author Michal Malohlava
 *
 */
public class CheckMega128ValuesTask extends PapaBenchPeriodicTask implements CheckMega128ValuesTaskConf {
	
	private FBWModule fbwModule;
	
	private int counterSinceLastMega128 = 0;
	
	public CheckMega128ValuesTask(FBWModule fbwModule) {
		super(getPriorityParameters(PRIORITY), 
				getPeriodicParameters(RELEASE_MS, PERIOD_MS), 
				null, // CHECKME 
				SIZE);
		
		this.fbwModule = fbwModule;
	}

	@Override
	public void handlePeriod() {
		// there should be condition reflecting SPI state on real hardware
		InterMCUMsg msg = fbwModule.getLinkToAutopilot().getMessageFromAutopilot();
		// message if message is fully received (SPI reception takes a time, however we return message 
		// which is preallocated for the given SPI reception)
		if (msg.isValid()) {
			counterSinceLastMega128 = 0;
			fbwModule.setMega128OK(true);
			if (fbwModule.getFBWMode() == FBWMode.AUTO) {
				this.fbwModule.getServosController().setServos(msg.radioCommands);
			}
		}
		
		if (counterSinceLastMega128 > RadioConf.STALLED_TIME) {
			fbwModule.setMega128OK(false);
		}
	}

	public String getTaskName() {
		return NAME;
	}

}

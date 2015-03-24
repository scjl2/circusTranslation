package papabench.scj.fbw.tasks;

import static papabench.scj.utils.LogUtils.log;
import static papabench.scj.utils.ParametersFactory.getPeriodicParameters;
import static papabench.scj.utils.ParametersFactory.getPriorityParameters;
import papabench.scj.commons.conf.FBWMode;
import papabench.scj.commons.conf.RadioConf;
import papabench.scj.commons.tasks.PapaBenchPeriodicTask;
import papabench.scj.fbw.conf.PapaBenchFBWConf.CheckFailsafeTaskConf;
import papabench.scj.fbw.modules.FBWModule;
/**
 * Switch to fail save state of airplane control.
 * 
 * The switching depends on message lost.
 * 
 * T = 50ms
 * 
 * @author Michal Malohlava
 *
 */
public class CheckFailsafeTask extends PapaBenchPeriodicTask implements CheckFailsafeTaskConf {
	
	private FBWModule fbwModule;
	
	public CheckFailsafeTask(FBWModule fbwModuleArg) {
		super(getPriorityParameters(PRIORITY), 
				getPeriodicParameters(RELEASE_MS, PERIOD_MS), 
				null, // CHECKME 
				SIZE);
		fbwModule = fbwModuleArg;		
	}

	@Override
	public void handlePeriod() {
		if (fbwModule.getFBWMode() == FBWMode.MANUAL && !fbwModule.isRadioOK()
			|| fbwModule.getFBWMode() == FBWMode.AUTO && !fbwModule.isMega128OK()) {

			fbwModule.getServosController().setServos(RadioConf.safestateRadioCommands);
			
			log(this, "Airplane was switched into failsafe mode");
		}
	}

	public String getTaskName() {
		return NAME;
	}

}

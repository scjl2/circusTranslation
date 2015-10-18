package papabench.scj.fbw.devices.impl;

import papabench.scj.commons.data.RadioCommands;
import papabench.scj.commons.data.impl.RadioCommandsImpl;
import papabench.scj.fbw.devices.ServosController;
import papabench.scj.utils.LogUtils;

/**
 * Simple servos controller.
 * 
 * @author Michal Malohlava
 *
 */
public class ServosControllerImpl implements ServosController {
	
	protected RadioCommands radioCommands;

	public void setServos(RadioCommands radioCommands) {
		// this should lead to memory access error - reference from tasks will disappear
		radioCommands.fillFrom(radioCommands);
		LogUtils.log(this, "Servos set to: " + radioCommands);		
	}

	public void init() {
		radioCommands = new RadioCommandsImpl();
	}

	public void reset() {
	}

}

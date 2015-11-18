/**
 * 
 */
package papabench.scj.simulator.devices;

import papabench.scj.commons.data.RadioCommands;
import papabench.scj.fbw.devices.impl.ServosControllerImpl;
import papabench.scj.simulator.SimulatedDevice;
import papabench.scj.simulator.model.FlightModel;
import papabench.scj.utils.LogUtils;

/**
 * @author Michal Malohlava
 *
 */
public class SimulatorServosControllerImpl extends ServosControllerImpl implements SimulatedDevice {
	
	private boolean processed = true;

	public void update(FlightModel flightModel) {
		if (!processed) {
			flightModel.processCommands(radioCommands);			
			processed = true;
		}
	}
	
	@Override
	public void setServos(RadioCommands radioCommands) {		
		super.setServos(radioCommands);
		
		LogUtils.log(this, "Radio commands for servos: " + radioCommands.toString());
		
		processed = false;
	}
}

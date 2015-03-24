/**
 * 
 */
package papabench.scj.fbw.devices.impl;

import papabench.scj.commons.data.RadioCommands;
import papabench.scj.commons.data.impl.RadioCommandsImpl;
import papabench.scj.fbw.devices.PPMDevice;

/**
 * Device for receiving commands from the ground station.
 * 
 * @author Michal Malohlava
 *
 */
public class PPMDeviceImpl implements PPMDevice {
	
	private RadioCommands lastRadioCommands;

	public RadioCommands getLastRadioCommands() {
		return lastRadioCommands;		
	}

	public boolean isLastRadioContainsAvgChannels() {
		return false;
	}

	public void lastRadioFromPpm() {
	}

	public void init() {
		lastRadioCommands = new RadioCommandsImpl();
	}

	public void reset() {
	}

	public boolean isValid() {		
		return false;
	}

	public void setValid(boolean value) {				
	}
}

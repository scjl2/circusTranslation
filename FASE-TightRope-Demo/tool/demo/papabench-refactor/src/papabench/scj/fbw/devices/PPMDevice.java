/**
 * 
 */
package papabench.scj.fbw.devices;

import papabench.scj.commons.conf.AirframeParametersConf;
import papabench.scj.commons.data.RadioCommands;
import papabench.scj.commons.devices.Device;

/**
 * Device for receiving commands from ground controller.
 * 
 * @author Michal Malohlava
 *
 */
public interface PPMDevice extends Device {
	
	/**
	 * Returns array of length equal to {@link AirframeParametersConf#RADIO_CTL_NB}.
	 * 
	 * @return
	 */
	RadioCommands getLastRadioCommands();

	/**
	 * 
	 * @return
	 */
	boolean isValid();
	
	/**
	 * Copy data from ppm buffer to the buffer used by MC0
	 */
	void lastRadioFromPpm();
	
}

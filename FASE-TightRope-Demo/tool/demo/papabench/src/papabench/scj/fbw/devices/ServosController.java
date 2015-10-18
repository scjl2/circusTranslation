/**
 * 
 */
package papabench.scj.fbw.devices;

import papabench.scj.commons.data.RadioCommands;
import papabench.scj.commons.devices.Device;

/**
 * @author Michal Malohlava
 *
 */
public interface ServosController extends Device {
	
	void setServos(RadioCommands radioCommands);

}

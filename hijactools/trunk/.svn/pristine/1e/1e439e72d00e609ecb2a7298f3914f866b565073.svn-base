/**
 * 
 */
package papabench.scj.bus;

import papabench.scj.commons.data.InterMCUMsg;
import papabench.scj.commons.devices.Bus;

/**
 * @author Michal Malohlava
 * 
 * FIXME this class is kind of simplification of communication between FBW and Autopilot modules.
 * 
 */
public interface SPIBus extends Bus {
	
	void sendMessage(InterMCUMsg msg);
	
	boolean getMessage(InterMCUMsg msg);	
}

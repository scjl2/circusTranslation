/**
 * 
 */
package papabench.scj.autopilot.modules;

import papabench.scj.bus.SPIBus;
import papabench.scj.commons.data.InterMCUMsg;
import papabench.scj.commons.modules.Module;

/**
 * 
 *  
 * @author Michal Malohlava
 *
 */
public interface LinkToFBW extends Module {
	
	InterMCUMsg getMessageFromFBW();
	
	void sendMessageToFBW(InterMCUMsg msg);
	
	void setSPIBus(SPIBus spiBus);
}

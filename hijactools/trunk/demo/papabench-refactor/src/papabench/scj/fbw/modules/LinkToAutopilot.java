/**
 * 
 */
package papabench.scj.fbw.modules;

import papabench.scj.bus.SPIBus;
import papabench.scj.commons.data.InterMCUMsg;
import papabench.scj.commons.modules.Module;

/**
 * Module for sending and receiving commands from autopilot.
 * 
 * @author Michal Malohlava
 * 
 * NOTE:
 *  - in original implementation it is called Mega128
 */
public interface LinkToAutopilot extends  Module {
	
	InterMCUMsg getMessageFromAutopilot();
	
	void sendMessageToAutopilot(InterMCUMsg msg);
	
	// --------------|
	// configuration |
	// --------------|
	
	//@Required
	void setSPIBus(SPIBus spiBus);
}

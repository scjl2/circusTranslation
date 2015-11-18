/**
 * 
 */
package papabench.scj.fbw.modules.impl;

import papabench.scj.bus.SPIBus;
import papabench.scj.commons.data.InterMCUMsg;
import papabench.scj.fbw.modules.LinkToAutopilot;

/**
 * @author Michal Malohlava
 *
 */
public class LinkToAutopilotImpl implements LinkToAutopilot {
	
	private SPIBus spiBus;

	/**
	 * Returns message from autopilot. 
	 * 
	 * Message is allocate in this method call!
	 */
	public InterMCUMsg getMessageFromAutopilot() {
		// allocate new message
		InterMCUMsg msg = new InterMCUMsg(true); 

		// let the bus fill the message
		spiBus.getMessage(msg);		
		
		return msg; 
	}

	public void sendMessageToAutopilot(InterMCUMsg msg) {
		spiBus.sendMessage(msg);
	}

	public void setSPIBus(SPIBus spiBus) {
		this.spiBus = spiBus;		
	}

	public void init() {
		if (spiBus == null) {
			throw new IllegalArgumentException("Module LinkToAutopilot is not configured.");
		}
	}

}

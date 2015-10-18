/**
 * 
 */
package papabench.scj.autopilot.modules.impl;

import papabench.scj.autopilot.modules.LinkToFBW;
import papabench.scj.bus.SPIBus;
import papabench.scj.commons.data.InterMCUMsg;
import papabench.scj.utils.LogUtils;

/**
 * In this test scenario FBWLinkDevice directly refers FBW module.
 * 
 * Notes:
 *  - direct link is only for testing purposes - it should be replaced by code handling SPI bus
 *   
 * @author Michal Malohlava
 *
 */
//@SCJAllowed
public class LinkToFBWImpl implements LinkToFBW {
	
	private SPIBus spiBus;
	
	public void init() {
		if (spiBus == null) {
			throw new IllegalArgumentException("FBWLink module is not configured correctly!");
		}
	}

	public void setSPIBus(SPIBus spiBusArg) {
		spiBus = spiBusArg;		
	}

	public InterMCUMsg getMessageFromFBW() {
		InterMCUMsg msg = new InterMCUMsg();
		
		// async receive -if the message is not fully received the message is marked as not-valid
		// The caller is in this case task, so the message will be allocated in the scope of caller task. 
		spiBus.getMessage(msg);
		
		return msg;
	}

	public void sendMessageToFBW(InterMCUMsg msg) {
		LogUtils.log(this, "Sending msg: " + msg);
		spiBus.sendMessage(msg);
	}
}

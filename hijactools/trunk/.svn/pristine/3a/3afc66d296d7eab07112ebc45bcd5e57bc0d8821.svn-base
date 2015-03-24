/**
 * 
 */
package papabench.scj.commons.data;

import papabench.scj.commons.conf.AirframeParametersConf;
import papabench.scj.commons.data.impl.RadioCommandsImpl;

/**
 * Message for communication between FBW and Autopilot module.
 * 
 * @author Michal Malohlava
 * 
 * TODO: define interface for manipulation with data
 */
//@SCJAllowed
public class InterMCUMsg {
	protected static final byte STATUS_RADIO_OK = 0;
	protected static final byte RADIO_REALLY_LOST = 1;
	protected static final byte AVERAGED_CHANNELS_SENT = 2;
	
	// updated by SPI interrupt
	volatile boolean isValid = false;
	
	public InterMCUMsg() {
		this(false);
	}
	
	public InterMCUMsg(boolean allocateRadioCommands) {
		if (allocateRadioCommands) {
			radioCommands = new RadioCommandsImpl();
		}
	}
	
	/**
	 * @see AirframeParametersConf#RADIO_CTL_NB
	 */
	public RadioCommands radioCommands;
	
	public byte ppmCpt;
	public byte status;
	public byte numberOfErrors;
	public byte voltSupply;
	
	/**
	 * Returns true if the message is completely received.
	 *  
	 * @return true/false depending of message state
	 */
	public boolean isValid() {
		return isValid;
	}
	
	public void setValid(boolean valid) {
		isValid = valid;
	}
	
	public boolean isRadioOK() {
		return (status & (1 << STATUS_RADIO_OK)) == 1;		
	}
	
	public void setRadioOK(boolean value) {
		status |= value ? 1 << STATUS_RADIO_OK : 0;
	}
	
	public boolean isRadioReallyLost() {		
		return (status & (1 << RADIO_REALLY_LOST)) == 1;
	}
	
	public void setRadioReallyLost(boolean value) {
		status |= value ? 1 << RADIO_REALLY_LOST : 0;		
	}
	
	public boolean isAveragedChannelsSent() {		 
		return (status & (1 << AVERAGED_CHANNELS_SENT)) == 1;
	}
	
	public void setAveragedChannelsSent(boolean value) {
		status |= value ? 1 << AVERAGED_CHANNELS_SENT : 0;
	}
	
	public void fillFrom(InterMCUMsg msg) {
		ppmCpt = msg.ppmCpt;
		status = msg.status;
		numberOfErrors = msg.numberOfErrors;
		voltSupply = msg.voltSupply;
		isValid = msg.isValid;
		
		radioCommands.fillFrom(msg.radioCommands);
	}

	@Override
	public String toString() {
		return "InterMCUMsg [isValid=" + isValid + ", numberOfErrors="
				+ numberOfErrors + ", ppmCpt=" + ppmCpt + ", radioCommands="
				+ radioCommands + ", status=" + status + ", voltSupply="
				+ voltSupply + "]";
	}
	
	
}

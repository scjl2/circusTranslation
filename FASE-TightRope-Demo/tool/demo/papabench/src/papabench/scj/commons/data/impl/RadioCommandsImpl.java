package papabench.scj.commons.data.impl;

import java.util.Arrays;

import papabench.scj.commons.conf.FBWMode;
import papabench.scj.commons.conf.RadioConf;
import papabench.scj.commons.data.RadioCommands;

/**
 * Radio commands
 * @author Michal Malohlava
 *
 */
//@SCJAllowed
public class RadioCommandsImpl implements RadioCommands {
	
	private boolean containsAveragedChannels = false;
	
	private int[] channels = new int[RadioConf.RADIO_CTL_NB];
	
	public RadioCommandsImpl() {	
	}
	
	/**
	 * Copy ctor.
	 */
	protected RadioCommandsImpl(RadioCommandsImpl radioCommands) {
		for (int i = 0 ; i < channels.length; i++) {
			this.channels[i] = radioCommands.channels[i];			
		}
		this.containsAveragedChannels = radioCommands.containsAveragedChannels;
	}
	
	public boolean containsAveragedChannels() {		
		return containsAveragedChannels;
	}

	public int getCalib() {		
		return channels[RADIO_CALIB];
	}

	public int[] getChannels() {
		return channels;
	}

	public int getGain1() {
		return channels[RADIO_GAIN1];
	}

	public int getGain2() {
		return channels[RADIO_GAIN2];
	}

	public int getLLS() {
		return channels[RADIO_LLS];
	}

	public FBWMode getMode() {
		return FBWMode.valueOf(channels[RADIO_MODE]);
	}

	public int getPitch() {
		return channels[RADIO_PITCH];
	}

	public int getRoll() {
		return channels[RADIO_ROLL];
	}

	public int getThrottle() {
		return channels[RADIO_THROTTLE];
	}

	public int getYaw() {
		return channels[RADIO_YAW];
	}

	public void setCalib(int value) {
		channels[RADIO_CALIB] = value;		
	}

	public void setGain1(int value) {
		channels[RADIO_GAIN1] = value;
	}

	public void setGain2(int value) {
		channels[RADIO_GAIN2] = value;
	}

	public void setLLS(int value) {
		channels[RADIO_LLS] = value;	
	}

	public void setMode(FBWMode mode) {
		channels[RADIO_MODE] = mode.getValue();		
	}

	public void setPitch(int value) {
		channels[RADIO_PITCH] = value;
	}

	public void setRoll(int value) {
		channels[RADIO_ROLL] = value;
	}

	public void setThrottle(int value) {
		channels[RADIO_THROTTLE] = value;
	}

	public void setYaw(int value) {
		channels[RADIO_YAW] = value;
	}
	
	public RadioCommands clone() {		
		return new RadioCommandsImpl(this);
	}
	
	public void fillFrom(RadioCommands radioCommands) {
//		assert(this.channels.length == radioCommands.getChannels().length);
		
		int[] channels = radioCommands.getChannels();
		for (int i = 0; i < channels.length; i++) {
			this.channels[i] = channels[i];									
		}
		
		this.containsAveragedChannels = radioCommands.containsAveragedChannels(); 
	}
	
	@Override
	public String toString() {		
		return "RadioCommands: " + Arrays.toString(this.channels);
	}
}

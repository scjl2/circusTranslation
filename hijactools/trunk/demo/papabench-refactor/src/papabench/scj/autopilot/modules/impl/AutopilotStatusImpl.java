/**
 * 
 */
package papabench.scj.autopilot.modules.impl;

import javax.safetycritical.Mission;

import papabench.scj.autopilot.conf.AutopilotMode;
import papabench.scj.autopilot.conf.LateralFlightMode;
import papabench.scj.autopilot.conf.VerticalFlightMode;
import papabench.scj.autopilot.modules.AutopilotStatus;
import papabench.scj.commons.conf.AirframeParametersConf;

/**
 * @author Michal Malohlava
 *
 */
//@SCJAllowed
public class AutopilotStatusImpl implements AutopilotStatus {
	
	/** autopilot mode */
	private AutopilotMode autopilotMode;
	/** */
	private LateralFlightMode lateralFlightMode;
	/** */
	private VerticalFlightMode verticalFlightMode;
	
	private Mission mission;
	
	private float roll;
	private float pitch;
	private float climb;
	private float rollPGain;
	private float pitchPGain;
	private float pitchOfRoll;
	
	
	private int gaz;
	private int aileron;
	private int elevator;
	
	
	private int voltSupply;
	private int MC1PpmCpt;
	
	private boolean launched = false;
	
	public void init() {
		autopilotMode = AutopilotMode.AUTO2;
		lateralFlightMode = LateralFlightMode.MANUAL;
		verticalFlightMode = VerticalFlightMode.MANUAL;
		rollPGain = AirframeParametersConf.ROLL_PGAIN;
		pitchPGain = AirframeParametersConf.PITCH_PGAIN;
	}

	public AutopilotMode getAutopilotMode() {
		return autopilotMode;
	}
	public void setAutopilotMode(AutopilotMode mode) {
		autopilotMode = mode;		
	}

	public LateralFlightMode getLateralFlightMode() {
		return lateralFlightMode;		
	}
	public void setLateralFlightMode(LateralFlightMode mode) {
		lateralFlightMode = mode;		
	}

	public VerticalFlightMode getVerticalFlightMode() {
		return verticalFlightMode;
	}
	public void setVerticalFlightMode(VerticalFlightMode mode) {
		verticalFlightMode = mode;
	}
	public float getRoll() {
		return roll;
	}
	public void setRoll(float rollArg) {
		roll = rollArg;
	}
	public float getPitch() {
		return pitch;
	}
	public void setPitch(float pitch) {
		pitch = pitch;
	}
	public float getClimb() {
		return climb;
	}
	public void setClimb(float climbArg) {
		climb = climbArg;
	}
	public float getRollPGain() {
		return rollPGain;
	}
	public void setRollPGain(float rollPGainArg) {
		rollPGain = rollPGainArg;
	}
	public float getPitchPGain() {
		return pitchPGain;
	}
	public void setPitchPGain(float pitchPGainArg) {
		pitchPGain = pitchPGainArg;
	}
	public float getPitchOfRoll() {
		return pitchOfRoll;
	}
	public void setPitchOfRoll(float pitchOfRollArg) {
		pitchOfRoll = pitchOfRollArg;
	}	
	public int getVoltSupply() {
		return voltSupply;
	}
	public void setVoltSupply(int voltSupplyArg) {
		voltSupply = voltSupplyArg;
	}
	public int getMC1PpmCpt() {
		return MC1PpmCpt;
	}
	public void setMC1PpmCpt(int mC1PpmCpt) {
		MC1PpmCpt = mC1PpmCpt;
	}
	public boolean isLaunched() {
		return launched;
	}
	
	public void setLaunched(boolean launch) {
		launched = launch;
	}
	/* Airframe parameters */
	public int getAileron() {
		return aileron;
	}
	public void setAileron(int aileronArg) {
		aileron = aileronArg;		
	}
	public void setElevator(int elevatorArg) {
		elevator = elevatorArg;		
	}
	public int getElevator() {
		return elevator;
	}
	public int getGaz() {
		return gaz;
	}
	public void setGaz(int gazArg) {
		gaz = gazArg;
	}

	@Override
	public String toString() {
		return "AutopilotStatusImpl [aileron=" + aileron + ", autopilotMode="
				+ autopilotMode + ", climb=" + climb + ", elevator=" + elevator
				+ ", gaz=" + gaz + ", lateralFlightMode=" + lateralFlightMode
				+ ", launched=" + launched + ", pitch=" + pitch + ", roll="
				+ roll + ", verticalFlightMode=" + verticalFlightMode + "]";
	}

	public void missionFinished() {
		Mission.getCurrentMission().requestSequenceTermination();		
	}
}

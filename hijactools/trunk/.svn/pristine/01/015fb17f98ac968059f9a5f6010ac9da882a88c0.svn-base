package papabench.scj.simulator.devices;

import papabench.scj.autopilot.data.Attitude;
import papabench.scj.autopilot.devices.impl.IRDeviceImpl;
import papabench.scj.simulator.SimulatedDevice;
import papabench.scj.simulator.model.FlightModel;

public class SimulatorIRDeviceImpl extends IRDeviceImpl implements SimulatedDevice {
	
	private int irLeft; // roll
	private int irFront; // pitch
	private int irTop; // top

	public void update(FlightModel flightModel) {
		Attitude attitude = flightModel.getState().getAttitude();
		
		float phiSensor = attitude.phi + getIrRollNeutral();
		float thetaSensor = attitude.theta + getIrPitchNeutral();
		
		irLeft = (int) (Math.sin(phiSensor) * getIrContrast());
		irFront =  (int) (Math.sin(thetaSensor) * getIrContrast());
		irTop = (int) (Math.cos(phiSensor) * Math.cos(thetaSensor) * getIrContrast());
	}
	
	@Override
	public void update() {		
		// do nothing
	}
	
	
	@Override
	public int getIrRoll() {	
		return irLeft;
	}
	
	@Override
	public int getIrPitch() {	
		return irFront;
	}
	
	@Override
	public int getIrTop() {
		return irTop;
	}	
}

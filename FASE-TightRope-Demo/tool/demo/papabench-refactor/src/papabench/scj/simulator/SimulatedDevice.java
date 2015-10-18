package papabench.scj.simulator;

import papabench.scj.simulator.model.FlightModel;

/**
 * Interface marking simulated devices.
 * 
 * @author Michal Malohlava
 *
 */
public interface SimulatedDevice {
	
	/**
	 * Update simulated device according to a given flight model.
	 * 
	 * @param flightModel actual state of the plane and environment 
	 */
	void update(FlightModel flightModel);
}

/**
 * 
 */
package papabench.scj.simulator.tasks;

import static papabench.scj.utils.ParametersFactory.getPeriodicParameters;
import static papabench.scj.utils.ParametersFactory.getPriorityParameters;
import papabench.scj.autopilot.data.Position3D;
import papabench.scj.autopilot.modules.AutopilotModule;
import papabench.scj.commons.tasks.PapaBenchPeriodicTask;
import papabench.scj.simulator.SimulatedDevice;
import papabench.scj.simulator.conf.PapaBenchSimulatorConf.SimulatorGPSTaskConf;
import papabench.scj.simulator.model.FlightModel;
import papabench.scj.utils.LogUtils;

/**
 * @author Michal Malohlava
 *
 */
public class SimulatorGPSTask extends PapaBenchPeriodicTask implements SimulatorGPSTaskConf {
	private FlightModel flightModel;
	private SimulatedDevice gpsDevice;
	
	public SimulatorGPSTask(FlightModel flightModelArg, AutopilotModule autopilotModule) {
		super(getPriorityParameters(PRIORITY), 
				getPeriodicParameters(RELEASE_MS, PERIOD_MS), 
				null, // CHECKME 
				SIZE);
		flightModel = flightModelArg;
		gpsDevice = (SimulatedDevice) autopilotModule.getGPSDevice();
	}
	
	@Override
	public void handlePeriod() {
		Position3D pos = flightModel.getState().getPosition();
		
		LogUtils.log(this, "Position: " + pos.x + "," + pos.y + "," + pos.z + "               " + flightModel.getState().getAirSpeed());		
		
		gpsDevice.update(flightModel);
	}

	public String getTaskName() {
		return NAME;
	}

}

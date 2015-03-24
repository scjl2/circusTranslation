/**
 * 
 */
package papabench.scj.simulator.tasks;

import static papabench.scj.utils.ParametersFactory.getPeriodicParameters;
import static papabench.scj.utils.ParametersFactory.getPriorityParameters;
import papabench.scj.autopilot.modules.AutopilotModule;
import papabench.scj.commons.tasks.PapaBenchPeriodicTask;
import papabench.scj.fbw.modules.FBWModule;
import papabench.scj.simulator.SimulatedDevice;
import papabench.scj.simulator.conf.PapaBenchSimulatorConf.SimulatorFlightModelTaskConf;
import papabench.scj.simulator.model.FlightModel;
import papabench.scj.utils.LogUtils;

/**
 * @author Michal Malohlava
 *
 */
public class SimulatorFlightModelTask extends PapaBenchPeriodicTask implements SimulatorFlightModelTaskConf {
	
	private FlightModel flightModel;
	private AutopilotModule autopilotModule;
	private FBWModule fbwModule;
	
	public SimulatorFlightModelTask(FlightModel flightModel, AutopilotModule autopilotModule, FBWModule fbwModule) {
		super(getPriorityParameters(PRIORITY), 
				getPeriodicParameters(RELEASE_MS, PERIOD_MS), 
				null, // CHECKME 
				SIZE);
		this.flightModel = flightModel;
		this.autopilotModule = autopilotModule;
		this.fbwModule = fbwModule;
	}
	
	@Override
	public void handlePeriod() {
		SimulatedDevice sensors = (SimulatedDevice) fbwModule.getServosController();
		sensors.update(flightModel);
		
		flightModel.updateState();
		
		LogUtils.log(this, "SIMULATOR - Flight model state:");
		LogUtils.log(this, flightModel.getState().toString());
	}

	public String getTaskName() {
		return NAME;
	}
}

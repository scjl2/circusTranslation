/**
 * 
 */
package papabench.scj;

import papabench.scj.autopilot.modules.AutopilotModule;
import papabench.scj.bus.SPIBusChannel;
import papabench.scj.commons.data.FlightPlan;
import papabench.scj.fbw.modules.FBWModule;
import papabench.scj.simulator.model.FlightModel;
import papabench.scj.utils.LogUtils;

/**
 * Representation of a whole PapaBench system. 
 * 
 * Creates and initialize autopilot and fly-by-wire modules, registers their tasks and interrupts.
 * Also, create an environment simulator and registers its tasks.
 * 
 * @see AutopilotModule
 * @see FBWModule
 * @see FlightModel
 * 
 * @author Michal Malohlava
 *
 */
public class PapaBenchSCJImpl implements PapaBench {
	
	private AutopilotModule autopilotModule;
	
	private FBWModule fbwModule;	
	
	private FlightPlan flightPlan;
	
	public void init() {
		// Allocate and initialize global objects: 
		//  - MC0 - autopilot
		autopilotModule = PapaBenchFactory.createAutopilotModule();
				
		//  - MC1 - FBW
		fbwModule = PapaBenchFactory.createFBWModule();		
		
		// Create SPIBusChannel and connect both modules
		SPIBusChannel spiBusChannel = PapaBenchFactory.createSPIBusChannel();
		spiBusChannel.init();
		autopilotModule.setSPIBus(spiBusChannel.getMasterEnd()); // = MC0: SPI master mode
		fbwModule.setSPIBus(spiBusChannel.getSlaveEnd()); // = MC1: SPI slave mode
		
		// setup flight plan
		LogUtils.log(this, "Flight plan: " + flightPlan.getName());
		autopilotModule.getNavigator().setFlightPlan(this.flightPlan);
		
		// initialize both modules - if the modules are badly initialized the runtime exception is thrown
		autopilotModule.init();
		fbwModule.init();
		
		// Register interrupt handlers
		PapaBenchFactory.registerAutopilotInterruptHandlers(autopilotModule);
		PapaBenchFactory.registerFBWInterruptHandlers(fbwModule);
		
		// Register period threads
		PapaBenchFactory.registerAutopilotPeriodicTasks(autopilotModule);
		PapaBenchFactory.registerFBWPeriodicTasks(fbwModule);
		
		// Create simulator
		FlightModel flightModel = PapaBenchFactory.createSimulator();
		flightModel.init();
		// Register simulator tasks
		PapaBenchFactory.registerSimulatorPeriodicTasks(flightModel, autopilotModule, fbwModule);				
	}

	public AutopilotModule getAutopilotModule() {
		return this.autopilotModule;
	}

	public FBWModule getFBWModule() {
		return this.fbwModule;
	}

	public void setFlightPlan(FlightPlan flightPlan) {
		this.flightPlan = flightPlan;
	}

}

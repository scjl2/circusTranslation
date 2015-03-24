/**
 * 
 */
package papabench.scj;

import papabench.scj.autopilot.modules.AutopilotModule;
import papabench.scj.autopilot.modules.impl.AutopilotModuleImpl;
import papabench.scj.autopilot.modules.impl.EstimatorModuleImpl;
import papabench.scj.autopilot.modules.impl.LinkToFBWImpl;
import papabench.scj.autopilot.modules.impl.NavigatorImpl;
import papabench.scj.autopilot.tasks.AltitudeControlTask;
import papabench.scj.autopilot.tasks.ClimbControlTask;
import papabench.scj.autopilot.tasks.LinkFBWSendTask;
import papabench.scj.autopilot.tasks.NavigationTask;
import papabench.scj.autopilot.tasks.RadioControlTask;
import papabench.scj.autopilot.tasks.ReportingTask;
import papabench.scj.autopilot.tasks.StabilizationTask;
import papabench.scj.bus.SPIBusChannel;
import papabench.scj.bus.impl.SPIBusChannelImpl;
import papabench.scj.fbw.devices.impl.PPMDeviceImpl;
import papabench.scj.fbw.modules.FBWModule;
import papabench.scj.fbw.modules.impl.FBWModuleImpl;
import papabench.scj.fbw.modules.impl.LinkToAutopilotImpl;
import papabench.scj.fbw.tasks.CheckFailsafeTask;
import papabench.scj.fbw.tasks.CheckMega128ValuesTask;
import papabench.scj.fbw.tasks.SendDataToAutopilotTask;
import papabench.scj.fbw.tasks.TestPPMTask;
import papabench.scj.simulator.devices.SimulatorGPSDeviceImpl;
import papabench.scj.simulator.devices.SimulatorIRDeviceImpl;
import papabench.scj.simulator.devices.SimulatorServosControllerImpl;
import papabench.scj.simulator.model.FlightModel;
import papabench.scj.simulator.model.impl.FlightModelImpl;
import papabench.scj.simulator.tasks.SimulatorFlightModelTask;
import papabench.scj.simulator.tasks.SimulatorGPSTask;
import papabench.scj.simulator.tasks.SimulatorIRTask;

/**
 * Factory creating an instance of PapaBench.
 * 
 * The factory does not initialize created modules !
 * 
 * @author Michal Malohlava
 *
 */
public class PapaBenchFactory {
	
	public static AutopilotModule createAutopilotModule() {
		
		AutopilotModule autopilotModule = new AutopilotModuleImpl();
		
		autopilotModule.setLinkToFBW(new LinkToFBWImpl());
		autopilotModule.setGPSDevice(new SimulatorGPSDeviceImpl());
		autopilotModule.setIRDevice(new SimulatorIRDeviceImpl());
		autopilotModule.setNavigator(new NavigatorImpl());
		autopilotModule.setEstimator(new EstimatorModuleImpl());
		
		return autopilotModule;
	}
	
	public static FBWModule createFBWModule() {
		FBWModule fbwModule = new FBWModuleImpl();
		
		fbwModule.setLinkToAutopilot(new LinkToAutopilotImpl());
		fbwModule.setPPMDevice(new PPMDeviceImpl());
		fbwModule.setServosController(new SimulatorServosControllerImpl());
		
		return fbwModule;		
	}
	
	public static SPIBusChannel createSPIBusChannel() {
		return new SPIBusChannelImpl();
	}
	
	public static FlightModel createSimulator() {
		return new FlightModelImpl();
	}
	
	public static void registerAutopilotPeriodicTasks(AutopilotModule autopilotModule) {
		// autopilot tasks
		new AltitudeControlTask(autopilotModule);
		new ClimbControlTask(autopilotModule);
		new NavigationTask(autopilotModule);
		new StabilizationTask(autopilotModule);
		new RadioControlTask(autopilotModule);
		new LinkFBWSendTask(autopilotModule);
		new ReportingTask(autopilotModule);		
	}
	
	public static void registerFBWPeriodicTasks(FBWModule fbwModule) {
		new CheckFailsafeTask(fbwModule);
		new CheckMega128ValuesTask(fbwModule);
		new SendDataToAutopilotTask(fbwModule);
		new TestPPMTask(fbwModule);
	}
	
	public static void registerSimulatorPeriodicTasks(FlightModel flightModel, AutopilotModule autopilotModule, FBWModule fbwModule) {
		new SimulatorFlightModelTask(flightModel, autopilotModule, fbwModule);
		new SimulatorGPSTask(flightModel, autopilotModule);
		new SimulatorIRTask(flightModel, autopilotModule);		
	}
	
	public static void registerAutopilotInterruptHandlers(AutopilotModule autopilotModule) {		
	}
	
	public static void registerFBWInterruptHandlers(FBWModule fbwModule) {		
	}
}

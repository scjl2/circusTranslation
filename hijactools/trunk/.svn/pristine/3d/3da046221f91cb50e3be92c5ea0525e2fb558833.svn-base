/**
 * 
 */
package papabench.scj.fbw.modules.impl;

import papabench.scj.bus.SPIBus;
import papabench.scj.commons.conf.FBWMode;
import papabench.scj.fbw.devices.PPMDevice;
import papabench.scj.fbw.devices.ServosController;
import papabench.scj.fbw.modules.FBWModule;
import papabench.scj.fbw.modules.LinkToAutopilot;

/**
 * @author Michal Malohlava
 *
 */
//@SCJAllowed
public class FBWModuleImpl extends FBWStatusImpl implements FBWModule {
	
	private LinkToAutopilot linkToAutopilot;
	
	private PPMDevice ppmDevice;
	private ServosController servosController;
	private SPIBus spiBus;

	public void init() {
		if (ppmDevice == null || servosController == null || linkToAutopilot == null || spiBus == null) {
			throw new IllegalArgumentException("Module FBWModule has wrong configuration");
		}
		
		// FIXME this is hard-written switch to AUTO mode
		setFBWMode(FBWMode.AUTO);
		linkToAutopilot.setSPIBus(spiBus);
		linkToAutopilot.init();
		
		ppmDevice.init();
		servosController.init();				
	}

	public LinkToAutopilot getLinkToAutopilot() {
		return linkToAutopilot;
	}

	public PPMDevice getPPMDevice() {
		return ppmDevice;
	}

	public SPIBus getSPIBus() {
		return spiBus;
	}

	public ServosController getServosController() {
		return servosController;
	}

	public void setPPMDevice(PPMDevice ppmDeviceArg) {
		ppmDevice = ppmDeviceArg;
	}

	public void setSPIBus(SPIBus spiBusArg) {
		spiBus = spiBusArg;
	}

	public void setServosController(ServosController servosControllerArg) {
		servosController = servosControllerArg;
	}

	public void setLinkToAutopilot(LinkToAutopilot linkToAutopilotArg) {
		linkToAutopilot = linkToAutopilotArg;		
	}
}

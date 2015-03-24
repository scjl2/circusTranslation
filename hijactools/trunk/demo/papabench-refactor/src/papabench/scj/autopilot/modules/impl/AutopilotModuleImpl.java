/**
 * 
 */
package papabench.scj.autopilot.modules.impl;

import papabench.scj.autopilot.devices.GPSDevice;
import papabench.scj.autopilot.devices.IRDevice;
import papabench.scj.autopilot.modules.AutopilotModule;
import papabench.scj.autopilot.modules.Estimator;
import papabench.scj.autopilot.modules.LinkToFBW;
import papabench.scj.autopilot.modules.Navigator;
import papabench.scj.bus.SPIBus;

/**
 * @author Michal Malohlava
 *
 */
//@SCJAllowed(members=true)
//@SCJAllowed
public class AutopilotModuleImpl extends AutopilotStatusImpl implements AutopilotModule {
	
	/* Module dependencies: */

	/* GPS device obtaining current position */
	private GPSDevice gpsDevice;
	/* IR device returning roll of airplane */
	private IRDevice irDevice;
	/* Communication SPI bus */
	private SPIBus spiBus;
	
	/* Estimator is responsible for holding/computing current position, altitude, speed */
	private Estimator estimator;
	
	/* Navigator module responsible for autopilot navigation according to the flight plan */
	private Navigator navigator;
	
	/* Communication link to FBW */
	private LinkToFBW linkToFBW;
	
	/* ------------------
	 * module iface impl. 
	 * ------------------ */
	public void init() {
		super.init();
				
		if (gpsDevice == null || irDevice == null || spiBus == null || estimator == null || navigator == null || linkToFBW == null) {
			throw new IllegalArgumentException("Autopilot module has wrong configuration");
		}
		
		gpsDevice.init();
		irDevice.init();
		
		estimator.setGPSDevice(gpsDevice);
		estimator.setIRDevice(irDevice);
		estimator.init();
		
		navigator.setAutopilotModule(this);
		navigator.init();
		
		linkToFBW.setSPIBus(spiBus);
		linkToFBW.init();
	}

	/* ---------------------------
	 * autopilot device iface impl. 
	 * --------------------------- */
	
	public void setGPSDevice(GPSDevice gpsDeviceArg) {
		gpsDevice = gpsDeviceArg;
	}
	
	public GPSDevice getGPSDevice() {		
		return gpsDevice;
	}	
	
	public void setIRDevice(IRDevice irDeviceArg) {
		irDevice = irDeviceArg;
	}
	
	public IRDevice getIRDevice() {
		return irDevice;		
	}

	public SPIBus getSpiBus() {		
		return spiBus;
	}

	public void setSPIBus(SPIBus spiBusArg) {
		spiBus = spiBusArg;
	}


	/* ---------------------------
	 * autopilot module iface impl.
	 * --------------------------- */
	
	public Estimator getEstimator() {
		return estimator;
	}

	public void setEstimator(Estimator estimatorArg) {
		estimator = estimatorArg;
	}
	
	public Navigator getNavigator() {		
		return navigator;
	}
	
	public void setNavigator(Navigator navigatorArg) {
		navigator = navigatorArg;
	}

	public LinkToFBW getLinkToFBW() {
		return linkToFBW;
	}

	public void setLinkToFBW(LinkToFBW fbwLink) {
		linkToFBW = fbwLink;
	}

	@Override
	public String toString() {
		/*
		return "AutopilotModuleImpl [estimator=" + estimator + ", gpsDevice="
				+ gpsDevice + ", irDevice=" + irDevice + ", linkToFBW="
				+ linkToFBW + ", navigator=" + navigator + ", spiBus=" + spiBus
				+ "]"; */		
		return super.toString();
	}
	
}

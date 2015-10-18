package papabench.scj.fbw.modules;

import papabench.scj.bus.SPIBus;
import papabench.scj.fbw.devices.PPMDevice;
import papabench.scj.fbw.devices.ServosController;

/**
 * 
 * @author Michal Malohlava
 *
 */
public interface FBWDevices {
	
	PPMDevice getPPMDevice();
	void setPPMDevice(PPMDevice ppmDevice);
	
	ServosController getServosController();
	void setServosController(ServosController servosController);
	
	SPIBus getSPIBus();
	void setSPIBus(SPIBus spiBus);
}

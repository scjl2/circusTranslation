/**
 * 
 */
package papabench.scj.autopilot.modules.impl;

import papabench.scj.autopilot.data.Attitude;
import papabench.scj.autopilot.data.HorizSpeed;
import papabench.scj.autopilot.data.Position3D;
import papabench.scj.autopilot.devices.GPSDevice;
import papabench.scj.autopilot.devices.IRDevice;
import papabench.scj.autopilot.modules.Estimator;
import papabench.scj.commons.conf.AirframeParametersConf;
import papabench.scj.commons.modules.Module;
import papabench.scj.utils.LogUtils;
import papabench.scj.utils.MathUtils;

/**
 * 
 * Notes:
 *  - internal data are allocated in the same memory as this module.
 *  
 * @author Michal Malohlava
 *
 */
//@SCJAllowed
public class EstimatorModuleImpl implements Estimator, Module {
	
	protected static final float RHO = 0.999f; /* The higher, the slower the estimation is changing */
	protected static final float INIT_WEIGHT = 100.0f; /* The number of times the initial value has to be taken */
	
	private GPSDevice gpsDevice;
	private IRDevice irDevice;
	
	/* position in meters */
	private Position3D position; 
	
	/* 
	 * airplane attitude in radians
	 * x -> + = right	(PHI) 
     * y -> CW, 0 = N	(PSI)
     * z -> + = up		(THETA)
     * theta = The pitch in degrees relative to a plane(mathematical) normal to the earth radius line at the point the plane(aircraft) is at
	 * phi = The roll of the aircraft in degrees
	 * psi = The true heading of the aircraft in degrees
	 */
	private Attitude attitude; 	
	
	/* speed in meters per second */
	private Position3D speed;   

	/* rotational speed in radians per second (phi,psi,theta) */
	private Position3D rotationalSpeed;
		
	/* flight time in seconds */
	private int flightTime = 0;

	/* horizontal ground speed in module and dir (m/s, rad (CW/North)) */
	private HorizSpeed horizontalSpeed; // (estimator_hspeed_mod, estimator_hspeed_dir)
   

	/* Wind and airspeed estimation sent by the GCS */
	private float windEast = 0f, windNorth = 0f; /* m/s */     
	private float airspeed = 0f; /* m/s */
	
	/* IR related values */
	private boolean irEstimationModeEnabled = false;
	private float radOfIR;
	private float ir;
	private float rad;
	private boolean irInitialized = false;
	private float lastHSpeedDir;
	private float lastGPSTow;
	private float sumXX, sumXY;

	public EstimatorModuleImpl() {
		position = new Position3D(0.0f, 0.0f, 0.0f);
		attitude = new Attitude(0.0f, 0.0f, 0.0f);
		speed    = new Position3D(0.0f, 0.0f, 0.0f);
		rotationalSpeed = new Position3D(0.0f, 0.0f, 0.0f);
		horizontalSpeed = new HorizSpeed(0.0f, 0.0f);
	}

	public void init() {
		if (gpsDevice == null || irDevice == null) {
			throw new IllegalArgumentException("Estimator modules is not properly configured");
		}
	}
	
	
	public void updateIR() { // -> estimator_update_ir_estim
		if (irInitialized) {
//			float dt = gpsDevice.getTow() - lastGPSTow;
			// FIXME
			float dt = 0.5f;
			if (dt > 0.1) {
				float phi = (horizontalSpeed.direction - lastHSpeedDir);
				phi = MathUtils.normalizeRadAngle(phi);
				phi = phi / dt * AirframeParametersConf.NOMINAL_AIRSPEED / AirframeParametersConf.G;
				phi = MathUtils.normalizeRadAngle(phi);
				
				ir = irDevice.getIrRoll();
				rad = phi;
				
				float absphi = Math.abs(phi);
				if (absphi < 1.0f && absphi > 0.05f 
						&& 
					(-irDevice.getIrContrast()/2 < irDevice.getIrRoll() && irDevice.getIrRoll() < irDevice.getIrContrast()/2) ) {
					
					sumXY = rad * ir + RHO*sumXY;
					sumXY = ir * ir + RHO*sumXX;
					
					radOfIR = sumXY / sumXY;
				}				
			}
			
		} else {
			irInitialized = true;
			
			float initIR = irDevice.getIrContrast();
			initIR = initIR * initIR;
			sumXY = INIT_WEIGHT * radOfIR * initIR;
			sumXX = INIT_WEIGHT * initIR;
		}
		
		lastHSpeedDir = horizontalSpeed.direction;
		lastGPSTow = gpsDevice.getTow();
	}
	
	public void updateIRState() {
		float radofir; 
		if (irEstimationModeEnabled) 
			radofir = radOfIR;
		else 
			radofir = irDevice.getIrRadOfIr();
		
		attitude.phi = radofir * irDevice.getIrRoll(); // phi updated
		attitude.theta = radofir * irDevice.getIrPitch(); // theta updated
	}
	
	public void updatePosition() {
		if (true) { // FIXME
		//if ((gpsDevice.getMode() & 1<<5) == 1) {
			updatePosition(gpsDevice.getEast(), gpsDevice.getNorth(), gpsDevice.getAltitude());
			updateSpeedPol(gpsDevice.getSpeed(), gpsDevice.getCourse(), gpsDevice.getClimb());
						
			// airplane is flying => update roll information
			if (flightTime > 0) {
				updateIR();
			}
		}
	}
	
	public void updateFlightTime() {
		flightTime++;		
		LogUtils.log(this, "Flight time = " + flightTime);
	}
	
	public void setGPSDevice(GPSDevice gpsDeviceArg) {
		gpsDevice = gpsDeviceArg;		
	}
	
	public void setIRDevice(IRDevice irDeviceArg) {
		irDevice = irDeviceArg;		
	}
	
	public Position3D getPosition() {		
		return position;
	}
	
	public Attitude getAttitude() {
		return attitude;
	}
	
	public HorizSpeed getHorizontalSpeed() {
		return horizontalSpeed;
	}
	
	public Position3D getSpeed() {
		return speed;
	}

	public void setFlightTime(int flightTimeArg) {
		flightTime = flightTimeArg;
	}

	public int getFlightTime() {
		return flightTime;
	}
	
	protected void updatePosition(float x, float y, float z) {
		position.x = x;
		position.y = y;
		position.z = z;
	}
	
	protected void updateAttitude(float phi, float psi, float theta) {
		attitude.psi = phi;
		attitude.phi = psi;
		attitude.theta = theta;
	}
	
	protected void updateSpeedPol(float vhmod, float vhdir, float vz) {
		horizontalSpeed.module = vhmod;
		horizontalSpeed.direction = vhdir;
		speed.z = vz;				
	}
}

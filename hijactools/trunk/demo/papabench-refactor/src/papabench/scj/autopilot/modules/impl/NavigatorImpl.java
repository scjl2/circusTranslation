/**
 * 
 */
package papabench.scj.autopilot.modules.impl;

import papabench.scj.autopilot.data.Position2D;
import papabench.scj.autopilot.modules.AutopilotModule;
import papabench.scj.autopilot.modules.Navigator;
import papabench.scj.commons.conf.AirframeParametersConf;
import papabench.scj.commons.data.FlightPlan;

/**
 * @author Michal Malohlava
 *
 */
public class NavigatorImpl implements Navigator {
	
	protected static final float MIN_HEIGHT_CARROT = 50;
	protected static final float MAX_HEIGHT_CARROT = 150;
	
	private FlightPlan flightPlan;	
	
	private AutopilotModule autopilotModule;
	
	private Position2D desiredPosition;
	private float desiredCourse;
	private float desiredAltitude;
	private float desiredRoll;
	private float desiredPitch;
	private float preClimb;
	private boolean autoPitch;
	private int desiredGaz;
	
	private int autoNavigateCounter;
		
	public void init() {
		if (flightPlan == null || autopilotModule == null) {
			throw new IllegalArgumentException("Navigator module has wrong configuration!");
		}
		
		flightPlan.setEstimator(autopilotModule.getEstimator());
		flightPlan.setAutopilotStatus(autopilotModule);
		flightPlan.setNavigator(this);
		
		flightPlan.init();
		
		desiredPosition = new Position2D(0, 0);
		desiredPitch = AirframeParametersConf.NAV_PITCH;
		desiredAltitude = flightPlan.getGroundAltitude() + MIN_HEIGHT_CARROT;
		autoNavigateCounter = 0;
	}
	
	public void autoNavigate() {
		flightPlan.execute();
	}
	
	public FlightPlan getFlightPlan() {
		return flightPlan;
	}

	public void setFlightPlan(FlightPlan flightPlanArg) {
		flightPlan = flightPlanArg;
	}
	
	public float getDesiredCourse() {
		return desiredCourse;
	}


	public void setDesiredCourse(float desiredCourseArg) {
		desiredCourse = desiredCourseArg;
	}


	public float getDesiredAltitude() {
		return desiredAltitude;
	}


	public void setDesiredAltitude(float desiredAltitudeArg) {
		desiredAltitude = desiredAltitudeArg;	
	}	
	public void setDesiredRoll(float roll) {
		desiredRoll = roll;
	}
	public float getDesiredRoll() {
		return desiredRoll;
	}
	
	public float getDesiredPitch() {
		return desiredPitch;
	}
	
	public void setDesiredPitch(float pitch) {
		desiredPitch = pitch;
	}


	public float getPreClimb() {
		return preClimb;
	}


	public void setPreClimb(float preClimbArg) {
		preClimb = preClimbArg;
	}


	public boolean isAutoPitch() {
		return autoPitch;
	}


	public void setAutoPitch(boolean autoPitchArg) {
		autoPitch = autoPitchArg;
	}


	public int getDesiredGaz() {
		return desiredGaz;
	}


	public void setDesiredGaz(int desiredGazArg) {
		desiredGaz = desiredGazArg;
	}


	public void setAutopilotModule(AutopilotModule autopilotModuleArg) {
		autopilotModule = autopilotModuleArg;
	}

	public Position2D getDesiredPosition() {
		return desiredPosition;
	}

	public void setDesiredPosition(float x, float y) {
		desiredPosition.x = x;
		desiredPosition.y = y;
	}
	
	public int getAutoNavigationCycleNumber() {
		return autoNavigateCounter;
	}


}

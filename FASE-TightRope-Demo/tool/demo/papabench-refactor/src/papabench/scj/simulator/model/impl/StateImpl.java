/**
 * 
 */
package papabench.scj.simulator.model.impl;
import static papabench.scj.commons.conf.AirframeParametersConf.CRUISE_THROTTLE;
import static papabench.scj.commons.conf.AirframeParametersConf.G;
import static papabench.scj.commons.conf.AirframeParametersConf.MAXIMUM_AIRSPEED;
import static papabench.scj.commons.conf.AirframeParametersConf.MAXIMUM_POWER;
import static papabench.scj.commons.conf.AirframeParametersConf.NOMINAL_AIRSPEED;
import static papabench.scj.commons.conf.AirframeParametersConf.ROLL_RESPONSE_FACTOR;
import static papabench.scj.commons.conf.AirframeParametersConf.WEIGHT;
import static papabench.scj.commons.conf.AirframeParametersConf.YAW_RESPONSE_FACTOR;
import static papabench.scj.commons.conf.RadioConf.MAX_THRUST;
import static papabench.scj.commons.conf.RadioConf.MIN_THRUST;
import papabench.scj.autopilot.data.Attitude;
import papabench.scj.autopilot.data.Position2D;
import papabench.scj.autopilot.data.Position3D;
import papabench.scj.commons.conf.AirframeParametersConf;
import papabench.scj.commons.data.RadioCommands;
import papabench.scj.simulator.conf.SimulatorConf;
import papabench.scj.simulator.model.State;
import papabench.scj.utils.LogUtils;
import papabench.scj.utils.MathUtils;

/**
 * Simulated environment state.
 * 
 * @author Michal Malohlava
 *
 */
public class StateImpl implements State {
	
	private Position3D position; // (m,m,m)
	
	private Attitude attitude; // psi/phi/theta in rad
	private Attitude rotSpeed; // rad/s
	private float zDot; // m/s
	private float airSpeed; // m/s
	private float time; // sec
	private float thrust; 
	private Position2D delta;
	
	public void init() {
		position = new Position3D(0, 0, 0);
		attitude = new Attitude(0, 0, 0);
		rotSpeed = new Attitude(0, 0, 0);
		delta = new Position2D(0, 0);
		zDot = 0;
		airSpeed = 0;
		time = 0;
		thrust = 0;
	}
	
	public void updateState(float dt, Position3D wind) {
		float now = time + dt;
		
		if (airSpeed == 0 && thrust > 0) {
			airSpeed = NOMINAL_AIRSPEED;			
		}
		
		if (airSpeed > 0) {
			float v2 = airSpeed*airSpeed;
			float vn2 = NOMINAL_AIRSPEED * NOMINAL_AIRSPEED;
			
			float phiDotDot = ROLL_RESPONSE_FACTOR * delta.x * v2/vn2 - rotSpeed.phi;
			rotSpeed.phi += phiDotDot*dt;
			rotSpeed.phi = MathUtils.symmetricalLimiter(rotSpeed.phi, SimulatorConf.MAX_PHI_DOT);
			attitude.phi = MathUtils.normalizeRadAngle(attitude.phi + rotSpeed.phi * dt);
			attitude.phi = MathUtils.symmetricalLimiter(attitude.phi, SimulatorConf.MAX_PHI);
			
			float psiDot = (float) (-G / airSpeed * Math.tan(YAW_RESPONSE_FACTOR * attitude.phi));
			
			attitude.psi = MathUtils.normalizeRadAngle(attitude.psi + psiDot * dt);
			
			float cM = 5e-7f * delta.y;
			float thetaDotDot = cM * v2 - rotSpeed.theta;
			rotSpeed.theta += thetaDotDot * dt;
			attitude.theta += rotSpeed.theta * dt;
			
			float gamma = (float) Math.atan2(zDot, airSpeed);
			float alpha = attitude.theta - gamma;
			float cZ = 0.2f * alpha + AirframeParametersConf.G / vn2; // FIXME why is there constant 0.2 (copied from paparazzi simulator)
			
			float lift = cZ * airSpeed * airSpeed;
			float zDotDot = (float) (lift/WEIGHT * Math.cos(attitude.theta) * Math.cos(attitude.phi) - AirframeParametersConf.G);
			zDot += zDotDot * dt;
			position.z += zDot * dt;
			
			float drag = CRUISE_THROTTLE + (v2 - vn2)*(1 - CRUISE_THROTTLE)/(MAXIMUM_AIRSPEED*MAXIMUM_AIRSPEED - vn2);
			float airSpeedDot = (float) (MAXIMUM_POWER / airSpeed * (thrust - drag)/WEIGHT - G*Math.sin(gamma));
			airSpeed += airSpeedDot * dt;
			airSpeed = Math.max(airSpeed, NOMINAL_AIRSPEED);
			
			float xDot = (float) (airSpeed * Math.cos(attitude.psi) + wind.x);
			float yDot = (float) (airSpeed * Math.sin(attitude.psi) + wind.y);
			
			position.x += xDot * dt;
			position.y += yDot * dt;
			position.z += wind.z * dt;			
		}
		
		time = now;
	}
	
	public void updateState(RadioCommands commands) {
		float cLda = 4e-5f;
		
		LogUtils.log(this, "Radio commands for simulator state update: " + commands.toString());
		delta.x = -cLda * commands.getRoll();
		delta.y = commands.getPitch();
		thrust = (commands.getThrottle() - MIN_THRUST) / (float) (MAX_THRUST - MIN_THRUST);
		LogUtils.log(this, "State Updated: delta.x="+delta.x + ", delta.y="+delta.y +", thrust="+thrust);
	}

	public Attitude getAttitude() {
		return attitude;
	}

	public Position2D getDelta() {
		return delta;
	}

	public Position3D getPosition() {
		return position;
	}

	public Attitude getRotationalSpeed() {
		return rotSpeed;
	}

	public float getAirSpeed() {		
		return airSpeed;
	}

	public float getThrust() {
		return thrust;
	}

	public float getTime() {		
		return time;
	}

	public float getZDot() {		
		return zDot;
	}

	@Override
	public String toString() {
		return "StateImpl [airSpeed=" + airSpeed + ", attitude=" + attitude
				+ ", delta=" + delta + ", position=" + position + ", rotSpeed="
				+ rotSpeed + ", thrust=" + thrust + ", time=" + time
				+ ", zDot=" + zDot + "]";
	}
}

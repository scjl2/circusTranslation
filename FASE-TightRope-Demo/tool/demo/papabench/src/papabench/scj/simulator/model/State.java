/**
 * 
 */
package papabench.scj.simulator.model;

import papabench.scj.autopilot.data.Attitude;
import papabench.scj.autopilot.data.Position2D;
import papabench.scj.autopilot.data.Position3D;
import papabench.scj.commons.data.RadioCommands;

/**
 * Flight model state.
 * 
 * @author Michal Malohlava
 *
 */
public interface State {
	
	void init();

	float getTime();
	
	Position3D getPosition();
	
	float getAirSpeed();
	
	/**
	 * Z-velocity m/s.
	 * 
	 * @return
	 */
	float getZDot();
	
	Attitude getAttitude();
	
	Attitude getRotationalSpeed(); /* psi, phi theta -> rad/s */
	
	Position2D getDelta();
	
	float getThrust();
	
	void updateState(float dt, Position3D wind);

	void updateState(RadioCommands commands);
}

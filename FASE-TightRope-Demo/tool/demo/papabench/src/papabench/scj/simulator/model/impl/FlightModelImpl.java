/**
 * 
 */
package papabench.scj.simulator.model.impl;

import papabench.scj.autopilot.data.Position3D;
import papabench.scj.commons.data.RadioCommands;
import papabench.scj.simulator.conf.SimulatorConf;
import papabench.scj.simulator.model.FlightModel;
import papabench.scj.simulator.model.State;

/**
 * Flight model.
 * 
 * @author Michal Malohlava
 *
 */
public class FlightModelImpl implements FlightModel {
	
	private State state;
	private Position3D wind;

	public void init() {
		state = new StateImpl();
		state.init();
		
		wind = new Position3D(0, 0, 0);		
	}
	
	public State getState() {
		return this.state;
	}

	public void updateState() {
		state.updateState(SimulatorConf.FM_PERIOD, wind);		
	}
	
	public void processCommands(RadioCommands commands) {
		state.updateState(commands);
	}
}

/**
 * 
 */
package papabench.scj.simulator.model;

import papabench.scj.commons.data.RadioCommands;
import papabench.scj.commons.modules.Module;



/**
 * @author Michal Malohlava
 *
 */
public interface FlightModel extends Module {
	
	State getState();
	
	void updateState();
	
	void processCommands(RadioCommands commands);
}

/**
 * 
 */
package papabench.scj.autopilot.modules;

import papabench.scj.commons.modules.Module;


/**
 * Note:
 *  - it would be nice to fill dependencies (Estimator, GPSDevice) like in IoC containers with possibility to choose allocation area (mission/immortal)
 *  
 * @author Michal Malohlava
 *
 */
public interface AutopilotModule extends Module, AutopilotStatus, AutopilotDevices {
	
	Estimator getEstimator();
	void setEstimator(Estimator estimator);
	
	Navigator getNavigator();
	void setNavigator(Navigator navigator);
	
	void setLinkToFBW(LinkToFBW fbwLink);
	LinkToFBW getLinkToFBW();
		
}

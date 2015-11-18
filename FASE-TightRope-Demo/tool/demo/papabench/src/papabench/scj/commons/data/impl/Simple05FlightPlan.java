/**
 * 
 */
package papabench.scj.commons.data.impl;

import papabench.scj.autopilot.conf.VerticalFlightMode;
import papabench.scj.autopilot.data.Position3D;
import papabench.scj.commons.conf.RadioConf;
import papabench.scj.commons.data.UTMPosition;
import papabench.scj.utils.PPRZUtils;


/**
 * Simple flight plan - it does 4 navigation cycles.
 * 
 * @author Michal Malohlava
 * 
 */
public class Simple05FlightPlan extends AbstractFlightPlan {
	
	protected static final int GROUND_ALTITUDE = 125;
	protected static final int SECURE_ALTITUDE = 140;
	

	public String getName() {
		return "Simple 02 flight plan (4 navigation cycles)";
	}
	
	public float getGroundAltitude() {
		return GROUND_ALTITUDE;
	}
	
	public float getSecureAltitude() {		
		return SECURE_ALTITUDE;
	}

	@Override
	protected int getNumberOfNavBlocks() {
		return 1;
	}

	@Override
	protected int getNumberOfWaypoints() {
		return 2;
	}

	@Override
	protected void initNavigationBlocks() {
		addNavBlock(new NavigationBlock(2))
			.addNavStage(new NavigationStage() {				
				@Override
				protected void execute() {	
					// launch airplane
					status().setLaunched(true);
					nextStage();
				}
			}).addNavStage(new NavigationStage() {				
				@Override
				protected void execute() {
					// climb for a while with course 15degrees and then switch to the next stage					
					if (estimator.getFlightTime() > 4) {
						missionFinished();
					} else {
						navigator().setDesiredCourse((float) Math.toRadians(15));
						navigator().setAutoPitch(false);
						navigator().setDesiredPitch(0.15f);
						status.setVerticalFlightMode(VerticalFlightMode.AUTO_GAZ);
						navigator().setDesiredGaz((int) PPRZUtils.trimuPPRZ(0.8f * RadioConf.MAX_PPRZ));
					}																									
				}
			});

	}

	@Override
	protected void initWaypoints() {		
		addWaypoint(new Position3D(0f, 0f, 200f)); // 0	
		addWaypoint(new Position3D(115f, -75f, 200f)); // 1		
	}

	@Override
	protected UTMPosition getCenterUTMPosition() {
		return null;
	}
}

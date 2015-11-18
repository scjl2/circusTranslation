/**
 * 
 */
package papabench.scj.commons.data.impl;

import static papabench.scj.commons.conf.AirframeParametersConf.CARROT;
import papabench.scj.autopilot.data.Position3D;
import papabench.scj.commons.data.UTMPosition;

/**
 * @author Michal Malohlava
 *
 */
public class RoundTripFlightPlan extends AbstractFlightPlan {
	
	protected static final int GROUND_ALTITUDE = 0;
	protected static final int SECURE_ALTITUDE = 25;
	
//	private final Position2D centerPos = new Position2D(10.548188f, 52.316323f);
			
	public String getName() {
		return "Round trip flight plan";
	}

	@Override
	protected int getNumberOfNavBlocks() {		
		return 2;
	}

	@Override
	protected int getNumberOfWaypoints() {		
		return 4;
	}
	
	public float getGroundAltitude() {		
		return GROUND_ALTITUDE;
	}
	
	public float getSecureAltitude() {		
		return SECURE_ALTITUDE;
	}

	@Override
	protected void initNavigationBlocks() {
		this.addNavBlock(new NavigationBlock(2) { // navigation block 0 contains, two stages
			@Override
			protected void preCall() {
				if (estimator.getPosition().z > getGroundAltitude() + 25) {
					gotoBlock(1);
				}
			}
		}).addNavStage(new NavigationStage() {			
			@Override
			protected void execute() {
				killThrottle();	estimator.setFlightTime(0);
				status.setLaunched(true); 
				nextStage();
			}
		}).addNavStage(new NavigationStage() {			
			@Override
			protected void execute() {
				if (navApproachingFrom(2, 1, CARROT)) {
					nextStageFrom(2);
				} else {
					navGotoWaypoint(2);
					navVerticalAutoThrottleMode((float) Math.toRadians(15));
					navVerticalThrottleMode(9600);				
				}
			}
		});
		
		
		this.addNavBlock(new NavigationBlock(2)) // navigation block 1
			.addNavStage(new NavigationStage() {				
				@Override
				protected void execute() {
					if (navApproachingFrom(2, 1, CARROT)) {
						nextStageFrom(2);
						
					} else {
						navGotoWaypoint(2);
						navVerticalAutoThrottleMode(0);
						navVerticalAltitudeMode(WPALT(2), 0f);						
					}
					
				}
			}).addNavStage(new NavigationStage() {
				
				@Override
				protected void execute() {
					if (navApproachingFrom(3, getLastWPNumber(), CARROT)) {
						nextStageFrom(3);
					} else {
						navGotoWaypoint(3);
						navVerticalAutoThrottleMode(0f);
						navVerticalAltitudeMode(WPALT(3), 0f);
					}
				}
			});
	}

	// FIXME replace by a method getWaypoint(X)
	@Override
	protected void initWaypoints() {
		this.addWaypoint(new Position3D(42, 42, 250)); // dummy
		this.addWaypoint(new Position3D(0f, 0f, 0f)); // HOME
		this.addWaypoint(new Position3D(-337, 17, 60)); // CHECK POINT #1
		this.addWaypoint(new Position3D(238, -30, 40)); // CHECK POINT #2		
	}
	
	@Override
	protected UTMPosition getCenterUTMPosition() {		
		return null;
	}
	
}

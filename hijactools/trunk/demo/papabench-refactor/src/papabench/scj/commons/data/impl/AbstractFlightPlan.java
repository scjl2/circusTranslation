/**
 * 
 */
package papabench.scj.commons.data.impl;

import papabench.scj.autopilot.data.Position3D;
import papabench.scj.autopilot.modules.AutopilotStatus;
import papabench.scj.autopilot.modules.Estimator;
import papabench.scj.autopilot.modules.Navigator;
import papabench.scj.commons.data.FlightPlan;
import papabench.scj.commons.data.UTMPosition;
import papabench.scj.utils.LogUtils;

/**
 * Abstract flight plan.
 * 
 * The concrete flight plan should be automatically generated from fligh-plan description and should inherit from
 * this class.
 * 
 * @author Michal Malohlava
 *
 */
public abstract class AbstractFlightPlan implements FlightPlan {
	
	/* injected dependencies */
	protected Estimator estimator;
	protected AutopilotStatus status;
	protected Navigator navigator;
	
	/* navigation flight plan */
	private NavigationBlock[] navigationBlocks;
	private Position3D[] waypoints;
	
	private NavigationBlock currentBlock = null;	
	private int lastWPNumber = 0;
	
	private int currentNumberOfBlocks = 0;
	private int currentNumberOfWaypoints = 0;
	
	public final void init() {
		if (estimator == null || status == null || navigator == null) {
			throw new IllegalArgumentException("FligPlan has wrong configuration!");
		}
		// let the user to setup navigation points
		waypoints = new Position3D[getNumberOfWaypoints()];
		initWaypoints();
		
		// allocated desired number of blocks
		navigationBlocks = new NavigationBlock[getNumberOfNavBlocks()];
		// let the user to setup navigation blocks and stages
		initNavigationBlocks();
		
		currentBlock = navigationBlocks[getStartNavBlockNumber()];
		currentBlock.init();
	}
	
	
	public void setEstimator(Estimator estimatorArg) { estimator = estimatorArg; }
	public void setAutopilotStatus(AutopilotStatus statusArg) {status = statusArg; }
	public void setNavigator(Navigator navigatorArg) {navigator = navigatorArg; }
	
	/**
	 * Initialize waypoints.
	 * 
	 *  User should use method {@link #addWaypoint(Position3D)} to fill an internal array of waypoints.
	 */
	protected abstract void initWaypoints();
	
	/**
	 * Initialize navigation blocks.
	 * 
	 * User should use method {@link #addNavBlock(NavigationBlock)} to add navigation blocks. 
	 */
	protected abstract void initNavigationBlocks();
			
	protected Position3D WP(int number) {
		return waypoints[number];
	}
	
	protected float WPALT(int number) {
        Position3D pos3d = WP(number);
		return pos3d.z;
	}
	
	protected int getLastWPNumber() {
		return lastWPNumber;
	}
	
	/**
	 * Returns number of initial navigation block. 
	 * 
	 * Default is the first navigation block.
	 * 
	 * @return number of initial navigation block
	 */
	protected int getStartNavBlockNumber() { return 0; }
		
	protected abstract int getNumberOfNavBlocks();
	protected abstract int getNumberOfWaypoints();
	
	protected abstract UTMPosition getCenterUTMPosition();
	
	public final void execute() {		
		
		if (currentBlock != null) {
			currentBlock.execute();			
		}		
	}
	
	public void nextBlock() {
		int num = currentBlock.getBlockNumber();
		if (++num < navigationBlocks.length) {
			currentBlock = navigationBlocks[num];
			currentBlock.init();
		} else {
			// FIXME mission finished -> what to do?
			// we call mission finished
			missionFinished();
		}
	}
	
	public void gotoBlock(int i) {
		// assert 0 =< o < navigationBlocks.length
		currentBlock = navigationBlocks[i];
		currentBlock.init();
	}
	
	public void setLastWPNumber(int wp) {		 
		lastWPNumber = wp;
	}
	
	public void missionFinished() {
		LogUtils.log(this, "Flightplan finished - mission termination requested");
		
		currentBlock = null;
		status.missionFinished();		
	}
	
	
	/**
	 * Add navigation block.
	 * 
	 * IMPORTANT: Order of blocks define number of 
	 * @param block
	 * 
	 * @return the reference to the parameter block
	 */
	protected final NavigationBlock addNavBlock(NavigationBlock block) {
		navigationBlocks[currentNumberOfBlocks] = block;
		block.setFlightPlan(this);
		block.setBlockNumber(currentNumberOfBlocks);
		currentNumberOfBlocks++;
		
		return block;
	}
	
	protected final void addWaypoint(Position3D point) {
		waypoints[currentNumberOfWaypoints++] = point;
	}
	
}


	


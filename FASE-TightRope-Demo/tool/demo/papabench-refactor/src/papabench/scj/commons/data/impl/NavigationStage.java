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
	 * Simple implementation of block navigation stage.
	 * 
	 * The user should instantiate this class with own logic.
	 * 
	 * @author Michal Malohlava
	 *
	 */
	public abstract class NavigationStage extends NavigatorCommands {
		public int stageNumber;

    	public Estimator estimator;
    	public AutopilotStatus status;
    	public Navigator navigator;
		
		private NavigationBlock block;
		private Position3D lastPosition = new Position3D(0, 0, 0);
		
		void setNavigationBlock(NavigationBlock blockArg) {
			block = blockArg;			
		}
				
		public abstract void execute();
		
		public void init() {
            Position3D pos3d = estimator.getPosition();
			lastPosition.x = pos3d.x;
			lastPosition.y = pos3d.y;
			lastPosition.z = pos3d.z;			
		}
		
		public final void nextStage() {
			block.nextStage();
		}
		
		public final void nextStageFrom(int wp) {
			block.nextStageFrom(wp);
		}
		
		public void setStageNumber(int number) {
			stageNumber = number;
		}
		
		public int getStageNumber() {
			return stageNumber;
		}
		
		public final void gotoStage(int stage) {
			block.gotoStage(stage);
		}
		
		public final void missionFinished() {
//			AbstractFlightPlan.this.missionFinished();
		}
		

        @Override
        protected Estimator estimator() {return estimator; }

		@Override
		protected Navigator navigator() { return navigator; }
		@Override
		protected AutopilotStatus status() {return status; }
		@Override
		protected Position3D WP(int n) {return null; }//return AbstractFlightPlan.this.WP(n); }
		@Override
		protected int getLastWPNumber() {			
            return 0;
//			return AbstractFlightPlan.this.getLastWPNumber();
		}
		@Override
		protected final Position3D getLastPosition() {
			return lastPosition;			
		}
	}

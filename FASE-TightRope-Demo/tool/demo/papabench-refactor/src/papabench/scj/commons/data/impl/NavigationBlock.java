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
	 * Simple implementation of navigation block.
	 * 
	 * The user should instantiate this class and overide block logic method.
	 * 
	 * @author Michal Malohlava
	 *
	 */
	public class NavigationBlock {
		private int blockNumber;		
		private AbstractFlightPlan flightPlan;
		
		private NavigationStage[] navigationStages;		
		private int currentNumberOfStages = 0;
		
		private NavigationStage currentStage = null;
		
		public NavigationBlock(int numberOfStages) {
			navigationStages = new NavigationStage[numberOfStages];
		}
		
		public final void execute() {			
			preCall();
			
			LogUtils.log(this, "Block " + blockNumber + " Stage " + currentStage.stageNumber + " executed");
			currentStage.execute();
			
//			if (!skipPostCall) {
//				postCall();
//				skipPostCall = false;
//			}
		}
		
		public void init() {
			currentStage = navigationStages[0];
			currentStage.init();
		}
		
		/**
		 * This method is call before the stage code is executed.
		 * 
		 * User can override this method.
		 */
		public void preCall() {}
		/**
		 * This method is call after execution stage code (stage can skip this code).
		 * 
		 * User can override this method.
		 */
		public void postCall() {}
		
		/**
		 * Add new stage into this block.
		 * 
		 * @param stage stage of this block
		 * 
		 * @return reference to this block to allow chaining.
		 */
		public final NavigationBlock addNavStage(NavigationStage stage) {
			navigationStages[currentNumberOfStages] = stage;
			stage.setNavigationBlock(this);
			stage.setStageNumber(currentNumberOfStages);
			currentNumberOfStages++;
			
			return this;
		}
		
		/**
		 * Called by flight plan to setup a navigation block number.
		 * Block number depends on addition of navigation block into the flight plan.
		 *  
		 * @param number navigation block number
		 */
		public void setBlockNumber(int number) {
			blockNumber = number;
		}
		
		public int getBlockNumber() {
			return blockNumber;
		}
		
		public void setFlightPlan(AbstractFlightPlan flightPlanArg) {
			flightPlan = flightPlanArg;
		}
		
		/**
		 * Called by the current stage to go to next stage.
		 */
		public void nextStage() {
			int num = currentStage.getStageNumber();
			if (++num < navigationStages.length) {				
				currentStage = navigationStages[num];
				currentStage.init();
			} else {
				nextBlock();
			}
		}
		
		public void nextStageFrom(int wp) {
			flightPlan.setLastWPNumber(wp);
			nextStage();
		}
		
		public void gotoStage(int stage) {
			currentStage = navigationStages[stage];
			currentStage.init();
		}
		
		public final void nextBlock() {
			flightPlan.nextBlock();
		}
		
		public final void gotoBlock(int i) {
			flightPlan.gotoBlock(i);						
		}
	}

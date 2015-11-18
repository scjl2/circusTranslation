/**
 * 
 */
package papabench.scj.commons.tasks;

import javax.realtime.PriorityParameters;
import javax.realtime.ReleaseParameters;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;

import papabench.scj.commons.conf.CommonTaskConfiguration;
import papabench.scj.utils.LogUtils;

/**
 * PeriodicEventHandler wrapper.
 * 
 * @author Michal Malohlava
 *
 */
public abstract class PapaBenchPeriodicTask extends PeriodicEventHandler implements CommonTaskConfiguration {
	
	/* 
	 * Static variable to distinguish between two repetition of PapaBench schedule
	 * 
	 * Required by JPF.
	 */	
	public static int PAPBENCH_TASKS_EXECUTION_COUNTER = 0;

	public PapaBenchPeriodicTask(PriorityParameters pp, ReleaseParameters rp,
			StorageParameters stp, long memSize) {
      /* Modified by Frank Zeyda */
		super(pp, null, stp, memSize);
      /* End of Modification */
	}
	
	@Override
	final public void handleAsyncEvent() {
		LogUtils.log(getTaskName(), "PERIOD handler enter");		
		
		try {
			PAPBENCH_TASKS_EXECUTION_COUNTER++;
			
			handlePeriod();			
		} finally {		
			LogUtils.log(getTaskName(), "PERIOD handler return");
		}
	}
	
	public abstract void handlePeriod();

   /* Added by Frank Zeyda */
   public void handleEvent() { }

   public void register() { }

   public StorageParameters getThreadConfigurationParameters() {
      return null;
   }
   /* End of Addition */
}

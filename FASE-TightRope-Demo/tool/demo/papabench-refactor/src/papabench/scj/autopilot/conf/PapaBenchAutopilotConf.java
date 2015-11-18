/**
 * 
 */
package papabench.scj.autopilot.conf;

import javax.realtime.RealtimeThread;

import papabench.scj.commons.conf.CommonTaskConfiguration;

/**
 * Configuration for Autopilot subsystem - declares timing properties for the tasks.
 * 
 * @see CommonTaskConfiguration
 * 
 * @author Michal Malohlava
 *
 * TODO this class should be automatically generated according to AADL design
 */
public class PapaBenchAutopilotConf {
	
	/**
	 * Navigation task configuration
	 */
	public static interface NavigationTaskConf extends CommonTaskConfiguration {
		public static final String NAME = "Navigation";		
      /* Modified by Frank Zeyda */
		public static final int PRIORITY = /*RealtimeThread.MAX_RT_PRIORITY*/ - 6;
      /* End of Modification */
		public static final int PERIOD_MS = 250;
		public static final int RELEASE_MS = 0;
		public static final int SIZE = 0;
	}
	
	/**
	 * Altitude control task configuration.
	 *
	 */
	public static interface AltitudeControlTaskConf extends CommonTaskConfiguration {
		public static final String NAME = "AltitudeControl";		
      /* Modified by Frank Zeyda */
		public static final int PRIORITY = /*RealtimeThread.MAX_RT_PRIORITY*/ - 7;	      /* End of Modification */
		public static final int PERIOD_MS = 250;
		public static final int RELEASE_MS = 0;
		public static final int SIZE = 0;		
	}
	
	/**
	 * Climb control task configuration.
	 *
	 */
	public static interface ClimbControlTaskConf extends CommonTaskConfiguration {
		public static final String NAME = "ClimbControl";		
      /* Modified by Frank Zeyda */
		public static final int PRIORITY = /*RealtimeThread.MAX_RT_PRIORITY*/ - 8;
      /* End of Modification */
		public static final int PERIOD_MS = 250;		
		public static final int RELEASE_MS = 0;
		public static final int SIZE = 0;		
	}
	
	/**
	 * Stabilization task configuration.
	 *
	 */
	public static interface StabilizationTaskConf extends CommonTaskConfiguration {
		public static final String NAME = "Stabilization";		
      /* Modified by Frank Zeyda */
		public static final int PRIORITY = /*RealtimeThread.MAX_RT_PRIORITY*/ - 9; /* has to have higher prior than LinkFBWSendTaskConf */
      /* End of Modification */
		public static final int PERIOD_MS = 50;
		public static final int RELEASE_MS = 0;
		public static final int SIZE = 0;		
	}
	
	/**
	 * Radio control task configuration.
	 */
	public static interface RadioControlTaskConf extends CommonTaskConfiguration {
		public static final String NAME = "RadioControl";		
      /* Modified by Frank Zeyda */
		public static final int PRIORITY = /*RealtimeThread.MAX_RT_PRIORITY*/ - 5;
      /* End of Modification */
		public static final int PERIOD_MS = 25;		
		public static final int RELEASE_MS = 0;
		public static final int SIZE = 0;		
	}
	
	/**
	 * Link fbw send task configuration
	 */
	public static interface LinkFBWSendTaskConf extends CommonTaskConfiguration {
		public static final String NAME = "LinkFBWSend";
      /* Modified by Frank Zeyda */
		public static final int PRIORITY = /*RealtimeThread.NORM_RT_PRIORITY*/ + 1;
      /* End of Modification */
		public static final int PERIOD_MS = 50;
		public static final int RELEASE_MS = 0;
		public static final int SIZE = 0;		
	}
	
	/**
	 * Reporting task configuration
	 */
	public static interface ReportingTaskConf extends CommonTaskConfiguration {
		public static final String NAME = "Reporting";
      /* Modified by Frank Zeyda */
		public static final int PRIORITY = /*RealtimeThread.NORM_RT_PRIORITY*/ - 1;
      /* End of Modification */
		public static final int PERIOD_MS = 100; // TODO: check this in AADL there is f=10Hz, in code f=20Hz
		public static final int RELEASE_MS = 0;
		public static final int SIZE = 0;		
	}
	
	
	

}

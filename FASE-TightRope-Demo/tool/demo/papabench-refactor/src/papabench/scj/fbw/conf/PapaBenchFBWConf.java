/**
 * 
 */
package papabench.scj.fbw.conf;

import javax.realtime.RealtimeThread;

import papabench.scj.commons.conf.CommonTaskConfiguration;

/**
 * @author Michal Malohlava
 *
 */
public interface PapaBenchFBWConf {
	
	/**
	 * Test PPM (?) task configuration.
	 */
	public static interface TestPPMTaskConf extends CommonTaskConfiguration {
		public static final String NAME = "TestPPM";
      /* Modified by Frank Zeyda */
		public static final int PRIORITY = /*RealtimeThread.MAX_RT_PRIORITY*/ - 3;
      /* End of Modification */      	
		public static final int PERIOD_MS = 25;
		public static final int RELEASE_MS = 0;
		public static final int SIZE = 0;
	}
	
	/**
	 * Check Failsafe task configuration.
	 */
	public static interface CheckFailsafeTaskConf extends CommonTaskConfiguration {
		public static final String NAME = "CheckFailsafe";
      /* Modified by Frank Zeyda */
		public static final int PRIORITY = /*RealtimeThread.MAX_RT_PRIORITY*/ - 10;
      /* End of Modification */
		public static final int PERIOD_MS = 50;	
		public static final int RELEASE_MS = 0;
		public static final int SIZE = 0;
	}
	
	/**
	 * Check mega128 values task configuration.
	 */
	public static interface CheckMega128ValuesTaskConf extends CommonTaskConfiguration {
		public static final String NAME = "CheckMega128Values";
      /* Modified by Frank Zeyda */
		public static final int PRIORITY = /*RealtimeThread.MAX_RT_PRIORITY*/ - 11;
      /* End of Modification */
		public static final int PERIOD_MS = 50;	
		public static final int RELEASE_MS = 0;
		public static final int SIZE = 0;
	}
	
	/**
	 * Send data to autopilot task configuration.
	 */
	public static interface SendDataToAutopilotTaskConf extends CommonTaskConfiguration {
		public static final String NAME = "SendDataToAutopilot";
      /* Modified by Frank Zeyda */
		public static final int PRIORITY = /*RealtimeThread.MAX_RT_PRIORITY*/ - 4;
      /* End of Modification */
		public static final int PERIOD_MS = 25;
		public static final int RELEASE_MS = 0;
		public static final int SIZE = 0;
	}
	
	
}

package papabench.scj.simulator.conf;

import javax.realtime.RealtimeThread;

import papabench.scj.commons.conf.CommonTaskConfiguration;

/**
 * 
 * @author Michal Malohlava
 *
 */
public interface PapaBenchSimulatorConf {
	
	public static interface SimulatorFlightModelTaskConf extends CommonTaskConfiguration {
		public static final String NAME = "SimulatorFlightModelTask";		
      /* Modified by Frank Zeyda */
		public static final int PRIORITY = /*RealtimeThread.MAX_RT_PRIORITY*/ - 12;
      /* End of Modification */
      public static final int PERIOD_MS = 25;
		public static final int RELEASE_MS = 0;
		public static final int SIZE = 0;
	}
	
	public static interface SimulatorGPSTaskConf extends CommonTaskConfiguration {
		public static final String NAME = "SimulatorGPSTask";		
      /* Modified by Frank Zeyda */
		public static final int PRIORITY = /*RealtimeThread.MAX_RT_PRIORITY*/ - 1;
      /* End of Modification */
		public static final int PERIOD_MS = 250;
		public static final int RELEASE_MS = 0;
		public static final int SIZE = 0;
	}
	
	public static interface SimulatorIRTaskConf extends CommonTaskConfiguration {
		public static final String NAME = "SimulatorIRTask";
      /* Modified by Frank Zeyda */
		public static final int PRIORITY = /*RealtimeThread.MAX_RT_PRIORITY*/ - 2;
      /* End of Modification */	
		public static final int PERIOD_MS = 50;
		public static final int RELEASE_MS = 0;
		public static final int SIZE = 0;
	}
}

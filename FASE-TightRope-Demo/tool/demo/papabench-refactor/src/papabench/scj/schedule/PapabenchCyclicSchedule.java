/**
 * 
 */
package papabench.scj.schedule;

import java.io.PrintStream;

import javax.realtime.RelativeTime;
import javax.safetycritical.CyclicSchedule;
import javax.safetycritical.PeriodicEventHandler;

import papabench.scj.autopilot.conf.PapaBenchAutopilotConf.ClimbControlTaskConf;
import papabench.scj.autopilot.conf.PapaBenchAutopilotConf.LinkFBWSendTaskConf;
import papabench.scj.autopilot.conf.PapaBenchAutopilotConf.NavigationTaskConf;
import papabench.scj.autopilot.conf.PapaBenchAutopilotConf.RadioControlTaskConf;
import papabench.scj.autopilot.conf.PapaBenchAutopilotConf.StabilizationTaskConf;
import papabench.scj.autopilot.tasks.AltitudeControlTask;
import papabench.scj.commons.conf.CommonTaskConfiguration;
import papabench.scj.fbw.conf.PapaBenchFBWConf.SendDataToAutopilotTaskConf;
import papabench.scj.fbw.conf.PapaBenchFBWConf.TestPPMTaskConf;
import papabench.scj.fbw.tasks.CheckFailsafeTask;
import papabench.scj.fbw.tasks.CheckMega128ValuesTask;
import papabench.scj.simulator.conf.PapaBenchSimulatorConf.SimulatorFlightModelTaskConf;
import papabench.scj.simulator.conf.PapaBenchSimulatorConf.SimulatorGPSTaskConf;
import papabench.scj.simulator.tasks.SimulatorFlightModelTask;
import papabench.scj.simulator.tasks.SimulatorIRTask;
import papabench.scj.utils.ParametersFactory;

/**
 * PapaBench specific schedule.
 * 
 * It contains autopilot module tasks, and also FBW module tasks which are allocated on different processor in the real paparazzi implementation.
 * The minor cycle of schedule takes 25ms, the major cycle length is 250ms.
 * 
 * TODO WARNING: we should check if: sum(C_i_j) <= 25ms to prove that time frames will not overrun
 *   - C_i is WCET for task 'j' running in time frame 'i'      
 * 
 * @author Michal Malohlava
 *
 */
//@SCJAllowed
public class PapabenchCyclicSchedule extends CyclicSchedule {
	
	// Minor cycle takes 25ms 
	// Each task period should be a multiple of this minor 
	public static final int MINOR_CYCLE_PERIOD = 25; // ms	
	public static final int MAJOR_CYCLE_PERIOD = 250; // ms
	public static final int NUMBER_OF_FRAMES = MAJOR_CYCLE_PERIOD / MINOR_CYCLE_PERIOD;
	
	// it would be nicer to generate it in the code below, however we can generate schedule by hand
	// tasks in array for each time frame should be sorted according to PRIORITY
	// WARNING !!!if you add a new task then reflects dependencies between tasks !!!
	public static final String[][] TIMELINE_SCHEDULE = {
		// 0 ms		
		{ SimulatorGPSTaskConf.NAME, SimulatorIRTask.NAME, TestPPMTaskConf.NAME, SendDataToAutopilotTaskConf.NAME, RadioControlTaskConf.NAME, NavigationTaskConf.NAME, AltitudeControlTask.NAME, ClimbControlTaskConf.NAME, StabilizationTaskConf.NAME, LinkFBWSendTaskConf.NAME, CheckFailsafeTask.NAME, CheckMega128ValuesTask.NAME, SimulatorFlightModelTaskConf.NAME }, // 250ms
		// 25ms
		{ TestPPMTaskConf.NAME, SendDataToAutopilotTaskConf.NAME, RadioControlTaskConf.NAME, SimulatorFlightModelTask.NAME }, // 25ms
		// 50ms
		{ SimulatorIRTask.NAME, TestPPMTaskConf.NAME, SendDataToAutopilotTaskConf.NAME, RadioControlTaskConf.NAME, StabilizationTaskConf.NAME, LinkFBWSendTaskConf.NAME, CheckFailsafeTask.NAME, CheckMega128ValuesTask.NAME, SimulatorFlightModelTaskConf.NAME }, // 50ms
		// 75 ms
		{ TestPPMTaskConf.NAME, SendDataToAutopilotTaskConf.NAME, RadioControlTaskConf.NAME, SimulatorFlightModelTask.NAME }, // 75ms
		// 100 ms
		{ SimulatorIRTask.NAME, TestPPMTaskConf.NAME, SendDataToAutopilotTaskConf.NAME, RadioControlTaskConf.NAME, StabilizationTaskConf.NAME, LinkFBWSendTaskConf.NAME, CheckFailsafeTask.NAME, CheckMega128ValuesTask.NAME, SimulatorFlightModelTaskConf.NAME  }, // 100ms
		// 125 ms
		{ TestPPMTaskConf.NAME, SendDataToAutopilotTaskConf.NAME, RadioControlTaskConf.NAME, SimulatorFlightModelTask.NAME }, // 125ms
		// 150 ms
		{ SimulatorIRTask.NAME, TestPPMTaskConf.NAME, SendDataToAutopilotTaskConf.NAME, RadioControlTaskConf.NAME, StabilizationTaskConf.NAME, LinkFBWSendTaskConf.NAME, CheckFailsafeTask.NAME, CheckMega128ValuesTask.NAME, SimulatorFlightModelTaskConf.NAME  }, // 150ms
		// 175 ms
		{ TestPPMTaskConf.NAME, SendDataToAutopilotTaskConf.NAME, RadioControlTaskConf.NAME, SimulatorFlightModelTask.NAME }, // 175ms
		// 200 ms
		{ SimulatorIRTask.NAME, TestPPMTaskConf.NAME, SendDataToAutopilotTaskConf.NAME, RadioControlTaskConf.NAME, StabilizationTaskConf.NAME, LinkFBWSendTaskConf.NAME, CheckFailsafeTask.NAME, CheckMega128ValuesTask.NAME, SimulatorFlightModelTaskConf.NAME  }, // 200ms
		// 225 ms
		{ TestPPMTaskConf.NAME, SendDataToAutopilotTaskConf.NAME, RadioControlTaskConf.NAME, SimulatorFlightModelTask.NAME }, // 225ms
	};
	
	/**
	 * Factory method to obtain a schedule for given periodic event handlers
	 *  
	 * @param peh
	 * @return
	 */
	//@SCJRestricted(Restric.INITIALIZATION)
//	@SCJAllowed
	public static PapabenchCyclicSchedule generateSchedule(PeriodicEventHandler[] pehs) {
		// CHECKME: is it ok to allocate schedule Frame here (MissionMemory scope) ?
		Frame[] frames = new Frame[NUMBER_OF_FRAMES];
		
		int frameStart = 0;
		for(int i = 0; i < NUMBER_OF_FRAMES; i++) {
			int pehsInFrame = TIMELINE_SCHEDULE[i].length;
			PeriodicEventHandler[] framePehs = new PeriodicEventHandler[pehsInFrame];
			
			for(int  j = 0; j < pehsInFrame; j++) {
				framePehs[j] = getPEHByName(pehs, TIMELINE_SCHEDULE[i][j]);				
			}
						
			RelativeTime offset = ParametersFactory.getRelativeTime(frameStart);			
			frames[i] = new Frame(offset, framePehs);			
			
			// next frame
			frameStart += MINOR_CYCLE_PERIOD;
		}
		
		// DEBUG
		printSchedule(frames, System.err);
				
		// compute number of frames and 
		return new PapabenchCyclicSchedule(frames);
	}
	
	protected static PeriodicEventHandler getPEHByName(PeriodicEventHandler[] pehs, String name) {
		for(int i = 0; i < pehs.length; i++) {
			// get a PEH by the given name
			// all PapaBench PEH has to implement interface CommonTaskConfiguration, else there is a bug in implementation
			if (((CommonTaskConfiguration) pehs[i]).getTaskName().equals(name)) {
				return pehs[i];
			}
		}
		
		throw new IllegalArgumentException("Schedule preparation failed - cannot find periodic event handler for name " + name);
	}
	
	public static void printSchedule(Frame[] frames, PrintStream os) {
		StringBuilder builder = new StringBuilder(frames.length * 50);
		builder.append("Schedule: \n");
		for (int i = 0; i < frames.length; i++) {
			Frame frame = frames[i];
			builder.append("\tFrame #").append(i).append(":").append(frame.getDuration().getMilliseconds()).append("ms\n");
			builder.append("\t\ttasks [ ");
			PeriodicEventHandler[] handlers = frame.getHandlers();
			for (int j = 0; j < handlers.length; j++) {
				
				builder.append(((CommonTaskConfiguration) handlers[j]).getTaskName()).append(" ");				
			}
			builder.append("]\n");
			
		}
		
		os.print(builder.toString());
	}
	
//	@SCJAllowed
	public PapabenchCyclicSchedule(Frame[] frames) {
		super(frames);		
	}	
}

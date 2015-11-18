
package cdx;

import java.io.PrintWriter;

import javax.realtime.AbsoluteTime;
import javax.realtime.Clock;
import javax.realtime.PriorityParameters;
import javax.realtime.PriorityScheduler;
import javax.realtime.RelativeTime;

//import javax.safetycritical.CyclicExecutive;
//import javax.safetycritical.CyclicSchedule;
import javax.safetycritical.Mission;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.Safelet;
//import javax.safetycritical.SingleMissionSequencer;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.annotate.Level;

/* Modified by Frank Zeyda */
public class Level1Safelet /*extends Mission*/ implements Safelet {
/* End of Modification */

//    public void setUp() {
    public void initializeApplication() {
        Constants.PRESIMULATE = true;
        new ImmortalEntry().run();
    }

    public void tearDown() {
        // RI Bug - runs too early
        dumpResults();
    }

   /* Added by Frank Zeyda */
   public MissionSequencer getSequencer() {
      MissionSequencer mSeq = new CDxMissionSequencer();
      return mSeq;
   }
   /* End of Addition */

   /* Commented out by Frank Zeyda. */

    /*public MissionSequencer getSequencer() {
    
        return new SingleMissionSequencer(
            new PriorityParameters( PriorityScheduler.instance().getMaxPriority() ),
            new StorageParameters( Constants.PERSISTENT_DETECTOR_SCOPE_SIZE + Constants.TRANSIENT_DETECTOR_SCOPE_SIZE, 10000, 10000),
            this
        ) {
            protected Mission getNextMission() {
                return null;
            }
        };
    }*/

   /* 

   /* The following three methods have been moved into a separate class
    * CDxMission for the mission rather than reusing the Level1Safelet
    * instance in this case. */

    /*protected void initialize() {

        try {            
            new CollisionDetectorHandler();

            if (Constants.DEBUG_DETECTOR) {
                System.out.println("Detector thread is " + Thread.currentThread());
                System.out
                    .println("Entering detector loop, detector thread priority is "
                            + +Thread.currentThread().getPriority() + " (NORM_PRIORITY is " + Thread.NORM_PRIORITY
                            + ", MIN_PRIORITY is " + Thread.MIN_PRIORITY + ", MAX_PRIORITY is " + Thread.MAX_PRIORITY
                            + ")");
            }

        } catch (Throwable e) {
            System.out.println("e: " + e.getMessage());
            e.printStackTrace();
        }
    }*/

	/*public void cleanup() {
    	System.err.println("Cleanup called.");
  	}*/

    /*public long missionMemorySize() {
        return Constants.PERSISTENT_DETECTOR_SCOPE_SIZE;
    }*/
   /* End of Commenting */

    public static void dumpResults() {
        
        String space = " ";
        String triZero = " 0 0 0 ";

        if (Constants.PRINT_RESULTS) {
            System.out
                .println("Dumping output [ timeBefore timeAfter heapFreeBefore heapFreeAfter detectedCollisions ] for "
                        + ImmortalEntry.recordedRuns + " recorded detector runs, in ns");
        }
        System.out.println("=====DETECTOR-STATS-START-BELOW====");
        for (int i = 0; i < ImmortalEntry.recordedRuns; i++) {
            System.out.print(ImmortalEntry.timesBefore[i]);
            System.out.print(space);
            System.out.print(ImmortalEntry.timesAfter[i]);
            System.out.print(space);
            System.out.print(ImmortalEntry.detectedCollisions[i]);
            System.out.print(space);
            System.out.print(ImmortalEntry.suspectedCollisions[i]);
            System.out.print(triZero);
            System.out.println(i);
        }

        System.out.println("=====DETECTOR-STATS-END-ABOVE====");

        System.out.println("Generated frames: " + Constants.MAX_FRAMES);
        System.out.println("Received (and measured) frames: " + ImmortalEntry.recordedRuns);
        System.out.println("Frame not ready event count (in detector): " + ImmortalEntry.frameNotReadyCount);
        System.out.println("Frames dropped due to full buffer in detector: " + ImmortalEntry.droppedFrames);
        System.out.println("Frames processed by detector: " + ImmortalEntry.framesProcessed);
        System.out.println("Reported missed detector periods (reported by waitForNextPeriod): "
                + ImmortalEntry.reportedMissedPeriods);
        System.out.println("Detector first release was scheduled for: "
                + NanoClock.asString(ImmortalEntry.detectorFirstRelease));
        // heap measurements

        // detector release times
        if (Constants.DETECTOR_RELEASE_STATS != "") {
            System.out.println("=====DETECTOR-RELEASE-STATS-START-BELOW====");
            for (int i = 0; i < ImmortalEntry.recordedDetectorReleaseTimes; i++) {
                System.out.print(ImmortalEntry.detectorReleaseTimes[i]);
                System.out.print(space);
                System.out.print(i * Constants.DETECTOR_PERIOD * 1000000L + ImmortalEntry.detectorReleaseTimes[0]);
                System.out.print(space);
                System.out.print(ImmortalEntry.detectorReportedMiss[i] ? 1 : 0);
                System.out.print(space);
                System.out.println(i);
            }
            System.out.println("=====DETECTOR-RELEASE-STATS-END-ABOVE====");
        }
    }
    
    public Level getLevel() {
        return Level.LEVEL_1;
    }

}

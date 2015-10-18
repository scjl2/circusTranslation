/**
 *  This file is part of miniCDx benchmark of oSCJ.
 *
 *   See: http://sss.cs.purdue.edu/projects/oscj/
 */
package cdx;

import java.io.DataOutputStream;

import javax.realtime.AbsoluteTime;
import javax.realtime.Clock;

/**
 * This thread runs only during start-up to run other threads. It runs in immortal memory, is allocated in immortal
 * memory, and it's constructor runs in immortal memory. It is a singleton, allocation from the Main class Ales: this
 * thread allocates - the scopes - the PersistentDetectorScope and TransientDetectorScope -
 */
public class ImmortalEntry implements Runnable {

    static public Object           initMonitor                  = new Object();
    static public boolean          detectorReady                = false;
    static public boolean          simulatorReady               = false;

    static public int              maxDetectorRuns;

    static public long             detectorFirstRelease         = -1;

    static public long[]           timesBefore;
    static public long[]           timesAfter;
    static public long[]           heapFreeBefore;
    static public long[]           heapFreeAfter;
    static public int[]            detectedCollisions;
    static public int[]            suspectedCollisions;
    static public AbsoluteTime	   releaseAt;

    static public long             detectorThreadStart;
    static public long[]           detectorReleaseTimes;
    static public boolean[]        detectorReportedMiss;

    static public int              reportedMissedPeriods        = 0;
    static public int              frameNotReadyCount           = 0;
    static public int              droppedFrames                = 0;
    static public int              framesProcessed              = 0;
    static public int              recordedRuns                 = 0;

    static public int              recordedDetectorReleaseTimes = 0;

    static public FrameBuffer      frameBuffer                  = null;

    static public DataOutputStream binaryDumpStream             = null;

    public ImmortalEntry() {
        maxDetectorRuns = Constants.MAX_FRAMES;

        timesBefore = new long[maxDetectorRuns];
        timesAfter = new long[maxDetectorRuns];
        heapFreeBefore = new long[maxDetectorRuns];
        heapFreeAfter = new long[maxDetectorRuns];
        detectedCollisions = new int[maxDetectorRuns];
        suspectedCollisions = new int[maxDetectorRuns];

        detectorReleaseTimes = new long[maxDetectorRuns + 10]; // the 10 is for missed deadlines
        detectorReportedMiss = new boolean[maxDetectorRuns + 10];
    }

    /** Called only once during initialization. Runs in immortal memory */
    public void run() {

        System.out.println("Detector: detector priority is " + Constants.DETECTOR_PRIORITY);
        System.out.println("Detector: detector period is " + Constants.DETECTOR_PERIOD);

        frameBuffer = new FrameBuffer();
        
        /* start the detector at rounded-up time, so that the measurements are not subject
         * to phase shift
         */
        
        detectorThreadStart = NanoClock.now();
        releaseAt = NanoClock.roundUp(Clock.getRealtimeClock().getTime().add(
                Constants.DETECTOR_STARTUP_OFFSET_MILLIS, 0));
        detectorFirstRelease = NanoClock.convert(releaseAt);
         
    }
}

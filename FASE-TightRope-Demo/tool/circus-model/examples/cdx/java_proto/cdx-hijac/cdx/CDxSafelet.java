package cdx;

import javax.safetycritical.MissionSequencer;
import javax.safetycritical.Safelet;
import javax.safetycritical.annotate.Level;

/* Imports from Level0Safelet */
import cdx.unannotated.NanoClock;
import immortal.Simulator;
/* End of Imports */

/* Class added by Frank Zeyda */

public class CDxSafelet implements Safelet {
    public CDxSafelet() { }

    /* Method from Level0Safelet */
    public void setup() {
        Constants.PRESIMULATE = true;
        new ImmortalEntry().run();
        new Simulator().generate();
    }
    /* End of Method */

    public MissionSequencer getSequencer() {
        return new CDxMissionSequencer();
    }

    /* Method from Level0Safelet */
    public void teardown() {
        dumpResults();
    }
    /* End of Method */

    /* Method from Level0Safelet */
    public static void dumpResults() {
        String space = " ";
        String triZero = " 0 0 0 ";

        if (Constants.PRINT_RESULTS) {
            System.out.println(
                "Dumping output [ timeBefore timeAfter heapFreeBefore heapFreeAfter detectedCollisions ] for " +
            ImmortalEntry.recordedRuns + " recorded detector runs, in ns");
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
        System.out.println("Received (and measured) frames: " +
            ImmortalEntry.recordedRuns);
        System.out.println("Frame not ready event count (in detector): " +
            ImmortalEntry.frameNotReadyCount);
        System.out.println("Frames dropped due to full buffer in detector: " +
            ImmortalEntry.droppedFrames);
        System.out.println("Frames processed by detector: " +
            ImmortalEntry.framesProcessed);
        // System.out.println("Detector stop indicator set: "
        // + ImmortalEntry.persistentDetectorScopeEntry.stop);
        System.out.println(
            "Reported missed detector periods (reported by waitForNextPeriod): "
            +
            ImmortalEntry.reportedMissedPeriods);
        System.out.println("Detector first release was scheduled for: " +
            NanoClock.asString(ImmortalEntry.detectorFirstRelease));
        // heap measurements
        Simulator.dumpStats();

        // detector release times
        if (Constants.DETECTOR_RELEASE_STATS != "") {
            System.out.println("=====DETECTOR-RELEASE-STATS-START-BELOW====");
            for (int i = 0; i < ImmortalEntry.recordedDetectorReleaseTimes; i++)
            {
                System.out.print(ImmortalEntry.detectorReleaseTimes[i]);
                System.out.print(space);
                System.out.print(i * Constants.DETECTOR_PERIOD * 1000000L +
                    ImmortalEntry.detectorReleaseTimes[0]);
                System.out.print(space);
                System.out.print(ImmortalEntry.detectorReportedMiss[i] ? 1 : 0);
                System.out.print(space);
                System.out.println(i);
            }
            System.out.println("=====DETECTOR-RELEASE-STATS-END-ABOVE====");
        }
    }
    /* End of Method */

    public Level getLevel() {
        return Level.LEVEL_1;
    }
}

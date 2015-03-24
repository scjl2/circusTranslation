package cdx;

import javax.safetycritical.AperiodicEvent;
import javax.realtime.MemoryArea;
import javax.safetycritical.Mission;

import javax.realtime.AbsoluteTime;
import javax.realtime.Clock;
import javax.realtime.RelativeTime;
import cdx.unannotated.NanoClock;

/* Class added by Frank Zeyda */

public class CDxMission extends Mission {
    /*public RawFrame currentFrame;*/
    /*public StateTable state;*/
    public final Partition work;
    public int collisions;
    /*public DetectorControl control;*/

    public CDxMission() {
        work = new Partition();
        collisions = 0;
    }

    public void initBenchmark() {
        /* Fragment from Level0Safelet */
        try {
            ImmortalEntry.detectorThreadStart = NanoClock.now();
            AbsoluteTime releaseAt =
                NanoClock.roundUp(Clock.getRealtimeClock().getTime().add(
                    Constants.DETECTOR_STARTUP_OFFSET_MILLIS, 0));
            ImmortalEntry.detectorFirstRelease = NanoClock.convert(releaseAt);
            /* Commented out by Frank Zeyda */
            //new CollisionDetectorHandler();
            /* End of Modification */

            if (Constants.DEBUG_DETECTOR) {
                System.out.println(
                    "Detector thread is " + Thread.currentThread());
                System.out.println(
                    "Entering detector loop, detector thread priority is " +
                    Thread.currentThread().getPriority() +
                    " (NORM_PRIORITY is " + Thread.NORM_PRIORITY +
                    ", MIN_PRIORITY is " + Thread.MIN_PRIORITY +
                    ", MAX_PRIORITY is " + Thread.MAX_PRIORITY + ")");
            }
        } catch (Throwable e) {
            System.out.println("e: " + e.getMessage());
            e.printStackTrace();
        }
        /* End of Fragment */
    }

    public void initialize() {
        initBenchmark();
        OutputHandler h1 = new OutputHandler(this);
        /*h1.register();*/
        AperiodicEvent output = new AperiodicEvent(h1);
        DetectorControl control = new DetectorControl(output);
        DetectorHandler[] hdls = new DetectorHandler[Constants.DETECTORS];
        for (int i = 1; i <= Constants.DETECTORS; i++) {
            hdls[i - 1] = new DetectorHandler(i, this, control);
            /*hdls[i - 1].register();*/
        }
        AperiodicEvent detect = new AperiodicEvent(hdls);
        ReducerHandler h2 = new ReducerHandler(this, detect, control);
        /*h2.register();*/
        AperiodicEvent reduce = new AperiodicEvent(h2);
        InputHandler h3 = new InputHandler(/*this, */ reduce);
        /*h3.register();*/
    }

    /* Method from the original CollisionDetectorHandler */
    public void cleanup() { }
    /* End of Method */

    /* Method from Level0Safelet */
    public long missionMemorySize() {
        return Constants.PERSISTENT_DETECTOR_SCOPE_SIZE;
    }
    /* End of Method */

    public Partition getWork() {
        return work;
    }

    /* Methods used to record the results of the detector handlers. */

    public synchronized void clearCollisions() {
        collisions = 0;
    }

    public synchronized void recordCollisions(int n) {
        collisions += n;
    }

    public synchronized int getCollisions() {
        return collisions;
    }
}

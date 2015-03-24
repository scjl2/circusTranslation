/**
 *  This file is part of miniCDx benchmark of oSCJ.
 *
 *   See: http://sss.cs.purdue.edu/projects/oscj/
 */
package cdx;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.RelativeTime;

import javax.safetycritical.Mission;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;


public class CollisionDetectorHandler extends PeriodicEventHandler {
    private final TransientDetectorScopeEntry cd = new TransientDetectorScopeEntry(
            new StateTable(), Constants.GOOD_VOXEL_SIZE);
    public final NoiseGenerator noiseGenerator = new NoiseGenerator();

    public boolean stop = false;

    public CollisionDetectorHandler() {

      super(new PriorityParameters(Constants.DETECTOR_PRIORITY),
            new PeriodicParameters(ImmortalEntry.releaseAt /* start */, new RelativeTime(Constants.DETECTOR_PERIOD, 0) /*period*/),
            null /* storage parameters */);//,
//            Constants.TRANSIENT_DETECTOR_SCOPE_SIZE);
    }

    public void runDetectorInScope(final TransientDetectorScopeEntry cd) {
        Benchmarker.set(14);

        final RawFrame f = cdx.ImmortalEntry.frameBuffer.getFrame();

        if (f == null) {
            ImmortalEntry.frameNotReadyCount++;
            System.out.println("Frame not ready");
            Benchmarker.done(14);
            return;
        }

        if ((cdx.ImmortalEntry.framesProcessed + cdx.ImmortalEntry.droppedFrames) == cdx.Constants.MAX_FRAMES) {
            stop = true;
            Benchmarker.done(14);
            return;
        } // should not be needed, anyway

        final long heapFreeBefore = Runtime.getRuntime().freeMemory();
        final long timeBefore = NanoClock.now();

        noiseGenerator.generateNoiseIfEnabled();
        Benchmarker.set(Benchmarker.RAPITA_SETFRAME);
        cd.setFrame(f);
        Benchmarker.done(Benchmarker.RAPITA_SETFRAME);
        // actually runs the detection logic in the given scope
        cd.run();
        
        final long timeAfter = NanoClock.now();
        final long heapFreeAfter = Runtime.getRuntime().freeMemory();

        if (ImmortalEntry.recordedRuns < ImmortalEntry.maxDetectorRuns) {
            ImmortalEntry.timesBefore[ImmortalEntry.recordedRuns] = timeBefore;
            ImmortalEntry.timesAfter[ImmortalEntry.recordedRuns] = timeAfter;
            ImmortalEntry.heapFreeBefore[ImmortalEntry.recordedRuns] = heapFreeBefore;
            ImmortalEntry.heapFreeAfter[ImmortalEntry.recordedRuns] = heapFreeAfter;
            ImmortalEntry.recordedRuns++;
        }

        cdx.ImmortalEntry.framesProcessed++;

        if ((cdx.ImmortalEntry.framesProcessed + cdx.ImmortalEntry.droppedFrames) == cdx.Constants.MAX_FRAMES) {
            stop = true;
        }
        Benchmarker.done(14);
    }

    /* Added by Frank Zeyda */
    public void handleEvent() { }
    /* End of Addition */

    public void handleAsyncEvent() {

        if (!stop) {
            long now = NanoClock.now();
            ImmortalEntry.detectorReleaseTimes[ImmortalEntry.recordedDetectorReleaseTimes] = now;
            ImmortalEntry.detectorReportedMiss[ImmortalEntry.recordedDetectorReleaseTimes] = false;
            ImmortalEntry.recordedDetectorReleaseTimes++;

            runDetectorInScope(cd);
        } else {
            Mission.getCurrentMission().requestSequenceTermination();
        }
    }

    //@Override
    public void cleanUp() {
        
    }


    //@Override
    public StorageParameters getThreadConfigurationParameters() {
        return null;
    }
}

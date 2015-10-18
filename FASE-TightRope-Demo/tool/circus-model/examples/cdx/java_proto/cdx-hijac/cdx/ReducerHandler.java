/**
 *  This file is part of miniCDx benchmark of oSCJ.
 *
 *   miniCDx is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   miniCDx is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with miniCDx.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 *   Copyright 2009, 2010
 *   @authors  Daniel Tang, Ales Plsek
 *
 *   See: http://sss.cs.purdue.edu/projects/oscj/
 */
package cdx;

import javax.safetycritical.Mission;
import javax.safetycritical.PeriodicEventHandler;
/* Added by Frank Zeyda */
import javax.safetycritical.AperiodicEventHandler;
import javax.realtime.AperiodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.PriorityScheduler;
import javax.safetycritical.AperiodicEvent;
/* End of Addition */
/* Modified by Frank Zeyda */
import javax.safetycritical.StorageConfigurationParameters;
/* End of Modification */

import cdx.unannotated.NanoClock;

/* This was the original DetectorHandler class of the CDx. */

/*@javax.safetycritical.annotate.Scope("cdx.Level0Safelet")*/
/*@javax.safetycritical.annotate.RunsIn("cdx.ReducerHandler")*/
/* Modified by Frank Zeyda */
public class ReducerHandler extends AperiodicEventHandler {
/* End of Modification */
    /* Added by Frank Zeyda */
    public final CDxMission mission;
    public final AperiodicEvent detect;
    public final DetectorControl control;
    /* End of Addition */

    private final TransientDetectorScopeEntry cd =
        new TransientDetectorScopeEntry(
        new StateTable(), Constants.GOOD_VOXEL_SIZE);
    public final NoiseGenerator noiseGenerator = new NoiseGenerator();

    public boolean stop = false;

    /* Modified by Frank Zeyda */
    public ReducerHandler(
            CDxMission mission,
            AperiodicEvent event,
            DetectorControl control) {
        // these very large limits are reported to work with stack traces... of
        // errors encountered...
        // most likely they are unnecessarily large
        super(
            new PriorityParameters(
                PriorityScheduler.instance().getNormPriority()),
            new AperiodicParameters(),
            new StorageConfigurationParameters(32768, 4096, 4096),
            Constants.TRANSIENT_DETECTOR_SCOPE_SIZE, "ReducerHandler");
        this.mission = mission;
        this.detect = event;
        this.control = control;
    }
    /* End of Modification */

    public void runDetectorInScope(final TransientDetectorScopeEntry cd) {
        Benchmarker.set(14);

        final RawFrame f = cdx.ImmortalEntry.frameBuffer.getFrame();
        if (f == null) {
            ImmortalEntry.frameNotReadyCount++;
            System.out.println("Frame not ready");
            Benchmarker.done(14);
            return;
        }

        if ((cdx.ImmortalEntry.framesProcessed +
             cdx.ImmortalEntry.droppedFrames) == cdx.Constants.MAX_FRAMES) {
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
            ImmortalEntry.heapFreeBefore[ImmortalEntry.recordedRuns] =
                heapFreeBefore;
            ImmortalEntry.heapFreeAfter[ImmortalEntry.recordedRuns] =
                heapFreeAfter;
            ImmortalEntry.recordedRuns++;
        }

        cdx.ImmortalEntry.framesProcessed++;

        if ((cdx.ImmortalEntry.framesProcessed +
             cdx.ImmortalEntry.droppedFrames) == cdx.Constants.MAX_FRAMES)
            stop = true;
        Benchmarker.done(14);
    }

    public void handleEvent() {
        if (Constants.TEST_DETECTOR) {
            System.out.println("[ReducerHandler] handleEvent() called");
        }
        try {
            if (!stop) {
                long now = NanoClock.now();
                ImmortalEntry.detectorReleaseTimes[
                    ImmortalEntry.recordedDetectorReleaseTimes] = now;
                ImmortalEntry.detectorReportedMiss[
                    ImmortalEntry.recordedDetectorReleaseTimes] = false;
                ImmortalEntry.recordedDetectorReleaseTimes++;

                runDetectorInScope(cd);
                /* Added by Frank Zeyda */
                control.start();
                /* Release all DetectorHandlers */
                detect.fire();
                /* End of Addition */
            } else {
                Mission.getCurrentMission().requestSequenceTermination();
            }
        } catch (Throwable e) {
            System.out.println("Exception thrown by runDetectorInScope: " +
                e.getMessage());
            e.printStackTrace();
        }
    }

    //@Override
    public void cleanUp() {
        // TODO Auto-generated method stub
    }

    //@Override
    public void register() {
        // TODO Auto-generated method stub
    }

    //@Override
    /* Modified by Frank Zeyda */
    public StorageConfigurationParameters getThreadConfigurationParameters() {
    /* End of Modification */
        // TODO Auto-generated method stub
        return null;
    }
}

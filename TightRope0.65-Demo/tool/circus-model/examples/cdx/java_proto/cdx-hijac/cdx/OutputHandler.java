package cdx;

import javax.realtime.AperiodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.PriorityScheduler;

import javax.safetycritical.AperiodicEventHandler;
import javax.safetycritical.StorageConfigurationParameters;

/* Class added by Frank Zeyda */

public class OutputHandler extends AperiodicEventHandler {
    public static final String TEST_ERROR =
        "Parellel computation of detected collisions gave inconsistent result.";
    
    public CDxMission mission;

    public OutputHandler(CDxMission mission) {
        super(
            new PriorityParameters(
                PriorityScheduler.instance().getMaxPriority()),
            new AperiodicParameters(),
            new StorageConfigurationParameters(32768, 4096, 4096),
            65536, "OutputHandler");
        this.mission = mission;
    }

    public void handleEvent() {
        if (Constants.TEST_DETECTOR) {
            System.out.println("[OutputHandler] called");
        }
        if (Constants.TEST_DETECTOR) {
            int oracle =
                ImmortalEntry.detectedCollisions[ImmortalEntry.recordedRuns-1];
            if (mission.getCollisions() != oracle) {
                System.out.println(TEST_ERROR);
                System.exit(-1);
            }
        }
        else {
            ImmortalEntry.detectedCollisions[ImmortalEntry.recordedRuns - 1] =
                mission.getCollisions();
        }
        /* TODO: Output collisions to external device. */
    }
}

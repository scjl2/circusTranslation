package cdx;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.PriorityScheduler;
import javax.realtime.RelativeTime;

import javax.safetycritical.AperiodicEvent;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageConfigurationParameters;

/* Class added by Frank Zeyda */

public class InputHandler extends PeriodicEventHandler {
    public final AperiodicEvent reduce;

    public InputHandler(AperiodicEvent event) {
        super(
            new PriorityParameters(
                PriorityScheduler.instance().getMaxPriority()),
            new PeriodicParameters(null,
                new RelativeTime(Constants.DETECTOR_PERIOD, 0)),
            new StorageConfigurationParameters(32768, 4096, 4096),
            65536, "InputHandler");
        this.reduce = event;
    }

    public void handleEvent() {
        if (Constants.TEST_DETECTOR) {
            System.out.println("[InputHandler] handleEvent() called");
        }
        /* TODO: Read frame from external device and insert into FrameBuffer. */
        /* Release ReducerHandler */
        reduce.fire();
    }
}

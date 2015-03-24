package cdx;

import javax.safetycritical.AperiodicEvent;

/* Class added by Frank Zeyda */

public class DetectorControl {
    protected final boolean[] handler_done;
    protected final AperiodicEvent output;

    public DetectorControl(AperiodicEvent event) {
        handler_done = new boolean[Constants.DETECTORS];
        output = event;
    }

    public synchronized void start() {
        for (int index = 0; index < handler_done.length; index++) {
            handler_done[index] = false;
        }
    }

    public synchronized void notify(int index) {
        handler_done[index-1] = true;
        if (done()) {
            /* Release output handler if all work is done! */
            output.fire();
        }
    }

    protected synchronized boolean done() {
        for (int index = 0; index < handler_done.length; index++) {
            if (!handler_done[index]) { return false; }
        }
        return true;
    }
}

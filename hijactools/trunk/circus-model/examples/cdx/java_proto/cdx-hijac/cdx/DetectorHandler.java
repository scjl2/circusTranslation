package cdx;

import collision.Vector3d;

import javax.realtime.AperiodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.PriorityScheduler;

import javax.safetycritical.AperiodicEventHandler;
import javax.safetycritical.StorageConfigurationParameters;

import javacp.util.Iterator;
import javacp.util.LinkedList;
import javacp.util.List;

/* Class added by Frank Zeyda */

public class DetectorHandler extends AperiodicEventHandler {
    public final CDxMission mission;
    public final DetectorControl control;
    public final int id;

    public DetectorHandler(int id,
        CDxMission mission,
        DetectorControl control) {
        super(
            new PriorityParameters(
                PriorityScheduler.instance().getNormPriority()),
            new AperiodicParameters(),
            new StorageConfigurationParameters(32768, 4096, 4096),
            Constants.TRANSIENT_DETECTOR_SCOPE_SIZE, "DetectorHandler");
        this.id = id;
        this.mission = mission;
        this.control = control;
    }

    /* Method from TransientDetectorScopeEntry */
    public int determineCollisions(final List motions, List ret) {
        // (Peta) changed to iterators so that it's not killing the algorithm
        Benchmarker.set(5);
        int _ret = 0;
        Motion[] _motions =
            (Motion[]) motions.toArray(new Motion[motions.size()]);
        // Motion[] _motions= (Motion)motions.toArray();
        for (int i = 0; i < _motions.length - 1; i++) {
            final Motion one = _motions[i]; // m2==two, m=one
            for (int j = i + 1; j < _motions.length; j++) {
                final Motion two = _motions[j];
                // if (checkForDuplicates(ret, one, two)) { // This is removed
                // because it is very very slow...
                final Vector3d vec = one.findIntersection(two);
                if (vec != null) {
                    ret.add(new Collision(
                        one.getAircraft(), two.getAircraft(), vec));
                    _ret++;
                }
                // }
            }
        }
        Benchmarker.done(5);
        return _ret;
        /*
         * Old code: Benchmarker.set(5); int _ret=0; for (int i = 0; i <
         * motions.size() - 1; i++) { final Motion one = (Motion)
         * motions.get(i); //m2==two, m=one for (int j = i + 1; j <
         * motions.size(); j++) { final Motion two = (Motion) motions.get(j); if
         * (checkForDuplicates(ret, one, two)) { final Vector3d vec =
         * one.findIntersection(two); if (vec != null) { ret.add(new
         * Collision(one.getAircraft(), two.getAircraft(), vec)); _ret++; }
         *
         * } } } Benchmarker.done(5); return _ret; //
         */
    }
    /* End of Method */

    public void handleEvent() {
        if (Constants.TEST_DETECTOR) {
            System.out.println("[DetectorHandler] (id=" + id + ") called");
        }
        List check = mission.getWork().getDetectorWork(id);
        /* Fragment from TransientDetectorScopeEntry */
        int c = 0;
        final List ret = new LinkedList();
        for (final Iterator iter = check.iterator(); iter.hasNext();)
            c += determineCollisions((List) iter.next(), ret);
        /* End of Fragment */
        /* Fragment from TransientDetectorScopeEntry */
        if (Constants.TEST_DETECTOR) {
            System.out.println("Collisions detected by DetectorHandler" +
                " (id=" + id + "): " + c);
        }
        /* End of Fragment */
        mission.recordCollisions(c);
        control.notify(id);
    }
}

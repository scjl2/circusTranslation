/**
 * @author Frank Zeyda, Kun Wei
 */
package cdx;

import javax.realtime.PriorityParameters;

import javax.safetycritical.AperiodicEvent;
import javax.safetycritical.AperiodicEventHandler;
import javax.safetycritical.AperiodicParameters;
import javax.safetycritical.PriorityScheduler;
import javax.safetycritical.StorageParameters;

import javacp.util.Iterator;
import javacp.util.List;

/**
 * DetectorHandler instances carry out the actual collisions detection.
 */
public class DetectorHandler extends AperiodicEventHandler {
  public final CDxMission mission;
  public final DetectorControl control;
  public final int id;

  public DetectorHandler(
      CDxMission mission,
      DetectorControl control,
      int id,
      AperiodicEvent event_bound) {
    super(
      new PriorityParameters(Constants.DETECTOR_PRIORITY),
      new AperiodicParameters(null, null),
      new StorageParameters(32768, 4096, 4096),
      event_bound, "DetectorHandler");
    this.mission = mission;
    this.control = control;
    this.id = id;
  }

  public void handleEvent() {
    /* Local variable to hold the collisions result for the partition. */
    int colls;
   
    /* Calculate the number of collisions for this detector's work. */
    colls = CalcPartCollisions();

    /* Record collisions result with the mission. */
    mission.recColls(colls);

    /* Notify DetectorControl that this handler has finished. */
    control.notify(id);
  }

  /* Implementation of the CalcPartCollisions action in the S anchor. */

  public int CalcPartCollisions() {
    int colls = 0;

    /* Get work for this detector via the shared Partition object. */
    List work = mission.work.getDetectorWork(id);

    for (Iterator iter = work.iterator(); iter.hasNext(); ) {
        colls += determineCollisions((List) iter.next());
    }

    return colls;
  }

  /* Compute the number of collisions for a List of Motion objects. */

  public int determineCollisions(final List motions) {
    int colls = 0;
    for (int i = 0; i < motions.size() - 1; i++) {
      Motion one = (Motion) motions.get(i);
      for (int j = i + 1; j < motions.size(); j++) {
        Motion two = (Motion) motions.get(j);
        Vector3d v = one.findIntersection(two);
        if (v != null) { colls++; }
      }
    }
    return colls;
  }
}

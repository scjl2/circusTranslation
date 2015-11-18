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

/**
 * OutputCollisionsHandler outputs the number of detected collisions to an
 * external device. For the purpose of the simulation, we merely print it on
 * the screen.
 */
public class OutputCollisionsHandler extends AperiodicEventHandler {
  public final CDxMission mission;

  public OutputCollisionsHandler(
      CDxMission missionArg,
      AperiodicEvent event_bound) {
    super(
      new PriorityParameters(
        PriorityScheduler.instance().getMaxPriority()),
      new AperiodicParameters(null, null),
      new StorageParameters(32768, 4096, 4096),
      event_bound, "OutputCollisionsHandler");
    mission = missionArg;
  }

  public void handleAsyncEvent() {
    /* The the collisions result form the CDxMission class. */
    int colls = mission.getColls();

    /* In a real system write collisions to an external device here. */
    System.out.println(colls + " collisions have been detected!");
  }
}

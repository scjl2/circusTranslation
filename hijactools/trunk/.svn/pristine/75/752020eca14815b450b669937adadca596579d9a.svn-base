/**
 * @author Frank Zeyda, Kun Wei
 */
package cdx;

import javax.realtime.AperiodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.PriorityScheduler;

import javax.safetycritical.AperiodicEventHandler;
import javax.safetycritical.StorageConfigurationParameters;

/**
 * OutputCollisionsHandler outputs the number of detected collisions to an
 * external device. For the purpose of the simulation, we merely print it on
 * the screen.
 */
public class OutputCollisionsHandler extends AperiodicEventHandler {
  public final CDxMission mission;

  public OutputCollisionsHandler(CDxMission mission) {
    super(
      new PriorityParameters(
        PriorityScheduler.instance().getMaxPriority()),
      new AperiodicParameters(),
      new StorageConfigurationParameters(32768, 4096, 4096),
      65536, "OutputHandler");
    this.mission = mission;
  }

  public void handleEvent() {
    System.out.println("[OutputHandler] called");

    /* The the collisions result form the CDxMission class. */
    int colls = mission.getColls();

    System.out.println(colls + " collisions have been detected!");

    /* In a real program: write collisions to an external device. */

    /* Signal to InputFrameHandler we are ready to process the next frame. */
    mission.simulator.detectorReady = true;
  }
}

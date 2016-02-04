/**
 * @author Frank Zeyda
 */
package cdx;

import javax.realtime.PriorityParameters;

import javax.safetycritical.Mission;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.PriorityScheduler;
import javax.safetycritical.StorageParameters;

/**
 * The mission sequencer of the parallel CDx application.
 *
 * It returns an instance of CDxMission. We note that in a real system this
 * mission does not terminate.
 */
public class CDxMissionSequencer extends MissionSequencer {
  public boolean mission_done;

  public CDxMissionSequencer() {
    super(
      new PriorityParameters(
        PriorityScheduler.instance().getNormPriority()),
      new StorageParameters(131072, 4096, 4096));
    mission_done = false;
  }

  public Mission getNextMission() {
    if (!mission_done) {
      mission_done = true;
      return new CDxMission();
    }
    else {
      return null;
    }
  }
}

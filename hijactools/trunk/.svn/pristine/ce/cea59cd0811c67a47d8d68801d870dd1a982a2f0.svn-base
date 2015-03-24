import javax.realtime.PriorityParameters;

import javax.safetycritical.Mission;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.PriorityScheduler;
import javax.safetycritical.StorageParameters;

import javax.safetycritical.annotate.*;

public class MainMissionSequencer extends MissionSequencer {

  private boolean mission_done;

  public MainMissionSequencer() {
    super(
      new PriorityParameters(
        PriorityScheduler.instance().getNormPriority()),
      new StorageParameters(131072, 4096, 4096));
    mission_done = false;
  }

  protected Mission getNextMission() {
    if (!mission_done) {
      mission_done = true;
      return new MyMission();
    }
    else { return null; }
  }
}

package accs;

import javax.realtime.PriorityParameters;

import javax.safetycritical.Mission;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.PriorityScheduler;
import javax.safetycritical.StorageParameters;

import javax.safetycritical.annotate.*;

public class ACCMissionSequencer extends MissionSequencer {
  private boolean mission_done;

  public ACCMissionSequencer() {
    super(
      new PriorityParameters(
        PriorityScheduler.instance().getNormPriority()),
      new StorageParameters(131072, 4096, 4096));
    mission_done = false;
  }

  protected Mission getNextMission() {
    if (!mission_done) {
      mission_done = true;
      ACCMission mission = new ACCMission();
      return mission;
    }
    else { return null; }
  }
}

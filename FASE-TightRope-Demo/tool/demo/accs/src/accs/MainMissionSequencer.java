package accs;

import javax.realtime.*;

import javax.safetycritical.*;

public class MainMissionSequencer extends MissionSequencer {
  private boolean mission_done;

  public MainMissionSequencer() {
    super(new PriorityParameters(5), null);
    mission_done = false;
  }

  protected Mission getNextMission() {
    if (!mission_done) {
      mission_done = true;
      return new MainMission();
    }
    else {
      return null;
    }
  }

  /* The following method is not part of SCJ (Andy Wellings). */

  protected Mission getInitialMission() {
    mission_done = true;
    return new MainMission();
  }
}

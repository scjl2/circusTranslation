import javax.realtime.*;

import javax.safetycritical.*;
import javax.safetycritical.annotate.*;

class MainMissionSequencer extends MissionSequencer {
  private int counter;

  public MainMissionSequencer() {
    super(new PriorityParameters(20), null);
    counter = 1;
  }

  public Mission getNextMission() {
    if (counter == 1) {
      counter++;
      return new Mission1();
    }
    if (counter == 2) {
      counter++;
      return new Mission2();
    }
    if (counter == 3) {
      counter++;
      return new Mission3();
    }
    return null;
  }
}

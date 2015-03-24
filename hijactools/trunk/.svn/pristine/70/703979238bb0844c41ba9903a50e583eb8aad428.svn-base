import javax.realtime.*;

import javax.safetycritical.*;
import javax.safetycritical.annotate.*;

class MainSafelet implements Safelet {
  public void setup() {
    System.out.println("MainSafelet::setup() called");
  }

  public MissionSequencer getSequencer() {
    return new MainMissionSequencer();
  }

  public void teardown() {
    System.out.println("MainSafelet::teardown() called");
  }

  public Level getLevel() {
    return Level.LEVEL_1;
  }
}

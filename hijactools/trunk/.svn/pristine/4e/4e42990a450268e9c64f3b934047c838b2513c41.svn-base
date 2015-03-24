import javax.realtime.*;

import javax.safetycritical.*;
import javax.safetycritical.annotate.*;

class Mission1 extends Mission {
  public void initialize() {
    System.out.println("Mission1::initialize() called");
    PeriodicEventHandler h2 = new Handler2();
  }

  public void cleanup() {
    System.out.println("Mission1::cleanup() called");
  }

  public long missionMemorySize() {
    return 131072;
  }

  public Level getLevel() {
    return Level.LEVEL_1;
  }
}

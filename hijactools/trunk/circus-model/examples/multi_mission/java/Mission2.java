import javax.realtime.*;

import javax.safetycritical.*;
import javax.safetycritical.annotate.*;

class Mission2 extends Mission {
  public void initialize() {
    System.out.println("Mission2::initialize() called");
    AperiodicEventHandler h1 = new Handler1();
    AperiodicEvent event = new AperiodicEvent(h1);
    PeriodicEventHandler h2 = new Handler3(event);
  }

  public void cleanup() {
    System.out.println("Mission2::cleanup() called");
  }

  public long missionMemorySize() {
    return 131072;
  }

  public Level getLevel() {
    return Level.LEVEL_1;
  }
}

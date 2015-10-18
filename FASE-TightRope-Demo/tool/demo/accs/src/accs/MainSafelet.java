package accs;

import javax.realtime.*;

import javax.safetycritical.*;

import javax.safetycritical.annotate.Level;

public class MainSafelet implements Safelet {
  public void setup() { }

  public void teardown() { }

  public Level getLevel() {
    return Level.LEVEL_1;
  }

  public MissionSequencer getSequencer() {
    return new MainMissionSequencer();
  }
}

package SCJChecker;

import javax.safetycritical.Safelet;
import javax.safetycritical.MissionSequencer;

import javax.safetycritical.annotate.*;

public class MainSafelet implements Safelet {
  public MainSafelet() { }

  public void setup() { }

  public void teardown() { }

  public MissionSequencer getSequencer() {
    return new MainMissionSequencer();
  }
}

package accs;

import javax.safetycritical.Safelet;
import javax.safetycritical.MissionSequencer;

import javax.safetycritical.annotate.*;

public class ACCSafelet implements Safelet {
  public ACCSafelet() { }

  public void initializeApplication() { }

  public void setup() { }

  public void teardown() { }

  public MissionSequencer getSequencer() {
    ACCMissionSequencer mSequencer = new ACCMissionSequencer();
    return mSequencer;
  }

  public int myTestMethod() {
        int test = 10;
        return test;
  }
}

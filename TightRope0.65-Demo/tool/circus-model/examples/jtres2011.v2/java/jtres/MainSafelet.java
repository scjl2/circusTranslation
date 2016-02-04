package jtres;

import javax.safetycritical.MissionSequencer;
import javax.safetycritical.Safelet;

public class MainSafelet implements Safelet {
   public void setUp() { }

   public MissionSequencer getSequencer() {
      /* Created in Immortal Memory */
      return new MainMissionSequencer();
   }

   public void tearDown() { }
}

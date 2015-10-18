import javax.safetycritical.MissionSequencer;
import javax.safetycritical.Safelet;
import javax.safetycritical.annotate.Level;

public class MainSafelet implements Safelet {
   public void setup() { }

   public void teardown() { }

   public MissionSequencer getSequencer() {
      return new MainMissionSequencer();
   }

   public Level getLevel() {
      return Level.LEVEL_1;
   }
}

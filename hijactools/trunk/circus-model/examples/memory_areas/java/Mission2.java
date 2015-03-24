import javax.safetycritical.Mission;

public class Mission2 extends Mission {
   public void initialize() {
      new Handler2();
   }

   public void cleanup() { }

   public long missionMemorySize() {
      return 131072;
   }
}

import javax.safetycritical.Mission;

public class Mission3 extends Mission {
   public void initialize() {
      new Handler3();
   }

   public void cleanup() { }

   public long missionMemorySize() {
      return 131072;
   }
}

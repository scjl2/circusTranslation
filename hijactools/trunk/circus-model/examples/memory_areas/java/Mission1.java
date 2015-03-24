import javax.safetycritical.Mission;

public class Mission1 extends Mission {
   public Handler1 handler;

   public void initialize() {
      handler = new Handler1();
   }

   public void cleanup() { }

   public long missionMemorySize() {
      return 131072;
   }
}

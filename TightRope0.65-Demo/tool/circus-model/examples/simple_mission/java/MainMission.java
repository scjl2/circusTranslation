import javax.realtime.AbsoluteTime;

import javax.safetycritical.Mission;

public class MainMission extends Mission {
   //public static AbsoluteTime[] releaseTimes = new AbsoluteTime[10];;

   public void initialize() {
      //new MainHandler(releaseTimes);
      new MainHandler();
   }

   public void cleanup() {
      /*for (int index = 0; index < releaseTimes.length; index++) {
         System.out.println("Handler released at " + releaseTimes[index]);
      }*/
   }

   public long missionMemorySize() {
      return 131072;
   }
}

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.PriorityScheduler;
import javax.realtime.RelativeTime;

import javax.safetycritical.Mission;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageConfigurationParameters;

public class MainHandler extends PeriodicEventHandler {
   public int counter;

   public MainHandler() {
      super(
         new PriorityParameters(
            PriorityScheduler.instance().getNormPriority()),
         new PeriodicParameters(null, new RelativeTime(100, 0)),
         new StorageConfigurationParameters(32768, 4096, 4096),
         65536, "MainHandler");
      counter = 0;
   }

   public void handleEvent() {
      System.out.println("[MainHandler] handleEvent() called");
      counter++;
      if (counter == 10) {
         Mission.getCurrentMission().requestTermination();
      }
   }
}

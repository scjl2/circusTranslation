import javax.realtime.ImmortalMemory;
import javax.realtime.MemoryArea;
import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.PriorityScheduler;
import javax.realtime.RealtimeThread;
import javax.realtime.RelativeTime;

import javax.safetycritical.Mission;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageConfigurationParameters;

public class Handler3 extends PeriodicEventHandler {
   public boolean done = false;

   public Handler3() {
      super(
         new PriorityParameters(
            PriorityScheduler.instance().getNormPriority()),
         new PeriodicParameters(null, new RelativeTime(100, 0)),
         new StorageConfigurationParameters(32768, 4096, 4096),
         65536, "Handler3");
   }

   public void handleEvent() {
      System.out.println("[Handler3] handleEvent() called");
      if (!done) {
         MemoryArea area = RealtimeThread.getCurrentMemoryArea();
         Handler3_MemoryEntry entry = new Handler3_MemoryEntry(this);
         area.executeInArea(entry);
         done = true;
      }
      else {
         System.out.println("[Handler3] requesting termination");
         Mission.getCurrentMission().requestTermination();         
      }
   }

   /*class MemoryEntry implements Runnable {
      public Handler3 outer;

      public MemoryEntry(Handler3 outer) {
         this.outer = outer;
      }

      public void run() {
         System.out.println("[MemoryEntry] run() callled");
         System.out.println("[MemoryEntry] inside " +
            RealtimeThread.getCurrentMemoryArea().getClass());
         new Integer(42);
      }
   }*/
}

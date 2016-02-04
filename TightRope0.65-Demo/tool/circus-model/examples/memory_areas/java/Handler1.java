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

public class Handler1 extends PeriodicEventHandler {
   public boolean done = false;

   public Handler1() {
      super(
         new PriorityParameters(
            PriorityScheduler.instance().getNormPriority()),
         new PeriodicParameters(null, new RelativeTime(100, 0)),
         new StorageConfigurationParameters(32768, 4096, 4096),
         65536, "Handler1");
   }

   public void handleEvent() {
      System.out.println("[Handler1] handleEvent() called");
      if (!done) {
         MemoryArea area = ImmortalMemory.instance();
         Handler1_MemoryEntry entry = new Handler1_MemoryEntry(this);
         area.executeInArea(entry);
         done = true;
      }
      else {
         System.out.println("[Handler1] requesting termination");
         Mission.getCurrentMission().requestTermination();         
      }
   }

   /*class MemoryEntry implements Runnable {
      public Handler1 outer;

      public MemoryEntry(Handler1 outer) {
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

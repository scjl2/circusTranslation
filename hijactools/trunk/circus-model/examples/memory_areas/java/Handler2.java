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

public class Handler2 extends PeriodicEventHandler {
   public boolean done = false;

   public Handler2() {
      super(
         new PriorityParameters(
            PriorityScheduler.instance().getNormPriority()),
         new PeriodicParameters(null, new RelativeTime(100, 0)),
         new StorageConfigurationParameters(32768, 4096, 4096),
         65536, "Handler2");
   }

   public void handleEvent() {
      System.out.println("[Handler2] handleEvent() called");
      if (!done) {
         MemoryArea area = MemoryArea.getMemoryArea(this);
         Handler2_MemoryEntry entry = new Handler2_MemoryEntry(this);
         area.executeInArea(entry);
         done = true;
      }
      else {
         System.out.println("[Handler2] requesting termination");
         Mission.getCurrentMission().requestTermination();         
      }
   }

   /*class MemoryEntry implements Runnable {
      public Handler2 outer;

      public MemoryEntry(Handler2 outer) {
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

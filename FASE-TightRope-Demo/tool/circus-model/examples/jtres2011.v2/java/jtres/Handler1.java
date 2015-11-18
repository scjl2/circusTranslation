package jtres;

import javax.realtime.AbsoluteTime;
import javax.realtime.RelativeTime;
import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.PriorityScheduler;
import javax.realtime.MemoryArea;
import javax.realtime.RawMemory;
import javax.realtime.RawInt;

import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.MissionMemory;

public class Handler1 extends PeriodicEventHandler {
   /* Device Register */
   public final static long REGISTER_ADDRESS = 0;

   private final RawInt register =
      RawMemory.createRawIntAccessInstance(
      RawMemory.IO_MEM_MAPPED, REGISTER_ADDRESS);

   /* Shared Data in Mission Memory */
   private List list;

   public Handler1(List list,
      PriorityParameters priority,
      PeriodicParameters period,
      StorageParameters storage)  {
      super(priority, period, storage, "Handler1");
      this.list = list;
   }

   public void handleAsyncEvent() {
      /* Read input value from hardware here. */
      /* Since we cannot use an interrupt handler, and open question is how
       * we implement this to be carried out at a high priority, and what
       * design would result in a blocking read that causes that waits for
       * the hardware to deliver the data. I will try and clarify this with
       * Andy this week if he is available for a chat. */
      int value = register.get();
      System.out.println("[Handler1] input " + value + " received");
      MissionMemory mission_memory =
         (MissionMemory) MemoryArea.getMemoryArea(this);
      mission_memory.executeInArea(new MissionMemoryEntry(value));
   }

   /* The insert() method allocates data and hence has to be carried out
    * in MissionMemory. */

   class MissionMemoryEntry implements Runnable {
      public int value;

      public MissionMemoryEntry(int value) {
         this.value = value;
      }

      public void run() {
         list.insert(value);
      }
   }
}

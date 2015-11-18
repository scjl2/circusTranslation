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
   public final static long IN_DATA_REGISTER_ADDRESS = 0;

   private final RawInt in_data_register =
      RawMemory.createRawIntAccessInstance(
      RawMemory.IO_MEM_MAPPED, IN_DATA_REGISTER_ADDRESS);

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
      /* We assume the the reading is asynchronous (non-blocking). */
      int value = in_data_register.get();
      System.out.println("[Handler1] input " + value + " received");
      MissionMemory mission_memory =
         (MissionMemory) MemoryArea.getMemoryArea(this);
      mission_memory.executeInArea(new MissionMemoryEntry(value));
   }

   /* The insert() method allocates data and hence the call it must be carried
    * out in MissionMemory. */

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

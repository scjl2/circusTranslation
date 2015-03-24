package accs;

import javax.realtime.*;

import javax.safetycritical.*;

public class WheelShaft extends AperiodicEventHandler {
   private long count;

   public WheelShaft(
         PriorityParameters priority,
         StorageParameters storage,
         AperiodicEvent event) {
      super(priority, storage, event, "WheelShaft");
      count = 0;
   }

   public synchronized long getCount() {
      return count;
   }

   public void handleAsyncEvent() {
      /* Can we have synchronized blocks at Level 1? */
      synchronized (this) {
         /* Both methods to obtain the fire count are not supported in the
          * current version 0.78 of the SCJ API.
         /*count = count + getAndClearPendingFireCount();*/
         //while (getAndDecrementPendingFireCount() > 0) {
            count++;
         //}
      }
   }

   public static int getCallibration() {
      return 100;
   }
}

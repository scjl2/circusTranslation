package accs;

import javax.realtime.*;

import javax.safetycritical.*;

public class Brake extends AperiodicLongEventHandler {
   private CruiseControl cruise;

   public Brake(CruiseControl cruise,
      PriorityParameters priority,
      StorageParameters storage,
      AperiodicLongEvent[] events) {
      super(priority, storage, events, "Brake");
      this.cruise = cruise;
   }

   public void handleAsyncLongEvent(long value) {
      int event = (int) value;
      switch (event) {
         case EventIds.BRAKE_ENGAGED:
            cruise.brakeEngaged();
            break;

         case EventIds.BRAKE_DISENGAGED:
            cruise.brakeDisengaged();
            break;

         default:
            throw new AssertionError();
      }
   }
}

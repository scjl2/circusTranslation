package accs;

import javax.realtime.*;

import javax.safetycritical.*;

public class Gear extends AperiodicLongEventHandler {
   private CruiseControl cruise;

   public Gear(CruiseControl cruise,
      PriorityParameters priority,
      StorageParameters storage,
      AperiodicLongEvent[] events) {
      super(priority, storage, events, "Gear");
      this.cruise = cruise;
   }

   public void handleAsyncLongEvent(long value) {
      int event = (int) value;
      switch (event) {
         case EventIds.TOP_GEAR_ENGAGED:
            cruise.topGearEngaged();
            break;

         case EventIds.TOP_GEAR_DISENGAGED:
            cruise.topGearDisengaged();
            break;

         default:
            throw new AssertionError();
      }
   }
}

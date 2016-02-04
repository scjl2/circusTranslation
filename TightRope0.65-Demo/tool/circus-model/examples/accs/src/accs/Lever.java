package accs;

import javax.realtime.*;

import javax.safetycritical.*;

public class Lever extends AperiodicEventHandler {
   private CruiseControl cruise;
   private LeverInput input;

   public Lever(CruiseControl cruise,
      PriorityParameters priority,
      StorageParameters storage,
      AperiodicEvent event,
      LeverInput input) {
      super(priority, storage, event, "Lever");
      this.cruise = cruise;
      this.input = input;
   }

   public void handleAsyncEvent() {
      LeverPosition position = input.getLeverInput();
      switch (position) {
         case ACTIVATE:
            cruise.activate();
            break;

         case DEACTIVATE:
            cruise.deactivate();
            break;

         case RESUME:
            cruise.resume();
            break;

         case START_ACCELERATION:
            cruise.startAcceleration();
            break;

         case STOP_ACCELERATION:
            cruise.stopAcceleration();
            break;

         default:
            throw new AssertionError("UNKNOWN LEVER INTERRUPT");
      }
   }
}

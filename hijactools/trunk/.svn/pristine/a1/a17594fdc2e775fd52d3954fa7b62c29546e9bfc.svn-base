package accs.isr;

import accs.ExternalEvents;

import javax.safetycritical.AperiodicLongEvent;
import javax.safetycritical.InterruptServiceRoutine;

import javax.safetycritical.annotate.*;

@InteractionClass
public class BrakeISR extends InterruptServiceRoutine {
   protected final AperiodicLongEvent brake_event;

   public BrakeISR(AperiodicLongEvent event) {
      super("BrakeISR");
      brake_event = event;
   }

   public void handle() {
      /* Determine external event that raised the interrupt. */
      long cause = 0;
      brake_event.fire(cause);
   }
}

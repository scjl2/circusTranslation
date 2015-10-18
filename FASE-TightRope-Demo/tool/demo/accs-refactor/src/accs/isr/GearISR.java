package accs.isr;

import accs.ExternalEvents;

import javax.safetycritical.AperiodicLongEvent;
import javax.safetycritical.InterruptServiceRoutine;

import javax.safetycritical.annotate.*;

@InteractionClass
public class GearISR extends InterruptServiceRoutine {
   protected final AperiodicLongEvent gear_event;

   public GearISR(AperiodicLongEvent event) {
      super("GearISR");
      gear_event = event;
   }

   public void handle() {
      /* Determine external event that raised the interrupt. */
      long cause = 0;
      gear_event.fire(cause);
   }
}

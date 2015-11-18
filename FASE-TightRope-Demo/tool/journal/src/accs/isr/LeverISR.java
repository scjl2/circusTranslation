package accs.isr;

import accs.ExternalEvents;

import javax.safetycritical.AperiodicLongEvent;
import javax.safetycritical.InterruptServiceRoutine;

import javax.safetycritical.annotate.*;

@InteractionClass
public class LeverISR extends InterruptServiceRoutine {
   protected final AperiodicLongEvent lever_event;

   public LeverISR(AperiodicLongEvent event) {
      super("LeverISR");
      lever_event = event;
   }

   public void handle() {
      /* Determine external event that raised the interrupt. */
      long cause = 0;
      lever_event.fire(cause);
   }
}

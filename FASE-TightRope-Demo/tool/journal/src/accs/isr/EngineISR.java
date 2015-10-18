package accs.isr;

import accs.ExternalEvents;

import javax.safetycritical.AperiodicLongEvent;
import javax.safetycritical.InterruptServiceRoutine;

import javax.safetycritical.annotate.*;

@InteractionClass
public class EngineISR extends InterruptServiceRoutine {
   protected final AperiodicLongEvent engine_event;

   public EngineISR(AperiodicLongEvent event) {
      super("EngineISR");
      engine_event = event;
   }

   public void handle() {
      /* Determine external event that raised the interrupt. */
      long cause = 0;
      engine_event.fire(cause);
   }
}

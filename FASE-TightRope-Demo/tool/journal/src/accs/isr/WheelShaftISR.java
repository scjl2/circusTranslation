package accs.isr;

import accs.ExternalEvents;

import javax.safetycritical.AperiodicEvent;
import javax.safetycritical.InterruptServiceRoutine;

import javax.safetycritical.annotate.*;

@InteractionClass
public class WheelShaftISR extends InterruptServiceRoutine {
   protected final AperiodicEvent shaft_event;

   public WheelShaftISR(AperiodicEvent event) {
      super("WheelShaftISR");
      shaft_event = event;
   }

   public void handle() {
      shaft_event.fire();
   }
}

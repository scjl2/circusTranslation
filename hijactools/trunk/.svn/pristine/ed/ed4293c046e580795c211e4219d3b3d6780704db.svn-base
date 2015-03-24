package accs.interrupts;

import accs.Events;

import javax.safetycritical.AperiodicEvent;
import javax.safetycritical.InterruptServiceRoutine;

public class WheelShaftInterruptHandler extends InterruptServiceRoutine {
   protected final AperiodicEvent event;

   public WheelShaftInterruptHandler(AperiodicEvent event) {
      super("WheelShaftInterrupt");
      this.event = event;
   }

   @Override
   public void handle() {
      event.fire();
   }
}

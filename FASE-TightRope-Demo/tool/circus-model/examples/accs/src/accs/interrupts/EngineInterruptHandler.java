package accs.interrupts;

import accs.Events;

import javax.safetycritical.AperiodicLongEvent;
import javax.safetycritical.InterruptServiceRoutine;

public class EngineInterruptHandler extends InterruptServiceRoutine {
   protected final AperiodicLongEvent event;

   public EngineInterruptHandler(AperiodicLongEvent event) {
      super("EngineInterrupt");
      this.event = event;
   }

   @Override
   public void handle() {
      /* Read cause of the interrupt from hardware register. */
      long param = 0;
      event.fire(param);
   }
}

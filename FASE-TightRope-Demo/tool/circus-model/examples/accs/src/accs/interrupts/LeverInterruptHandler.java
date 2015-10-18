package accs.interrupts;

import accs.Events;
import accs.LeverInput;
import accs.LeverPosition;

import javax.safetycritical.AperiodicEvent;
import javax.safetycritical.InterruptServiceRoutine;

public class LeverInterruptHandler extends InterruptServiceRoutine {
   protected final AperiodicEvent event;
   protected final LeverInput input;

   public LeverInterruptHandler(AperiodicEvent event, LeverInput input) {
      super("LeverInterrupt");
      this.event = event;
      this.input = input;
   }

   @Override
   public void handle() {
      /* Read parameter of the interrupt from hardware register. */
      int param = 0;
      switch(param) {
         case 1:
            input.setLeverInput(LeverPosition.ACTIVATE);
            break;

         case 2:
            input.setLeverInput(LeverPosition.DEACTIVATE);
            break;

         case 3:
            input.setLeverInput(LeverPosition.START_ACCELERATION);
            break;

         case 4:
            input.setLeverInput(LeverPosition.STOP_ACCELERATION);
            break;

         case 5:
            input.setLeverInput(LeverPosition.RESUME);
            break;

         default:
            throw new AssertionError();
      }
      event.fire();
   }
}

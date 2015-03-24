package accs.interrupts;

import accs.Events;
import accs.EventIds;

import javax.safetycritical.AperiodicLongEvent;
import javax.safetycritical.InterruptServiceRoutine;

public class BrakeInterruptHandler extends InterruptServiceRoutine {
   public static final int BRAKE_ENGAGED_CAUSE = 1;
   public static final int BRAKE_DISENGAGED_CAUSE = 2;

   protected final AperiodicLongEvent brake_engaged;
   protected final AperiodicLongEvent brake_disengaged;

   public BrakeInterruptHandler(
      AperiodicLongEvent brake_engaged,
      AperiodicLongEvent brake_disengaged) {
      super("BrakeInterrupt");
      this.brake_engaged = brake_engaged;
      this.brake_disengaged = brake_disengaged;
   }

   @Override
   public void handle() {
      /* Read cause of the interrupt from hardware register. */
      int cause = 0;
      switch(cause) {
         case BRAKE_ENGAGED_CAUSE:
            brake_engaged.fire(EventIds.BRAKE_ENGAGED);
            break;

         case BRAKE_DISENGAGED_CAUSE:
            brake_disengaged.fire(EventIds.BRAKE_DISENGAGED);
            break;

         default:
            throw new AssertionError();
      }
   }
}

package accs.interrupts;

import accs.Events;
import accs.EventIds;

import javax.safetycritical.AperiodicLongEvent;
import javax.safetycritical.InterruptServiceRoutine;

public class GearInterruptHandler extends InterruptServiceRoutine {
   public static final int TOP_GEAR_ENGAGED_CAUSE = 1;
   public static final int TOP_GEAR_DISENGAGED_CAUSE = 2;   

   protected final AperiodicLongEvent top_gear_engaged;
   protected final AperiodicLongEvent top_gear_disengaged;

   public GearInterruptHandler(
      AperiodicLongEvent top_gear_engaged,
      AperiodicLongEvent top_gear_disengaged) {
      super("GearInterrupt");
      this.top_gear_engaged = top_gear_engaged;
      this.top_gear_disengaged = top_gear_disengaged;
   }

   @Override
   public void handle() {
      /* Read cause of the interrupt from hardware register. */
      int cause = 0;
      switch(cause) {
         case TOP_GEAR_ENGAGED_CAUSE:
            top_gear_engaged.fire(EventIds.TOP_GEAR_ENGAGED);
            break;

         case TOP_GEAR_DISENGAGED_CAUSE:
            top_gear_disengaged.fire(EventIds.TOP_GEAR_DISENGAGED);
            break;

         default:
            throw new AssertionError();
      }
   }
}

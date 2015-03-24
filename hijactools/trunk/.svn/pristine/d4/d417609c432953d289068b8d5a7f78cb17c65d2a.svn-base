package jtres;

import javax.safetycritical.PriorityScheduler;
import javax.safetycritical.Services;

public class Interrupts {
   /* Interrupt Handlers */
   public final OutInterruptHandler out_interrupt;

   public Interrupts(Events events) {
      /* Created in Mission Memory */
      out_interrupt = new OutInterruptHandler(events.out);
   }

   /* Register Interrupt Handlers */
   public void register() {
      out_interrupt.register();
   }

   /* Set Priority of Interrupt Handlers */
   public void setPriorities() {
      Services.setCeiling(out_interrupt,
         PriorityScheduler.instance().getMaxPriority());
   }
}

package jtres;

import javax.safetycritical.AperiodicEvent;
import javax.safetycritical.InterruptServiceRoutine;

public class OutInterruptHandler extends InterruptServiceRoutine {
   protected final AperiodicEvent event;

   public OutInterruptHandler(AperiodicEvent event) {
      super("OutInterruptHandler");
      this.event = event;
   }

   @Override
   public void handle() {
      System.out.println("[OutInterruptHandler] request for output");
      event.fire();
   }
}

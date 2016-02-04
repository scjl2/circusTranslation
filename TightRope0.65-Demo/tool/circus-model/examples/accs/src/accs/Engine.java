package accs;

import javax.realtime.*;

import javax.safetycritical.*;

public class Engine extends AperiodicLongEventHandler {
   private CruiseControl cruise;

   public Engine(CruiseControl cruise,
      PriorityParameters priority,
      StorageParameters storage,
      AperiodicLongEvent event) {
      super(priority, storage, event, "Engine");
      this.cruise = cruise;
   }

   public void handleAsyncLongEvent(long value) {
      /* Some conversion needs to take place here. */
      boolean flag = value == 0 ? false : true;
      if (flag) {
         cruise.engineOn();
      }
      else {
         cruise.engineOff();
      }
   }
}

package accs;

import javax.realtime.PriorityParameters;
import javax.realtime.PeriodicParameters;

import javax.safetycritical.*;

public class VoltageOutput extends PeriodicEventHandler {
   private ThrottleController throttle;

   public VoltageOutput(ThrottleController throttle,
         PeriodicParameters period,
         StorageParameters storage) {
      super(
         new PriorityParameters(
            PriorityScheduler.instance().getMaxPriority()),
         period, storage, "VoltageOutput");
   }

   public void handleAsyncEvent() {
      int voltage = throttle.getVoltage();
      if (voltage != -1) {
         /* Write Voltage to the device here. */
      }
   }
}

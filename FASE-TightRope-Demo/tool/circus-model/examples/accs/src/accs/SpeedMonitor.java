package accs;

import javax.realtime.*;

import javax.safetycritical.*;

public class SpeedMonitor extends PeriodicEventHandler {
   private WheelShaft wheel_shaft;
   private long numberRotations, lastNumberRotations;
   private final int calibration; // nearest centimeter per rotation
   private int currentSpeed = 0; // nearest killometers per hour
   private final int iterationsInOneHour;
   private final int cmInKillometer = 100000;

   public SpeedMonitor(WheelShaft wheel_shaft,
         PriorityParameters priority,
         PeriodicParameters period,
         StorageParameters storage) {
      super(priority, period, storage, "SpeedMonitor");
      lastNumberRotations = wheel_shaft.getCount();
      calibration = wheel_shaft.getCallibration();
      iterationsInOneHour =
         (int) ((1000 / period.getPeriod().getMilliseconds()) * 3600);
      this.wheel_shaft = wheel_shaft;
   }

   public synchronized int getCurrentSpeed() {
      return currentSpeed;
   }

   public void handleAsyncEvent() {
      numberRotations = wheel_shaft.getCount();
      long difference = numberRotations - lastNumberRotations;
      currentSpeed = (int)
         ((difference * calibration * iterationsInOneHour) / cmInKillometer);
      lastNumberRotations = numberRotations;
   }
}

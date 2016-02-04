package accs;

import javax.realtime.*;

import javax.safetycritical.*;

/* A more substantial alteration to convert this class from RTSJ to SCJ was
 * made since schedulePeriodic() and deschedulePeriodic() are not directly
 * supported by the SCJ API. We hence emulate the functionality of these
 * methods be virtue of a flag that determines whether the handler should be
 * periodically scheduled or not. Factually, the dispatch never stops. */

/* There is no code that writes the voltage out to the throttle device yet. */

public class ThrottleController extends PeriodicEventHandler {
   /* We set the period of the throttle controller to 0.1 sec. With an
    * increment of 0.01 volts per period, this give a maximal possible
    * change of voltage on the throttle of 0.1 volts per second, meeting the
    * requirement for comfortable acceleration. */
   final public static RelativeTime THROTTLE_PERIOD = new RelativeTime(100, 0);

   final public static float VOLTAGE_INCREMENT = 0.01f;

   private SpeedMonitor speedo;
   private boolean accelerating = false;
   private boolean maintainSpeed = false;
   private int cruiseSpeed = 0;
   private float voltage = 0.0f;

   /* Flag added since [de]schedulePeriodic() are not supporting in SCJ. */
   private boolean schedule_periodic = true;

   public ThrottleController(SpeedMonitor speedo,
         PriorityParameters priority,
         StorageParameters storage) {
      super(priority,
         new PeriodicParameters(null, THROTTLE_PERIOD),
         storage, "ThrottleController");
      this.speedo = speedo;
   }

   public synchronized void setCruiseSpeed(int kph) {
      cruiseSpeed = kph;
      maintainSpeed = true;
      accelerating = false;
   }

   public synchronized void accelerate() {
      accelerating = true;
   }

   private synchronized void increaseVoltage() {
      if (voltage <= 8) {
         voltage += VOLTAGE_INCREMENT;
         voltage = Math.min(voltage, 8);
      }
   }

   /* The following method was missing in the original RTSJ code. */
   private synchronized void decreaseVoltage() {
      if (voltage >= 0) {
         voltage -= VOLTAGE_INCREMENT;
         voltage = Math.max(voltage, 0);
      }
   }

   /* The following two methods have been added to control the handler. */
   public synchronized void schedulePeriodic() {
      schedule_periodic = true;
   }

   public synchronized void deschedulePeriodic() {
      schedule_periodic = false;
   }

   public synchronized int getVoltage() {
      if (schedule_periodic) {
         return (int) voltage;
      }
      else { return -1; }
   }

   public void handleAsyncEvent() {
      /* The outer conditional was added to emulate [de]scheduling. */
      if (schedule_periodic) {
         if (accelerating) {
            /* Accelerating */
            increaseVoltage();
         }
         else {
            if (maintainSpeed) {
               /* Maintaining Speed */
               if (cruiseSpeed - speedo.getCurrentSpeed() > 2) {
                  increaseVoltage();
               }
               else if (speedo.getCurrentSpeed() - cruiseSpeed < 2) {
                  /* The original code for RTSJ sets voltage to 0 here, however
                   * I am not sure this voilates the requirement for comfortable
                   * acceleration. */
                  /* Added by Frank Zeyda */
                  decreaseVoltage();
               }
               else {
                  float volts =
                     2.0f * (cruiseSpeed - speedo.getCurrentSpeed() + 2.0f);
                  if (volts > voltage) {
                     increaseVoltage();
                  }
                  else {
                     /* Added by Frank Zeyda */
                     decreaseVoltage();
                  }
               }
            }
         }
      }
   }
}

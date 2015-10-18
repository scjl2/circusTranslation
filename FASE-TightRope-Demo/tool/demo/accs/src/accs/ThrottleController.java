/* REVIEWED */
package accs;

import javax.realtime.*;

import javax.safetycritical.*;

/* Some modifications to this class were necessary for SCJ compliance. That is
 * we cannot call the methods schedulePeriodic(), deschedulePeriodic() and
 * start(). (Whereas the RTSJ version which derives ThrottleController from
 * RealtimeThread, we use a PeriodicEventHandler schedulable.) Instead, we
 * simulate the enabling and disabling of the periodic handler by means of a
 * boolean flag that can be set and cleared via external method calls. */

/* I made a few further modifications to this class to fix some bugs (?) */

public class ThrottleController extends PeriodicEventHandler {
  /* We set the period of the throttle controller to 0.1 sec. With an
   * increment of 0.01 volts per period, this give a maximal possible
   * change of voltage on the throttle of 0.1 volts, meeting the requirement
   * for comfortable acceleration. */
  final public static RelativeTime
    THROTTLE_PERIOD = new RelativeTime(100, 0);

  final public static float VOLTAGE_INCREMENT = 0.01f;

  private ShaftSimulator shaft;
  private SpeedMonitor speed;
  private boolean accelerating = false;
  private boolean maintainSpeed = false;
  private int cruiseSpeed = 0;
  private float voltage = 0.0f;

  /* Added since [de]schedulePeriodic() are not supporting in SCJ. */
  private boolean schedule_periodic = true;

  public ThrottleController(ShaftSimulator shaft, SpeedMonitor speed,
      PriorityParameters priority,
      StorageConfigurationParameters storage) {
    super(priority, new PeriodicParameters(null, THROTTLE_PERIOD),
      storage, 10000, "ThrottleController");
    this.speed = speed;
    this.shaft = shaft;
  }

  public synchronized void setCruiseSpeed(int kph) {
    cruiseSpeed = kph;
    maintainSpeed = true;
    accelerating = false;
    System.out.println("THROTTLE SET SPEED: " + kph);
  }

  public synchronized void accelerate() {
    accelerating = true;
    System.out.println("THROTTLE ACCELERATING");
  }

  private synchronized void increaseVoltage() {
    if (voltage <= 8) {
      System.out.println("THROTTLE OPENING");
      voltage += VOLTAGE_INCREMENT;
      voltage = Math.min(voltage, 8);
      shaft.increaseSpeed();
    }
    /* Write voltage out here. */
  }

  /* The following method was missing in the original RTSJ code. */
  private synchronized void decreaseVoltage() {
    if (voltage >= 0) {
      System.out.println("THROTTLE CLOSING");
      voltage -= VOLTAGE_INCREMENT;
      voltage = Math.max(voltage, 0);
      shaft.decreaseSpeed();
    }
    /* Write voltage out here. */
  }

  /* The following two methods have been added to control the handler. */
  public synchronized void schedulePeriodic() {
    schedule_periodic = true;
  }

  public synchronized void deschedulePeriodic() {
    schedule_periodic = false;
  }

  public void handleEvent() {
    /* Modification for SCJ compliance: only execute handler if scheduled. */
    if (schedule_periodic) {
      if (accelerating) {
        System.out.println("THROTTLE ACCELERATING");
        increaseVoltage();
      }
      else {
        if (maintainSpeed) {
          System.out.println("THROTTLE MAINTINING SPEED");
          if (cruiseSpeed - speed.getCurrentSpeed() > 2) {
            increaseVoltage();
          }
          else if (speed.getCurrentSpeed() - cruiseSpeed < 2) {
            /* The original code for RTSJ sets voltage to 0 here, however this
             * seems to voilate the requirement for comfortable acceleration. */
            decreaseVoltage();
          }
          else {
            float volts =
              2.0f * (cruiseSpeed - speed.getCurrentSpeed() + 2.0f);
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

  /* Additional methods that need to be defined for the infrastructure. */
  public StorageConfigurationParameters getThreadConfigurationParameters() {
    return null;
  }

  public void register() { }
}

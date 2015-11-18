/* REVIEWED */
package accs;

import javax.realtime.*;

import javax.safetycritical.*;

/* The class was reworked to give a more accurate model for the shaft. */

/* Does a change of the period of a periodic event handler take effect even
 * after the event handler has been initialised and started? Ask Andy... */

public class ShaftSimulator extends PeriodicEventHandler {
  private AsyncEvent shaft_interrupt;
  private RelativeTime period;
  private boolean braking = false;

  /* Threshold for the minimum and maximum number of revolutions. Note that we
   * do not allow 0 since then the period would become infinite. */
  public final static int MIN_RPM = 1;
  public final static int MAX_RPM = 8000;

  /* The increment corresponds to VOLTAGE_INCREMENT in ThrottleController. */
  public final static int RPM_INCREMENT = 10;
  private int rpm;

  public ShaftSimulator(AsyncEvent shaft_interrupt,
      PriorityParameters priority,
      PeriodicParameters periodic,
      StorageConfigurationParameters storage) {
    super(priority, periodic, storage, 10000, "ShaftSimulator");
    this.shaft_interrupt = shaft_interrupt;
    period = periodic.getPeriod();
    rpm = MIN_RPM;
  }

  public synchronized void setBraking(boolean breaking) {
    this.braking = braking;
  }

  public synchronized void increaseSpeed() {
    if (rpm < MAX_RPM) {
      System.out.println("SHAFT SIMULATOR SPEEDING UP");
      rpm += RPM_INCREMENT;
      rpm = Math.min(rpm, MAX_RPM);
      period.set(60000 / rpm);
    }
  }

  public synchronized void decreaseSpeed() {
    if (rpm > MIN_RPM) {
      System.out.println("SHAFT SIMULATOR SLOWING DOWN");
      rpm -= RPM_INCREMENT;
      rpm = Math.max(rpm, MIN_RPM);
      period.set(60000 / rpm);
    }
  }

  public void handleEvent() {
    if (braking) decreaseSpeed();
    shaft_interrupt.fire();
  }

  /* Additional methods that need to be defined for the infrastructure. */
  public StorageConfigurationParameters getThreadConfigurationParameters() {
    return null;
  }

  public void register() { }
}

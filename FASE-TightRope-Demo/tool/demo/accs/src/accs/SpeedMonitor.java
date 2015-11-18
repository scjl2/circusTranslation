/* REVIEWED */
package accs;

import javax.realtime.*;

import javax.safetycritical.*;

public class SpeedMonitor extends PeriodicEventHandler {
  private WheelShaft wheel_shaft;
  private long numberRotations, lastNumberRotations;
  private final int calibration; // nearest centimeter (cm per rotation)
  private int currentSpeed = 0; // nearest killometers per hour
  private final int iterationsInOneHour;
  private final int cmInKillometer = 100000;

  public SpeedMonitor(WheelShaft wheel_shaft,
      PriorityParameters priority,
      PeriodicParameters period,
      StorageConfigurationParameters storage) {
    super(priority, period, storage, 10000, "SpeedMonitor");
    lastNumberRotations = wheel_shaft.getCount();
    calibration = wheel_shaft.getCallibration();
    iterationsInOneHour =
      (int) ((1000 / period.getPeriod().getMilliseconds()) * 3600);
    this.wheel_shaft = wheel_shaft;
  }

  public int getCurrentSpeed() {
    return currentSpeed;
  }

  public void handleEvent() {
    numberRotations = wheel_shaft.getCount();
    long difference = numberRotations - lastNumberRotations;
    currentSpeed = (int)
      ((difference * calibration * iterationsInOneHour) / cmInKillometer);
    System.out.println("CURRENT SPEED IS " + currentSpeed);
    lastNumberRotations = numberRotations;
  }

  /* Additional methods that need to be defined for the infrastructure. */
  public StorageConfigurationParameters getThreadConfigurationParameters() {
    return null;
  }

  public void register() { }
}

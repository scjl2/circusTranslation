package accs;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.RelativeTime;

import javax.safetycritical.AperiodicEvent;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.PriorityScheduler;
import javax.safetycritical.StorageParameters;

import javax.safetycritical.annotate.*;

@HandlerId("SpeedMonitorHId")
public class SpeedMonitor extends PeriodicEventHandler {
  public final int calibration; /* cm per rotation */
  public final int iterationsInOneHour;
  public final int cmInKilometer = 100000;

  private WheelShaft wheel_shaft;
  private long numberRotations = 0;
  private long lastNumberRotations;
  private int currentSpeed = 0; /* kilometers per hour */

  public SpeedMonitor(WheelShaft shaft, long period) {
    super(
      new PriorityParameters(
        PriorityScheduler.instance().getMaxPriority()),
      new PeriodicParameters(null, new RelativeTime(period, 0)),
      new StorageParameters(32768, 4096, 4096), "SpeedMonitor");

    calibration = wheel_shaft.getCallibration();
    iterationsInOneHour = (int) ((3600*1000) / period);
    wheel_shaft = shaft;
    lastNumberRotations = wheel_shaft.getCount();
  }

  public synchronized int getCurrentSpeed() {
    return currentSpeed;
  }

  public void handleAsyncEvent() {
    numberRotations = wheel_shaft.getCount();
    long difference = numberRotations - lastNumberRotations;
    currentSpeed = (int)
      ((difference * calibration * iterationsInOneHour) / cmInKilometer);
    lastNumberRotations = numberRotations;
  }
}

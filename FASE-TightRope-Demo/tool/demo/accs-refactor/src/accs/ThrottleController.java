package accs;

import java.lang.reflect.InvocationTargetException;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.RelativeTime;

import javax.safetycritical.AperiodicEvent;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.PriorityScheduler;
import javax.safetycritical.RawIntegralAccess;
import javax.safetycritical.RawMemory;
import javax.safetycritical.StorageParameters;

import javax.safetycritical.annotate.*;

@HandlerId("ThrottleHId")
public class ThrottleController extends PeriodicEventHandler {
  /* We set the period of the throttle controller to 0.1 sec. With an
   * increment of 0.01 volts per period, this give a maximal possible
   * change of voltage on the throttle of 0.1 volts per sec, meeting
   * the requirement for comfortable acceleration. It also meets the
   * requirement on the frequency of voltage updates (3 per sec) when
   * the throttle is controlled by the software. */
  final public static long THROTTLE_PERIOD = 100;
  final public static float VOLTAGE_INCREMENT = 0.01f;

  private SpeedMonitor speedo;
  private boolean schedule_throttle = false;
  private boolean accelerating = false;
  private boolean maintainSpeed = false;
  private int cruiseSpeed = 0;
  private float voltage = 0.0f;

  public ThrottleController(SpeedMonitor speedoArg) {
    super(
      new PriorityParameters(
        PriorityScheduler.instance().getMaxPriority()),
      new PeriodicParameters(null, new RelativeTime(THROTTLE_PERIOD, 0)),
      new StorageParameters(32768, 4096, 4096), "ThrottleController");
    speedo = speedoArg;
  }

  public synchronized void setCruiseSpeed(int kph) {
    cruiseSpeed = kph;
    maintainSpeed = true;
    accelerating = false;
  }

  public synchronized void accelerate() {
    accelerating = true;
  }

  @DeviceAccess("set_voltage~!~voltage \\then \\Skip")
  private synchronized void writeVoltage() {
    final long SET_VOLTAGE_REG = 0x0;
    try {
      RawIntegralAccess io_port =
        RawMemory.createRawIntegralInstance(
        RawMemory.IO_PORT_MAPPED, SET_VOLTAGE_REG, 1);
      /* Write out voltage to the throttle as a single byte. */
      io_port.setByte(0, (byte) (voltage * 10));
    }
    catch (InstantiationException e) { }
    catch (IllegalAccessException e) { }
    catch (InvocationTargetException e) { }
  }

  private synchronized void increaseVoltage() {
    if (voltage <= 8) {
      voltage += VOLTAGE_INCREMENT;
      voltage = Math.min(voltage, 8);
    }
    //System.out.println("THROTTLE OPENING");
    writeVoltage();
  }

  private synchronized void decreaseVoltage() {
    if (voltage >= 0) {
      voltage -= VOLTAGE_INCREMENT;
      voltage = Math.max(voltage, 0);
    }
    //System.out.println("THROTTLE CLOSING");
    writeVoltage();
  }

  private synchronized void resetVoltage() {
    //System.out.println("THROTTLE CLOSED");
    voltage = 0;
    writeVoltage();
  }

  public synchronized void schedulePeriodic() {
    schedule_throttle = true;
  }

  public synchronized void deschedulePeriodic() {
    schedule_throttle = false;
  }

  public void handleAsyncEvent() {
    if (schedule_throttle) {
      if (accelerating) {
        increaseVoltage();
      }
      else {
        if (maintainSpeed) {
          if (cruiseSpeed - speedo.getCurrentSpeed() > 2) {
            increaseVoltage();
          }
          else if (cruiseSpeed - speedo.getCurrentSpeed() < -2) {
            resetVoltage();
          }
          else {
            float volts =
              2.0f * (cruiseSpeed - speedo.getCurrentSpeed() + 2.0f);
            if (volts > voltage) {
              increaseVoltage();
            }
            else {
              /* Added by Frank Zeyda. I think we need it! */
              decreaseVoltage();
            }
          }
        }
      }
    }
  }
}

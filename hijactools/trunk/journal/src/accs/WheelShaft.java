package accs;

import javax.realtime.PriorityParameters;

import javax.safetycritical.AperiodicEvent;
import javax.safetycritical.AperiodicEventHandler;
import javax.safetycritical.AperiodicParameters;
import javax.safetycritical.PriorityScheduler;
import javax.safetycritical.StorageParameters;

import javax.safetycritical.annotate.*;

@HandlerId("WheelshaftHId")
@BoundEvent("wheelshaft")
public class WheelShaft extends AperiodicEventHandler {
  /* public static final int CALLIBRATION = 100; */

  private long count;

  public WheelShaft(@Ignore AperiodicEvent shaft_event) {
    super(
      new PriorityParameters(
        PriorityScheduler.instance().getNormPriority()),
      new AperiodicParameters(null, null),
      new StorageParameters(32768, 4096, 4096),
      shaft_event, "WheelShaft");
    count = 0;
  }

  /* Should we include support for static methods? */

  public static int getCallibration() {
    return 100;
  }

  public synchronized long getCount() {
    return count;
  }

  public void handleAsyncEvent() {
    /*count = count + getAndClearPendingFireCount();*/
    count++;
  }
}

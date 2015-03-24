/* REVIEWED */
package accs;

import javax.realtime.*;

import javax.safetycritical.*;

public class WheelShaft extends AperiodicEventHandler {
  private long count;

  public WheelShaft(
      PriorityParameters priority,
      AperiodicParameters release,
      StorageConfigurationParameters storage) {
    super(priority, release, storage, 10000, "WheelShaft");
    count = 0;
  }

  public static int getCallibration() {
    return 100;
  }

  public synchronized long getCount() {
    return count;
  }

  /* Why handleEvent() instead of handleAsyncEvent() ? */
  public void handleEvent() {
    synchronized (this) {
      /*count = count + getAndClearPendingFireCount();*/
      while (getAndDecrementPendingFireCount() > 0) {
        count++;
      }
    }
    /*System.out.println("WHEEL SHAFT INTERRUPT - count = " + count);*/
  }

  /* Additional methods that need to be defined for the infrastructure. */
  public StorageConfigurationParameters getThreadConfigurationParameters() {
    return null;
  }

  public void register() { }
}

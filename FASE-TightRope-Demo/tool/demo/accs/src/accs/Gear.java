/* REVIEWED */
package accs;

import javax.realtime.*;

import javax.safetycritical.*;

public class Gear extends AperiodicEventHandler {
  private CruiseControl cruise;
  private SimulatorDriver sim;

  public Gear(CruiseControl cruise, SimulatorDriver sim,
      PriorityParameters priority,
      AperiodicParameters release,
      StorageConfigurationParameters storage) {
    super(priority, release, storage, 10000, "Gear");
    this.cruise = cruise;
    this.sim = sim;
  }

  /* Why handleEvent() instead of handleAsyncEvent() ? */
  public void handleEvent() {
    CarEventType last = sim.lastEvent();
    switch (last) {
      case GEAR_TOP:
        cruise.topGearEngaged();
        break;

      case GEAR_LOWER:
        cruise.topGearDisengaged();
        break;

      default:
        throw new AssertionError("UNKNOWN GEAR INTERRUPT");
    }
  }

  /* Additional methods that need to be defined for the infrastructure. */
  public StorageConfigurationParameters getThreadConfigurationParameters() {
    return null;
  }

  public void register() { }
}

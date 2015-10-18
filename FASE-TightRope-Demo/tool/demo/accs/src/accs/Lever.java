/* REVIEWED */
package accs;

import javax.realtime.*;

import javax.safetycritical.*;

public class Lever extends AperiodicEventHandler {
  private CruiseControl cruise;
  private SimulatorDriver sim;

  public Lever(CruiseControl cruise, SimulatorDriver sim,
      PriorityParameters priority,
      AperiodicParameters release,
      StorageConfigurationParameters storage) {
    super(priority, release, storage, 10000, "Lever");
    this.cruise = cruise;
    this.sim = sim;
  }

  /* Why handleEvent() instead of handleAsyncEvent() ? */
  public void handleEvent() {
    CarEventType last = sim.lastEvent();
    switch (last) {
      case LEVER_DEACTIVATE:
        cruise.deactivate();
        break;

      case LEVER_RESUME:
        cruise.resume();
        break;

      case LEVER_ACTIVATE:
        cruise.activate();
        break;

      case LEVER_START_ACCELERATING:
        cruise.startAcceleration();
        break;

      case LEVER_STOP_ACCELERATING:
        cruise.stopAcceleration();
        break;

      default:
        throw new AssertionError("UNKNOWN LEVER INTERRUPT");
    }
  }

  /* Additional methods that need to be defined for the infrastructure. */
  public StorageConfigurationParameters getThreadConfigurationParameters() {
    return null;
  }

  public void register() { }
}

/* REVIEWED */
package accs;

import javax.realtime.*;

import javax.safetycritical.*;

public class Engine extends AperiodicEventHandler {
  private CruiseControl cruise;
  private SimulatorDriver sim;

  public Engine(CruiseControl cruise, SimulatorDriver sim,
      PriorityParameters priority,
      AperiodicParameters release,
      StorageConfigurationParameters storage) {
    super(priority, release, storage, 10000, "Engine");
    this.cruise = cruise;
    this.sim = sim;
  }

  /* Why handleEvent() instead of handleAsyncEvent() ? */
  public void handleEvent() {
    CarEventType last = sim.lastEvent();
    switch (last) {
      case ENGINE_ON:
        cruise.engineOn();
        break;

      case ENGINE_OFF:
        cruise.engineOff();
        break;

      default:
        throw new AssertionError("UNKNOWN ENGINE INTERRUPT");
    }
  }

  /* Additional methods that need to be defined for the infrastructure. */
  public StorageConfigurationParameters getThreadConfigurationParameters() {
    return null;
  }

  public void register() { }
}

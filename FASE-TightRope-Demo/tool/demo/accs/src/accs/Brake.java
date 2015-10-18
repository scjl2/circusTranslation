/* REVIEWED */
package accs;

import javax.realtime.*;

import javax.safetycritical.*;

public class Brake extends AperiodicEventHandler {
  private CruiseControl cruise;
  private SimulatorDriver sim;

  public Brake(CruiseControl cruise, SimulatorDriver sim,
      PriorityParameters priority,
      AperiodicParameters release,
      StorageConfigurationParameters storage) {
    super(priority, release, storage, 10000, "Brake");
    this.cruise = cruise;
    this.sim = sim;
  }

  /* Why handleEvent() instead of handleAsyncEvent() ? */
  public void handleEvent() {
    CarEventType last = sim.lastEvent();
    switch (last) {
      case BRAKE_ON:
        cruise.brakeEngaged();
        break;

      case BRAKE_OFF:
        cruise.brakeDisengaged();
        break;

      default:
        throw new AssertionError("UNKNOWN BRAKE INTERRUPT");
    }
  }

  /* Additional methods that need to be defined for the infrastructure. */
  public StorageConfigurationParameters getThreadConfigurationParameters() {
    return null;
  }

  public void register() { }
}

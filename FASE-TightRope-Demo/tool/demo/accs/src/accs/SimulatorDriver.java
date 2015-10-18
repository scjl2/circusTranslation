/* REVIEWED */
package accs;

import javax.realtime.*;

import javax.safetycritical.*;

/* To implement the delay I suppose we may alternatively be able to call the
 * sleep() method on the current realtime thread and us an aperiodic event
 * handler whose handleAyncEvent() method never terminates. */

public class SimulatorDriver extends PeriodicEventHandler {
  /* The granularity of the simulation is one millisecond. */
  public static final RelativeTime
    SIMULATOR_PERIOD = new RelativeTime(1, 0);

  private Simulator sim;
  private CarEventType event_type;
  private AsyncEvent engine_interrupt;
  private AsyncEvent lever_interrupt;
  private AsyncEvent brake_interrupt;
  private AsyncEvent gear_interrupt;

  /* Simulates the delay in units of milliseconds. */
  private int delay = 0;

  public SimulatorDriver(Simulator sim,
      AsyncEvent engine_interrupt,
      AsyncEvent lever_interrupt,
      AsyncEvent brake_interrupt,
      AsyncEvent gear_interrupt,
      PriorityParameters priority,
      StorageConfigurationParameters storage) {
    super(priority, new PeriodicParameters(null, SIMULATOR_PERIOD), storage,
      10000, "SimulatorDriver");
    this.sim = sim;
    this.engine_interrupt = engine_interrupt;
    this.lever_interrupt = lever_interrupt;
    this.brake_interrupt = brake_interrupt;
    this.gear_interrupt = gear_interrupt;
  }

  public synchronized CarEventType lastEvent() {
    return event_type;
  }

  /* Increments the delay counter if greater than 0. */
  private synchronized void incrementDelay() {
    if (delay > 0) delay--;
  }

  public void handleEvent() {
    if (delay == 0) {
      if (sim.hasNext()) {
        CarEvent nextEvent = sim.nextEvent();
        synchronized (this) {
          event_type = nextEvent.getType();
        }
        delay = nextEvent.getDelay();
        switch (event_type) {
          case ENGINE_OFF:
          case ENGINE_ON:
            engine_interrupt.fire();
            break;

          case LEVER_DEACTIVATE:
          case LEVER_RESUME:
          case LEVER_ACTIVATE:
          case LEVER_START_ACCELERATING:
          case LEVER_STOP_ACCELERATING:
            lever_interrupt.fire();
            break;

         case GEAR_TOP:
         case GEAR_LOWER:
           gear_interrupt.fire();
           break;

          case BRAKE_ON:
          case BRAKE_OFF:
            brake_interrupt.fire();
            break;
        }
        /* An alternative solution may delay the current thread. */
        /*RealtimeThread.currentRealtimeThread().sleep(...);*/
      }
    }
    else {
      incrementDelay();
    }
  }

  /* Additional methods that need to be defined for the infrastructure. */
  public StorageConfigurationParameters getThreadConfigurationParameters() {
    return null;
  }

  public void register() { }
}

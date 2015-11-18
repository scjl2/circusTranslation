package accs;

import javax.realtime.*;

import javax.safetycritical.*;
import javax.safetycritical.annotate.*;

//import static javax.safetycritical.annotate.Phase.*;

class MainMission extends Mission {
  /* Asynchronous events */
  private AsyncEvent shaft_interrupt;
  private AsyncEvent engine_interrupt;
  private AsyncEvent lever_interrupt;
  private AsyncEvent gear_interrupt;
  private AsyncEvent brake_interrupt;

  /* Aperiodic event handlers */
  private WheelShaft shaft;
  private Engine engine;
  private Lever lever;
  private Gear gear;
  private Brake brake;

  /* Periodic event handlers */
  private SpeedMonitor speedo;
  private ThrottleController throttle;

  /* Simulation */
  private Simulator sim;
  private SimulatorDriver driver;
  private ShaftSimulator shaft_sim;

  /* Cruise Control */
  private CruiseControl cruise;

  public int minPriority() {
    return javax.safetycritical.PriorityScheduler.instance().getMinPriority();
  }

  /* Create asynchronous events for interrupts */

  //@SCJRestricted(INITIALIZATION)
  private void createAsyncEvents() {
    shaft_interrupt = new AsyncEvent();
    engine_interrupt = new AsyncEvent();
    lever_interrupt = new AsyncEvent();
    gear_interrupt = new AsyncEvent();
    brake_interrupt = new AsyncEvent();
  }

  /* Create simulator components */

  //@SCJRestricted(INITIALIZATION)
  private void createSimulator() {
    /* The Simulator class reads the SCENARIO file */
    sim = new Simulator("SCENARIO");

    /* The SimulatorDriver class executes the simulation */
    driver = new SimulatorDriver(sim,
      engine_interrupt,
      lever_interrupt,
      brake_interrupt,
      gear_interrupt,
      new PriorityParameters(minPriority() + 8),
      new StorageConfigurationParameters(10000, 10000, 10000));

    /* The ShaftSimulater class simulates rotation of the shaft */
    shaft_sim = new ShaftSimulator(
      shaft_interrupt,
      new PriorityParameters(minPriority() + 5),
      new PeriodicParameters(null, new RelativeTime(1, 0)),
      new StorageConfigurationParameters(10000, 10000, 10000));
  }

  /* Create periodic event handlers: SpeedMoniter and ThrottleController */

  //@SCJRestricted(INITIALIZATION)
  public void createPeriodicHandlers() {
    /* The wheel shaft is not periodic but has to be created here. */
    shaft =
      new WheelShaft(
        new PriorityParameters(minPriority() + 2),
        new AperiodicParameters(),
        new StorageConfigurationParameters(10000, 10000, 10000));

    speedo = new SpeedMonitor(
      shaft,
      new PriorityParameters(minPriority() + 4),
      new PeriodicParameters(null, new RelativeTime(1000, 0)),
      new StorageConfigurationParameters(10000, 10000, 10000));

    throttle = new ThrottleController(
      shaft_sim, speedo,
      new PriorityParameters(minPriority() + 3),
      new StorageConfigurationParameters(10000, 10000, 10000));

  }

  /* Create cruise control main controller. */

  //@SCJRestricted(INITIALIZATION)
  private void createCruiseControl() {
    cruise = new CruiseControl(throttle, speedo, shaft_sim);
  }

  /* Create aperiodic event handlers: Engine, Lever, Gear and Brake. */

  //@SCJRestricted(INITIALIZATION)
  public void createAperiodicHandlers() {
    engine =
      new Engine(cruise, driver,
        new PriorityParameters(minPriority() + 6),
        new AperiodicParameters(),
        new StorageConfigurationParameters(10000, 10000, 10000));

    lever =
      new Lever(cruise, driver,
        new PriorityParameters(minPriority() + 6),
        new AperiodicParameters(),
        new StorageConfigurationParameters(10000, 10000, 10000));

    gear =
      new Gear(cruise, driver,
        new PriorityParameters(minPriority() + 6),
        new AperiodicParameters(),
        new StorageConfigurationParameters(10000, 10000, 10000));

    brake =
      new Brake(cruise, driver,
        new PriorityParameters(minPriority() + 6),
        new AperiodicParameters(),
        new StorageConfigurationParameters(10000, 10000, 10000));
  }

  /* Associate interrupts with event handlers */

  //@SCJRestricted(INITIALIZATION)
  private void registerInterrupts() {
    shaft_interrupt.addHandler(shaft);
    engine_interrupt.addHandler(engine);
    lever_interrupt.addHandler(lever);
    gear_interrupt.addHandler(gear);
    brake_interrupt.addHandler(brake);
  }

  /* Initialization methods for the mission. */

  public void initialize() {
    createAsyncEvents();
    createSimulator();
    createPeriodicHandlers();
    createCruiseControl();
    createAperiodicHandlers();
    registerInterrupts();
  }

  public long missionMemorySize() {
    return 131072;
  }
}

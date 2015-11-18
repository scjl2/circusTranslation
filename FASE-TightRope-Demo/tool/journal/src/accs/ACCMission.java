package accs;

import accs.isr.WheelShaftISR;
import accs.isr.EngineISR;
import accs.isr.BrakeISR;
import accs.isr.GearISR;
import accs.isr.LeverISR;

import javax.safetycritical.AperiodicEvent;
import javax.safetycritical.AperiodicLongEvent;
import javax.safetycritical.Mission;

import javax.safetycritical.annotate.*;

@MissionId("ACCMId")
class ACCMission extends Mission {
  /* Aperiodic Events */
  @InteractionCode private AperiodicEvent shaft_event;
  @InteractionCode private AperiodicLongEvent engine_event;
  @InteractionCode private AperiodicLongEvent lever_event;
  @InteractionCode private AperiodicLongEvent gear_event;
  @InteractionCode private AperiodicLongEvent brake_event;

  /* Interrupt Service Routines */
  @InteractionCode private WheelShaftISR shaft_isr;
  @InteractionCode private EngineISR engine_isr;
  @InteractionCode private BrakeISR brake_isr;
  @InteractionCode private GearISR gear_isr;
  @InteractionCode private LeverISR lever_isr;

  public ACCMission() { }

  /* Should we make handlers and data objects instance variables? */

  @InteractionCode
  @SCJRestricted(Restrict.INITIALIZE)
  private void createEvents() {
    shaft_event = new AperiodicEvent();
    engine_event = new AperiodicLongEvent();
    brake_event = new AperiodicLongEvent();
    gear_event = new AperiodicLongEvent();
    lever_event = new AperiodicLongEvent();
  }

  @InteractionCode
  @SCJRestricted(Restrict.INITIALIZE)
  private void createISRs() {
    shaft_isr = new WheelShaftISR(shaft_event);
    engine_isr = new EngineISR(engine_event);
    brake_isr = new BrakeISR(brake_event);
    gear_isr = new GearISR(gear_event);
    lever_isr = new LeverISR(lever_event);
  }

  @InteractionCode
  @SCJRestricted(Restrict.INITIALIZE)
  private void registerISRs() {
    shaft_isr.register();
    engine_isr.register();
    brake_isr.register();
    gear_isr.register();
    lever_isr.register();
  }

  @InteractionCode
  @SCJRestricted(Restrict.CLEANUP)
  private void unregisterISRs() {
    shaft_isr.unregister();
    engine_isr.unregister();
    brake_isr.unregister();
    gear_isr.unregister();
    lever_isr.unregister();
  }

  public void initialize() {
    createEvents();
    createISRs();
    registerISRs();
    WheelShaft shaft = new WheelShaft(shaft_event);
    SpeedMonitor speedo = new SpeedMonitor(shaft, 500);
    ThrottleController throttle = new ThrottleController(speedo);
    Controller cruise = new Controller(throttle, speedo);
    Engine engine = new Engine(cruise, engine_event);
    Brake brake = new Brake(cruise, brake_event);
    Gear gear = new Gear(cruise, gear_event);
    Lever lever = new Lever(cruise, lever_event);
    /* Register event handlers with the mission. */
    shaft.register();
    engine.register();
    brake.register();
    gear.register();
    lever.register();
    speedo.register();
    throttle.register();
  }

  public void cleanup() {
    unregisterISRs();
  }

  public long missionMemorySize() {
    return 131072;
  }
}

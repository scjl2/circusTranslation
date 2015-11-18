package accs;

import javax.realtime.AbsoluteTime;
import javax.realtime.RelativeTime;
import javax.realtime.PriorityParameters;
import javax.realtime.PeriodicParameters;

import javax.safetycritical.AperiodicLongEvent;
import javax.safetycritical.Mission;
import javax.safetycritical.PriorityScheduler;
import javax.safetycritical.Services;
import javax.safetycritical.StorageParameters;

public class MainMission extends Mission {
   private Events events;
   private Inputs inputs;
   private Interrupts interrupts;

   /* Create SCJ Events */
   public void createEvents() {
      events = new Events();
   }

   /* Create Input Objects */
   public void createInputs() {
      inputs = new Inputs();
   }

   /* Create Interrupt Handlers */
   public void createInterrupts() {
      interrupts = new Interrupts(events, inputs);
      interrupts.register();
      interrupts.setPriorities();
   }

   public void initialize() {
      /* Create SCJ Events (not in the Circus model) */
      createEvents();

      /* Create Input Objects (not in the Circus model) */
      createInputs();

      /* Create Interrupt Handlers (not in the Circus model) */
      createInterrupts();

      /* Aperiodic Event Handler: WheelShaft */
      WheelShaft wheel_shaft = new WheelShaft(
         new PriorityParameters(
            PriorityScheduler.instance().getMinPriority() + 2),
         new StorageParameters(10000, 10000, 10000),
         events.wheelshaft);

      wheel_shaft.register();

      /* Periodic Event Handler: SpeedMonitor */
      SpeedMonitor speedo = new SpeedMonitor(wheel_shaft,
         new PriorityParameters(
            PriorityScheduler.instance().getMinPriority() + 4),
         new PeriodicParameters(
            new AbsoluteTime(0, 0), new RelativeTime(1000, 0)),
         new StorageParameters(10000, 10000, 10000));

      speedo.register();

      /* Periodic Event Handler: ThrottleController */
      ThrottleController throttle = new ThrottleController(
         speedo,
         new PriorityParameters(
            PriorityScheduler.instance().getMinPriority() + 3),
         new StorageParameters(10000, 10000, 10000));

      throttle.register();

      /* Periodic Event Handler: VoltageOutput  */
      VoltageOutput voltage = new VoltageOutput(
         throttle,
         new PeriodicParameters(
            new AbsoluteTime(0, 0), new RelativeTime(100, 0)),
         new StorageParameters(10000, 10000, 10000));

      voltage.register();

      /* Data Object: CruiseControl */
      CruiseControl cruise = new CruiseControl(throttle, speedo);

      /* Aperiodic Event Handler: Engine */
      Engine engine = new Engine(cruise,
         new PriorityParameters(
            PriorityScheduler.instance().getMinPriority() + 6),
         new StorageParameters(10000, 10000, 10000),
         events.engine);

      engine.register();

      /* Brake Events */
      AperiodicLongEvent[] brake_events =
         { events.brake_engaged, events.brake_disengaged };

      /* Aperiodic Event Handler: Brake */
      Brake brake = new Brake(cruise,
         new PriorityParameters(
            PriorityScheduler.instance().getMinPriority() + 6),
         new StorageParameters(10000, 10000, 10000),
         brake_events);

      brake.register();


      /* Gear Events */
      AperiodicLongEvent[] gear_events =
         { events.top_gear_engaged, events.top_gear_disengaged };

      /* Aperiodic Event Handler: Gear */
      Gear gear = new Gear(cruise,
         new PriorityParameters(
            PriorityScheduler.instance().getMinPriority() + 6),
         new StorageParameters(10000, 10000, 10000),
         gear_events);

      gear.register();

      /* Aperiodic Event Handler: Lever */
      Lever lever = new Lever(cruise,
         new PriorityParameters(
            PriorityScheduler.instance().getMinPriority() + 6),
         new StorageParameters(10000, 10000, 10000),
         events.lever, inputs.lever);

      lever.register();
   }

   public long missionMemorySize() {
      return 131072;
   }
}

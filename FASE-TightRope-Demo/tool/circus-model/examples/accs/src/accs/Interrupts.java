package accs;

import accs.interrupts.WheelShaftInterruptHandler;
import accs.interrupts.EngineInterruptHandler;
import accs.interrupts.BrakeInterruptHandler;
import accs.interrupts.GearInterruptHandler;
import accs.interrupts.LeverInterruptHandler;

import javax.safetycritical.PriorityScheduler;
import javax.safetycritical.Services;

public class Interrupts {
   /* Interrupt Handlers of the Cruise Controller */
   public final WheelShaftInterruptHandler shaft_interrupt;
   public final EngineInterruptHandler engine_interrupt;
   public final BrakeInterruptHandler brake_interrupt;
   public final GearInterruptHandler gear_interrupt;
   public final LeverInterruptHandler lever_interrupt;

   /* Create Interrupts Handlers */
   public Interrupts(Events events, Inputs inputs) {
      shaft_interrupt = new WheelShaftInterruptHandler(events.wheelshaft);
      engine_interrupt = new EngineInterruptHandler(events.engine);
      brake_interrupt = new BrakeInterruptHandler(
         events.brake_engaged, events.brake_disengaged);
      gear_interrupt = new GearInterruptHandler(
         events.top_gear_engaged, events.top_gear_disengaged);
      lever_interrupt = new LeverInterruptHandler(events.lever, inputs.lever);
   }

   /* Register Interrupt Handlers */
   public void register() {
      shaft_interrupt.register();
      engine_interrupt.register();
      brake_interrupt.register();
      gear_interrupt.register();
      lever_interrupt.register();
   }

   /* Set Priority of Interrupt Handlers */
   public void setPriorities() {
      Services.setCeiling(shaft_interrupt,
         PriorityScheduler.instance().getMaxPriority());
      Services.setCeiling(engine_interrupt,
         PriorityScheduler.instance().getMaxPriority());
      Services.setCeiling(brake_interrupt,
         PriorityScheduler.instance().getMaxPriority());
      Services.setCeiling(gear_interrupt,
         PriorityScheduler.instance().getMaxPriority());
      Services.setCeiling(lever_interrupt,
         PriorityScheduler.instance().getMaxPriority());
   }
}

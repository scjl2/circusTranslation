package jtres;

import javax.realtime.AbsoluteTime;
import javax.realtime.RelativeTime;
import javax.realtime.PriorityParameters;
import javax.realtime.PeriodicParameters;

import javax.safetycritical.AperiodicLongEvent;
import javax.safetycritical.Mission;
import javax.safetycritical.PriorityScheduler;
import javax.safetycritical.Services;
import javax.safetycritical.StorageParameters;

/* All objects in this class are created in mission memory. */

public class MainMission extends Mission {
   /* Elements of the Architecture */
   private Events events;
   private Interrupts interrupts;

   /* Mission Memory Variable */
   private List list;

   public void initialize() {
      /* Initialise the Architecture (Not in the Circus Model) */
      initArchitecture();   

      /* Create Persistent Objects in MissionMemory */
      list = new List();

      /* Periodic Event Handler: Handler1 */
      Handler1 handler1 = new Handler1(list,
         new PriorityParameters(
            PriorityScheduler.instance().getNormPriority()),
         new PeriodicParameters(
            new AbsoluteTime(0, 0), new RelativeTime(100, 0)),
         new StorageParameters(4096, 4096, 4096));

      /* Register Handler */
      handler1.register();

      /* Aperiodic Event Handler: Handler2 */
      Handler2 handler2 = new Handler2(list,
         new PriorityParameters(
            PriorityScheduler.instance().getMinPriority() + 6),
         new StorageParameters(10000, 10000, 10000),
         events.out);

      /* Register Handler */
      handler2.register();
   }

   public void initArchitecture() {
      /* Create SCJ Events */
      createEvents();

      /* Create Interrupt Handlers */
      createInterrupts();
   }

   /* Create SCJ Events */
   public void createEvents() {
      events = new Events();
   }

   /* Create Interrupt Handlers */
   public void createInterrupts() {
      interrupts = new Interrupts(events);
      interrupts.register();
      interrupts.setPriorities();
   }

   public void cleanup() { }

   public long missionMemorySize() {
      return 131072;
   }
}

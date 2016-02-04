package javax.safetycritical;

import javax.realtime.PriorityParameters;
import javax.realtime.AperiodicParameters;

import javax.safetycritical.StorageParameters;
import javax.safetycritical.AperiodicEvent;

import javax.safetycritical.annotate.*;
import static javax.safetycritical.annotate.Level.*;
import static javax.safetycritical.annotate.Restrict.*;

@SCJAllowed(LEVEL_1)
public class AperiodicLongEventHandler {
   @SCJAllowed(LEVEL_1)
   @SCJRestricted(INITIALIZATION)
   public AperiodicLongEventHandler(
      PriorityParameters priority,
      AperiodicParameters release,
      StorageParameters storage,
      AperiodicLongEvent event) {
   }

   @SCJAllowed(LEVEL_1)
   @SCJRestricted(INITIALIZATION)
   public AperiodicLongEventHandler(
      PriorityParameters priority,
      AperiodicParameters release,
      StorageParameters storage,
      AperiodicLongEvent event,
      String name) {
   }

   @SCJAllowed(LEVEL_1)
   @SCJRestricted(INITIALIZATION)
   public AperiodicLongEventHandler(
      PriorityParameters priority,
      AperiodicParameters release,
      StorageParameters storage,
      AperiodicLongEvent[] events) {
   }

   @SCJAllowed(LEVEL_1)
   @SCJRestricted(INITIALIZATION)
   public AperiodicLongEventHandler(
      PriorityParameters priority,
      AperiodicParameters release,
      StorageParameters storage,
      AperiodicLongEvent[] events,
      String name) {
   }

   @SCJAllowed(LEVEL_1)
   @SCJRestricted(INITIALIZATION)
   public AperiodicLongEventHandler(
      PriorityParameters priority,
      StorageParameters storage,
      AperiodicLongEvent event) {
   }

   @SCJAllowed(LEVEL_1)
   @SCJRestricted(INITIALIZATION)
   public AperiodicLongEventHandler(
      PriorityParameters priority,
      StorageParameters storage,
      AperiodicLongEvent event,
      String name) {
   }

   @SCJAllowed(LEVEL_1)
   @SCJRestricted(INITIALIZATION)
   public AperiodicLongEventHandler(
      PriorityParameters priority,
      StorageParameters storage,
      AperiodicLongEvent[] events) {
   }

   @SCJAllowed(LEVEL_1)
   @SCJRestricted(INITIALIZATION)
   public AperiodicLongEventHandler(
      PriorityParameters priority,
      StorageParameters storage,
      AperiodicLongEvent[] events,
      String name) {
   }

   //@Override
   @SCJAllowed
   @SCJRestricted(INITIALIZATION)
   public final void register() { }
}

package javax.safetycritical;

import javax.realtime.PriorityParameters;
import javax.realtime.AperiodicParameters;

import javax.safetycritical.StorageParameters;
import javax.safetycritical.AperiodicEvent;

import javax.safetycritical.annotate.*;
import static javax.safetycritical.annotate.Level.*;
import static javax.safetycritical.annotate.Restrict.*;

@SCJAllowed(LEVEL_1)
public class AperiodicEventHandler {
   @SCJAllowed(LEVEL_1)
   @SCJRestricted(INITIALIZATION)
   public AperiodicEventHandler(
      PriorityParameters priority,
      AperiodicParameters release,
      StorageParameters storage,
      AperiodicEvent event) {
   }

   @SCJAllowed(LEVEL_1)
   @SCJRestricted(INITIALIZATION)
   public AperiodicEventHandler(
      PriorityParameters priority,
      AperiodicParameters release,
      StorageParameters storage,
      AperiodicEvent event,
      String name) {
   }

   @SCJAllowed(LEVEL_1)
   @SCJRestricted(INITIALIZATION)
   public AperiodicEventHandler(
      PriorityParameters priority,
      AperiodicParameters release,
      StorageParameters storage,
      AperiodicEvent[] events) {
   }

   @SCJAllowed(LEVEL_1)
   @SCJRestricted(INITIALIZATION)
   public AperiodicEventHandler(
      PriorityParameters priority,
      AperiodicParameters release,
      StorageParameters storage,
      AperiodicEvent[] events,
      String name) {
   }

   @SCJAllowed(LEVEL_1)
   @SCJRestricted(INITIALIZATION)
   public AperiodicEventHandler(
      PriorityParameters priority,
      StorageParameters storage,
      AperiodicEvent event) {
   }

   @SCJAllowed(LEVEL_1)
   @SCJRestricted(INITIALIZATION)
   public AperiodicEventHandler(
      PriorityParameters priority,
      StorageParameters storage,
      AperiodicEvent event,
      String name) {
   }

   @SCJAllowed(LEVEL_1)
   @SCJRestricted(INITIALIZATION)
   public AperiodicEventHandler(
      PriorityParameters priority,
      StorageParameters storage,
      AperiodicEvent[] events) {
   }

   @SCJAllowed(LEVEL_1)
   @SCJRestricted(INITIALIZATION)
   public AperiodicEventHandler(
      PriorityParameters priority,
      StorageParameters storage,
      AperiodicEvent[] events,
      String name) {
   }

   //@Override
   @SCJAllowed
   @SCJRestricted(INITIALIZATION)
   public final void register() { }
}

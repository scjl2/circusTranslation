package javax.safetycritical;

import javax.realtime.PriorityParameters;
import javax.realtime.PeriodicParameters;

import javax.safetycritical.StorageParameters;

import javax.safetycritical.annotate.*;
import static javax.safetycritical.annotate.Level.*;
import static javax.safetycritical.annotate.Restrict.*;

@SCJAllowed
public abstract class PeriodicEventHandler {
   @SCJAllowed
   @SCJRestricted(INITIALIZATION)
   public PeriodicEventHandler(
      PriorityParameters priority,
      PeriodicParameters release,
      StorageParameters storage) {
   }

   @SCJAllowed(LEVEL_1)
   @SCJRestricted(INITIALIZATION)
   public PeriodicEventHandler(
      PriorityParameters priority,
      PeriodicParameters release,
      StorageParameters storage,
      String name) {
   }

   //@Override
   @SCJAllowed
   @SCJRestricted(INITIALIZATION)
   public final void register() { }
}

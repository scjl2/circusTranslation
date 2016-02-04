package javax.safetycritical;

import javax.realtime.PriorityParameters;

import javax.safetycritical.annotate.*;
import static javax.safetycritical.annotate.Level.*;
import static javax.safetycritical.annotate.Restrict.*;

@SCJAllowed
public abstract class
MissionSequencer<SpecificMission extends Mission> extends ManagedEventHandler {

   @SCJAllowed
   @SCJRestricted(INITIALIZATION)
   public MissionSequencer(
      PriorityParameters priority,
      StorageParameters storage,
      String name) throws IllegalStateException {
      super(null, null, null, 0, "");
   }

   @SCJAllowed
   @SCJRestricted(INITIALIZATION)
   public MissionSequencer(
      PriorityParameters priority,
      StorageParameters storage) throws IllegalStateException {
      super(null, null, null, 0, "");
   }

   @SCJAllowed(SUPPORT)
   public abstract SpecificMission getNextMission();

   //@Override
   //@SCJAllowed(INFRASTRUCTURE)
   //public final void handleAsyncEvent() { }

   //@Override
   @SCJAllowed
   @SCJRestricted(INITIALIZATION)
   public final void register() { }

   @SCJAllowed(LEVEL_2)
   public final void requestSequenceTermination() { }

   @SCJAllowed(LEVEL_2)
   public final void sequenceTerminationPending() { }

   /* To quiet the compiler due to dependency with the RTSJ RI. */
   @Override
   public final void handleEvent() {
   }
}

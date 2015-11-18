package javax.safetycritical;

import javax.realtime.PriorityParameters;
import javax.realtime.PeriodicParameters;

import javax.safetycritical.annotate.*;

@SCJAllowed
public abstract class PeriodicEventHandler extends ManagedEventHandler {
  @SCJAllowed
  @SCJRestricted({Restrict.INITIALIZE})
  public PeriodicEventHandler(
    PriorityParameters priority,
    PeriodicParameters release,
    StorageParameters storage) {
    super (priority, release, storage, null);
  }

  @SCJAllowed(Level.LEVEL_1)
  @SCJRestricted({Restrict.INITIALIZE})
  public PeriodicEventHandler(
    PriorityParameters priority,
    PeriodicParameters release,
    StorageParameters storage,
    String name) {
    super (priority, release, storage, name);
  }

  @SCJAllowed @SCJRestricted({Restrict.INITIALIZE})
  public final void register() {
  }
}

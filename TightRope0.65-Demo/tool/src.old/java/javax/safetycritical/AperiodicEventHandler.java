package javax.safetycritical;

import javax.realtime.PriorityParameters;

import javax.safetycritical.annotate.*;

@SCJAllowed(Level.LEVEL_1)
public abstract class AperiodicEventHandler extends ManagedEventHandler {
  @SCJAllowed(Level.LEVEL_1)
  @SCJRestricted({Restrict.INITIALIZE})
  public AperiodicEventHandler(
    PriorityParameters priority,
    AperiodicParameters release,
    StorageParameters storage,
    AperiodicEvent event) {
    super (priority, null, storage, null);
  }

  @SCJAllowed(Level.LEVEL_1)
  @SCJRestricted({Restrict.INITIALIZE})
  public AperiodicEventHandler(
    PriorityParameters priority,
    AperiodicParameters release,
    StorageParameters storage,
    AperiodicEvent event,
    String name) {
    super (priority, null, storage, name);
  }

  @SCJAllowed(Level.LEVEL_1)
  @SCJRestricted({Restrict.INITIALIZE})
  public AperiodicEventHandler(
    PriorityParameters priority,
    AperiodicParameters release,
    StorageParameters storage,
    AperiodicEvent[] event) {
    super (priority, null, storage, null);
  }

  @SCJAllowed(Level.LEVEL_1)
  @SCJRestricted({Restrict.INITIALIZE})
  public AperiodicEventHandler(
    PriorityParameters priority,
    AperiodicParameters release,
    StorageParameters storage,
    AperiodicEvent[] events,
    String name) {
    super (priority, null, storage, name);
  }

  @SCJAllowed(Level.INFRASTRUCTURE)
  @SCJRestricted({Restrict.INITIALIZE})
  public final void register() {
  }
}

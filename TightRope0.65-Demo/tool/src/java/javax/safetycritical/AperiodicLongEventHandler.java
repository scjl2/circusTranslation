package javax.safetycritical;

import javax.realtime.PriorityParameters;

import javax.safetycritical.annotate.*;

@SCJAllowed(Level.LEVEL_1)
public abstract class AperiodicLongEventHandler
  extends ManagedLongEventHandler {
  @SCJAllowed(Level.LEVEL_1)
  @SCJRestricted({Restrict.INITIALIZE})
  public AperiodicLongEventHandler(
    PriorityParameters priority,
    AperiodicParameters release,
    StorageParameters storage,
    AperiodicLongEvent event) {
    super (priority, null, storage, null);
  }

  @SCJAllowed(Level.LEVEL_1)
  @SCJRestricted({Restrict.INITIALIZE})
  public AperiodicLongEventHandler(
    PriorityParameters priority,
    AperiodicParameters release,
    StorageParameters storage,
    AperiodicLongEvent event,
    String name) {
    super (priority, null, storage, name);
  }

  @SCJAllowed(Level.LEVEL_1)
  @SCJRestricted({Restrict.INITIALIZE})
  public AperiodicLongEventHandler(
    PriorityParameters priority,
    AperiodicParameters release,
    StorageParameters storage,
    AperiodicLongEvent[] event) {
    super (priority, null, storage, null);
  }

  @SCJAllowed(Level.LEVEL_1)
  @SCJRestricted({Restrict.INITIALIZE})
  public AperiodicLongEventHandler(
    PriorityParameters priority,
    AperiodicParameters release,
    StorageParameters storage,
    AperiodicLongEvent[] events,
    String name) {
    super (priority, null, storage, name);
  }

  @SCJAllowed(Level.INFRASTRUCTURE)
  @SCJRestricted({Restrict.INITIALIZE})
  public final void register() {
  }
}

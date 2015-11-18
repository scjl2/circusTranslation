package javax.safetycritical;

import javax.realtime.PriorityParameters;
import javax.realtime.ReleaseParameters;

import javax.safetycritical.annotate.*;

@SCJAllowed
public abstract class ManagedEventHandler extends BoundAsyncEventHandler
  implements ManagedSchedulable {
  @SCJAllowed
  @SCJRestricted({Restrict.INITIALIZE})
  protected ManagedEventHandler(
    PriorityParameters priority,
    ReleaseParameters release,
    StorageParameters storage,
    String name) {
  }

  @SCJAllowed
  @SCJRestricted({Restrict.CLEANUP})
  public void cleanup() {
  }

  @SCJAllowed
  public String getName() {
    return null;
  }

  @SCJAllowed(Level.INFRASTRUCTURE)
  @SCJRestricted({Restrict.INITIALIZE})
  public void register() {
  }
}

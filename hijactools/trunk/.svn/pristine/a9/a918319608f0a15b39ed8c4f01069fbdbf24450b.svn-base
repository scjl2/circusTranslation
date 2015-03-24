package javax.safetycritical;

import javax.safetycritical.annotate.*;

@SCJAllowed
public interface ManagedSchedulable extends Schedulable {
  @SCJAllowed(Level.INFRASTRUCTURE)
  public void register();

  @SCJAllowed(Level.SUPPORT)
  public void cleanup();
}

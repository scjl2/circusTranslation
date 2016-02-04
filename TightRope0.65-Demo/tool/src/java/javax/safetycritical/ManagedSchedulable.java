package javax.safetycritical;

import javax.realtime.Schedulable;

import javax.safetycritical.annotate.*;

@SCJAllowed
public interface ManagedSchedulable extends Schedulable {
  @FrameworkMethod
  @SCJAllowed(Level.INFRASTRUCTURE)
  public void register();

  @SCJAllowed(Level.SUPPORT)
  public void cleanup();
}

package javax.safetycritical;

import javax.safetycritical.annotate.*;

@SCJAllowed(Level.LEVEL_1)
public abstract class InterruptServiceRoutine {
  @SCJAllowed(Level.LEVEL_1)
  public InterruptServiceRoutine(String name) { }

  @SCJAllowed(Level.LEVEL_1)
  public final int getId() {
    return 0;
  }

  @SCJAllowed(Level.LEVEL_1)
  protected abstract void handle();

  @SCJAllowed(Level.LEVEL_1)
  public final boolean isRegistered() {
    return false;
  }

  @SCJAllowed(Level.LEVEL_1)
  public void register() { }

  @SCJAllowed(Level.LEVEL_1)
  public void unregister() { }
}

package javax.realtime;

import javax.safetycritical.annotate.*;

@SCJAllowed
public abstract class ReleaseParameters {
  @SCJAllowed
  protected ReleaseParameters() { }

  @SCJAllowed(Level.LEVEL_1)
  protected ReleaseParameters(
    RelativeTime deadline, AsyncEventHandler missHandler) { }

  @SCJAllowed(Level.LEVEL_1)
  public Object clone() {
    return null;
  }

  @SCJAllowed(Level.LEVEL_1)
  public RelativeTime getDeadline() {
    return null;
  }

  @SCJAllowed(Level.LEVEL_1)
  public AsyncEventHandler getDeadlineMissHandler() {
    return null;
  }
}

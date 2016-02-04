package javax.safetycritical;

import javax.safetycritical.annotate.*;

@SCJAllowed
public abstract class Mission {
  @SCJAllowed
  public Mission() {
  }

  @SCJAllowed(Level.SUPPORT)
  protected void cleanup() {
  }

  @SCJAllowed(Level.SUPPORT)
  protected abstract void initialize();

  @SCJAllowed
  public void requestTermination() {
  }

  @SCJAllowed
  public final void requestSequenceTermination() {
  }

  @SCJAllowed
  public final boolean terminationPending() {
    return false;
  }

  @SCJAllowed
  public final boolean sequenceTerminationPending() {
    return false;
  }

  @SCJAllowed
  public static Mission getCurrentMission() {
    return null;
  }
}

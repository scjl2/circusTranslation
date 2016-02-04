package javax.safetycritical;

import javax.safetycritical.annotate.*;

@SCJAllowed
public abstract class Mission {
  @SCJAllowed
  public Mission() {
  }

  @ActiveMethod
  @SCJAllowed(Level.SUPPORT)
  protected abstract void initialize();

  @ActiveMethod
  @SCJAllowed(Level.SUPPORT)
  protected void cleanup() {
  }

  @FrameworkMethod
  @SCJAllowed(Level.SUPPORT)
  public void requestTermination() {
  }

  @FrameworkMethod
  @SCJAllowed(Level.SUPPORT)
  public final boolean terminationPending() {
    return false;
  }

  @SCJAllowed
  protected abstract long missionMemorySize();

  @FrameworkMethod
  @SCJAllowed
  public final void requestSequenceTermination() {
  }

  @FrameworkMethod
  @SCJAllowed
  public final boolean sequenceTerminationPending() {
    return false;
  }

  @SCJAllowed
  public static Mission getCurrentMission() {
    return null;
  }
}

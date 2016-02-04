package javax.safetycritical;

import javax.realtime.BoundAsyncEventHandler;
import javax.realtime.PriorityParameters;

import javax.safetycritical.annotate.*;

@SCJAllowed
public abstract class MissionSequencer extends BoundAsyncEventHandler {
  @SCJAllowed
  @SCJRestricted({Restrict.INITIALIZE})
  public MissionSequencer(
    PriorityParameters priority,
    StorageParameters storage) {
  }

  @ActiveMethod
  @SCJAllowed(Level.SUPPORT)
  protected abstract Mission getNextMission();

  @SCJAllowed(Level.SUPPORT)
  public final void handleAsyncEvent() {
  }

  @ActiveMethod
  @SCJRestricted({Restrict.INITIALIZE})
  public final void register() {
  }

  @ActiveMethod
  @SCJAllowed(Level.LEVEL_2)
  public final void requestSequenceTermination() {
  }
}

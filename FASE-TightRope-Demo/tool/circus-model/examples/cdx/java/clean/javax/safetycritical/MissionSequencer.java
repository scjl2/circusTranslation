package javax.safetycritical;

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

  @SCJAllowed(Level.SUPPORT)
  protected abstract Mission getNextMission();

  @SCJAllowed(Level.SUPPORT)
  public final void handleAsyncEvent() {
  }

  @SCJRestricted({Restrict.INITIALIZE})
  public final void register() {
  }

  @SCJAllowed(Level.LEVEL_2)
  public final void requestSequenceTermination() {
  }
}

package javax.realtime;

import javax.safetycritical.annotate.*;

@SCJAllowed
public class PeriodicParameters extends ReleaseParameters {
  @SCJAllowed
  public PeriodicParameters(HighResolutionTime start, RelativeTime period) {
  }

  @SCJAllowed(Level.LEVEL_1)
  public PeriodicParameters(HighResolutionTime start, RelativeTime period,
    RelativeTime deadline, AsyncEventHandler missHandler) {
  }

  @SCJAllowed
  public HighResolutionTime getStart() {
    return null;
  }

  @SCJAllowed
  public RelativeTime getPeriod() {
    return null;
  }

  @SCJAllowed
  public RelativeTime getDeadline() {
    return null;
  }

  @SCJAllowed
  public AsyncEventHandler getHandler() {
    return null;
  }
}

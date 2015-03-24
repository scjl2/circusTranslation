package javax.safetycritical;

import javax.realtime.ReleaseParameters;
import javax.realtime.RelativeTime;
import javax.realtime.AsyncEventHandler;

import javax.safetycritical.annotate.*;

@SCJAllowed(Level.LEVEL_1)
public class AperiodicParameters extends ReleaseParameters {
  @SCJAllowed(Level.LEVEL_1)
  public AperiodicParameters(
    RelativeTime deadline,
    AsyncEventHandler missHandler) {
  }
}

package javax.safetycritical;

import javax.safetycritical.annotate.*;

@SCJAllowed(Level.LEVEL_1)
public class AperiodicLongEvent extends AsyncLongEvent {
  @SCJAllowed(Level.LEVEL_1)
  @SCJRestricted({Restrict.INITIALIZE})
  public AperiodicLongEvent() {
  }
}

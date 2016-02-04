package javax.safetycritical;

import javax.safetycritical.annotate.*;

@SCJAllowed(Level.LEVEL_1)
public class AperiodicEvent extends AsyncEvent {
  @SCJAllowed(Level.LEVEL_1)
  @SCJRestricted({Restrict.INITIALIZE})
  public AperiodicEvent() {
  }

  public AperiodicEvent(AperiodicEventHandler handler) {
  }

}

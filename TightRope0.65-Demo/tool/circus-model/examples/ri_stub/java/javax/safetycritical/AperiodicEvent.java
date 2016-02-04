package javax.safetycritical;

import javax.realtime.AsyncEvent;

import javax.safetycritical.annotate.*;
import static javax.safetycritical.annotate.Level.*;
import static javax.safetycritical.annotate.Restrict.*;

@SCJAllowed(LEVEL_1)
public class AperiodicEvent extends AsyncEvent {
   @SCJAllowed(LEVEL_1)
   @SCJRestricted(INITIALIZATION)
   public AperiodicEvent() { }
}

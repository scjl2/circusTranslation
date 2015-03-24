package javax.safetycritical;

import javax.realtime.AsyncLongEvent;

import javax.safetycritical.annotate.*;
import static javax.safetycritical.annotate.Level.*;
import static javax.safetycritical.annotate.Restrict.*;

@SCJAllowed(LEVEL_1)
public class AperiodicLongEvent extends AsyncLongEvent {
   @SCJAllowed(LEVEL_1)
   @SCJRestricted(INITIALIZATION)
   public AperiodicLongEvent() { }
}

package javax.realtime;

import javax.safetycritical.annotate.*;
import static javax.safetycritical.annotate.Level.*;
import static javax.safetycritical.annotate.Restrict.*;

@SCJAllowed(LEVEL_1)
public class AsyncEvent extends AbstractAsyncEvent {
   protected AsyncEvent() { }

   @SCJAllowed(LEVEL_1)
   public void fire() {
   }
}

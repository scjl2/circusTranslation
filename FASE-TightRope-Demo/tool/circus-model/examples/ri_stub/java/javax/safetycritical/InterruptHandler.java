package javax.safetycritical;

import javax.safetycritical.annotate.*;
import static javax.safetycritical.annotate.Level.*;
import static javax.safetycritical.annotate.Restrict.*;

@SCJAllowed(LEVEL_1)
public abstract class InterruptHandler {
   @SCJAllowed(LEVEL_1)
   public InterruptHandler(int InterruptID) throws IllegalStateException { }

   @SCJAllowed(LEVEL_1)
   public static int getInterruptPriority(int InterruptID)
      throws IllegalArgumentException { return 0; }

   @SCJAllowed(LEVEL_1)
   public synchronized  void handleInterrupt() { }
}

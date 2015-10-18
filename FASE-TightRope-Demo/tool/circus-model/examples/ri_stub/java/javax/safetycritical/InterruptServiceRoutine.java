package javax.safetycritical;

import javax.safetycritical.annotate.*;
import static javax.safetycritical.annotate.Level.*;
import static javax.safetycritical.annotate.Restrict.*;

@SCJAllowed(LEVEL_1)
public abstract class InterruptServiceRoutine {
   protected String name;

   @SCJAllowed(LEVEL_1)
   public InterruptServiceRoutine(String name) {
      this.name = name;
   }

   @SCJAllowed(LEVEL_1)
   public final int getId() {
      return 0;
   }

   @SCJAllowed(LEVEL_1)
   public final String getName() {
      return name;
   }

   @SCJAllowed(LEVEL_1)
   protected abstract void handle();

   @SCJAllowed(LEVEL_1)
   public final boolean isRegistered() {
      return false;
   }

   @SCJAllowed(LEVEL_1)
   public void register() { }

   @SCJAllowed(LEVEL_1)
   public void unregister() { }
}

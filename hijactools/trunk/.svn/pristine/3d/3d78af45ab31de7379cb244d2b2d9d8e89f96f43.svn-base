package javax.safetycritical;

import javax.realtime.HighResolutionTime;

import javax.safetycritical.annotate.*;
import static javax.safetycritical.annotate.Level.*;
import static javax.safetycritical.annotate.Restrict.*;

@SCJAllowed
public abstract class Services {
   @SCJAllowed
   public static void captureBackTrace(Throwable association) { }

   @SCJAllowed(LEVEL_2)
   @SCJRestricted(MAY_BLOCK)
   public static void delay(int ns_delay) { }

   @SCJAllowed(LEVEL_2)
   @SCJRestricted(MAY_BLOCK)
   public static void delay(HighResolutionTime delay) { }

   @SCJAllowed(LEVEL_1)
   public static int getDefaultCeiling() { return 0; }

   @SCJAllowed
   public static Level getDeploymentLevel() { return LEVEL_0; }

   @SCJAllowed(LEVEL_1)
   public static int getInterruptPriority(int InterruptID)
      throws IllegalArgumentException { return 0; }

   @SCJAllowed(LEVEL_1)
   public static void nanoSpin(int nanos) { }

   @SCJAllowed(LEVEL_1)
   public static void registerInterruptHandler(int InterruptID,
      InterruptHandler IH) throws IllegalArgumentException { }

   @SCJAllowed(LEVEL_1)
   @SCJRestricted(INITIALIZATION)
   public static void setCeiling(Object o, int pri)
      throws IllegalThreadStateException { }
}

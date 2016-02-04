package javax.safetycritical;

import javax.safetycritical.annotate.*;
import static javax.safetycritical.annotate.Level.*;
import static javax.safetycritical.annotate.Restrict.*;

@SCJAllowed
public interface Safelet<MissionLevel extends Mission> {
   @SCJAllowed(SUPPORT)
   @SCJRestricted(INITIALIZATION)
   public MissionSequencer<MissionLevel> getSequencer();

   @SCJAllowed(SUPPORT)
   @SCJRestricted(INITIALIZATION)
   public void setUp();

   @SCJAllowed(SUPPORT)
   @SCJRestricted(CLEANUP)
   public void tearDown();
}

package javax.safetycritical;

import javax.safetycritical.annotate.*;

@SCJAllowed
public interface Safelet {
  @SCJAllowed(Level.SUPPORT)
  @SCJRestricted(Restrict.INITIALIZE)
  public void setup();

  @SCJAllowed(Level.SUPPORT)
  @SCJRestricted(Restrict.INITIALIZE)
  public MissionSequencer getSequencer();

  @SCJAllowed(Level.SUPPORT)
  @SCJRestricted(Restrict.CLEANUP)
  public void teardown();
}

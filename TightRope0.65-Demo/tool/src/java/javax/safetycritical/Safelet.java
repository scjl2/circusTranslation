package javax.safetycritical;

import javax.safetycritical.annotate.*;

@SCJAllowed
public interface Safelet {
  @ActiveMethod
  @SCJAllowed(Level.SUPPORT)
  @SCJRestricted(Restrict.INITIALIZE)
  public void setup();

  @Ignore
  @SCJAllowed(Level.SUPPORT)
  @SCJRestricted(Restrict.INITIALIZE)
  public MissionSequencer getSequencer();

  @ActiveMethod
  @SCJAllowed(Level.SUPPORT)
  @SCJRestricted(Restrict.CLEANUP)
  public void teardown();
}

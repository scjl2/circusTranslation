package javax.safetycritical;

import javax.safetycritical.annotate.*;

@SCJAllowed(Level.LEVEL_0)
public interface RawIntegralAccess {

  @SCJAllowed(Level.LEVEL_0)
  @SCJRestricted({Restrict.INTERRUPT_SAFE})
  public byte getByte(long offset);

  @SCJAllowed(Level.LEVEL_0)
  @SCJRestricted({Restrict.INTERRUPT_SAFE})
  public void getBytes(long offset, byte[] bytes, int low, int number);

  @SCJAllowed(Level.LEVEL_0)
  @SCJRestricted({Restrict.INTERRUPT_SAFE})
  public void setByte(long offset, byte value);

  @SCJAllowed(Level.LEVEL_0)
  @SCJRestricted({Restrict.INTERRUPT_SAFE})
  public void setBytes(long offset, byte[] bytes, int low, int number);
}

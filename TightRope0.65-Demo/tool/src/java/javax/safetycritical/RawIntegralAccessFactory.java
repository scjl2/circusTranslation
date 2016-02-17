package javax.safetycritical;

import javax.safetycritical.annotate.*;

@SCJAllowed(Level.LEVEL_0)
public interface RawIntegralAccessFactory {
  @SCJAllowed(Level.LEVEL_0)
  public RawMemoryName getName();

  @SCJAllowed(Level.LEVEL_0)
  public RawIntegralAccess newIntegralAccess(long base, long size);
}

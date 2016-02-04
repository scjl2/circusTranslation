package javax.safetycritical;

import java.lang.reflect.InvocationTargetException;

import javax.safetycritical.annotate.*;

@SCJAllowed(Level.LEVEL_0)
public class RawMemory {
  @SCJAllowed(Level.LEVEL_0)
  public static final RawMemoryName DMA_ACCESS = null;

  @SCJAllowed(Level.LEVEL_0)
  public static final RawMemoryName MEM_ACCESS = null;

  @SCJAllowed(Level.LEVEL_0)
  public static final RawMemoryName IO_PORT_MAPPED = null;

  @SCJAllowed(Level.LEVEL_0)
  public static final RawMemoryName IO_MEM_MAPPED = null;

  @SCJAllowed(Level.LEVEL_0)
  public static RawIntegralAccess createRawIntegralInstance(
      RawMemoryName type, long base, long size) throws
        InstantiationException,
        IllegalAccessException,
        InvocationTargetException {
    return null;
  }

  @SCJAllowed(Level.LEVEL_0)
  public static void registerAccessFactory(
    RawIntegralAccessFactory factory) {
  }
}

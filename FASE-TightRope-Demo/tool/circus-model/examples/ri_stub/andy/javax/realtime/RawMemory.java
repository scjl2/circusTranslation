package javax.realtime;

import javax.safetycritical.annotate.SCJAllowed;

@SCJAllowed
public final class RawMemory
{
  @SCJAllowed
  public static final RawMemoryName DMA_ACCESS = new RawMemoryName(){};

  @SCJAllowed
  public static final RawMemoryName MEM_ACCESS = new RawMemoryName(){};

  @SCJAllowed
  public static final RawMemoryName IO_PORT_MAPPED = new RawMemoryName(){};

  @SCJAllowed
  public static final RawMemoryName IO_MEM_MAPPED = new RawMemoryName(){};

  public static RawByte createRawByteAccessInstance(RawMemoryName type,
                                                    long base) {
    return null;
  }
  
  public static RawByte createRawByteAccessArrayInstance(RawMemoryName type,
                                                         long base) {
    return null;
  }
  
  public static RawInt 	createRawIntAccessInstance(RawMemoryName type,
                                                   long base) {
    return null;
  }
  
  public static RawLong createRawLongAccessInstance(RawMemoryName type,
                                                    long base) {
    return null;
  }

  @SCJAllowed
  public static RawIntegralAccess createRawIntegralInstance(RawMemoryName type,
                                                            long base,
                                                            long size)
  {
    return null;
  }
  
  @SCJAllowed
  public static void
  registerRawIntegralAccessFactory(RawIntegralAccessFactory factory)
  {
  }
  
  public static RawIntegralAccess
  createRawIntegralAccessInstance(RawMemoryName ioMemMapped,
                                  long controlRegAddr, int i) {
    // TODO Auto-generated method stub
    return null;
  }
}
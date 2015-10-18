package javax.realtime;

import javax.safetycritical.annotate.*;

@SCJAllowed
public abstract class MemoryArea implements AllocationContext {
  @SCJAllowed
  public MemoryArea() {
  }

  @SCJAllowed
  public static MemoryArea getMemoryArea(Object object) {
    return null;
  }

}

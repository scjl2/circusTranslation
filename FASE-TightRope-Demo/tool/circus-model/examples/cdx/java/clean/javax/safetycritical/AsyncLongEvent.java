package javax.safetycritical;

import javax.safetycritical.annotate.*;

@SCJAllowed(Level.LEVEL_1)
public class AsyncLongEvent extends AbstractAsyncEvent {
  @SCJAllowed(Level.LEVEL_1)
  public void fire(long value) {
  }
}

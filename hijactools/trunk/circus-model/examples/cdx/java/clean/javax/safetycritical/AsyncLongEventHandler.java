package javax.safetycritical;

import javax.safetycritical.annotate.*;

@SCJAllowed
public abstract class AsyncLongEventHandler extends AbstractAsyncEventHandler {
  @SCJAllowed(Level.SUPPORT)
  public abstract void handleAsyncLongEvent(long value);
}

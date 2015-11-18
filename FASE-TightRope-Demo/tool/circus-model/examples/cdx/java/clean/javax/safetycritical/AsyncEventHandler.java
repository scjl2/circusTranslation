package javax.safetycritical;

import javax.safetycritical.annotate.*;

@SCJAllowed
public abstract class AsyncEventHandler extends AbstractAsyncEventHandler {
  @SCJAllowed(Level.SUPPORT)
  public abstract void handleAsyncEvent();
}

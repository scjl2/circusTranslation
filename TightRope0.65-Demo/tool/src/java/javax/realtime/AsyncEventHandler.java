package javax.realtime;

import javax.safetycritical.annotate.*;

@SCJAllowed
public abstract class AsyncEventHandler extends AbstractAsyncEventHandler {
  @Ignore
  @SCJAllowed(Level.SUPPORT)
  public abstract void handleAsyncEvent();
}

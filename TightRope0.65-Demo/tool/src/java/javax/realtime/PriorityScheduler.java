package javax.realtime;

import javax.safetycritical.annotate.*;

@SCJAllowed(Level.LEVEL_1)
public class PriorityScheduler {
  @SCJAllowed(Level.LEVEL_1)
  public int getMaxPriority() {
    return 0;
  }

  @SCJAllowed(Level.LEVEL_1)
  public int getNormPriority() {
    return 0;
  }

  @SCJAllowed(Level.LEVEL_1)
  public int getMinPriority() {
    return 0;
  }

  public static PriorityScheduler instance() {
    return new PriorityScheduler();
  }

}

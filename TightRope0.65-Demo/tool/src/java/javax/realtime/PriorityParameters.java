package javax.realtime;

import javax.safetycritical.annotate.*;

@SCJAllowed
public class PriorityParameters extends SchedulingParameters {
  @SCJAllowed
  public PriorityParameters(int priority) {
  }

  @SCJAllowed
  public int getPriority() {
    return 0;
  }
}

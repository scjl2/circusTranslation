package javax.realtime;

import javax.safetycritical.annotate.*;

/* TODO: The stub for this class is not fully specified yet. */

@SCJAllowed
public class AbsoluteTime extends HighResolutionTime {
  @SCJAllowed
  public AbsoluteTime(long ms, int ns) {
  }

  public long getMilliseconds() {
    return 0;
  }

  public long getNanoseconds() {
    return 0;
  }

  public AbsoluteTime subtract(AbsoluteTime at) {
    return at;
  }

  public void set(long ms, long ns) {
  }

  public AbsoluteTime add(int millis, int nanos) {
     return null;
  }

}

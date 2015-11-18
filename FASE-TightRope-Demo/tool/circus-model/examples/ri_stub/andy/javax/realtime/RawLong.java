package javax.realtime;

/**
 * An object to access a byte in memory in raw mode outside RTSJ memory areas.
 */
public abstract class RawLong
{
  /**
   * An object to access a long in memory in raw mode outside RTSJ memory areas.
   */
  public abstract long get();

  public abstract void set(long value);
}

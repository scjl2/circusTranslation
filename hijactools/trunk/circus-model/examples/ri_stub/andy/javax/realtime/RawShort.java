package javax.realtime;

/**
 * An object to access a byte in memory in raw mode outside RTSJ memory areas.
 */
public abstract class RawShort
{
  /**
   * An object to access a short in memory in raw mode outside RTSJ
   * memory areas.
   */
  public abstract short get();

  public abstract void set(short value);
}

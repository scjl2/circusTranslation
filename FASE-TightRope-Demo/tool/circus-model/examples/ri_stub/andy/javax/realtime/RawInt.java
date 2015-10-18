package javax.realtime;

/**
 * An object to access an int in memory in raw mode outside RTSJ memory areas.
 */
public abstract class RawInt
{
  public abstract int get();

  public abstract void put(int value);
}

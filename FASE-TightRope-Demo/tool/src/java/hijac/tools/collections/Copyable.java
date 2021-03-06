package hijac.tools.collections;

/**
 * <p>Ability of an object to create a copy of itself.</p>
 *
 * <p>This is an extension to implementing the {@link Cloneable} 
 * interface. The fact that the value returned is typed  removes
 * the inconvenience of dynamic objects casts when cloning.</p>
 *
 * @author Frank Zeyda
 * @version $Revision: 210 $
 */
public interface Copyable<T> extends Cloneable {
   /**
    * Returns a copy of this object. It is not further specified here
    * whether the copy is deep or shallow.
    *
    * @return A copy of this object which can be deep or shallow.
    */
   public T copy();
}

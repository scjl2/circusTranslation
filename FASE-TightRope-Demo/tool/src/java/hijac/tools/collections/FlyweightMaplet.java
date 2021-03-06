package hijac.tools.collections;

import java.lang.ref.WeakReference;

import java.util.WeakHashMap;

/* Fly-weight construction of Maplet<A, B> objects. */

/**
 * @author Frank Zeyda
 * @version $Revision: 198 $
 */
public class FlyweightMaplet {
   protected static final
      WeakHashMap<Maplet, WeakReference<Maplet>>
         OBJECT_STORE = new WeakHashMap<Maplet, WeakReference<Maplet>>();

   @SuppressWarnings("unchecked")
   public static <A, B> Maplet<A, B> create(A x, B y) {
      /* Can we avoid to always construct a maplet? */
      Maplet<A, B> maplet = new Maplet<A, B>(x, y);
      Maplet<A, B> result = null;
      WeakReference<Maplet> ref = OBJECT_STORE.get(maplet);
      if (ref != null) {
         result = (Maplet<A, B>) ref.get();
      }
      if (result == null) {
         OBJECT_STORE.put(maplet, new WeakReference<Maplet>(maplet));
         result = maplet;
      }
      return result;
   }
}

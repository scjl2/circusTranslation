package hijac.tools.utils;

import java.util.Map;
import java.util.WeakHashMap;

/* The purpose of this class is to generate unique ids for Java Objects. Note
 * that System.identityHashCode() is not suitable for this. In general, the
 * contract of hashCode() does not require the ids to be unique, and it is
 * easy even in practice to produce collisions where similar objects have
 * the same hash code. The solution below, though less efficient, is safe. */

/**
 * @author Frank Zeyda
 * @version $Revision: 198 $
 */
public final class ObjectIds {
   public static final long INIT_ID = 0;

   private final WeakHashMap<Object, Long> id_map;
   private long next;
   private boolean overflow;

   private static ObjectIds instance;

   private ObjectIds() {
      id_map = new WeakHashMap<Object, Long>();
      next = INIT_ID;
      overflow = false;
   }

   /* The look-up may become slow once we iterate through the entire range
    * of long values. In practice, I suppose this will never happen. */

   private void incNext() {
      if (overflow) {
         long tmp = next;
         do {
            next++;
            if (next == tmp) {
               throw new AssertionError("Overflow of object id map.");
            }
         } while (id_map.containsValue(next));
      }
      else {
         if (next == INIT_ID) {
            overflow = true;
            incNext();
         }
         else {
            next++;
         }
      }
   }

   /* There is no notable reason to expose the following method. */

   private synchronized long get(Object o) {
      Long id = id_map.get(o);
      if (id == null) {
         incNext();
         id_map.put(o, next);
         return next;
      }
      assert id != null;
      return id;
   }

   /* There is no notable reason to expose the following method. */

   private static ObjectIds instance() {
      if (instance == null) {
         instance = new ObjectIds();
      }
      return instance;
   }

   /* The following is the only method exposed by the class. */

   public static long getId(Object o) {
      return instance().get(o);
   }
}

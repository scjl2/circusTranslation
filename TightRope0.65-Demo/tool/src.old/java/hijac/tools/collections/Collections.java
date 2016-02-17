package hijac.tools.collections;

import java.util.Collection;

/**
 * @author Frank Zeyda
 * @version $Revision: 198 $
 */
public class Collections {
   public static <T> void addAll(Collection<T> c, Iterable<? extends T> iter) {
      for (T o : iter) {
         c.add(o);
      }
   }
}

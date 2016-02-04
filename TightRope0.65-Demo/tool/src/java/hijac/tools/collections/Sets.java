package hijac.tools.collections;

import java.util.Set;

/**
 * @author Frank Zeyda
 * @version $Revision: 198 $
 */
public class Sets {
   public static <T> T Choice(Set<T> c) {
      return c.iterator().next();
   }
}

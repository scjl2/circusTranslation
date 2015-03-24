package hijac.tools.collections;

import java.util.Collections;

/**
 * @author Frank Zeyda
 * @version $Revision: 198 $
 */
public class Arrays {
   public static <T> boolean disjoint(T[] array1, T[] array2) {
      return Collections.disjoint(
         java.util.Arrays.asList(array1),
         java.util.Arrays.asList(array2));
   }
}

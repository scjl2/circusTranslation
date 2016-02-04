package hijac.tools.utils;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author Frank Zeyda
 * @version $Revision: 198 $
 */
public class Strings {
   public static String[] suffixArray(String[] array, String suffix) {
      String[] result = new String[array.length];
      for (int i = 0; i < array.length; i++) {
         result[i] = array[i] + suffix;
      }
      return result;
   }

   public static <T> String toString(Collection<T> c, StringConverter<T> conv,
      String left, String right, String sep) {
      StringBuilder result = new StringBuilder();
      result.append(left);
      for (Iterator<T> iter = c.iterator(); iter.hasNext(); ) {
         result.append(conv.toString(iter.next()));
         if (iter.hasNext()) { result.append(sep); }
      }
      result.append(right);
      return result.toString();
   }
}

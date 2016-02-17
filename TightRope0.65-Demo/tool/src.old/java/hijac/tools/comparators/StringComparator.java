package hijac.tools.comparators;

import java.util.Comparator;

/**
 * @author Frank Zeyda
 * @version $Revision: 198 $
 */
public class StringComparator<T> implements Comparator<T> {
   public int compare(T o1, T o2) {
      return o1.toString().compareTo(o2.toString());
   }
}

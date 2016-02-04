package hijac.tools.collections;

import hijac.tools.comparators.ExecutableElementComparator;
import hijac.tools.comparators.StringComparator;
import hijac.tools.comparators.TypeElementComparator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

/**
 * @author Frank Zeyda
 * @version $Revision: 198 $
 */
public class Sorters {
   public static <T> List<T> LEXICAL(Collection<T> elements) {
      List<T> result = new ArrayList<T>(elements);
      Collections.sort(result, new StringComparator<T>());
      return result;
   }

   public static List<TypeElement>
      TypeElement(Collection<TypeElement> elements) {
      List<TypeElement> result =
         new ArrayList<TypeElement>(elements);
      Collections.sort(result, new TypeElementComparator());
      return result;
   }

   public static List<ExecutableElement>
         ExecutableElement(Collection<ExecutableElement> elements) {
      List<ExecutableElement> result =
         new ArrayList<ExecutableElement>(elements);
      Collections.sort(result, new ExecutableElementComparator());
      return result;
   }
}

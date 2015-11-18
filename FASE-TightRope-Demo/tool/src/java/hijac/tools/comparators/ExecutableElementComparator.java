package hijac.tools.comparators;

import hijac.tools.utils.ModelUtils;

import java.util.Comparator;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;

/**
 * @author Frank Zeyda
 * @version $Revision: 206 $
 */
public class ExecutableElementComparator
      implements Comparator<ExecutableElement> {
   public int compare(ExecutableElement e1, ExecutableElement e2) {
      int result;
      String s1 = ModelUtils.removeAnonymous(
         e1.getEnclosingElement().toString());
      String s2 = ModelUtils.removeAnonymous(
         e2.getEnclosingElement().toString());
      result = s1.compareTo(s2);
      if (result == 0) {
         if (e1.getKind().equals(ElementKind.CONSTRUCTOR) &&
            e2.getKind().equals(ElementKind.METHOD)) {
            result = -1;
         }
         if (e1.getKind().equals(ElementKind.METHOD) &&
            e2.getKind().equals(ElementKind.CONSTRUCTOR)) {
            result = 1;
         }
      }
      if (result == 0) {
         assert e1.getKind().equals(e2.getKind());
         result = e1.toString().compareTo(e2.toString());
      }
      return result;
   }
}

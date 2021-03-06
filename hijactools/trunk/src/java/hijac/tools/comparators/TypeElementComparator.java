package hijac.tools.comparators;

import hijac.tools.utils.ModelUtils;

import java.util.Comparator;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

/**
 * @author Frank Zeyda
 * @version $Revision: 206 $
 */
public class TypeElementComparator implements Comparator<TypeElement> {
   public int compare(TypeElement e1, TypeElement e2) {
      String s1 = ModelUtils.removeAnonymous(e1.toString());
      String s2 = ModelUtils.removeAnonymous(e2.toString());
      return s1.compareTo(s2);
   }
}

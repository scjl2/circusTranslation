package hijac.tools.analysis.utils;

import hijac.tools.comparators.Comparators;

import java.util.Comparator;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

/**
 * @author Frank Zeyda
 * @version $Revision: 198 $
 */
public class TypeElementComparator implements Comparator<TypeElement> {
   public final Types typeUtils;

   public TypeElementComparator(Types typeUtils) {
      this.typeUtils = typeUtils;
   }

   public int compare(TypeElement e1, TypeElement e2) {
      TypeMirror t1 = e1.asType();
      TypeMirror t2 = e2.asType();
      if (typeUtils.isSameType(t1, t2)) {
         return 0;
      }
      else {
         /* If not the same use the universal comparator. */
         return Comparators.TypeMirror.compare(t1, t2);
      }
   }
}

package hijac.tools.analysis;

import javacutils.ElementUtils;


import hijac.tools.config.Types;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;

/* Code domain of a class or method. This signifies if the element is either
 * in the application code, Java code, or SCJ / RTSJ infrastructure code. */

/**
 * @author Frank Zeyda
 * @version $Revision: 230 $
 */
public enum Domain {
   JAVA, USER, FRAMEWORK /* OTHER (?) */;

   public static Domain forClass(String class_name) {
      if (Types.isFrameworkType(class_name)) {
         return FRAMEWORK;
      }
      else if (Types.isJavaType(class_name)) {
         return JAVA;
      }
      else {
         /* For now all other package locations are considered USER. */
         return USER;
      }
   }

   public static Domain forPackage(PackageElement element) {
      String package_name = element.getQualifiedName().toString();
      return forClass(package_name + "."); /* Bit of a hack.. */
   }

   public static Domain forClass(TypeElement element) {
      String class_name = element.getQualifiedName().toString();
      return forClass(class_name);
   }

   public static Domain forMethod(ExecutableElement element) {
      String class_name =
         ElementUtils.getQualifiedClassName(element).toString();
      return forClass(class_name);
   }
}

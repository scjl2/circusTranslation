package hijac.tools.analysis;

import hijac.tools.utils.ModelUtils;

import java.util.EnumSet;
import java.util.Set;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeElement;

/**
 * @author Frank Zeyda
 * @version $Revision: 206 $
 */
public class SCJClassTag {
   protected final TypeElement type_element;
   protected final Domain domain;

   public SCJClassTag(TypeElement type_element) {
      assert type_element.getKind() == ElementKind.CLASS ||
         type_element.getKind() == ElementKind.ENUM ||
         type_element.getKind() == ElementKind.INTERFACE;
      this.type_element = type_element;
      this.domain = Domain.forClass(type_element);
   }

   /* Getter Methods */

   public TypeElement getTypeElement() {
      return type_element;
   }

   public Domain getDomain() {
      return domain;
   }

   /* Setter Methods */

   /* Enquiry Methods */

   public boolean isClass() {
      return type_element.getKind() == ElementKind.CLASS;
   }

   public boolean isEnum() {
      return type_element.getKind() == ElementKind.ENUM;
   }

   public boolean isInterface() {
      return type_element.getKind() == ElementKind.INTERFACE;
   }

   public boolean isTopLevel() {
      return type_element.getNestingKind() == NestingKind.TOP_LEVEL;
   }

   public boolean isInner() {
      return type_element.getNestingKind() == NestingKind.MEMBER;
   }

   public boolean isLocal() {
      return type_element.getNestingKind() == NestingKind.LOCAL;
   }

   public boolean isAnonymous() {
      return type_element.getNestingKind() == NestingKind.ANONYMOUS;
   }

   public boolean isAbstract() {
      return type_element.getModifiers().contains(Modifier.ABSTRACT);
   }

   public boolean isFramework() {
      return domain.equals(Domain.FRAMEWORK);
   }

   /* Utiltiy Methods */

   public String getName() {
      return ModelUtils.fromTypeElement(type_element);
   }

   /* String Representation */

   public String toString() {
      String result = getName();
      if (isClass() && isAbstract()) {
         result += " [ABSTRACT]";
      }
      if (isEnum()) {
         result += " [ENUM]";
      }
      if (isInterface()) {
         result += " [INTERFACE]";
      }
      if (isInner()) {
         result += " [INNER]";
      }
      if (isLocal()) {
         result += " [LOCAL]";
      }
      if (isAnonymous()) {
         result += " [ANONYMOUS]";
      }
      return result;
   }
}

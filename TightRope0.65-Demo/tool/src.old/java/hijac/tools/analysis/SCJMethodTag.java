package hijac.tools.analysis;

import hijac.tools.utils.ModelUtils;

import java.util.EnumSet;
import java.util.Set;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;

/**
 * @author Frank Zeyda
 * @version $Revision: 206 $
 */
public class SCJMethodTag {
   protected final ExecutableElement method;
   protected final Domain domain;
   protected Set<Domain> call_domains;
   protected boolean allocates = false;
   protected boolean may_suspend;


   public SCJMethodTag(ExecutableElement method) {
      this.method = method;
      domain = Domain.forMethod(method);
      call_domains = EnumSet.noneOf(Domain.class);
      may_suspend = method.getModifiers().contains(Modifier.SYNCHRONIZED);
   }

   /* Getter Methods */

   public ExecutableElement getExecutableElement() {
      return method;
   }

   public Domain getDomain() {
      return domain;
   }

   public Set<Domain> getCallDomains() {
      return call_domains;
   }

   public boolean allocates() {
      return allocates;
   }

   public boolean maySuspend() {
      return may_suspend;
   }

   /* Setter Methods */

   public void setAllocates() {
      allocates = true;
   }

   public void setMaySuspend() {
      may_suspend = true;
   }

   /* Enquiry Methods */

   public boolean isCtor() {
      return method.getKind().equals(ElementKind.CONSTRUCTOR);
   }

   public boolean isStatic() {
      return method.getModifiers().contains(Modifier.STATIC);
   }

   public boolean isFramework() {
      return domain.equals(Domain.FRAMEWORK);
   }

   public boolean callsFramework() {
      return call_domains.contains(Domain.FRAMEWORK);
   }

   /* String Representation */

   public String toString() {
      String result = toStringSimple();
      if (isCtor()) {
         result += " [CTOR]";
      }
      if (isStatic()) {
         result += " [STATIC]";
      }
      if (allocates()) {
         result += " [ALLOCATES]";
      }
      if (maySuspend()) {
         result += " [MAY_SUSPEND]";
      }
      if (callsFramework()) {
         result += " [CALLS_FRAMEWORK]";
      }
      return result;
   }

   public String toStringSimple() {
      String class_name =
         ModelUtils.removeAnonymous(method.getEnclosingElement().toString());
      String method_name = method.toString();
      if (isCtor()) {
         assert method_name.indexOf("(") != -1;
         String method_para = method_name.substring(
            method_name.indexOf("("), method_name.length());
         return class_name + method_para;
      }
      else {
         return class_name + "." + method.toString();
      }
   }
}

package hijac.tools.modelgen.circus.freemarker;

import freemarker.template.TemplateMethodModelEx;

import hijac.tools.application.ApplicationError;
import hijac.tools.modelgen.circus.utils.TransUtils;

import java.util.List;

import javax.lang.model.type.TypeMirror;


/**
 * Template model for a method that encodes a type into a valid Z
 * identifier that can be used in CZT LaTeX markup.
 *
 * @author Frank Zeyda
 * @version $Revision$
 */
public class EncTypeMethodModel implements TemplateMethodModelEx {
   /*public static final TYPE_TEMPLATE = */

   public EncTypeMethodModel() { }

   /**
    * Implementation of the {@link TemplateMethodModelEx} interface.
    *
    * @param arguments List of arguments of the template method.
    * @return The type as a String. It is obtained by executing the
    * respective macro in the {@code Type.flt} file.
    * 
    */
   public Object exec(List arguments) {
      /* It would be better to generate a meaningful error here. */
      assert arguments.size() == 1;
      assert arguments.get(0) instanceof TypeMirrorTemplateModel;        
      TypeMirrorTemplateModel arg1 =
         (TypeMirrorTemplateModel) arguments.get(0);
      /* Use the template for type translation? */
      return TransUtils.encodeType(
         (TypeMirror) arg1.getAdaptedObject(TypeMirror.class));
   }
}

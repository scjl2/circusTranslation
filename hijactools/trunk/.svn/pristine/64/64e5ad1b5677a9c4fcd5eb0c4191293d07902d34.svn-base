package hijac.tools.modelgen.circus.freemarker;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateScalarModel;
import freemarker.template.TemplateModelException;

import hijac.tools.application.ApplicationError;
import hijac.tools.modelgen.circus.utils.TransUtils;

import java.util.List;

/**
 * Template model for a method that encodes a String into a valid Z
 * identifier that can be used in CZT LaTeX markup.
 *
 * @author Frank Zeyda
 * @version $Revision$
 */
public class EncNameMethodModel implements TemplateMethodModelEx {
   /**
    * Constructs an object of this class.
    */
   public EncNameMethodModel() { }

   /**
    * Implementation of the {@link TemplateMethodModelEx} interface.
    *
    * @param arguments List of arguments of the template method.
    * @return String obtained through {@link TemplateScalarModel} encoded
    * for CZT LaTeX markup.
    */
   public Object exec(List arguments) {
      /* It would be better to generate a meaningful error here. */
      assert arguments.size() == 1;
      assert arguments.get(0) instanceof TemplateScalarModel;
      TemplateScalarModel arg1 = (TemplateScalarModel) arguments.get(0);
      try {
         return TransUtils.encodeName(arg1.getAsString());
      }
      catch (TemplateModelException e) {
         throw new ApplicationError(e);
      }
   }
}

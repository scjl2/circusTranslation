package hijac.tools.modelgen.circus.freemarker;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

import java.io.IOException;
import java.util.Map;

/**
 * @author Frank Zeyda
 * @version $Revision$
 */
public class IgnoreTrailingDirective implements TemplateDirectiveModel {
   public IgnoreTrailingDirective() { }

   public void execute(Environment env, Map params, TemplateModel[] loopVars,
      TemplateDirectiveBody body) throws TemplateModelException, IOException {
      if (!params.isEmpty()) {
         throw new TemplateModelException(
            "This directive does not allow parameters.");
      }
      if (loopVars.length != 0) {
         throw new TemplateModelException(
            "This directive does not allow loop variables.");
      }
      env.getOut().write("%it%");
   }
}

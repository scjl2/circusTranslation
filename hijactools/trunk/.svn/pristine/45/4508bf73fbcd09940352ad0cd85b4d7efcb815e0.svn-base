package hijac.tools.modelgen.circus.freemarker;

import hijac.tools.modelgen.circus.templates.TranslationError;
import hijac.tools.modelgen.circus.utils.TransUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * @author Frank Zeyda
 * @version $Revision$
 */
public class ErrorDirective implements TemplateDirectiveModel {
   public ErrorDirective() { }

   public void execute(Environment env, Map params, TemplateModel[] loopVars,
      TemplateDirectiveBody body) throws TemplateException, IOException {
      if (!params.isEmpty()) {
         throw new TemplateModelException(
            "This directive does not allow parameters.");
      }
      if (loopVars.length != 0) {
         throw new TemplateModelException(
            "This directive does not allow loop variables.");
      }
      env.getOut().write(TransUtils.FAILED_RESULT);
      StringWriter writer = new StringWriter();
      body.render(writer);
      throw new TranslationError(writer.toString(), env);
   }
}

package hijac.tools.modelgen.circus.freemarker;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

import hijac.tools.modelgen.circus.utils.LaTeX;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * @author Frank Zeyda
 * @version $Revision$
 */
public class KeepNewlinesDirective implements TemplateDirectiveModel {
   public KeepNewlinesDirective() { }

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
      StringWriter writer = new StringWriter();
      body.render(writer);
      String nested = writer.toString();
      String output = nested.replace("\n", LaTeX.MACRO_NL);
      env.getOut().write(output);
   }
}

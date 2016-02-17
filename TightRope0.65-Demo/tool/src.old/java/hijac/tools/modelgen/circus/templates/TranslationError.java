package hijac.tools.modelgen.circus.templates;

import freemarker.core.Environment;
import freemarker.template.TemplateException;

/**
 * @author Frank Zeyda
 * @version $Revision$
 */
public class TranslationError extends TemplateException {
   public TranslationError(String description, Environment env) {
      super(description, env);
   }
}

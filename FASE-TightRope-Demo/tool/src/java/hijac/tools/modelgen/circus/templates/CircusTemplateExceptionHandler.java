package hijac.tools.modelgen.circus.templates;

import freemarker.core.Environment;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import hijac.tools.application.ApplicationError;

import java.io.Writer;

class CircusTemplateExceptionHandler implements TemplateExceptionHandler {
   public CircusTemplateExceptionHandler() { }

   public void handleTemplateException(
         TemplateException e, Environment env, Writer out)
            throws TranslationError {
      if (e instanceof TranslationError) {
         throw (TranslationError) e;
      }
      throw new ApplicationError(e);
   }
}

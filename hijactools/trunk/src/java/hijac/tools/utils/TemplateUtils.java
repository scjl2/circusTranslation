package hijac.tools.utils;

import hijac.tools.application.ApplicationError;

import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;

/**
 * General utilities for working with FreeMarker templates.
 *
 * @author Frank Zeyda
 * @version $Revision$
 */
public class TemplateUtils {
   public static String process(Template template, Object rootMap) {
      StringWriter result = new StringWriter();
      try {
         template.process(rootMap, result);
         result.flush();
         result.close();
      }
      catch (TemplateException e) {
         throw new ApplicationError(e);
      }
      catch (IOException e) {
         throw new ApplicationError(e);
      }
      return result.toString();
   }

   public static String process(Template template, Object rootMap,
         ObjectWrapper wrapper) {
      StringWriter result = new StringWriter();
      try {
         template.process(rootMap, result, wrapper);
         result.flush();
         result.close();
      }
      catch (TemplateException e) {
         throw new ApplicationError(e);
      }
      catch (IOException e) {
         throw new ApplicationError(e);
      }
      return result.toString();
   }
}

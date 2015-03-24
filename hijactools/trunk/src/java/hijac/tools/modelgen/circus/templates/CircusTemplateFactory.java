package hijac.tools.modelgen.circus.templates;

import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.log.Logger;
import freemarker.template.Configuration;
import freemarker.template.Template;

import hijac.tools.application.ApplicationError;
import hijac.tools.modelgen.circus.SCJApplication;
import hijac.tools.modelgen.circus.datamodel.MacroModel;
import hijac.tools.modelgen.circus.freemarker.ActionMethodModel;
import hijac.tools.modelgen.circus.freemarker.DataOpMethodModel;
import hijac.tools.modelgen.circus.freemarker.EncNameMethodModel;
import hijac.tools.modelgen.circus.freemarker.EncTypeMethodModel;
import hijac.tools.modelgen.circus.freemarker.ErrorDirective;
import hijac.tools.modelgen.circus.freemarker.IgnoreTrailingDirective;
import hijac.tools.modelgen.circus.freemarker.KeepNewlinesDirective;
import hijac.tools.modelgen.circus.freemarker.NewlineDirective;
import hijac.tools.utils.TemplateUtils;

import java.io.File;
import java.io.IOException;

/**
 * Utility class for retrieving and processing FreeMarker templates of
 * the <i>Circus</i> translator. This also provides custom directives
 * and methods that can be used from within the <i>Circus</i> templates.
 *
 * @author Frank Zeyda
 * @version $Revision$
 */
public class CircusTemplateFactory {
   /**
    * Location of the <i>Circus</i> template files.
    */
   public static final File CIRCUS_TEMPLATE_DIR =
      new File("src/resources/templates/circus");

   /**
    * Configuration used when processing <i>Circus</i> templates.
    */
   public final Configuration CIRCUS_CONFIG = new Configuration();

   /**
    * Fallback template loader to support dynamic creation of templates for
    * macro calls.
    */
   public final StringTemplateLoader
      CIRCUS_ON_THE_FLY_LOADER = new StringTemplateLoader();

   /**
    * Template model for a method that translates a JDK statement tree into
    * an action.
    */
   public final ActionMethodModel ACTION_METHOD;

   /**
    * Template model for a method that translates a JDK statement tree into
    * a data operation.
    */
   public final  DataOpMethodModel DATAOP_METHOD;

   /**
    * The constructor of the <i>Circus</i> template factory configures
    * additional directives and methods that can be used by the templates.
    */
   /* TODO: Review and document the directives and methods. */
   public CircusTemplateFactory(SCJApplication context) {
      try {
         /* We use both the default loader and a custom string loader. */
         CIRCUS_CONFIG.setDirectoryForTemplateLoading(CIRCUS_TEMPLATE_DIR);
         CIRCUS_CONFIG.setObjectWrapper(CircusObjectWrapper.INSTANCE);
         TemplateLoader[] loaders = new TemplateLoader[2];
         loaders[0] = CIRCUS_CONFIG.getTemplateLoader();
         loaders[1] = CIRCUS_ON_THE_FLY_LOADER;
         CIRCUS_CONFIG.setTemplateLoader(new MultiTemplateLoader(loaders));
         CIRCUS_CONFIG.setTemplateExceptionHandler(
            new CircusTemplateExceptionHandler());
         /* Initialise custom directives. */
         CIRCUS_CONFIG.setSharedVariable("it", new IgnoreTrailingDirective());
         CIRCUS_CONFIG.setSharedVariable("nl", new NewlineDirective());
         CIRCUS_CONFIG.setSharedVariable("keep_newlines",
            new KeepNewlinesDirective());
         CIRCUS_CONFIG.setSharedVariable("ERROR", new ErrorDirective());
         /* Initialise custom methods. */
         CIRCUS_CONFIG.setSharedVariable("ENCNAME", new EncNameMethodModel());
         CIRCUS_CONFIG.setSharedVariable("ENCTYPE", new EncTypeMethodModel());
         ACTION_METHOD = new ActionMethodModel(context);
         DATAOP_METHOD = new DataOpMethodModel(context);
         CIRCUS_CONFIG.setSharedVariable("ACTION", ACTION_METHOD);
         CIRCUS_CONFIG.setSharedVariable("DATAOP", DATAOP_METHOD);
      }
      catch (IOException e) {
         throw new ApplicationError(e);
      }
      /* Configure Logger */
      try {
         Logger.selectLoggerLibrary(Logger.LIBRARY_NONE);
      }
      catch (ClassNotFoundException e) {
         throw new ApplicationError(e);
      }
   }

   /**
    * Method to obtain a <i>Circus</i> template form the template store.
    *
    * @param name Name of the template.
    * @return Template saved/stored under the given name.
    */
   public Template getTemplate(String name) {
      try {
         return CIRCUS_CONFIG.getTemplate(name);
      }
      catch(IOException e) {
         throw new ApplicationError(e);
      }
   }

   /**
    * Method to add a <i>Circus</i> template to the template store.
    *
    * @param name Name of the template to add.
    * @param templateSource Definition of the template as a string.
    */
   public void putTemplate(String name, String templateSource) {
      CIRCUS_ON_THE_FLY_LOADER.putTemplate(name, templateSource);
      /*CIRCUS_CONFIG.clearTemplateCache();*/
      /* Why does the method below throw an IOException? */
      try {
         CIRCUS_CONFIG.removeTemplateFromCache("MacroCall");
      }
      catch(IOException e) {
         throw new ApplicationError(e);
      }
   }

   /**
    * Method to perform a dynamic <i>Circus</i> template macro call.
    *
    * @param rootMap Root data model used in the macro call.
    * @param source Name of the template file in which the macro resides.
    * @param macro Name of the template macro to be called.
    * @param args A variable number of arguments passed to the macro.
    * @return Result string of evaluating the macro call.
    */
   public String doMacroCall(MacroModel rootMap,
         String source, String macro, Object... args) {
      StringBuilder templateSource = new StringBuilder();
      templateSource.append("<#include \"" + source + "\">\n");
      templateSource.append("<@" + macro);
      for (int index = 1; index <= args.length; index++) {
         String arg_str = "arg" + index;
         templateSource.append(" " + arg_str);
         rootMap.put(arg_str, args[index - 1]);
      }
      templateSource.append("/>");
      putTemplate("MacroCall", templateSource.toString());
      Template template = getTemplate("MacroCall");
      return TemplateUtils.process(
         template, rootMap, CircusObjectWrapper.INSTANCE);
   }
}

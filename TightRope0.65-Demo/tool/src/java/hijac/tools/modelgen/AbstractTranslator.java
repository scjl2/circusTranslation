package hijac.tools.modelgen;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.ObjectWrapper;

import hijac.tools.analysis.SCJAnalysis;
import hijac.tools.application.ApplicationError;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class that facilitates implementation of the {@link Translator}
 * interface.
 *
 * @author Frank Zeyda
 * @version $Revision$
 */
public abstract class AbstractTranslator<A extends SCJApplication>
      implements Translator<A> {
   private final String name;
   private final List<TranslationError> errors;

   /**
    * Application context in which translation is performed.
    */
   protected A CONTEXT;

   /**
    * Analysis of the application context.
    */
   protected SCJAnalysis ANALYSIS;

   /**
    * Current target of the translator.
    */
   protected Target TARGET;

   /**
    * Current output object used by the translator.
    */
   protected Output OUTPUT;


   /**
    * Creates a new abstract translator with a given name.
    *
    * @param name Name of the translator.
    */
   protected AbstractTranslator(String name) {
      assert name != null;
      this.name = name;
      this.errors = new ArrayList<TranslationError>();
   }

   /**
    * Returns the current SCJ application context.
    *
    * @return Current SCJ application context.
    */
   protected A getContext() {
      return CONTEXT;
   }

   /**
    * Returns the analysis of the current application context.
    *
    * @return Analysis of the current application context.
    */
   protected SCJAnalysis getAnalysis() {
      return ANALYSIS;
   }

   /**
    * Returns the current target object.
    *
    * @return Current target object of the translator.
    */
   protected Object getTarget() {
      return TARGET;
   }

   /**
    * Returns the current output object.
    *
    * @return Current output object used to write the translation output.
    */
   protected Output getOutput() {
      return OUTPUT;
   }

   /**
    * Records a new translation error.
    *
    * @param message A textual description if the error.
    */
   /*protected*/ public void addError(String message) {
      assert message != null;
      errors.add(new TranslationError(message, this));
   }

   /**
    * Erases all previously recorded error messages.
    */
   protected void resetErrors() {
      errors.clear();
   }

   /**
    * Obtains the writer for a given file as a string. This is done using the
    * current output object.
    *
    * @param filename Name of the file given as a string.
    * @return Writer for respective file.
    */
   protected PrintWriter getWriter(String filename) {
      return OUTPUT.getWriter(filename);
   }

   /**
    * Processes a string template and subsequently write the result to a file
    * using the {@link #OUTPUT} object.
    *
    * @param template FreeMarker template to be processed.
    * @param model Data model to be used when processing the template.
    * @param filename Name of the file to write the processed template to.
    */
   protected void write(Template template, Object model, String filename) {
      PrintWriter writer = getWriter(filename);
      try {
         template.process(model, writer);
      }
      catch (TemplateException e) {
         throw new ApplicationError(e);
      }
      catch (IOException e) {
         throw new ApplicationError(e);
      }
   }

   /**
    * Processes a string template and subsequently write the result to a file
    * using the {@link #OUTPUT} object.
    *
    * @param template FreeMarker template to be processed.
    * @param wrapper Object wrapper to be used.
    * @param model Data model to be used when processing the template.
    * @param filename Name of the file to write the processed template to.
    */
   protected void write(Template template, ObjectWrapper wrapper,
         Object model, String filename) {
      PrintWriter writer = getWriter(filename);
      try {
         template.process(model, writer, wrapper);
      }
      catch (TemplateException e) {
         throw new ApplicationError(e);
      }
      catch (IOException e) {
         throw new ApplicationError(e);
      }
   }

   /* Implementation of the Translator interface. */

   /**
    * Defines the behaviour upon invocation of the translator. This is the
    * only method that a subclass has to implement in order to implement
    * the {@link Translator} interface when deriving from this class.
    */
   protected abstract void execute();

   /**
    * Name of the translator as specified during construction.
    */
   @Override
   public String getName() {
      return name;
   }

   /**
    * By default indicates that no description is available. This method
    * has to be overridden in order to provide a description.
    */
   @Override
   public String getDescription() {
      return "No description is available for this translator.";
   }

   /**
    * Sets the SCJ application context.
    *
    * @param context Application context to be used.
    */
   public void setContext(A context) {
      assert context != null;
      CONTEXT = context;
      ANALYSIS = CONTEXT.getAnalysis();
      resetErrors();
   }

   /**
    * Sets the translation target. This also results in all recorded errors
    * being erased.
    */
   @Override
   public final void setTarget(Target target) {
      assert target != null;
      TARGET = target;
      resetErrors();
   }

   @Override
   public final void setOutput(Output output) {
      assert output != null;
      OUTPUT = output;
   }

   /**
    * Invokes the translator on the configured target. The behaviour depends
    * on the implementation of {@link #execute()}.
    */
   public boolean invoke() {
      resetErrors();
      execute();
      return success();
   }

   public boolean success() {
      return errors.isEmpty();
   }

   public TranslationError[] getErrors() {
      return errors.toArray(new TranslationError[0]);
   }

   /* Overridden method of the Object class. */

   /**
    * Indicates whether some other object is equal to this translator.
    *
    * Translators are considered equal if they have the same name.
    *
    * @return Boolean flag that indicates if two translators are equal.
    */
   @Override
   public boolean equals(Object o) {
      if (o instanceof Translator) {
         return ((Translator) o).getName().equals(name);
      }
      return false;
   }

   /**
    * Returns a hash code value of the translator.
    *
    * @return A hash code value for the translator.
    */
   @Override
   public int hashCode() {
      return name.hashCode();
   }

   /**
    * Returns the name of the translator.
    *
    * @return Name of this translator as a string.
    */
   @Override
   public String toString() {
      return name;
   }
}

package hijac.tools.modelgen;

import hijac.tools.modelgen.targets.ApplicationTarget;
import hijac.tools.modelgen.targets.ClassTarget;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

/**
 * A composite translator aggregates a set of translators. They can be
 * invoked on the current target or automatically on all targets of an SCJ
 * application.
 *
 * @author Frank Zeyda
 * @version $Revision$
 */
public abstract class CompositeTranslator<T extends SCJApplication>
      extends AbstractTranslator<T> {
   private final List<Translator<T>> translators;

   /**
    * Creates a now composite translator with a given name.
    *
    * @param name Name of the composite translator.
    */
   public CompositeTranslator(String name) {
      super(name);
      translators = new ArrayList<Translator<T>>();
   }

   /**
    * Adds a translator to this composite translator. It is then
    * automatically invoked as part of the invocation of this translator.
    *
    * @param translator Translator to be added.
    */
   public void add(Translator<T> translator) {
      translators.add(translator);
   }

   /**
    * Removes a translator from this composite translator.
    *
    * @param translator Translator to be removed.
    */
   public void remove(Translator<T> translator) {
      translators.remove(translator);
   }

   /**
    * Removes all translators registered via {@link #add(Translator)}.
    */
   public void clear() {
      translators.clear();
   }

   /**
    * The description provides information about registered translators.
    */
   @Override
   public final String getDescription() {
      return "Composite translator aggregating " + translators + ".";
   }

   /**
    * A composite translator is by definition always applicable.
    *
    * @return Always returns {@code true}.
    */
   @Override
   public boolean isApplicable() {
      return true;
   }

   /**
    * Invocation of all configured translators on the current target.
    *
    * Note that this method only invokes translators that are applicable to
    * the current target.
    */
   @Override
   public void execute() {
      for (Translator<T> translator : translators) {
         translator.setContext(CONTEXT);
         translator.setTarget(TARGET);
         translator.setOutput(OUTPUT);
         if (translator.isApplicable()) {
            translator.invoke();
         }
      }
   }

   /**
    * Determines whether the last translation was successful.
    *
    * Here this means that all applicable translators of the last invocation
    * succeeded.
    */
   @Override
   public boolean success() {
      for (Translator<T> translator : translators) {
         if (!translator.success()) { return false; }
      }
      return true;
   }
   /**
    * Returns all errors raised by the last invocation.
    *
    * The result accumulates the errors of each applicable translator.
    */
   @Override
   public TranslationError[] getErrors() {
      List<TranslationError> errors =
         new ArrayList<TranslationError>();
      for (Translator<T> translator : translators) {
         errors.addAll(Arrays.asList(translator.getErrors()));
      }
      return errors.toArray(new TranslationError[0]);
   }

   /* Utility Method for invoking the translator on all targets. */

   /**
    * Invokes the composite translator on all targets of the current SCJ
    * application context.
    * Invocation here first applies all applicable translators to the
    * {@link ApplicationTarget}, and then likewise applies them to each
    * {@link ClassModel} associated with one of the classes of the SCJ
    * application. If the application of a translator to one of the
    * targets fails, the process does not proceed with the next target.
    *
    * @return Boolean flag that indicates whether whether translation
    * errors were encountered.
    */
   public boolean invokeOnAllTargets() {
      setTarget(new ApplicationTarget());
      if (!invoke()) { return false; }
      for (TypeElement type_element : ANALYSIS.getTypeElements()) {
         if (type_element.getKind() == ElementKind.CLASS) {
            /*setTarget(new ClassModel(CONTEXT, type_element));*/
            setTarget(new ClassTarget(type_element));
            if (!invoke()) { return false; }
         }
      }
      return true;
   }
}

package hijac.tools.modelgen;

/**
 * Common interface implemented by all translator components.
 *
 * @author Frank Zeyda
 * @version $Revision$
 */
public interface Translator<A extends SCJApplication> {
   /**
    * Returns the name of the translator.
    *
    * @return Name of this translator as a string.
    */
   public String getName();

   /**
    * Returns a short description of the translator.
    *
    * @return Description of this translator as a string.
    */
   public String getDescription();

  /**
   * Sets the application context for translation.
   *
   * This is important since analysis (and other) information is
   * inferred from the application context during translation.
   *
   * @param application Application context of the SCJ program.
   */
   public void setContext(A application);

   /**
    * Sets the translation target.
    *
    * The target can be an object of any class implementing the {@link Target}
    * interface. To determines if the translator is applicable to the target,
    * {@link #isApplicable()} may subsequently be called.
    *
    * @param target Target the translator is applied to when calling
    * {@link #invoke()}.
    */
   public void setTarget(Target target);

   /**
    * Sets the output utility object for writing the translation result.
    *
    * It is used by the translator to write out the results of the translation
    * and usually takes care of creation and population of the target files.
    *
    * @param output Utility object to be used for writing the translation
    * results.
    */
   public void setOutput(Output output);

   /**
    * Determines whether the translator is applicable to the current target.
    *
    * @return {@code true} if and only if the translator is applicable to the
    * current target.
    */
   public boolean isApplicable();

   /**
    * Invokes the translator on the current target.
    *
    * The result should be written using the output utility object configured
    * by {@link #setOutput setOutput(Output)}. This method should not throw an
    * exception in case of translation errors but instead collate and return
    * them via {@link #getErrors()}.
    *
    * @return {@code true} if and only if translation succeeded.
    */
   public boolean invoke();

   /**
    * Determines whether the last invocation was successful.
    *
    * @return {@code true} if and only if the last invocation was successful.
    */
   public boolean success();

   /**
    * Returns all errors raised by the last translation.
    *
    * @return Array of translation errors. May be empty if no errors were
    * encountered.
    */
   public TranslationError[] getErrors();
}

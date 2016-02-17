package hijac.tools.modelgen;

/**
 * Class that encapsulates errors occurring during the translator process.
 *
 * @author Frank Zeyda
 * @version $Revision$
 */
public class TranslationError extends Exception {
   protected final Translator source;

   /**
    * Creates a new translation error associated with a specific translator.
    */
   public TranslationError(String message, Translator source) {
      super(message);
      assert message != null;
      assert source != null;
      this.source = source;
   }

   /**
    * Creates a new translation error that is not associated with a source.
    */
   public TranslationError(String message) {
      super(message);
      assert message != null;
      this.source = null;
   }

   /**
    * Returns the source of the translation error.
    *
    * @return Source of the translation error as an instance of the
    * {@link Translator} interface, or {@code null} if a source has
    * not been specified upon construction of the error.
    */
   public Translator getSource() {
      return source;
   }

   /**
    * Yields a string representation of the error. This includes the
    * error message as well as the source of the error, if applicable.
    *
    * @return Error message and source of the error, if present.
    */
   public String toString() {
      if (source != null) {
         return getMessage() + " [raised by " + source.getClass() + "]";
      }
      else {
         return getMessage();
      }
   }
}

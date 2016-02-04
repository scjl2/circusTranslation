package hijac.tools.utils;

/* Error raised as a result of a validation failure. */

/**
 * @author Frank Zeyda
 * @version $Revision: 198 $
 */
public class ValidationError extends AssertionError {
   public ValidationError(String message) {
      super(message);
   }
}

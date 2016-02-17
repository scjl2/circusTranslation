package hijac.tools.utils;

/* Interface that supports (run-time) validation of objects. */

/**
 * @author Frank Zeyda
 * @version $Revision: 198 $
 */
public interface Validatable {
   /* Expected to throw a ValidationError exception if validation fails. */
   public void validate();
}

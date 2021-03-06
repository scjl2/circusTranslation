package hijac.tools.analysis;

import java.util.Collections;
import java.util.List;

/**
 * @author Frank Zeyda
 * @version $Revision: 198 $
 */
public class AnalysisException extends Exception {
   protected List<AnalysisError> errors;

   public AnalysisException(List<AnalysisError> errors) {
      assert errors != null;
      assert !errors.isEmpty();
      this.errors = errors;
   }

   public AnalysisException(AnalysisError error) {
      assert error != null;
      errors = Collections.singletonList(error);
   }

   public AnalysisError getFirstError() {
      return errors.get(0);
   }

   public List<AnalysisError> getErrors() {
      return errors;
   }
}

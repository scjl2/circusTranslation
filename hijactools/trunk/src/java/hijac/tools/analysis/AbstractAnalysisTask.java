package hijac.tools.analysis;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* Abstract class that facilitates the implementation of analysis tasks. */

/**
 * @author Frank Zeyda
 * @version $Revision: 198 $
 */
public abstract class AbstractAnalysisTask implements AnalysisTask {
   protected final String name;
   protected final SCJDataKey[] requires;
   protected final SCJDataKey[] generates;
   protected final List<AnalysisError> errors;

   protected SCJAnalysis ANALYSIS;

   protected AbstractAnalysisTask(
         String name, SCJDataKey[] requires, SCJDataKey[] generates) {
      checkArgs(name, requires, generates);
      this.name = name;
      this.requires = requires;
      this.generates = generates;
      this.errors = new ArrayList<AnalysisError>();
   }

   private void checkArgs(
         String name, SCJDataKey[] requires, SCJDataKey[] generates) {
      assert name != null;
      assert requires != null;
      assert generates != null;
      assert Collections.disjoint(
         Arrays.asList(requires), Arrays.asList(generates));
   }

   /* Methods that can be used by the implementing class. */

   protected void resetErrors() {
      errors.clear();
   }

   protected void addError(String message) {
      assert message != null;
      errors.add(new AnalysisError(message, this));
   }

   protected SCJAnalysis getAnalysis() {
      return ANALYSIS;
   }

   /* The following method rather than invoke() must be implemented by the
    * subclass in order to specify the behaviour of the analysis task. */

   public abstract void doTask();

   /* Implementation of the AnalysisTask interface. */

   public String getName() {
      return name;
   }

   public String getDescription() {
      return "No description is available for this task.";
   }

   public SCJDataKey[] requires() {
      return requires;
   }

   public SCJDataKey[] generates() {
      return generates;
   }

   public final boolean invoke(SCJAnalysis analysis) {
      resetErrors();
      ANALYSIS = analysis;
      System.out.println("[Task " + name + "]");
      doTask();
      return success();
   }

   public boolean success() {
      return errors.isEmpty();
   }

   public AnalysisError[] getErrors() {
      return errors.toArray(new AnalysisError[0]);
   }

   @Override
   public boolean equals(Object o) {
      if (o instanceof AnalysisTask) {
         return ((AnalysisTask) o).getName().equals(name);
      }
      return false;
   }

   @Override
   public int hashCode() {
      return name.hashCode();
   }

   /* Supplementary Methods */

   @Override
   public String toString() {
      return name;
   }
}

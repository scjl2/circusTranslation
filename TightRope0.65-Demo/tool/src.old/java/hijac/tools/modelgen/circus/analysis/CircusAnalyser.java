package hijac.tools.modelgen.circus.analysis;

import hijac.tools.analysis.TaskProcessor;
import hijac.tools.analysis.tasks.SCJTypesTask;
import hijac.tools.analysis.tasks.MethodCallDepTask;

/**
 * @author Frank Zeyda
 * @version $Revision$
 */
public class CircusAnalyser extends TaskProcessor {
   public CircusAnalyser() {
      add(new SCJTypesTask());
      add(new UniqueMethodNamesTask());
      add(new MethodCallDepTask());
   }
}

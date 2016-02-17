package hijac.tools.config;

import hijac.tools.analysis.DefaultTasks;
import hijac.tools.analysis.tasks.*;

/**
 * @author Frank Zeyda
 * @version $Revision: 198 $
 */
public class Tasks {
   static {
      DefaultTasks.register(new ClassTagsTask());
      DefaultTasks.register(new MethodCallDepTask());
      DefaultTasks.register(new MethodTagsTask());
      DefaultTasks.register(new SCJTypesTask());
      DefaultTasks.register(new AnalyseTypesTask());
   }

   public static void kickstart() { }
}

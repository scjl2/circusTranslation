package hijac.tools.analysis;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @version $Revision: 198 $
 * @author Frank Zeyda
 */
public final class DefaultTasks {
   protected static final Set<AnalysisTask> TASKS;

   static {
      TASKS = new HashSet<AnalysisTask>();
   }

   public static void register(AnalysisTask task) {
      TASKS.add(task);
   }

   public static void remove(String name) {
      Iterator<AnalysisTask> iter = TASKS.iterator();
      while (iter.hasNext()) {
         if (iter.next().getName().equals(name)) {
            iter.remove();
         }
      }
   }

   public static void clear() {
      TASKS.clear();
   }
}

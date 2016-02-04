package hijac.tools.analysis;

import hijac.tools.collections.Arrays;
import hijac.tools.collections.CyclicException;
import hijac.tools.collections.Maplet;
import hijac.tools.collections.Relation;
import hijac.tools.collections.RelationFactory;
import hijac.tools.collections.Relations;
import hijac.tools.comparators.Comparators;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Frank Zeyda
 * @version $Revision: 198 $
 */
public class AnalysisUtils {
   public static List<AnalysisTask> findExecOrder(Set<AnalysisTask> tasks)
         throws CyclicException {
      Relation<AnalysisTask, AnalysisTask> task_dep =
         RelationFactory.newRelation(Comparators.AnalysisTask);
      for (AnalysisTask task1 : tasks) {
      for (AnalysisTask task2 : tasks) {
         if (!Arrays.disjoint(
               task1.generates(), task2.requires())) {
            task_dep.add(
               new Maplet<AnalysisTask, AnalysisTask>(task1, task2));
         }
      }}
      List<AnalysisTask> order = new ArrayList<AnalysisTask>();
      /* Add tasks without dependencies in no particular order. */
      for (AnalysisTask task : tasks) {
         if (!task_dep.domain().contains(task) &&
            !task_dep.range().contains(task)) { order.add(task); }
      }
      /* Add tasks with dependencies using their topological order. */
      order.addAll(Relations.topologicalSort(task_dep));
      return order;
   }
}

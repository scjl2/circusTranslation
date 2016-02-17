package hijac.tools.analysis;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Set;
import java.util.List;
import java.util.HashSet;

import hijac.tools.collections.CyclicException;

/**
 * Class that implements compound analysis tasks that execute a set of given
 * tasks in a suitable order.
 *
 * @author Frank Zeyda
 * @version $Revision: 198 $
 */
public class TaskProcessor implements AnalysisTask {
   /**
    * Task processor whose task set comprises the default tasks.
    *
    * @see hijac.tools.analysis.DefaultTasks
    */
   static public final TaskProcessor DEFAULT;

   static {
      DEFAULT = new TaskProcessor();
      DEFAULT.tasks = DefaultTasks.TASKS;
   }

   protected /*final*/ Set<AnalysisTask> tasks;
   protected final List<AnalysisError> errors;

   /**
    * Constructs a new task processor with an empty list of tasks.
    */
   public TaskProcessor() {
      tasks = new HashSet<AnalysisTask>();
      errors = new ArrayList<AnalysisError>();
   }

   /**
    * Constructs a new task processor with an initial list of tasks.
    *
    * @param initTasks Initial list of tasks of the processor.
    */
   public TaskProcessor(Set<AnalysisTask> initTasks) {
      tasks = new HashSet<AnalysisTask>(initTasks);
      errors = new ArrayList<AnalysisError>();
   }

   /*********************/
   /* Protected Methods */
   /*********************/

   protected void resetErrors() {
      errors.clear();
   }

   protected AnalysisError addError(String message) {
      assert message != null;
      AnalysisError error =
         new AnalysisError(
            AnalysisError.Type.PROCESSOR_ERROR, this, message);
      errors.add(error);
      return error;
   }

   /************************************************/
   /* Implementation of the AnalysisTask interface */
   /************************************************/

   /**
    * Returns {@code "TaskProcessor"} as this tasks name.
    */
   public String getName() {
      return "TaskProcessor";
   }

   /**
    * Returns a descriptions that includes the processor's task set.
    */
   public String getDescription() {
      return "Compound task that aggregates " + tasks.toString();
   }

   /**
    * Returns the union of all data keys required by this processor's tasks.
    */
   public SCJDataKey[] requires() {
      Set<SCJDataKey> result = new HashSet<SCJDataKey>();
      for (AnalysisTask task : tasks) {
         result.addAll(Arrays.asList(task.requires()));
      }
      return result.toArray(new SCJDataKey[0]);
   }

   /**
    * Returns the union of all data keys generated by this processor's tasks.
    */
   public SCJDataKey[] generates() {
      Set<SCJDataKey> result = new HashSet<SCJDataKey>();
      for (AnalysisTask task : tasks) {
         result.addAll(Arrays.asList(task.generates()));
      }
      return result.toArray(new SCJDataKey[0]);
   }

   /**
    * Sequentially invokes all tasks of this task processor.
    *
    * <p>The processor automatically infers a suitable task execution order
    * that takes into account dependencies between tasks. If no such order
    * exists, in other words the dependency relation is cyclic, an error is
    * recorded.</p>
    *
    * <p>Note that execution proceeds even if one or more tasks fails.
    * However, only those tasks are executed whose required data keys are
    * present. The method besides checks that each task generates the data
    * items it promised.<p>
    */
   public boolean invoke(SCJAnalysis analysis) {
      resetErrors();
      boolean result = true;
      try {
         List<AnalysisTask> order = AnalysisUtils.findExecOrder(tasks);
         //System.out.println("Task execution order: " + order + ".");
         for (AnalysisTask task : order) {
            /* Only execute task if required fields are available. */
            if (analysis.containsKeys(task.requires())) {
               result &= task.invoke(analysis);
               /* Did the task generate what it promised? */
               if (task.success() &&
                     !analysis.containsKeys(task.generates())) {
                  addError("Task " + task.getName() +
                     " did not generate the fields it promised.");
                  result = false;
               }
            }
            else {
               /* Should we record and error here? */
            }
         }
      }
      catch (CyclicException e) {
         addError("Task set exhibits cyclic dependency.");
         result = false;
      }
      return result;
   }

   public boolean success() {
      return getErrors().length == 0;
   }

   /**
    * Returns the cumulative list of errors of all failed tasks after
    * invocation.
    */
   public AnalysisError[] getErrors() {
      List<AnalysisError> result = new ArrayList<AnalysisError>();
      result.addAll(errors);
      for (AnalysisTask task : tasks) {
         result.addAll(Arrays.asList(task.getErrors()));
      }
      return result.toArray(new AnalysisError[0]);
   }

   /******************/
   /* Public Methods */
   /******************/

   /**
    * Adds a task to the processor's task set.
    *
    * @param task The task to be added.
    */
   public void add(AnalysisTask task) {
      tasks.add(task);
   }

   /**
    * Removes a task from the processor's task set.
    *
    * @param task The task to be removed.
    */
   public void remove(AnalysisTask task) {
      tasks.remove(task);
   }

   /**
    * Clears the processor's task set.
    */
   public void clear() {
      tasks.clear();
   }

   /**
    * Invokes a specific task of this task processor. The task must be
    * included in this processor's task set.
    *
    * <p>Unlike {@link #invoke(SCJAnalysis)} this method immediately throws
    * an exception if the analysis task fails.</p>
    *
    * @param analysis Analysis object on which the task is performed.
    * @param name Name of the task to be invoked.
    */
   public boolean invoke(SCJAnalysis analysis, String name)
         throws AnalysisException {
      resetErrors();
      for (AnalysisTask task : tasks) {
         if (task.getName().equals(name)) {
            if (!task.invoke(analysis)) {
               task.getErrors()[0].raise();
            }
         }
      }
      addError("No such task.").raise();
      /* NEVER REACHED */
      return false;
   }

   /**
    * Returns a string representation of this object.
    */
   @Override
   public String toString() {
      return getName();
   }
}

package hijac.tools.analysis;

/* Captures possible errors occurring during analysis. */

/**
 * @author Frank Zeyda
 * @version $Revision: 198 $
 */
public class AnalysisError  {
   protected Type type;
   protected Object source;
   protected String message;

   protected AnalysisError(Type type, Object source, String message) {
      this.type = type;
      this.source = source;
      this.message = message;
   }

   public AnalysisError(String message, AnalysisTask task) {
      this(Type.TASK_ERROR, task, message);
      assert task != null;
      assert message != null;
   }

   public AnalysisError(String message, TaskProcessor proc) {
      this(Type.PROCESSOR_ERROR, proc, message);
      assert proc != null;
      assert message != null;
   }

   public AnalysisError(String message) {
      this(Type.UNKNOWN, null, message);
      assert message != null;
   }

   public Type getType() {
      return type;
   }

   public AnalysisTask getTask() {
      if (type == Type.TASK_ERROR) {
         return (AnalysisTask) source;
      }
      else {
         throw new IllegalStateException(
            "Error type is not " + Type.TASK_ERROR + ".");
      }
   }

   public TaskProcessor getProcessor() {
      if (type == Type.PROCESSOR_ERROR) {
         return (TaskProcessor) source;
      }
      else {
         throw new IllegalStateException(
            "Error type is not " + Type.PROCESSOR_ERROR + ".");
      }
   }

   public String getMessage() {
      return message;
   }

   /* The following method raise this error as an exception. */

   public void raise() throws AnalysisException {
      throw new AnalysisException(this);
   }

   public String toString() {
      if (type == Type.TASK_ERROR || type == Type.PROCESSOR_ERROR) {
         /*return "[" + source + "] " + message;*/
         return message;
      }
      else {
         return message;
      }
   }

   /* Inner enumeration for possible error types. */

   public enum Type {
      TASK_ERROR, PROCESSOR_ERROR, UNKNOWN;

      public boolean isUnknown() {
         return this == UNKNOWN;
      }
   };
}

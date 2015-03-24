package hijac.tools.application;

import hijac.tools.modelgen.circus.templates.TranslationError;

/**
 * Class that represents a serious error causing an application to abort.
 *
 * @author Frank Zeyda
 * @version $Revision$
 */
public class ApplicationError extends Error {
   public ApplicationError() {
      super();
   }

   public ApplicationError(String message) {
      super(message);
   }

   public ApplicationError(String message, Throwable cause) {
      super(message, cause);
   }

   public ApplicationError(Throwable cause) {
      super(null, cause);
   }

   public void printError() {
      if (getInitialCause() instanceof TranslationError) {        
         System.out.println("=================");
         System.out.println("Translation Error");     
         System.out.println("=================");
         System.out.println(getInitialCause().getMessage());
      }
      else {
         System.out.println("=================");
         System.out.println("Application Error");     
         System.out.println("=================");
         Throwable cause = this;
         while (cause != null) {
            System.out.println("Caused by: " +
               cause.getClass().getSimpleName());
            if (cause.getMessage() != null) {
               System.out.println("-> " + cause.getMessage());
            }
            printStackTraceSegment(cause.getStackTrace(), 1);
            cause = cause.getCause();
         }
      }
   }

   public static void printStackTraceSegment(
         StackTraceElement[] stackTrace, int n) {
      int counter = 0;
      for (StackTraceElement element : stackTrace) {
         System.out.println("-> at " + element.toString());
         counter++;
         if (counter == n) {
            System.out.println("-> at ...");
            break;
         }
      }
   }

   public Throwable getInitialCause() {
      Throwable cause = this;
      while (cause.getCause() != null) {
         cause = cause.getCause();
      }
      return cause;
   }
}

package hijac.tools.application;

public class UncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
   public void uncaughtException(Thread t, Throwable e) {
      if (e instanceof ApplicationError) {
         ((ApplicationError) e).printError();
      }
      else {
         System.out.println("==================");
         System.out.println("Uncaught Exception");
         System.out.println("==================");
         System.out.print("Exception in thread \"" + t.getName() + "\" ");
         e.printStackTrace();
      }
      System.exit(-1);
   }
}

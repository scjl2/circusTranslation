import javax.realtime.RealtimeThread;

public class Handler3_MemoryEntry implements Runnable {
   public Handler3 outer;

   public Handler3_MemoryEntry(Handler3 outer) {
      this.outer = outer;
   }

   public void run() {
      System.out.println("[MemoryEntry] run() callled");
      System.out.println("[MemoryEntry] inside " +
         RealtimeThread.getCurrentMemoryArea().getClass());
      new Integer(42);
   }
}

import javax.realtime.RealtimeThread;

public class Handler1_MemoryEntry implements Runnable {
   public Handler1 outer;

   public Handler1_MemoryEntry(Handler1 outer) {
      this.outer = outer;
   }

   public void run() {
      System.out.println("[MemoryEntry] run() callled");
      System.out.println("[MemoryEntry] inside " +
         RealtimeThread.getCurrentMemoryArea().getClass());
      new Integer(42);
   }
}

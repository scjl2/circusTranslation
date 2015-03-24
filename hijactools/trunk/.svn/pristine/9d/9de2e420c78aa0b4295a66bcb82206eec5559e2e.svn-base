import javax.realtime.RealtimeThread;

public class Handler2_MemoryEntry implements Runnable {
   public Handler2 outer;

   public Handler2_MemoryEntry(Handler2 outer) {
      this.outer = outer;
   }

   public void run() {
      System.out.println("[MemoryEntry] run() callled");
      System.out.println("[MemoryEntry] inside " +
         RealtimeThread.getCurrentMemoryArea().getClass());
      new Integer(42);
   }
}

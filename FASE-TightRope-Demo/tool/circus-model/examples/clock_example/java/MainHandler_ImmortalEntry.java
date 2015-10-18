import javax.realtime.Clock;

public class MainHandler_ImmortalEntry implements Runnable {
   public MainHandler outer;

   public MainHandler_ImmortalEntry(MainHandler outer) {
      this.outer = outer;
   }

   /* The following code must be executed in immortal memory since getTime()
    * allocates memory for the AbsoluteTime object. Otherwise we would have
    * a downward reference as releaseTimes was created in immortal memory. */
   public void run() {
      /*Clock clock = new Clock();*/
      Clock clock = Clock.getRealtimeClock();
      outer.releaseTimes[outer.index++] = clock.getTime();
   }
}

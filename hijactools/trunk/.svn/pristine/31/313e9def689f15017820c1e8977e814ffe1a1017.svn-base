import javax.realtime.*;

import javax.safetycritical.*;
import javax.safetycritical.annotate.*;

class Handler2 extends PeriodicEventHandler {
  private int counter = 0;

  public Handler2() {
    super(
      new PriorityParameters(
         javax.safetycritical.PriorityScheduler.instance().getMaxPriority()),
      new PeriodicParameters(new AbsoluteTime(), new RelativeTime(500, 0)),
      new StorageConfigurationParameters(10000, 10000, 10000),
      10000, "Handler2");
  }

  public void handleEvent() {
    System.out.println("Handler2::handleEvent() called <" + counter + ">");
    counter++;
    if (counter == 5) {
      Mission.getCurrentMission().requestTermination();
    }
  }
}

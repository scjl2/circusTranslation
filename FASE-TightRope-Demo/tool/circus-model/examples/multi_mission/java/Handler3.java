import javax.realtime.*;

import javax.safetycritical.*;
import javax.safetycritical.annotate.*;

class Handler3 extends PeriodicEventHandler {
  private AperiodicEvent event;

  private int counter = 0;

  public Handler3(AperiodicEvent event) {
    super(
      new PriorityParameters(
         javax.safetycritical.PriorityScheduler.instance().getMaxPriority()),
      new PeriodicParameters(new AbsoluteTime(), new RelativeTime(500, 0)),
      new StorageConfigurationParameters(10000, 10000, 10000),
      10000, "Handler3");
    this.event = event;
  }

  public void handleEvent() {
    System.out.println("Handler3::handleEvent() called <" + counter + ">");
    event.fire();
    counter++;
    if (counter == 5) {
      Mission.getCurrentMission().requestTermination();
    }
  }
}

import javax.realtime.*;

import javax.safetycritical.*;
import javax.safetycritical.annotate.*;

class Handler1 extends AperiodicEventHandler {
  public Handler1() {
    super(
      new PriorityParameters(
         javax.safetycritical.PriorityScheduler.instance().getMaxPriority()),
      new AperiodicParameters(),
      new StorageConfigurationParameters(10000, 10000, 10000),
      10000, "Handler1");
  }

  public void handleEvent() {
    System.out.println("Handler1::handleEvent() called");
  }
}

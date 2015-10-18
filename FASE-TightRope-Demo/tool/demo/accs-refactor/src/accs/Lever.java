package accs;

import static accs.ExternalEvents.*;

import javax.realtime.PriorityParameters;

import javax.safetycritical.AperiodicLongEvent;
import javax.safetycritical.AperiodicLongEventHandler;
import javax.safetycritical.AperiodicParameters;
import javax.safetycritical.PriorityScheduler;
import javax.safetycritical.StorageParameters;

import javax.safetycritical.annotate.*;

@HandlerId("LeverHId")
@BoundEvent(value = "lever", type = "LEVEL\\_INPUT")
public class Lever extends AperiodicLongEventHandler {
  private Controller cruise;

  public Lever(Controller cruiseArg, @Ignore AperiodicLongEvent lever_event) {
    super(
      new PriorityParameters(
        PriorityScheduler.instance().getNormPriority()),
      new AperiodicParameters(null, null),
      new StorageParameters(32768, 4096, 4096),
      lever_event, "Lever");
    cruise = cruiseArg;
  }

  public void handleAsyncLongEvent(long param) {
    int event = (int) param;
    switch (event) {
      case ACTIVATE:
        cruise.activate();
        break;

      case DEACTIVATE:
        cruise.deactivate();
        break;

      case START_ACCELERATING:
        cruise.startAccelerating();
        break;

      case STOP_ACCELERATING:
        cruise.stopAccelerating();
        break;

      case RESUME:
        cruise.resume();
        break;

      default:
        assert false : "UNKNOWN LEVER INTERRUPT";
    }
  }
}

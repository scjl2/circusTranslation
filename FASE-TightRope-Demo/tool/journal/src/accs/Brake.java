package accs;

import static accs.ExternalEvents.*;

import javax.realtime.PriorityParameters;

import javax.safetycritical.AperiodicLongEvent;
import javax.safetycritical.AperiodicLongEventHandler;
import javax.safetycritical.AperiodicParameters;
import javax.safetycritical.PriorityScheduler;
import javax.safetycritical.StorageParameters;

import javax.safetycritical.annotate.*;

@HandlerId("BrakeHId")
@BoundEvents({
@BoundEvent("brake\\_pressed"),
@BoundEvent("brake\\_released")})
public class Brake extends AperiodicLongEventHandler {
  private Controller cruise;

  public Brake(Controller cruise, @Ignore AperiodicLongEvent brake_event) {
    super(
      new PriorityParameters(
        PriorityScheduler.instance().getNormPriority()),
      new AperiodicParameters(null, null),
      new StorageParameters(32768, 4096, 4096),
      brake_event, "Brake");
    this.cruise = cruise;
  }

  public void handleAsyncLongEvent(long param) {
    int event = (int) param;
    switch (event) {
      case BRAKE_ON:
        cruise.brakePressed();
        break;

      case BRAKE_OFF:
        cruise.brakeReleased();
        break;

      default:
        assert false : "UNKNOWN BRAKE INTERRUPT";
    }
  }
}

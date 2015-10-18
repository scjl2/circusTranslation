package accs;

import static accs.ExternalEvents.*;

import javax.realtime.PriorityParameters;

import javax.safetycritical.AperiodicLongEvent;
import javax.safetycritical.AperiodicLongEventHandler;
import javax.safetycritical.AperiodicParameters;
import javax.safetycritical.PriorityScheduler;
import javax.safetycritical.StorageParameters;

import javax.safetycritical.annotate.*;

@HandlerId("GearHId")
@BoundEvents({
@BoundEvent("top\\_gear\\_engaged"),
@BoundEvent("top\\_gear\\_disengaged")})
public class Gear extends AperiodicLongEventHandler {
  private Controller cruise;

  public Gear(Controller cruise, @Ignore AperiodicLongEvent gear_event) {
    super(
      new PriorityParameters(
        PriorityScheduler.instance().getNormPriority()),
      new AperiodicParameters(null, null),
      new StorageParameters(32768, 4096, 4096),
      gear_event, "Gear");
    this.cruise = cruise;
  }

  public void handleAsyncLongEvent(long param) {
    int event = (int) param;
    switch (event) {
      case TOP_GEAR_ENGAGED:
        cruise.topGearEngaged();
        break;

      case TOP_GEAR_DISENGAGED:
        cruise.topGearDisengaged();
        break;

      default:
        assert false : "UNKNOWN GEAR INTERRUPT";
    }
  }
}

package accs;

import static accs.ExternalEvents.*;

import javax.realtime.PriorityParameters;

import javax.safetycritical.AperiodicLongEvent;
import javax.safetycritical.AperiodicLongEventHandler;
import javax.safetycritical.AperiodicParameters;
import javax.safetycritical.PriorityScheduler;
import javax.safetycritical.StorageParameters;

import javax.safetycritical.annotate.*;

@HandlerId("EngineHId")
@BoundEvent(value = "engine", type = "BOOL")
public class Engine extends AperiodicLongEventHandler {
  private Controller cruise;

  public Engine(Controller cruise, @Ignore AperiodicLongEvent engine_event) {
    super(
      new PriorityParameters(
        PriorityScheduler.instance().getNormPriority()),
      new AperiodicParameters(null, null),
      new StorageParameters(32768, 4096, 4096),
      engine_event, "Engine");
    this.cruise = cruise;
  }

  public void handleAsyncLongEvent(long param) {
    int event = (int) param;
    switch (event) {
      case ENGINE_ON:
        cruise.engineOn();
        break;

      case ENGINE_OFF:
        cruise.engineOff();
        break;

      default:
        assert false : "UNKNOWN ENGINE INTERRUPT";
    }
  }
}

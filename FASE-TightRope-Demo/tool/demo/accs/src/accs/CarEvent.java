/* REVIEWED */
package accs;

import javax.realtime.*;

import javax.safetycritical.*;

public class CarEvent {
  private CarEventType type;
  private int delay;

  public CarEvent(CarEventType type, int delay) {
    this.type = type;
    this.delay = delay;
  }

  public synchronized CarEventType getType() {
    return type;
  }

  public synchronized int getDelay() {
    return delay;
  }

  public String toString() {
    return "CarEvent[" + type + "," + delay + "]";
  }
}

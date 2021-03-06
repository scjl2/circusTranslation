/**
 * @author Frank Zeyda
 */
package cdx;

import javax.safetycritical.AperiodicEvent;

/**
 * This class is used to control the execution of OutputCollisionsHandler.
 * In particular, it ensures the release of this handler once all instances
 * of DetectorHandler have finished their computational work.
 */
public class DetectorControl {
  private final boolean[] idle;

  /* Note that the following field is not in the OhCircus class model; this
   * is because the class model only accounts for the data operations in the
   * class but not the active behaviour of firing the SCJ event below. */
  private final AperiodicEvent output;

  public DetectorControl(AperiodicEvent event, int n) {
    idle = new boolean[n];
    output = event;
  }

  public synchronized void start() {
    for (int index = 0; index < idle.length; index++) {
      idle[index] = false;
    }
  }

  public synchronized void notify(int id) {
    idle[id - 1] = true;
    if (done()) {
        /* Release handler to output the detected collisions. */
        output.fire();
    }
  }

  protected synchronized boolean done() {
    boolean result = true;
    for (int index = 0; index < idle.length; index++) {
      if (!idle[index]) { result = false; }
    }
    return result;
  }
}

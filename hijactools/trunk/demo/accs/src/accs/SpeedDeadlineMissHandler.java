package accs;

import javax.realtime.*;

import javax.safetycritical.*;

/* There is a pending issue how this class may be integrated into the SCJ
 * implementation, namely how are missed deadlines dealt with? */

public class SpeedDeadlineMissHandler extends AperiodicEventHandler {
  private SpeedMonitor speedo;
  private int missedDeadlines = 0;
  private final int THRESHOLD = 10;

  public SpeedDeadlineMissHandler(SpeedMonitor speedo,
      PriorityParameters priority,
      AperiodicParameters release,
      StorageConfigurationParameters storage) {
    super(priority, release, storage, 10000, "SpeedDeadlineMissHandler");
    this.speedo = speedo;
  }

  /*public void handleAsyncEvent() {*/
  public void handleEvent() {
    missedDeadlines = missedDeadlines++;
    if (missedDeadlines > THRESHOLD) {
      /* Turn on light on dashboard to indicate that the system needs tuning. */
    }
    /* Commented out since not available in the SCJ. */
    /*speedo.schedulePeriodic();*/
  }

  /* Additional methods that need to be defined for the SCJ infrastructure. */
  public StorageConfigurationParameters getThreadConfigurationParameters() {
    return null;
  }

  public void register() { }
}

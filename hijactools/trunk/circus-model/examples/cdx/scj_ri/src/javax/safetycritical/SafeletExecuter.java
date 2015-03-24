package javax.safetycritical;

import javax.realtime.ImmortalMemory;

/* Class added by Frank Zeyda */

public class SafeletExecuter implements Runnable {
  public Safelet safelet;

  public SafeletExecuter(Safelet safelet) {
    this.safelet = safelet;
  }

  protected void message(String msg) {
    System.out.println("[SafeletExecuter] " + msg);
  }

  public void run() {
    message("Safelet started");
    safelet.setup();
    MissionSequencer sequencer = safelet.getSequencer();
    sequencer.start();
    sequencer.waitForTermination();
    safelet.teardown();
    message("Safelet terminated");
  }

  static public void run(Safelet safelet) {
    SafeletExecuter executer = new SafeletExecuter(safelet);
    /* The Safelet needs to be executed in immortal memory. */
    ImmortalMemory.instance().executeInArea(executer);
  }
}

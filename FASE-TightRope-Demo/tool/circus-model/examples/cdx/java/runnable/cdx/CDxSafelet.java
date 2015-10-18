/**
 * @author Frank Zeyda, Kun Wei
 */
package cdx;

import javax.safetycritical.Safelet;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.annotate.Level;

/**
 * Safelet of the parallel CDx.
 *
 * It returns an instance of CDxMissionSequencer.
 */
public class CDxSafelet implements Safelet {
  public CDxSafelet() { }

  public void setup() { }

  public MissionSequencer getSequencer() {
    return new CDxMissionSequencer();
  }

  public void teardown() { }

    /**
     * Using an outdated version of the SCJ reference implementation.
     */
  public Level getLevel() {
    return Level.LEVEL_1;
  }
}

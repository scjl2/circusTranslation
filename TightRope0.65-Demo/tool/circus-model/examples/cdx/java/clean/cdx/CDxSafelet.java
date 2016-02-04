/**
 * @author Frank Zeyda, Kun Wei
 */
package cdx;

import javax.safetycritical.Safelet;
import javax.safetycritical.MissionSequencer;

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
}

/**
 * @author Frank Zeyda
 */
package cdx;

import javax.safetycritical.SafeletExecuter;

/**
 * Entry point of the SCJ application.
 */
public class Main {
  public static void main(String[] args) {
    SafeletExecuter.run(new CDxSafelet());
  }
}

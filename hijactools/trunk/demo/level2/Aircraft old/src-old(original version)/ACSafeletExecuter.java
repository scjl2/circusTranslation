/** Aircraft - Mode Change Example
 * 
 * 	Executes the application's <code>Safelet</code>
 * 
 *   @author Matt Luckcuck <ml881@york.ac.uk>
 */
package scjlevel2examples.aircraft;

import javax.safetycritical.LaunchLevel2;

public class ACSafeletExecuter
{

	/**
	 * Runs the Safelet, which starts the application
	 */
	public static void main(String[] args)
	{
		ACSafelet GERTI = new ACSafelet();
		new LaunchLevel2(GERTI);
	}
}


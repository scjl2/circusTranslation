/** Aircraft - Mode Change Example
 * 
 * 	Executes the application's <code>Safelet</code>
 * 
 *   @author Matt Luckcuck <ml881@york.ac.uk>
 */
package scjlevel2examples.aircraft;

import javax.safetycritical.Launcher;

public class ACSafeletExecuter  extends Launcher	
{

	public ACSafeletExecuter(Safelet<Mission> safelet)
	{
		super(safelet, 2);
	}

	public static void main(String[] args)
	{
		Console.println("FlatBufferSafeletExecuter ");
		// Run the safelet which starts the whole application
		ACSafelet GERTI = new ACSafelet();

		new ACSafeletExecuter(GERTI).run();
		
		//new LaunchLevel2(new FlatBuffer()).run();
	}
}


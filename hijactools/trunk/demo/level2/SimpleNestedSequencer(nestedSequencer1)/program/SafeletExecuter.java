/** Simple Nested Sequencer
 * 
 * 	Executes the application's <code>Safelet</code>
 * 
 *   @author Matt Luckcuck <ml881@york.ac.uk>
 */
package nestedSequencer1;

import javax.safetycritical.LaunchLevel2;

import devices.Console;


public class SafeletExecuter 
{
	/**
	 * Runs the Safelet, which starts the application
	 */
	public static void main(String[] args)
	{
			
		Console.println("Launcher");
		
		new LaunchLevel2(new MySafelet());
	}
}

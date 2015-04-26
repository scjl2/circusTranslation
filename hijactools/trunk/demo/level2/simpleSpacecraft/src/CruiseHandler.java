/** Spacecraft - Mode Change Example
 * 
 * 	Handler for responding to the pilot starting an engine burn
 * 	
 *   @author Matt Luckcuck <ml881@york.ac.uk>
 */
package scjlevel2examples.spacecraft;

import javax.realtime.AperiodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.AperiodicEventHandler;
import javax.safetycritical.StorageParameters;

import devices.Console;

public class CruiseHandler extends AperiodicEventHandler
{
	/**
	 * Class Constructor
	 * 
	 * @param priorityParameters
	 *            the priority parameters for this handler
	 * @param periodicParameters
	 *            the periodic parameters for this handler
	 * @param storageConfigurationParameters
	 *            the storage parameters for this handler
	 * @param size
	 *            the size of the private memory for this handler
	 */
	public CruiseHandler(PriorityParameters priority,
			AperiodicParameters release, StorageParameters storage)
	{
//		super(priority, release, storage, "Cruise Handler");
		super(priority, release, storage);
	}

	/**
	 * Called when the handler is fired
	 */
	@Override
	public void handleAsyncEvent()
	{
		Console.println("Handling Cruise");
		// Activate the engine burn
	}
}
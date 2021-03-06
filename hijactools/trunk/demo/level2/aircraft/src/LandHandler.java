/** Aircraft - Mode Change Example

 *   Handler for dealing with the Aircraft's landing pahse

 *   @author Matt Luckcuck <ml881@york.ac.uk>
 */
package aircraft;

import javax.realtime.AperiodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.AperiodicEventHandler;
import javax.safetycritical.StorageParameters;

public class LandHandler extends AperiodicEventHandler
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
	public LandHandler(PriorityParameters priority,
			AperiodicParameters release, StorageParameters storage, String name)
	{
		super(priority, release, storage, null);

	}

	/**
	 * Called when the handler is fired
	 */
	@Override
	public void handleAsyncEvent()
	{
		System.out.println("Handling Landing");

	}
}

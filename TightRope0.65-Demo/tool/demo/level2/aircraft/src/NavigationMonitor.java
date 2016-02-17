/** Aircraft - Mode Change Example
 *
 * 	Checks the navigation systems of the aircraft to ensure that the craft is on the correct course
 *
 *   @author Matt Luckcuck <ml881@york.ac.uk>
 */
package scjlevel2examples.aircraft;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;

/**
 * Handler for monitoring the conditions which have to be true for the craft to
 * begin cruising
 *
 * @author Matt Luckcuck
 *
 */
public class NavigationMonitor extends PeriodicEventHandler
{
	private final MainMission mainMission;


	public NavigationMonitor(PriorityParameters priority,
			PeriodicParameters periodic, StorageParameters storage,
			String name, MainMission mainMission)
	{
		super(priority, periodic, storage);
		this.mainMission = mainMission;
	}

	/**
	 * Called when the handler is fired
	 */
	@SuppressWarnings("unused")
	@Override
	public void handleAsyncEvent()
	{
		// read and check these variables
		double heading = mainMission.getHeading();
		double airSpeed = mainMission.getAirSpeed();
		double altitude = mainMission.getAltitude();

		// Obviously this would then check the variables again expected values

	}
}

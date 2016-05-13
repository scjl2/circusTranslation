/** Aircraft - LandingGearHandler
 *  
 * 	Marker interface for missions that use the LandingGearHandler 
 */
package aircraft;

public interface LandingGearUser
{
	public void deployLandingGear();

	public void stowLandingGear();

	public boolean isLandingGearDeployed();

}

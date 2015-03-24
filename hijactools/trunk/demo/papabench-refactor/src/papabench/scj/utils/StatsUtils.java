/**
 * 
 */
package papabench.scj.utils;

import java.util.Random;

/**
 * @author Michal Malohlava
 *
 */
final public class StatsUtils {
	
	public static float randNormalDistribution(float mu, float sigma) {
		Random random = new Random();
		
		float u1 = random.nextFloat();
		float u2 = random.nextFloat();
		
		return (float) (mu + sigma * Math.sqrt(-2 * Math.log(u1)) * Math.cos(2*Math.PI * u2));
	}
}

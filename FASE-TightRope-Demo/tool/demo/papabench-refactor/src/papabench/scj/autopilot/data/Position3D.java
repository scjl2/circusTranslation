/**
 * 
 */
package papabench.scj.autopilot.data;

/**
 * Position in 3D.
 * 
 * @author Michal Malohlava
 *
 */
public class Position3D extends Position2D {
	
	public Position3D(float x, float y, float z1) {
		super(x,y);		
		z = z1;
	}
	
	public float z;
	
	@Override
	public String toString() {		
		return "[" + x + "," + y + "," + z + "]";
	}
}

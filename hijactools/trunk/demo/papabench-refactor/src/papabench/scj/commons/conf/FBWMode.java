/**
 * 
 */
package papabench.scj.commons.conf;

/**
 * @author Michal Malohlava
 * 
 */
public enum FBWMode {
	MANUAL(0), AUTO(1), FAILSAFE(2);

	int value;

	private FBWMode(int value1) {
		value = value1;
	}

	public int getValue() {
		return value;
	}
	
	public static FBWMode valueOf(int i) {
		return values()[i];				
	}
}

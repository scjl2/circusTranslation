/**
 * 
 */
package papabench.scj.fbw.modules;

import papabench.scj.commons.conf.FBWMode;

/**
 * FBW status
 * 
 * @author Michal Malohlava
 *
 */
public interface FBWStatus {
	
	FBWMode getFBWMode();
	void setFBWMode(FBWMode mode);
	
	boolean isRadioOK();
	void setRadioOK(boolean value);
	
	boolean isRadioReallyLost();
	void setRadioReallyLost(boolean value); 
	
	boolean isMega128OK();
	void setMega128OK(boolean value);
}

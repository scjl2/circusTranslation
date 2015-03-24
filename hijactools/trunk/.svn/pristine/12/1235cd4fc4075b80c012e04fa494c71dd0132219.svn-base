/**
 * 
 */
package papabench.scj.fbw.modules.impl;

import papabench.scj.commons.conf.FBWMode;
import papabench.scj.fbw.modules.FBWStatus;

/**
 * @author Michal Malohlava
 *
 */
public class FBWStatusImpl implements FBWStatus {
	
	protected FBWMode fbwMode;
	protected boolean isMega128OK = true;
	protected boolean isRadioOK = true;
	protected boolean isRadioReallyLost = false;

	public FBWMode getFBWMode() {
		return fbwMode;
	}

	public boolean isMega128OK() {
		return isMega128OK;
	}

	public boolean isRadioOK() {
		return isRadioOK;
	}

	public boolean isRadioReallyLost() {
		return isRadioReallyLost;
	}

	public void setFBWMode(FBWMode mode) {
		fbwMode = mode;		
	}

	public void setMega128OK(boolean value) {
		isMega128OK = value;
	}

	public void setRadioOK(boolean value) {
		isRadioOK = value;
	}

	public void setRadioReallyLost(boolean value) {
		isRadioReallyLost = value;
	}

}

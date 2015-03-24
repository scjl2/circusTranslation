import javax.safetycritical.Safelet;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.annotate.Level;

// Implementaion of a Safelet

public class PMSafelet implements Safelet {

	public void initializeApplication() {
	}

	public MissionSequencer getSequencer() {
		return new MainPMMissionSequence();
	}

}

import javax.safetycritical.Safelet;
import javax.safetycritical.MissionSequencer;

public class MainSafelet implements Safelet {

    public MainSafelet() { }

    public void initializeApplication() {
    }

    public MissionSequencer getSequencer() {
       return new MainMissionSequencer();
    }

}

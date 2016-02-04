package hijac.tools.scjmsafe.checker.environment;

import java.util.ArrayList;

import hijac.tools.scjmsafe.language.MSafeMission;

public class MissionEnv {

    private ArrayList<String> mids;
    private ArrayList<MSafeMission> missions;

    public void MissionEnv(ArrayList<String> ids, ArrayList<MSafeMission> missions) {
        mids = ids;
        this.missions = missions;
    }

}

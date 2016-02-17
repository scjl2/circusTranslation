package hijac.tools.scjmsafe.language;

import java.util.ArrayList;

import hijac.tools.scjmsafe.language.Commands.*;
import hijac.tools.scjmsafe.language.Method.*;

public class MSafeMissionSeq extends MSafeSuperClass {

    private Command getNextMission;

    public MSafeMissionSeq(String id) {
        name = id;
        fields = new ArrayList<Declaration>(0);
        init = new ArrayList<Command>(0);
        constrs = new ArrayList<Method>(0);
        methods = new ArrayList<Method>(0);
    }

    public void addGetNextMission(Command m) {
        getNextMission = m;
    }

    public Command getGetNextMission() {
        return getNextMission;
    }

}

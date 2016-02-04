package hijac.tools.scjmsafe.language;

import java.util.ArrayList;

import hijac.tools.scjmsafe.language.Commands.*;
import hijac.tools.scjmsafe.language.Method.*;

public class MSafeSafelet extends MSafeSuperClass {

    private Command initializeApplication;
    private MSafeMissionSeq mSeq;
    private Command getSequencer;

    public MSafeSafelet(String id) {
        name = id;
        fields = new ArrayList<Declaration>(0);
        init = new ArrayList<Command>(0);
        constrs = new ArrayList<Method>(0);
        methods = new ArrayList<Method>(0);
    }

    public void addInitApp(Command m) {
        initializeApplication = m;
    }

    public Command getInitApp() {
        return initializeApplication;
    }

    public void addMSeq(MSafeMissionSeq missionSeq) {
        mSeq = missionSeq;
    }

    public MSafeMissionSeq getMissionSeq() {
        return mSeq;
    }

    public void addGetSequencer(Command m) {
        getSequencer = m;
    }

    public Command getGetSequencer() {
        return getSequencer;
    }

}

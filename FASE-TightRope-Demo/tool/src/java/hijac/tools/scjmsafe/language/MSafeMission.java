package hijac.tools.scjmsafe.language;

import java.util.ArrayList;

import hijac.tools.scjmsafe.language.Commands.*;
import hijac.tools.scjmsafe.language.Method.*;

public class MSafeMission extends MSafeSuperClass {

    private Command initialize;
    private ArrayList<String> handlers;
    private Command cleanUp;

    public MSafeMission(String id) {
        name = id;
        fields = new ArrayList<Declaration>(0);
        init = new ArrayList<Command>(0);
        constrs = new ArrayList<Method>(0);
        handlers = new ArrayList<String>(0);
        methods = new ArrayList<Method>(0);
    }

    public void addInitialize(Command m) {
        initialize = m;
    }

    public Command getInitialize() {
        return initialize;
    }

    public void addHandler(String handler) {
        handlers.add(handler);
    }

    public ArrayList<String> getHandlers() {
        return handlers;
    }

    public void addCleanUp(Command m) {
        cleanUp = m;
    }

    public Command getCleanUp() {
        return cleanUp;
    }

}

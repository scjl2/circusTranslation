package hijac.tools.scjmsafe.language;

import java.util.ArrayList;

import hijac.tools.scjmsafe.language.Commands.*;
import hijac.tools.scjmsafe.language.Method.*;

public class MSafeHandler extends MSafeSuperClass {

    private Command hAe;

    public MSafeHandler(String id) {
        name = id;
        fields = new ArrayList<Declaration>(0);
        init = new ArrayList<Command>(0);
        constrs = new ArrayList<Method>(0);
        methods = new ArrayList<Method>(0);
    }

    public void addHAE(Command m) {
        hAe = m;
    }

    public Command getHAE() {
        return hAe;
    }

}

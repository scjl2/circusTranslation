package hijac.tools.scjmsafe.checker.environment;

import java.util.ArrayList;

import hijac.tools.scjmsafe.language.MSafeHandler;

public class HandlerEnv {

    private ArrayList<String> hids;
    private ArrayList<MSafeHandler> handlers;

    public void HandlerEnv(ArrayList<String> ids, ArrayList<MSafeHandler> handlers) {
        hids = ids;
        this.handlers = handlers;
    }

}

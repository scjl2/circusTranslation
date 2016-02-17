package hijac.tools.scjmsafe.checker.environment;

import java.util.ArrayList;

import hijac.tools.scjmsafe.language.MSafeClass;

public class ClassEnv {

    private ArrayList<String> cids;
    private ArrayList<MSafeClass> classes;

    public void ClassEnv(ArrayList<String> ids, ArrayList<MSafeClass> classes) {
        cids = ids;
        this.classes = classes;
    }

}

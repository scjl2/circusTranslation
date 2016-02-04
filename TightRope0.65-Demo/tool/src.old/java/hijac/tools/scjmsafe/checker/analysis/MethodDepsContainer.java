package hijac.tools.scjmsafe.checker.analysis;

import hijac.tools.scjmsafe.language.Method.*;
import hijac.tools.scjmsafe.language.MSafeSuperClass;

import java.util.ArrayList;

public class MethodDepsContainer {

    Method method;
    boolean constr;
    MSafeSuperClass assocClass;
    ArrayList<MethodDepEntry> deps;
    boolean analysed;

    public MethodDepsContainer(Method m, MSafeSuperClass c, boolean cons) {
        method = m;
        assocClass = c;
        constr = cons;
        deps = new ArrayList<MethodDepEntry>(0);
        analysed = false;
    }

    public Method getMethod() {
        return method;
    }

    public MSafeSuperClass getMSafeClass() {
        return assocClass;
    }

    public void addDep(MethodDepEntry dep) {
        deps.add(dep);
    }

    public boolean containsDep(MethodDepEntry dep) {
        boolean result = false;
        for (MethodDepEntry m : deps) {
            if (m.getName().equals(dep.getName())) {
                result = true;
            }
        }
        return result;
    }

    public void removeDep(MethodDepEntry dep) {
        deps.remove(dep);
    }

    public boolean isConstr() {
        return constr;
    }

    public ArrayList<MethodDepEntry> getDeps() {
        return deps;
    }

    public boolean beenAnalysed() {
        return analysed;
    }

    public void markAsAnalysed() {
        analysed = true;
    }

}

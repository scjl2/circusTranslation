package hijac.tools.scjmsafe.language;

import java.util.ArrayList;

import hijac.tools.scjmsafe.language.Commands.*;
import hijac.tools.scjmsafe.language.Method.*;

public class MSafeClass extends MSafeSuperClass {

    String classExtends;

    public MSafeClass(String id, String classExtends, String classEmbeddedParent) {
        name = id;
        fields = new ArrayList<Declaration>(0);
        init = new ArrayList<Command>(0);
        constrs = new ArrayList<Method>(0);
        methods = new ArrayList<Method>(0);
        this.classExtends = classExtends;
        embeddedInClass = classEmbeddedParent;
    }

    public MSafeClass(String id, String classEmbeddedParent) {
        name = id;
        fields = new ArrayList<Declaration>(0);
        init = new ArrayList<Command>(0);
        constrs = new ArrayList<Method>(0);
        methods = new ArrayList<Method>(0);
        classExtends = "";
        embeddedInClass = classEmbeddedParent;
    }

    public String getExtends() {
        return classExtends;
    }

}

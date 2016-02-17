package hijac.tools.scjmsafe.language;

import java.util.ArrayList;

import hijac.tools.scjmsafe.language.Commands.*;
import hijac.tools.scjmsafe.language.Method.*;
import hijac.tools.scjmsafe.language.Expressions.*;

public abstract class MSafeSuperClass {

    protected String name;
    protected ArrayList<Declaration> fields;
    protected ArrayList<Command> init;
    protected ArrayList<Method> constrs;
    protected ArrayList<Method> methods;
    protected String embeddedInClass;

    public void addName(String id) {
        name = id;
    }

    public String getName() {
        return name;
    }

    public void addField(Declaration dec) {
        fields.add(dec);
    }

    public ArrayList<Declaration> getFields() {
        return fields;
    }

    public void addInit(Command c) {
        init.add(c);
    }

    public ArrayList<Command> getInit() {
        return init;
    }

    public void addConstr(Method m) {
        constrs.add(m);
    }

    public ArrayList<Method> getConstrs() {
        return constrs;
    }

    public ArrayList<Method> getConstr(ArrayList<Expr> params) {
        ArrayList<Method> result = new ArrayList<Method>(0);
        ArrayList<String> paramTypes = new ArrayList<String>(0);
        for (Expr e : params) {
            paramTypes.add(e.getTypeName());
        }
        for (Method c : constrs) {
            ArrayList<String> constrTypes = new ArrayList<String>(0);
            for (Variable v : c.getParams()) {
                constrTypes.add(v.getTypeName());
            }
            if (paramTypes.size() == constrTypes.size()) {
                boolean match = true;
                for (int i = 0; i < paramTypes.size(); i++) {
                    if (!paramTypes.get(i).equals(constrTypes.get(i))) {
                        if (!paramTypes.get(i).equals("Unknown") && !paramTypes.get(i).equals("Val") && !constrTypes.get(i).equals("Unknown")) {
                            match = false;
                        }
                    }
                }
                if (match) {
                    result.add(c);
                }
            }
        }
        return result;
    }

    public void addMethod(Method m) {
        methods.add(m);
    }

    public ArrayList<Method> getMethods() {
        return methods;
    }

    public void addEmbeddedInClass(String embeddedIn) {
        embeddedInClass = embeddedIn;
    }

    public String getEmbeddedInClass() {
        return embeddedInClass;
    }

}

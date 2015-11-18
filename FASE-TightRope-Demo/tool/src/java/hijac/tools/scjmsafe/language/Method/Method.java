package hijac.tools.scjmsafe.language.Method;

import java.util.ArrayList;

import com.sun.source.tree.*;
import com.sun.source.util.*;

import hijac.tools.scjmsafe.language.Commands.*;
import hijac.tools.scjmsafe.language.Expressions.*;

public class Method {

    private String name;
    private Tree returnType;
    private String returnTypeName;
    private ArrayList<Variable> params;
    private MethodProperties properties;
    private ArrayList<Expr> varsToRemove;
    private Command body;
    private ArrayList<Expr> visibleFields;

    public Method(String id) {
        name = id;
        params = new ArrayList<Variable>(0);
        properties = new MethodProperties();
        varsToRemove = new ArrayList<Expr>(0);
        visibleFields = new ArrayList<Expr>(0);
    }
    
    public void addName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addReturnType(Tree returnType) {
        this.returnType = returnType;
    }

    public Tree getReturnType() {
        return returnType;
    }

    public void addReturnTypeName(String returnTypeName) {
        this.returnTypeName = returnTypeName;
    }

    public String getReturnTypeName() {
        return returnTypeName;
    }


    public void addParam(Variable var) {
        params.add(var);
    }
    
    public ArrayList<Variable> getParams() {
        return params;
    }

    public ArrayList<String> getParamTypes() {
        ArrayList<String> result = new ArrayList<String>(0);
        for (Variable v : params) {
            result.add(v.getVarType().getTypeName());
        }
        if (result.size() != params.size()) {
            System.out.println("ERROR - Analysing return types of a method has failed");
        }
        return result;
    }

    public ArrayList<String> getParamTypesNoResult() {
        ArrayList<String> result = new ArrayList<String>(0);
        for (Variable v : params) {
            if (!v.getVarType().isResultVar()) {
                result.add(v.getVarType().getTypeName());
            }
        }
        return result;
    }

    public void addProperties(MethodProperties p) {
        properties = p;
    }

    public MethodProperties getProperties() {
        return properties;
    }

    public void addVarToRemove(Expr e) {
        boolean found = false;
        for (Expr e1 : varsToRemove) {
            if (e1.equalTo(e)) {
                found = true;
            }
        }
        if (!found) {
            varsToRemove.add(e);
        }
    }
    
    public ArrayList<Expr> getVarsToRemove() {
        return varsToRemove;
    }

    public void addBody(Command c) {
        body = c;
    }

    public Command getBody() {
        return body;
    }

    public void addVisibleField(Expr e) {
        visibleFields.add(e);
    }

    public ArrayList<Expr> getVisibleFields() {
        return visibleFields;
    }

}

package hijac.tools.scjmsafe.language.Method;

import java.util.ArrayList;

import com.sun.source.tree.*;
import com.sun.source.util.*;

import hijac.tools.scjmsafe.language.Commands.*;
import hijac.tools.scjmsafe.language.Expressions.*;

public class MethodSig {

    private String name;
    private String className;
    private String classExtends;
    private ArrayList<String> descendants;
    private Tree returnType;
    private String returnTypeName;
    private ArrayList<String> paramTypes;

    public MethodSig(String id, String classID, String classExtends, Tree type, ArrayList<String> params) {
        name = id;
        returnType = type;
        className = classID;
        this.classExtends = classExtends;
        descendants = new ArrayList<String>(0);
        paramTypes = params;
        if (returnType != null) {
            returnTypeName = returnType.toString();
        } else {
            returnTypeName = "void";
        }
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

    public void addClassName(String name) {
        className = name;
    }

    public String getClassName() {
        return className;
    }

    public String getClassExtends() {
        return classExtends;
    }

    public ArrayList<String> getDescendants() {
        return descendants;
    }

    public void addDescendant(String s) {
        descendants.add(s);
    }

    public void addDescendants(ArrayList<String> descendants) {
        this.descendants = descendants;
    }

    public ArrayList<String> getParamTypes() {
        return paramTypes;
    }

}

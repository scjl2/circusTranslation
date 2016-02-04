package hijac.tools.scjmsafe.checker.analysis;

import java.util.ArrayList;

public class MethodDepEntry {

    String methodName;
    ArrayList<String> params;
    String className;

    public MethodDepEntry(String name, ArrayList<String> params, String c) {
        methodName = name;
        this.params = params;
        className = c;
    }

    public String getName() {
        return methodName;
    }

    public String getClassName() {
        return className;
    }

    public ArrayList<String> getParams() {
        return params;
    }

    public void print() {
        System.out.print(methodName + "(");
        for (int i = 0; i < params.size(); i++) {
            System.out.print(params.get(i));
            if (i < params.size() -1) {
                System.out.print(", ");
            }
        }
        System.out.print(") in class " + className);
    }
}

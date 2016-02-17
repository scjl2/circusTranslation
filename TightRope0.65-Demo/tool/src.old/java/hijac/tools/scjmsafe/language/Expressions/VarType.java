package hijac.tools.scjmsafe.language.Expressions;

public class VarType {

    private String typeName;
    private boolean isArray;
    private boolean isPrimitive;
    private boolean isReference;
    private boolean resultVar;

    public VarType() {
        typeName = "Unknown";
        resultVar = false;
        isArray = false;
        isReference = false;
        isPrimitive = false;
    }

    public VarType(String type) {
        typeName = type;
        resultVar = false;
        isArray = false;
        isReference = false;
        isPrimitive = false;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String name) {
        typeName = name;
    }

    public void setPrimitive() {
        isPrimitive = true;
    }
    
    public void setReference() {
        isReference = true;
    }

    public void setArray() {
        isArray = true;
    }

    public void setResultVar() {
        resultVar = true;
    }

    public boolean isPrimitive() {
        return isPrimitive;
    }

    public boolean isReference() {
        return isReference;
    }

    public boolean isArray() {
        return isArray;
    }

    public boolean isResultVar() {
        return resultVar;
    }

    public void printType() {
        if (isPrimitive()) {
            System.out.println("Primitive");
        }
        if (isReference()) {
            System.out.println("Reference");
        }
        if (isArray()) {
            System.out.println("Array");
        }
    }

}

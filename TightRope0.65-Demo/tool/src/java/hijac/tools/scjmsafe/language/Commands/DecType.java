package hijac.tools.scjmsafe.language.Commands;

public class DecType {

    private boolean isPrimitive = false;
    private boolean isReference = false;
    private boolean isArray = false;

    String typeName;

    public DecType(String type) {
        typeName = type;
    }

    public String getTypeName() {
        return typeName;
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

    public boolean isPrimitive() {
        return isPrimitive;
    }

    public boolean isReference() {
        return isReference;
    }

    public boolean isArray() {
        return isArray;
    }


    public void printDecTypeType() {
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

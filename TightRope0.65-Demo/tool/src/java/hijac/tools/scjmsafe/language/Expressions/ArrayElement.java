package hijac.tools.scjmsafe.language.Expressions;

public class ArrayElement extends Identifier {

    private String name;
    private String type;

    public ArrayElement(String n) {
        name = n;
    }

    public ArrayElement(String n, String t) {
        name = n;
        type = t;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setType(String t) {
        type = t;
    }

    @Override
    public void printExpression() {
        System.out.print(name + "[Val]");
    }

    @Override
    public String getExpressionString() {
        return (name + "[Val]");
    }

    @Override
    public String getTypeName() {
        return type;
    }

    public int getLength() {
        return 1;
    }

}

package hijac.tools.scjmsafe.language.Expressions;

//public class Null extends Expr {
public class Null extends Identifier {

    public Null() {

    }

    public void printExpression() {
        System.out.print("null");
    }

    public String getExpressionString() {
        return "null";
    }

    public String getName() {
        return "null";
    }

    @Override
    public String getTypeName() {
        return "null";
    }

    public int getLength() {
        return 0;
    }

}

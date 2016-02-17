package hijac.tools.scjmsafe.language.Expressions;

//public class This extends Expr {
public class This extends Identifier {

    public This() {

    }

    public void printExpression() {
        System.out.print("this");
    }

    public String getExpressionString() {
        return "this";
    }

    public String getName() {
        return "this";
    }

    @Override
    public String getTypeName() {
        return "Unknown";
    }

    public int getLength() {
        return 1;
    }

}

package hijac.tools.scjmsafe.language.Expressions;

public class OtherExpr extends Expr {

    public OtherExpr() {}

    public void printExpression() {
        System.out.print("OtherExpr");
    }

    public String getExpressionString() {
        return "OtherExpr";
    }

    @Override
    public String getTypeName() {
        return "OtherExpr";
    }

    public int getLength() {
        return 0;
    }

}

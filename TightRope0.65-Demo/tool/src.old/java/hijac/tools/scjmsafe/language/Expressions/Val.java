package hijac.tools.scjmsafe.language.Expressions;

public class Val extends Expr {

    public Val() { }


    public void printExpression() {
        System.out.print("Val");
    }

    public String getExpressionString() {
        return "Val";
    }

    @Override
    public String getTypeName() {
        return "Val";
    }

    public int getLength() {
        return 1;
    }

}

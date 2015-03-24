package hijac.tools.scjmsafe.language.Expressions;

public abstract class Expr {

    public abstract void printExpression();

    public abstract String getExpressionString();

    public boolean equalTo(Expr e) {
        if (e.getExpressionString().equals(this.getExpressionString())) {
            return true;
        } else {
            return false;
        }
    }

    public abstract String getTypeName();

    public abstract int getLength();

}

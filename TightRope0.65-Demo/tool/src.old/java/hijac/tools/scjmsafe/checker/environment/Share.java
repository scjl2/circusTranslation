package hijac.tools.scjmsafe.checker.environment;
import hijac.tools.scjmsafe.language.Expressions.*;

public class Share {

    private Expr le;
    private Expr re;

    public Share(Expr lexpr, Expr rexpr) {
        le = lexpr;
        re = rexpr;
    }

    public Expr getLExpr() {
        return le;
    }
    
    public void setLExpr(Expr e ) {
        le = e;
    }

    public Expr getRExpr() {
        return re;
    }

    public void setRExpr(Expr e ) {
        re = e;
    }

    public boolean containsExpr(Expr expr) {
        boolean result = false;
        if (le.equalTo(expr) || re.equalTo(expr)) {
            result = true;
        }
        return result;
    }

    public boolean equalTo(Share s) {
        if (le.equalTo(s.getLExpr()) && re.equalTo(s.getRExpr())) {
            return true;
        } else if (le.equalTo(s.getRExpr()) && re.equalTo(s.getLExpr())) {  // TODO - Added this
            return true;
        } else {
            return false;
        }
    }

    public String getShareString() {
        return (le.getExpressionString() + " -> " + re.getExpressionString());
    }

    public void printShare() {
        System.out.print("      (");
        le.printExpression();
        System.out.print(" -> ");
        re.printExpression();
        System.out.println(")");
    }

    public Share clone() {
        Share result = new Share(le, re);
        return result;
    }

}

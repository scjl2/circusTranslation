package hijac.tools.scjmsafe.checker.environment;

import java.util.HashSet;

import hijac.tools.scjmsafe.language.RefCon.*;
import hijac.tools.scjmsafe.language.Expressions.*;

public class DecMapping {

    private Expr le;
    private RefCon refCon;

    public DecMapping(Expr lexpr, RefCon rc) {
        le = lexpr;
        refCon = rc;
    }

    public Expr getExpr() {
        return le;
    }

    public void setExpr(Expr e) {
        le = e;
    }

    public RefCon getRefCon() {
        return refCon;
    }

    public void setRefCon(RefCon rc) {
        refCon = rc;
    }

    public boolean containsExpr(Expr expr) {
        boolean result = false;
        if (le.getExpressionString().equals(expr.getExpressionString())) {
            result = true;
        }
        return result;
    }

    public void printRefCon() {
        System.out.print("{");
        refCon.printRefCon();
        System.out.print("}");
    }

    public void printDecMapping() {
        System.out.print("      (");
        le.printExpression();
        System.out.print(" -> ");
        printRefCon();
        System.out.println(")");
    }

    public DecMapping clone() {
        DecMapping result = new DecMapping(le, refCon);
        return result;
    }

}

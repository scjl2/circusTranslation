package hijac.tools.scjmsafe.language.Method;

import java.util.HashSet;

import hijac.tools.scjmsafe.language.Expressions.*;
import hijac.tools.scjmsafe.language.RefCon.*;

public class MethodDecMapping {

    private Expr le;
    private MetaRefCon metaRefCon;


    public MethodDecMapping(Expr lexpr, MetaRefCon mrc) {
        le = lexpr;
        metaRefCon = mrc;
    }

    public void setExpr(Expr e) {
        le = e;
    }

    public Expr getExpr() {
        return le;
    }

    public void setMetaRefCon(MetaRefCon mrc) {
        metaRefCon = mrc;
    }

    public MetaRefCon getMetaRefCon() {
        return metaRefCon;
    }

    public boolean containsExpr(Expr expr) {
        boolean result = false;
        if (le.getExpressionString().equals(expr.getExpressionString())) {
            result = true;
        }
        return result;
    }

    public void printMetaRefCon() {
        System.out.print("{");
        metaRefCon.printMetaRefCon();
        System.out.print("}");
    }

    public void printMethodDecMapping() {
        System.out.print("      (");
        le.printExpression();
        System.out.print(" -> ");
        printMetaRefCon();
        System.out.println(")");
    }

    public MethodDecMapping clone() {
        MethodDecMapping result = new MethodDecMapping(le, metaRefCon);
        return result;
    }

}

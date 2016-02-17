package hijac.tools.scjmsafe.language.RefCon;

import java.util.ArrayList;

import hijac.tools.scjmsafe.language.Expressions.*;

public class Erc extends MetaRefCon {

    private Expr le;

    public Erc(Expr lexpr) {
        le = lexpr;
    }

    public Expr getExpr() {
        return le;
    }

    public void setExpr(Expr e) {
        le = e;
    }

    public void printMetaRefCon() {
        System.out.print("Erc ");
        le.printExpression();
    }

    public String getMetaRefConString() {
        return "Erc " + le.getExpressionString();
    }


    public boolean equalTo(MetaRefCon arg) {
        if (arg instanceof Erc) {
            Erc temp = (Erc) arg;
            if (le.equalTo(temp.getExpr())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public Erc clone() {
        return new Erc(le);
    }

}

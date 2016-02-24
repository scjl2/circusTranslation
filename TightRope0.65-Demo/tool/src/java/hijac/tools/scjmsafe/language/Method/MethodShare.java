package hijac.tools.scjmsafe.language.Method;

import hijac.tools.scjmsafe.language.Expressions.*;

public class MethodShare {

    private Expr le;
    private Expr re;

    public void MethodShare(Expr lexpr, Expr rexpr) {
        le = lexpr;
        re = rexpr;
    }

    public Expr getLExpr() {
        return le;
    }
    
    public Expr getRExpr() {
        return re;
    }

}

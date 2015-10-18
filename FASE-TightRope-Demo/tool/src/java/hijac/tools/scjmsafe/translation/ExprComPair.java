package hijac.tools.scjmsafe.translation;

import hijac.tools.scjmsafe.language.*;
import hijac.tools.scjmsafe.language.Commands.*;
import hijac.tools.scjmsafe.language.Expressions.*;

public class ExprComPair {

    private Expr e;
    private Command c;

    public ExprComPair() {
        e = null;
        c = null;
    }

    public void addExpr(Expr expr) {
        e = expr;
    }

    public Expr getExpr() {
        return e;
    }

    public void addCom(Command com) {
        c = com;
    }

    public Command getCom() {
        return c;
    }
    
}

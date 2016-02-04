package hijac.tools.scjmsafe.language.Commands;

import hijac.tools.scjmsafe.language.Expressions.*;
import hijac.tools.scjmsafe.translation.PrintUtil;

public class Assignment extends Command {

    private Expr le;
    private Expr re;

    public Assignment(Expr lexpr, Expr rexpr) {
        le = lexpr;
        re = rexpr;
    }

    public Expr getLExpr() {
        return le;
    }

    public Expr getRExpr() {
        return re;
    }

    public void printCommand(int indent) {
        PrintUtil.printIndent(indent);
        le.printExpression();
        System.out.print(" = ");
        re.printExpression();
        System.out.print(";");
    }

}

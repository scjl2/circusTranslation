package hijac.tools.scjmsafe.language.Commands;

import hijac.tools.scjmsafe.language.Expressions.*;
import hijac.tools.scjmsafe.translation.PrintUtil;

public class While extends Command {

    private Expr e;
    private Command c1;

    public While(Expr expr, Command com1) {
        e = expr;
        c1 = com1;
    }

    public Command getC1() {
        return c1;
    }

    public Expr getExpr() {
        return e;
    }

    public void printCommand(int indent) {
        PrintUtil.printIndent(indent);
        System.out.print("while (");
        e.printExpression();
        System.out.print(") {\n");
        c1.printCommand(indent+2);
        System.out.println();
        PrintUtil.printIndent(indent);
        System.out.print("}");
    }

}

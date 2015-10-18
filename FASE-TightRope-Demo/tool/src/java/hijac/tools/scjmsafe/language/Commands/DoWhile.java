package hijac.tools.scjmsafe.language.Commands;

import hijac.tools.scjmsafe.language.Expressions.*;
import hijac.tools.scjmsafe.translation.PrintUtil;

public class DoWhile extends Command {

    private Command c1;
    private Expr e;

    public DoWhile(Command com1, Expr expr) {
        c1 = com1;
        e = expr;
    }

    public Command getC1() {
        return c1;
    }

    public Expr getExpr() {
        return e;
    }

    public void printCommand(int indent) {
        PrintUtil.printIndent(indent);
        System.out.print("do {\n");
        c1.printCommand(indent+2);
        System.out.println();
        PrintUtil.printIndent(indent);
        System.out.print("} while (");
        e.printExpression();
        System.out.print(");");
    }

}

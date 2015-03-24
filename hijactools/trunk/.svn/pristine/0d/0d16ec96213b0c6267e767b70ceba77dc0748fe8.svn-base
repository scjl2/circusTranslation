package hijac.tools.scjmsafe.language.Commands;

import hijac.tools.scjmsafe.language.Expressions.*;
import hijac.tools.scjmsafe.translation.PrintUtil;

public class If extends Command {

    private Expr e;
    private Command c1;
    private Command c2;

    public If(Expr expr, Command com1, Command com2) {
        e = expr;
        c1 = com1;
        c2 = com2;
    }

    public Expr getExpr() {
        return e;
    }

    public Command getC1() {
        return c1;
    }

    public Command getC2() {
        return c2;
    }

    public void printCommand(int indent) {
        PrintUtil.printIndent(indent);
        System.out.print("if (");
        e.printExpression();
        System.out.print(") {\n");
        c1.printCommand(indent+2);
        System.out.println();
        PrintUtil.printIndent(indent);
        System.out.print("} else {\n");
        c2.printCommand(indent+2);
        System.out.println();
        PrintUtil.printIndent(indent);
        System.out.print("}");        
    }

}

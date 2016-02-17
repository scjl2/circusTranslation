package hijac.tools.scjmsafe.language.Commands;

import hijac.tools.scjmsafe.language.Expressions.*;
import hijac.tools.scjmsafe.translation.PrintUtil;

public class For extends Command {

    private Command c1;
    private Expr e;
    private Command c2;
    private Command c3;

    public For(Command com1, Expr expr, Command com2, Command com3) {
        c1 = com1;
        e = expr;
        c2 = com2;
        c3 = com3;
    }

    public Command getC1() {
        return c1;
    }

    public Expr getExpr() {
        return e;
    }

    public Command getC2() {
        return c2;
    }

    public Command getC3() {
        return c3;
    }

    public void printCommand(int indent) {
        PrintUtil.printIndent(indent);
        System.out.print("for (");
        System.out.print("(");
        if (c1 instanceof Sequence) {
            Sequence c1seq = (Sequence) c1;
            c1seq.printNoBreak();
        } else {
            c1.printCommand(0);
        }
        System.out.print("), ");
        e.printExpression();
        System.out.print(", ");
        System.out.print("(");
        if (c2 instanceof Sequence) {
            Sequence c2seq = (Sequence) c2;
            c2seq.printNoBreak();
        } else {
            c2.printCommand(0);
        }
        System.out.print(")");
        System.out.print(") {\n");
        c3.printCommand(indent+2);
        System.out.println();
        PrintUtil.printIndent(indent);
        System.out.print("}");
    }

}


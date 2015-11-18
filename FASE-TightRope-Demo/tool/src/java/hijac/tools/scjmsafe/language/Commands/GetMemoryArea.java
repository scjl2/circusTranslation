package hijac.tools.scjmsafe.language.Commands;

import hijac.tools.scjmsafe.language.Expressions.*;

import hijac.tools.scjmsafe.translation.PrintUtil;

public class GetMemoryArea extends Command {

    private Expr ref;
    private Expr expr;

    public GetMemoryArea(Expr r, Expr e) {
        ref = r;
        expr = e;
    }

    public Expr getRef() {
        return ref;
    }

    public void setRef(Expr e) {
        ref = e;
    }

    public Expr getExpr() {
        return expr;
    }

    public void printCommand(int indent) {
        PrintUtil.printIndent(indent);
        System.out.print("GetMemoryArea(");
        ref.printExpression();
        System.out.print(", ");
        expr.printExpression();
        System.out.print(");");
    }

}

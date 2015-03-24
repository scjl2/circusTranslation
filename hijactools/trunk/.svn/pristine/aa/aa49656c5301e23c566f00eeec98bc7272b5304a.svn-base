package hijac.tools.scjmsafe.language.Commands;

import hijac.tools.scjmsafe.language.Expressions.*;
import hijac.tools.scjmsafe.translation.PrintUtil;

import java.util.ArrayList;

public class Switch extends Command {

    private Expr e;
    private ArrayList<Command> seqCom;

    public Switch(Expr expr, ArrayList<Command> seq) {
        e = expr;
        seqCom = seq;
    }

    public Expr getExpr() {
        return e;
    }

    public ArrayList<Command> getCommands() {
        return seqCom;
    }

    public void printCommand(int indent) {
        PrintUtil.printIndent(indent);
        System.out.print("switch (");
        e.printExpression();
        System.out.print(") {\n");
        for (int i = 0; i < seqCom.size(); i++) {
            PrintUtil.printIndent(indent+2);
            System.out.print("case Val : {\n"); // TODO - Check
            seqCom.get(i).printCommand(indent+4);
            System.out.println();
            PrintUtil.printIndent(indent+2);
            System.out.print("}\n");
        }
        PrintUtil.printIndent(indent);
        System.out.print("}");
    }

}

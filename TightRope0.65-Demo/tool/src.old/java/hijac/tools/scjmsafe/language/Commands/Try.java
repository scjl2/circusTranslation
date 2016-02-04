package hijac.tools.scjmsafe.language.Commands;

import hijac.tools.scjmsafe.language.Expressions.*;
import hijac.tools.scjmsafe.translation.PrintUtil;

import java.util.ArrayList;

public class Try extends Command {

    private Command tryCom;
    private ArrayList<Command> catchExprs;
    private ArrayList<Command> catchComs;
    private Command finallyCom;

    public Try(Command com1, ArrayList<Command> exprs, ArrayList<Command> coms, Command com2) {
        tryCom = com1;
        catchExprs = exprs;
        catchComs = coms;
        finallyCom = com2;
    }

    public Command getTryCommand() {
        return tryCom;
    }

    public ArrayList<Command> getCatchExprs() {
        return catchExprs;
    }

    public ArrayList<Command> getCatchComs() {
        return catchComs;
    }

    public Command getFinallyCommand() {
        return finallyCom;
    }

    public void printCommand(int indent) {
        PrintUtil.printIndent(indent);
        System.out.print("try {\n");
        tryCom.printCommand(indent+2);
        System.out.println();
        PrintUtil.printIndent(indent);
        System.out.println("}");
        for (int i = 0; i < catchExprs.size(); i++) {
            PrintUtil.printIndent(indent);
            System.out.print("catch (");
            Command temp = catchExprs.get(i);
            if (temp instanceof Declaration) {
                ((Declaration) temp).printSingle(0);
            } else if (temp instanceof Sequence) {
                ((Sequence) temp).printNoBreak();
            } else {
                temp.printCommand(0);
            }
            System.out.print(") {\n");
            catchComs.get(i).printCommand(indent+2);
            System.out.print("\n");
            PrintUtil.printIndent(indent);
            System.out.print("}\n");
        }
        System.out.println();
        PrintUtil.printIndent(indent);
        System.out.print("finally {\n");
        finallyCom.printCommand(indent+2);
        System.out.println();
        PrintUtil.printIndent(indent);
        System.out.print("}");
    }

}

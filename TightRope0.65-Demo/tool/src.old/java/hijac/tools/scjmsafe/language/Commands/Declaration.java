package hijac.tools.scjmsafe.language.Commands;

import hijac.tools.scjmsafe.language.Expressions.*;
import hijac.tools.scjmsafe.translation.PrintUtil;

public class Declaration extends Command {

    private Variable var;

    public Declaration(Variable var) {
        this.var = var;
    }

    public void setVar(Variable var) {
        this.var = var;
    }

    public Variable getVar() {
        return var;
    }

    public void printCommand(int indent) {
        PrintUtil.printIndent(indent);
        System.out.print(var.getVarType().getTypeName() + " " + var.getName() + ";");
    }

    public void printSingle(int indent) {
        PrintUtil.printIndent(indent);
        System.out.print(var.getVarType().getTypeName() + " " + var.getName());
    }

}

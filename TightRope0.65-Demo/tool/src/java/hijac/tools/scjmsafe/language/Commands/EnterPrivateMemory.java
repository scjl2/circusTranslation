package hijac.tools.scjmsafe.language.Commands;

import hijac.tools.scjmsafe.language.Expressions.*;

import hijac.tools.scjmsafe.translation.PrintUtil;

public class EnterPrivateMemory extends Command {

    private MethodCall runMethod;

    public EnterPrivateMemory(MethodCall mc) {
        runMethod = mc;
    }

    public MethodCall getMethodCall() {
        return runMethod;
    }

    public void printCommand(int indent) {
        PrintUtil.printIndent(indent);
        System.out.print("EnterPrivateMemory(");
        runMethod.getExpr().printExpression();
        System.out.print(");");
    }

}

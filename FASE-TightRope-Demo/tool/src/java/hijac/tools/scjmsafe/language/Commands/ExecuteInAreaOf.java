package hijac.tools.scjmsafe.language.Commands;

import hijac.tools.scjmsafe.language.Expressions.*;

import hijac.tools.scjmsafe.language.RefCon.*;
import hijac.tools.scjmsafe.translation.PrintUtil;

public class ExecuteInAreaOf extends Command {

    private MetaRefCon mrc;
    private MethodCall runMethod;

    public ExecuteInAreaOf(MetaRefCon metaRefCon, MethodCall mc) {
        mrc = metaRefCon;
        runMethod = mc;
    }

    public MetaRefCon getMetaRefCon() {
        return mrc;
    }

    public MethodCall getMethodCall() {
        return runMethod;
    }

    public void printCommand(int indent) {
        PrintUtil.printIndent(indent);
        System.out.print("ExecuteInAreaOf(");
        mrc.printMetaRefCon();
        System.out.print(", ");
        runMethod.getExpr().printExpression();
        System.out.print(");");
    }

}

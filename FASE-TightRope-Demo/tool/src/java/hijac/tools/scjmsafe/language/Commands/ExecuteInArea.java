package hijac.tools.scjmsafe.language.Commands;

import hijac.tools.scjmsafe.language.RefCon.*;
import hijac.tools.scjmsafe.translation.PrintUtil;

public class ExecuteInArea extends Command {

    private RefCon rc;
    private Command c1;

    public void ExecuteInArea(RefCon refcon, Command com) {
        rc = refcon;
        c1 = com;
    }

    public void printCommand(int indent) {
        PrintUtil.printIndent(indent);
        System.out.print("ExecuteInArea(RefCon) {\n");  // TODO 
        c1.printCommand(indent+2);
        System.out.println();
        PrintUtil.printIndent(indent);
        System.out.print("}");
    }

}

package hijac.tools.scjmsafe.language.Commands;

import hijac.tools.scjmsafe.translation.PrintUtil;

public class RequestTermination extends Command {

    public RequestTermination() {}

    public void printCommand(int indent) {
        PrintUtil.printIndent(indent);
        System.out.print("RequestTermination();");
    }

}

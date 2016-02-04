package hijac.tools.scjmsafe.language.Commands;

import hijac.tools.scjmsafe.translation.PrintUtil;

public class Skip extends Command {

    public Skip() {};

    public void printCommand(int indent) {
        PrintUtil.printIndent(indent);
        System.out.print("Skip;");
    }

}

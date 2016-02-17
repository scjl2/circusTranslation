package hijac.tools.scjmsafe.language.Commands;

import hijac.tools.scjmsafe.translation.PrintUtil;

public class Scope extends Command {

    private Command c1;

    public Scope(Command com1) {
        c1 = com1;
    }

    public void addCommand(Command c) {
        c1 = c;
    }

    public Command getCommand() {
        return c1;
    }

    public void printCommand(int indent) {
        PrintUtil.printIndent(indent);
        System.out.print("{\n");
        c1.printCommand(indent+2);
        System.out.println();
        PrintUtil.printIndent(indent);
        System.out.print("}");
    }


}

package hijac.tools.scjmsafe.language.Commands;

import hijac.tools.scjmsafe.translation.PrintUtil;

public class Sequence extends Command {

    private Command c1;
    private Command c2;

    public Sequence(Command com1, Command com2) {
        c1 = com1;
        c2 = com2;
    }

    public void addC1(Command com) {
        c1 = com;
    }

    public Command getC1() {
        return c1;
    }

    public void addC2(Command com) {
        c2 = com;
    }

    public Command getC2() {
        return c2;
    }

    public void printCommand(int indent) {
        c1.printCommand(indent);
        System.out.print("\n");
        c2.printCommand(indent);
    }

    public void printNoBreak() {
        if (c1 instanceof Sequence) {
            Sequence c1seq = (Sequence) c1;
            c1seq.printNoBreak();
        } else {
            c1.printCommand(0);
        }
        System.out.print(" ");
        if (c2 instanceof Sequence) {
            Sequence c2seq = (Sequence) c2;
            c2seq.printNoBreak();
        } else {
            c2.printCommand(0);
        }
    }

}

package hijac.tools.scjmsafe.checker.analysis;

import java.util.ArrayList;

public class MethodDep {

    private MethodDepEntry first;
    private MethodDepEntry second;

    public MethodDep(MethodDepEntry first, MethodDepEntry second) {
        this.first = first;
        this.second = second;
    }

    public MethodDepEntry getFirst() {
        return first;
    }

    public MethodDepEntry getSecond() {
        return second;
    }

    public void print() {
        first.print();
        System.out.print(" -> ");
        second.print();
        System.out.println();
    }
}

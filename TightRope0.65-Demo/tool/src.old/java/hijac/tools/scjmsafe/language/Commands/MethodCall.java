package hijac.tools.scjmsafe.language.Commands;

import hijac.tools.scjmsafe.language.Expressions.*;
import hijac.tools.scjmsafe.translation.PrintUtil;
import hijac.tools.scjmsafe.language.Method.MethodSig;

import java.util.ArrayList;

public class MethodCall extends Command {

    private Expr le;
    private String mname;
    private ArrayList<Expr> args;
    private ArrayList<MethodSig> possibleMethods;

    public MethodCall(Expr lexpr, String name, ArrayList<Expr> args) {
        le = lexpr;
        mname = name;
        this.args = args;
    }

    public Expr getExpr() {
        return le;
    }

    public void addName(String name) {
        mname = name;
    }

    public String getName() {
        return mname;
    }

    public void addArg(Expr e) {
        args.add(e);
    }

    public ArrayList<Expr> getArgs() {
        return args;
    }

    public void addPossibleMethods(ArrayList<MethodSig> methods) {
        possibleMethods = methods;
    }

    public ArrayList<MethodSig> getPossibleMethods() {
        return possibleMethods;
    }

    public void printCommand(int indent) {
        PrintUtil.printIndent(indent);
        if (le != null) {
            le.printExpression();
            System.out.print(".");
        }
        System.out.print(mname + "(");
//        System.out.print(mname + "(");
        for (int i = 0; i < args.size(); i++) {
            args.get(i).printExpression();
            if (i+1 < args.size()) {
                System.out.print(", ");
            }
        }
        System.out.print(");");
    }

}

package hijac.tools.scjmsafe.language.Commands;

import java.util.ArrayList;

import hijac.tools.scjmsafe.language.Expressions.*;
import hijac.tools.scjmsafe.translation.PrintUtil;
import hijac.tools.scjmsafe.language.RefCon.*;

public class NewInstance extends Command {

    private Expr le;
    private MetaRefCon mrc;
    private VarType varType;
    private ArrayList<Expr> params;     // Parameters for class initialisations OR Initialisation values for Arrays

    public NewInstance(Expr lexpr, MetaRefCon metarc, VarType varType, ArrayList<Expr> params) {
        le = lexpr;
        mrc = metarc;
        this.varType = varType;
        this.params = params;
    }

    public Expr getExpr() {
        return le;
    }

    public void setExpr(Expr e) {
        le = e;
    }

    public VarType getVarType() {
        return varType;
    }

    public MetaRefCon getMetaRefCon() {
        return mrc;
    }

    public ArrayList<Expr> getParams() {
        return params;
    }

    public void printCommand(int indent) {
        PrintUtil.printIndent(indent);
        System.out.print("NewInstance(");
        le.printExpression();
        System.out.print(", ");
        mrc.printMetaRefCon();
        System.out.print(", ");
        System.out.print(varType.getTypeName());
        System.out.print(", (");
        for (int i = 0; i < params.size(); i++) {
            params.get(i).printExpression();
            if (i+1 < params.size()) {
                System.out.print(", ");
            }
        }
        System.out.print("));");
    }


}

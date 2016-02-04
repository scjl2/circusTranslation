package hijac.tools.scjmsafe.language.Method;

import java.util.HashSet;

import hijac.tools.scjmsafe.language.Expressions.*;
import hijac.tools.scjmsafe.language.RefCon.*;

import hijac.tools.scjmsafe.checker.CheckingUtil;

public class MethodRefMapping {

    private Expr le;
    private HashSet<MetaRefCon> metaRefCons;
    private boolean check;

    public MethodRefMapping() {
        metaRefCons = new HashSet<MetaRefCon>(0);
        check = true;
    }

    public MethodRefMapping(Expr lexpr, HashSet<MetaRefCon> mrcs) {
        le = lexpr;
        metaRefCons = mrcs;
        check = true;
    }

    public void setExpr(Expr e) {
        le = e;
        check = true;
    }

    public Expr getExpr() {
        return le;
    }

    public void setMetaRefCons(HashSet<MetaRefCon> mrcs) {
        metaRefCons = mrcs;
        check = true;
    }

    public void addMetaRefCon(MetaRefCon mrc) {
//        System.out.println("Adding MRC : " + mrc.getMetaRefConString());
        boolean found = false;
        for (MetaRefCon mrc1 : metaRefCons) {
//            System.out.println("  Comparing against MRC : " + mrc1.getMetaRefConString());
            if (mrc1.getMetaRefConString().equals(mrc.getMetaRefConString())) {
                found = true;
                break;
            }
        }
        if (!found) {
//            System.out.println("ADDED IT!!!");
            metaRefCons.add(mrc);
            check = true;
        }
    }

    public void addMetaRefCons(HashSet<MetaRefCon> mrcs) {
        for (MetaRefCon mrc : mrcs) {
            addMetaRefCon(mrc);
        }
    }

    public void removeMetaRefCon(MetaRefCon mrc) {
        metaRefCons.remove(mrc);
        check = true;
    }

    public HashSet<MetaRefCon> getMetaRefCons() {
        return metaRefCons;
    }

    public boolean check() {
        return check;
    }

    public void checked() {
        check = false;
    }

    public boolean containsExpr(Expr expr) {
        boolean result = false;
        if (le.equalTo(expr)) {
            result = true;
        }
        return result;
    }

    public boolean equalTo(MethodRefMapping mapping) {
        if (le.equalTo(mapping.getExpr())) {
            return CheckingUtil.metaRefConSetsEqual(metaRefCons, mapping.getMetaRefCons());
        } else {
            return false;
        }
    }

    public void printMetaRefCons() {
        System.out.print("{");
        int count = 0;
        for (MetaRefCon mrc : metaRefCons) {
            mrc.printMetaRefCon();
            if (count+1 < metaRefCons.size()) {
                System.out.print(", ");
            }
            count++;
        }
        System.out.print("}");
    }

    public void printMethodRefMapping() {
        System.out.print("      (");
        le.printExpression();
        System.out.print(" -> ");
        printMetaRefCons();
        System.out.println(")");
    }

    public MethodRefMapping clone() {
        MethodRefMapping result = new MethodRefMapping();
        result.setExpr(le);
        for (MetaRefCon mrc : metaRefCons) {
            result.addMetaRefCon(mrc.clone());
        }
        return result;
    }

}

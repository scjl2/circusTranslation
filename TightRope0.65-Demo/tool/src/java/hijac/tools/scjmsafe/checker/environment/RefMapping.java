package hijac.tools.scjmsafe.checker.environment;

import java.util.HashSet;

import hijac.tools.scjmsafe.language.RefCon.*;
import hijac.tools.scjmsafe.language.Expressions.*;

import hijac.tools.scjmsafe.checker.CheckingUtil;

public class RefMapping {

    private Expr le;
    private HashSet<RefCon> refCons;
    private boolean check;

    public RefMapping(Expr lexpr, HashSet<RefCon> rcs) {
        le = lexpr;
        refCons = rcs;
        check = true;
    }

    public Expr getExpr() {
        return le;
    }

    public void setExpr(Expr e) {
        le = e;
        check = true;
    }

    public HashSet<RefCon> getRefCons() {
        return refCons;
    }

    public void setRefCons(HashSet<RefCon> rcs) {
        refCons = rcs;
        check = true;
    }

    public void addRefCon(RefCon rc) {
        boolean found = false;
        for (RefCon local : refCons) {
            if (rc.getRefConString().equals(local.getRefConString())) {
                found = true;
            }
        }
        if (!found) {
            refCons.add(rc);
            check = true;
        }
    }

    public void addRefCons(HashSet<RefCon> rcs) {
        for (RefCon rc : rcs) {
            addRefCon(rc);
        }
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

    public boolean equalTo(RefMapping mapping) {
        if (le.equalTo(mapping.getExpr())) {
            return CheckingUtil.refConSetsEqual(refCons, mapping.getRefCons());
        } else {
            return false;
        }
    }

    public void printRefCons() {
        System.out.print("{");
        int count = 0;
        for (RefCon rc : refCons) {
            rc.printRefCon();
            if (count+1 < refCons.size()) {
                System.out.print(", ");
            }
            count++;
        }
        System.out.print("}");
    }

    public void printRefMapping() {
        System.out.print("      (");
        le.printExpression();
        System.out.print(" -> ");
        printRefCons();
        System.out.println(")");
    }

    public RefMapping clone() {
        HashSet<RefCon> refConsClone = new HashSet<RefCon>(0);
        for (RefCon rc : refCons) {
            refConsClone.add(rc.clone());
        }
        RefMapping result = new RefMapping(le, refConsClone);
        return result;
    }

}

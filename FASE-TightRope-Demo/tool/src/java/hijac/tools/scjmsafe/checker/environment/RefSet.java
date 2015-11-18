package hijac.tools.scjmsafe.checker.environment;

import java.util.HashSet;
import java.util.Iterator;
import java.util.ArrayList;

import hijac.tools.scjmsafe.language.RefCon.*;
import hijac.tools.scjmsafe.language.Expressions.*;

public class RefSet {

    private HashSet<RefMapping> refSet;

    public RefSet() {
        refSet = new HashSet<RefMapping>(0);
    }

    public RefSet(HashSet<RefMapping> refs) {
        refSet = refs;
    }

    public void updateRefMapping(Expr e, HashSet<RefCon> rcs) {
        boolean found = false;
        for (RefMapping mapping : refSet) {
            if (mapping.getExpr().equalTo(e)) {
                mapping.addRefCons(rcs);
                found = true;
                break;
            }
        }
        if (!found) {
            refSet.add(new RefMapping(e, rcs));
        }
    }

    public void updateRefMapping(RefMapping ref) {
        updateRefMapping(ref.getExpr(), ref.getRefCons());
    }

    public void updateRefMappings(HashSet<RefMapping> refs) {
        for (RefMapping mapping : refs) {
            updateRefMapping(mapping.getExpr(), mapping.getRefCons());
        }
    }

    public HashSet<RefMapping> getRefSet() {
        return refSet;
    }

    public void removeRefMapping(RefMapping mapping) {
        refSet.remove(mapping);
    }

    public HashSet<Expr> getExprs() {
        HashSet<Expr> result = new HashSet<Expr>(0);
        for (RefMapping rm : refSet) {
            result.add(rm.getExpr());
        }
        return result;
    }

    public RefMapping getRefMapping(Expr e) {
        RefMapping result = null;
        for (RefMapping rm : refSet) {
            if (rm.getExpr().equalTo(e)) {
                result = rm;
                break;
            }
        }
        return result;
    }

    public void removeExprFromRefSet(Expr expr) {
        HashSet<RefMapping> temp = new HashSet<RefMapping>(0);
        for (RefMapping rm : refSet) {
            if (EnvUtil.exprPrefixOfFieldAccess(expr, rm.getExpr())) {
                temp.add(rm);
            }
        }
        refSet.removeAll(temp);
/*
        Iterator<RefMapping> refSetIt = refSet.iterator();
        while (refSetIt.hasNext()) {
            RefMapping rm = refSetIt.next();
            if (EnvUtil.exprPrefixOfFieldAccess(expr, rm.getExpr())) {
                refSetIt.remove();
            }
        }
*/
    }

    public HashSet<Expr> matchingExprsInRefSet(Expr e) {
        HashSet<Expr> result = new HashSet<Expr>(0);
        for (RefMapping rm : refSet) {
            Expr refSetExpr = rm.getExpr();
            if (e.equalTo(refSetExpr)) {
                result.add(refSetExpr);
            } else if (EnvUtil.exprPrefixOfFieldAccess(e, refSetExpr)) {
                result.add(refSetExpr);
            }
        }
        return result;
    }


    public void printRefSet() {
        System.out.println("    {");
        for (RefMapping rm : refSet) {
            rm.printRefMapping();
        }
        System.out.println("    }");
    }

    public RefSet clone() {
        RefSet result = new RefSet();
        for (RefMapping mapping : refSet) {
            result.updateRefMapping(mapping.clone());
        }
        return result;
    }

    public boolean equalTo(RefSet rs) {
        boolean result = true;
        if (refSet.size() == rs.getRefSet().size()) {
            for (RefMapping m1 : refSet) {
                boolean found = false;
                for (RefMapping m2 : rs.getRefSet()) {
                    if (m1.equalTo(m2)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    result = false;
                    break;
                }
            }
        } else {
            result = false;
        }
        return result;
    }
}

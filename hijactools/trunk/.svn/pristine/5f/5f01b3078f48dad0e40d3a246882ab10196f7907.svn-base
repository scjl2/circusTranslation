package hijac.tools.scjmsafe.language.Method;

import java.util.HashSet;
import java.util.Iterator;
import java.util.ArrayList;

import hijac.tools.scjmsafe.language.Expressions.*;
import hijac.tools.scjmsafe.language.RefCon.*;
import hijac.tools.scjmsafe.checker.environment.EnvUtil;

public class MethodRefChange {

    private HashSet<MethodRefMapping> methodRefs;

    public MethodRefChange() {
        methodRefs = new HashSet<MethodRefMapping>(0);
    }

    public MethodRefChange(HashSet<MethodRefMapping> refs) {
        methodRefs = refs;
    }

    public void removeRefMapping(MethodRefMapping mrp) {
        methodRefs.remove(mrp);
    }

    public void updateRefMapping(Expr e, HashSet<MetaRefCon> mrcs) {
//        System.out.println("Updating RefMapping : " + e.getExpressionString() + " with " + mrcs.size() + " metaRefCons");
        boolean found = false;
        for (MethodRefMapping mapping : methodRefs) {
            if (mapping.getExpr().equalTo(e)) {
                mapping.addMetaRefCons(mrcs);
                found = true;
                break;
            }
        }
        if (!found) {
            methodRefs.add(new MethodRefMapping(e, mrcs));
        }
    }

    public void updateRefMapping(MethodRefMapping ref) {
        updateRefMapping(ref.getExpr(), ref.getMetaRefCons());
    }

    public void updateRefMappings(HashSet<MethodRefMapping> refs) {
        for (MethodRefMapping mapping : refs) {
            updateRefMapping(mapping.getExpr(), mapping.getMetaRefCons());
        }
    }


    public HashSet<MethodRefMapping> getMethodRefs() {
        return methodRefs;
    }

    public HashSet<Expr> getExprs() {
        HashSet<Expr> result = new HashSet<Expr>(0);
        for (MethodRefMapping mapping : methodRefs) {
            result.add(mapping.getExpr());
        }
        return result;
    }

    public MethodRefMapping getMethodRefMapping(Expr e) {
        MethodRefMapping result = null;
        for (MethodRefMapping rm : methodRefs) {
            if (rm.getExpr().equalTo(e)) {
                result = rm;
                break;
            }
        }
        return result;
    }

    public void removeExprFromRefSet(Expr expr) {
//        System.out.println("Removing expression " + expr.getExpressionString() + " from properties (ref set)");
        Iterator<MethodRefMapping> refSetIt = methodRefs.iterator();
        while (refSetIt.hasNext()) {
            MethodRefMapping rm = refSetIt.next();
            if (EnvUtil.exprPrefixOfFieldAccess(expr, rm.getExpr())) {
                refSetIt.remove();
            }
        }
    }

    public HashSet<Expr> matchingExprsInRefSet(Expr e) {
        HashSet<Expr> result = new HashSet<Expr>(0);
        for (MethodRefMapping rm : methodRefs) {
            Expr refSetExpr = rm.getExpr();
            if (e.equalTo(refSetExpr)) {
                result.add(refSetExpr);
            } else if (EnvUtil.exprPrefixOfFieldAccess(e, refSetExpr)) {
                result.add(refSetExpr);
            }
        }
        return result;
    }

    public void printMethodRefSet() {
        System.out.println("    {");
        for (MethodRefMapping rm : methodRefs) {
            rm.printMethodRefMapping();
        }
        System.out.println("    }");
    }

    public MethodRefChange clone() {
        MethodRefChange result = new MethodRefChange();
        for (MethodRefMapping mapping : methodRefs) {
            result.updateRefMapping(mapping.clone());
        }
        return result;
    }

    public boolean isEmpty() {
        if (methodRefs.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean equalTo(MethodRefChange mrc) {
        boolean result = true;
        if (methodRefs.size() == mrc.getMethodRefs().size()) {
            for (MethodRefMapping m1 : methodRefs) {
                boolean found = false;
                for (MethodRefMapping m2 : mrc.getMethodRefs()) {
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

package hijac.tools.scjmsafe.checker;

import hijac.tools.scjmsafe.language.Commands.*;
import hijac.tools.scjmsafe.language.Expressions.*;
import hijac.tools.scjmsafe.language.RefCon.*;
import hijac.tools.scjmsafe.language.Method.*;
import hijac.tools.scjmsafe.language.MSafeHandler;

import hijac.tools.scjmsafe.checker.environment.*;
import hijac.tools.scjmsafe.checker.analysis.MethodPropertiesBuilder;

import java.util.ArrayList;
import java.util.HashSet;

public class CheckingUtil {

    public static void checkMemorySafety(Environment env) {
//        for (EnvEntry entry : env.getEnv()) {
            removePrimTypes(env);
            removeEmptyRefs(env);
            RefSet refSet = env.getRefSet();
            for (RefMapping mapping1 : refSet.getRefSet()) {
                if (mapping1.check() && !EnvUtil.getFirstOfExpr(mapping1.getExpr()).getExpressionString().equals("Result")) {
                    Expr e1 = mapping1.getExpr();
                    HashSet<RefCon> e1Rcs = mapping1.getRefCons();
                    if (e1Rcs.size() > 0) {
                        // Checking static vars...
                        if (SCJmSafeChecker.STATICVARS.contains(e1.getExpressionString())) {
                            IMem iMem = new IMem();
                            for (RefCon rc : e1Rcs) {
                                if (!dominates(rc, iMem)) {
                                    System.out.println("Checking to see if static expr " + e1.getExpressionString() + " is safe in current env");
                                    System.out.print("  POSSIBLE MEMORY SAFETY VIOLATION - The static expr '" + e1.getExpressionString() + "' may reference an object stored in '");
                                    rc.printRefCon();
                                    System.out.print("' when it should reside in '");
                                    iMem.printRefCon();
                                    System.out.println("'");
                                    System.out.println("\nEnvironment that may be unsafe:");
                                    EnvUtil.printEnv(env);
                                }
                            }
                            mapping1.checked();
                        } else {
                            boolean checked = false;
                            for (RefMapping mapping2 : refSet.getRefSet()) {
                                if (!checked) {
                                    Expr e2 = mapping2.getExpr();
                //System.out.print("Checking to see if \n");
                //mapping1.printRefMapping();
                //System.out.print(" -> ");
                //mapping2.printRefMapping();
                //System.out.print(" is safe in current env\n");
                                    if (EnvUtil.exprPrefixOfFieldAccess(e2, e1) && !(e1.equalTo(e2)) && (e2.getLength() == e1.getLength() - 1)) {
                                        boolean check = true;
                                        if (SCJmSafeChecker.MSEQEXPR != null) {
                                            if (e2.equalTo(SCJmSafeChecker.MSEQEXPR)) {
                                                for (Expr e3 : SCJmSafeChecker.MISSIONEXPRS) {
                                                    if (EnvUtil.exprPrefixOfFieldAccess(e3, e1)) {
                                                        check = false;
                                                        break;
                                                    }
                                                }
                                            } else {
                                                for (Expr e3 : SCJmSafeChecker.HANDLEREXPRS) {
                                                    boolean comparingAgainstHandler = false;
                                                    if (e2.equalTo(e3)) {
                                                        comparingAgainstHandler = true;
                                                    }
                                                    if (comparingAgainstHandler) {
                                                        boolean isHandlerField = false;
                                                        for (Expr e4 : SCJmSafeChecker.HANDLERFIELDS) {
                                                            if (e1.equalTo(e4)) {
                                                                isHandlerField = true;
                                                            }
                                                        }
                                                        if (!isHandlerField) {
                                                            check = false;
                                                            FieldAccess fa2 = (FieldAccess) e3;
                                                            Variable handlerExpr = (Variable) fa2.getLast();
                                                            String handlerClassName = handlerExpr.getVarType().getTypeName();
                //                                                System.out.println("Need to check " + e1.getExpressionString() + " against PRMem of handler " + handlerClassName);
                                                            MSafeHandler tempHandler = new MSafeHandler(handlerClassName);
                                                            PRMem prMem = new PRMem(tempHandler);
                                                            if (mapping2 != null) {
                                                                for (RefCon rc1 : e1Rcs) {
                                                                    checked = true;
                                                                    if (!dominates(rc1, prMem)) {
                                                                        System.out.println("Checking to see if field " + e1.getExpressionString() + " of " + e2.getExpressionString() + " is safe in current env");
                                                                        System.out.print("  POSSIBLE MEMORY SAFETY VIOLATION - The field '" + e1.getExpressionString() + "' may reference an object stored in '");
                                                                        rc1.printRefCon();
                                                                        System.out.print("' when it should reside in '");
                                                                        prMem.printRefCon();
                                                                        System.out.println("'");
                                                                        System.out.println("' or higher");
                                                                        System.out.println("\nEnvironment that may be unsafe:");
                                                                        EnvUtil.printEnv(env);
                                                                    }
                                                                }
                                                                mapping1.checked();
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        if (check) {
                                            HashSet<RefCon> e2Rcs = mapping2.getRefCons();
                                            for (RefCon rc2 : e2Rcs) {
                                                for (RefCon rc1 : e1Rcs) {
                                                    checked = true;
                                                    if (!dominates(rc1, rc2)) {
                                                        System.out.println("Checking to see if field " + e1.getExpressionString() + " of " + e2.getExpressionString() + " is safe in current env ");
                                                        System.out.print("  POSSIBLE MEMORY SAFETY VIOLATION - The field '" + e1.getExpressionString() + "' may reference an object stored in '");
                                                        rc1.printRefCon();
                                                        System.out.print("' when its parent '" + e2.getExpressionString() + "' resides in '");
                                                        rc2.printRefCon();
                                                        System.out.println("'");
                                                        System.out.println("\nEnvironment  that may be unsafe:");
                                                        EnvUtil.printEnv(env);
                                                    }
                                                }
                                            }
                                            mapping1.checked();
                                            break;
                                        }
                                    }
                                }
                            }
                        }
//                        mapping1.checked();
                    }
                }
            }
//        }
    }




//    public static void checkMemorySafetyMethod(Environment env, Method m, RefCon currentRC, Expr currentExpr) {
    public static void checkMemorySafetyMethod(Environment env, ArrayList<Expr> vars, RefCon currentRC, Expr currentExpr) {
        System.out.println("checkMemorySafetyMethod");
//        ArrayList<Expr> exprsToRemove = m.getVarsToRemove();
        ArrayList<Expr> exprsToRemove = vars;
        ArrayList<Expr> exprsToRemoveCurrent = new ArrayList<Expr>(0);
        for (Expr e : exprsToRemove) {
            exprsToRemoveCurrent.add(EnvUtil.mergeExprs(currentExpr, e));
        }
        removePrimTypes(env);
        removeEmptyRefs(env);
//        for (EnvEntry entry : env.getEnv()) {
            System.out.println("  checkingEnvironment");
            RefSet refSet = env.getRefSet();
            for (RefMapping mapping1 : refSet.getRefSet()) {
                if (mapping1.check() && !EnvUtil.getFirstOfExpr(mapping1.getExpr()).getExpressionString().equals("Result")) {
                    Expr e1 = mapping1.getExpr();
                    HashSet<RefCon> e1Rcs = mapping1.getRefCons();
                    if (e1Rcs.size() > 0) {
/*
                        // Checking static vars...
                        if (SCJmSafeChecker.STATICVARS.contains(e1.getExpressionString())) {
                            IMem iMem = new IMem();
                            for (RefCon rc : e1Rcs) {
                                if (!dominates(rc, iMem)) {
                                    System.out.println("Checking to see if static expr " + e1.getExpressionString() + " is safe in current env");
                                    System.out.print("  POSSIBLE MEMORY SAFETY VIOLATION - The static expr '" + e1.getExpressionString() + "' may reference an object stored in '");
                                    rc.printRefCon();
                                    System.out.print("' when its should reside in '");
                                    iMem.printRefCon();
                                    System.out.println("'");
                                    System.out.println("\nEnvironment that may be unsafe:");
                                    EnvUtil.printEnv(env);
                                }
                            }
                            mapping1.checked();
                        } else {*/
                            boolean foundRemove = false;
                            for (Expr e : exprsToRemoveCurrent) {
                                if (e.equalTo(e1)) {
                                    foundRemove = true;
                                    break;
                                }
                            }
                            if (foundRemove) {
    //                                System.out.println("Checking to see if field " + e2.getExpressionString() + " (to be removed) of " + e1.getExpressionString() + " is safe in CurrenRC = " + currentRC.getRefConString());
                                for (RefCon rc1 : e1Rcs) {
                                    if (!dominates(rc1, currentRC)) {
                                        System.out.println("Checking to see if local variable " + e1.getExpressionString() + " (to be removed) is safe in current env");
                                        System.out.print("  POSSIBLE MEMORY SAFETY VIOLATION - The field '" + e1.getExpressionString() + "' may reference an object stored in '");
                                        rc1.printRefCon();
                                        System.out.print("' when it should reside in ");
                                        currentRC.printRefCon();
                                        System.out.println("' or higher");
                                        System.out.println("\nEnvironment that may be unsafe:");
                                        EnvUtil.printEnv(env);
                                    }
                                }
                                mapping1.checked();
    /*
                            } else {
                                boolean checked = false;
                                for (RefMapping mapping2 : refSet.getRefSet()) {
                                    if (!checked) {
                                        Expr e2 = mapping2.getExpr();
                                        if (EnvUtil.exprPrefixOfFieldAccess(e2, e1) && !(e1.equalTo(e2)) && (e2.getLength() == e1.getLength() - 1)) {
                                            boolean check = true;
                                            if (SCJmSafeChecker.MSEQEXPR != null) {
                                                if (e2.equalTo(SCJmSafeChecker.MSEQEXPR)) {
                                                    for (Expr e3 : SCJmSafeChecker.MISSIONEXPRS) {
                                                        if (EnvUtil.exprPrefixOfFieldAccess(e3, e1)) {
                                                            check = false;
                                                        }
                                                    }
                                                } else {
                                                    for (Expr e3 : SCJmSafeChecker.HANDLEREXPRS) {
                                                        boolean comparingAgainstHandler = false;
                                                        if (e2.equalTo(e3)) {
                                                            comparingAgainstHandler = true;
                                                        }
                                                        if (comparingAgainstHandler) {
                                                            boolean isHandlerField = false;
                                                            for (Expr e4 : SCJmSafeChecker.HANDLERFIELDS) {
                                                                if (e1.equalTo(e4)) {
                                                                    isHandlerField = true;
                                                                }
                                                            }
                                                            if (!isHandlerField) {
                                                                check = false;
                                                                FieldAccess fa2 = (FieldAccess) e3;
                                                                Variable handlerExpr = (Variable) fa2.getLast();
                                                                String handlerClassName = handlerExpr.getVarType().getTypeName();
    //                                                            System.out.println("Need to check " + e1.getExpressionString() + " against PRMem of handler " + handlerClassName);
                                                                MSafeHandler tempHandler = new MSafeHandler(handlerClassName);
                                                                PRMem prMem = new PRMem(tempHandler);
                                                                for (RefCon rc1 : e1Rcs) {
                                                                    checked = true;
                                                                    if (!dominates(rc1, prMem)) {
                                                                        System.out.println("Checking to see if local variable " + e1.getExpressionString() + " of " + e2.getExpressionString() + " is safe in current env");
                                                                        System.out.print("  POSSIBLE MEMORY SAFETY VIOLATION - The field '" + e1.getExpressionString() + "' may reference an object stored in '");
                                                                        rc1.printRefCon();
                                                                        System.out.print("' when it should reside in '");
                                                                        prMem.printRefCon();
                                                                        System.out.println("' or higher");
                                                                        System.out.println("\nEnvironment that may be unsafe:");
                                                                        EnvUtil.printEnv(env);
                                                                    }
                                                                }
                                                                mapping1.checked();
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            if (check) {
                                                HashSet<RefCon> e2Rcs = mapping2.getRefCons();
                                                for (RefCon rc2 : e2Rcs) {
                                                    for (RefCon rc1 : e1Rcs) {
                                                        checked = true;
                                                        if (!dominates(rc1, rc2)) {
                                                            System.out.println("Checking to see if field " + e1.getExpressionString() + " of " + e2.getExpressionString() + " is safe in current env");
                                                            System.out.print("  POSSIBLE MEMORY SAFETY VIOLATION - The field '" + e1.getExpressionString() + "' may reference an object stored in '");
                                                            rc1.printRefCon();
                                                            System.out.print("' when its parent '" + e2.getExpressionString() + "' resides in '");
                                                            rc2.printRefCon();
                                                            System.out.println("'");
                                                            System.out.println("\nEnvironment that may be unsafe:");
                                                            EnvUtil.printEnv(env);
                                                        }
                                                    }
                                                }
                                                mapping1.checked();
                                            } else {
                                                checked = true;
                                            }
                                        }
                                    }
                                }/*
                                if (!checked) {
                                    RefMapping mapping2 = null;
                                    int length = 0;
                                    for (RefMapping mapping3 : refSet.getRefSet()) {
                                        Expr e2 = mapping3.getExpr();
                                        if (EnvUtil.exprPrefixOfFieldAccess(e2, e1) && !(e1.equalTo(e2))) {
                                            if (mapping3.getRefCons().size() > 0 && e2.getLength() > length) { 
                                                mapping2 = mapping3;
                                                length = e2.getLength();
                                            }
                                        }
                                    }
                                    if (mapping2 != null) {
                                        Expr e2 = mapping2.getExpr();
                                        boolean check = true;
                                        if (SCJmSafeChecker.MSEQEXPR != null) {
                                            if (e2.equalTo(SCJmSafeChecker.MSEQEXPR)) {
                                                for (Expr e3 : SCJmSafeChecker.MISSIONEXPRS) {
                                                    if (EnvUtil.exprPrefixOfFieldAccess(e3, e1)) {
                                                        check = false;
                                                    }
                                                }
                                            } else {
                                                for (Expr e3 : SCJmSafeChecker.HANDLEREXPRS) {
                                                    boolean comparingAgainstHandler = false;
                                                    if (e2.equalTo(e3)) {
                                                        comparingAgainstHandler = true;
                                                    }
                                                    if (comparingAgainstHandler) {
                                                        boolean isHandlerField = false;
                                                        for (Expr e4 : SCJmSafeChecker.HANDLERFIELDS) {
                                                            if (e1.equalTo(e4)) {
                                                                isHandlerField = true;
                                                            }
                                                        }
                                                        if (!isHandlerField) {
                                                            check = false;
                                                            FieldAccess fa2 = (FieldAccess) e3;
                                                            Variable handlerExpr = (Variable) fa2.getLast();
                                                            String handlerClassName = handlerExpr.getVarType().getTypeName();
                //                                                System.out.println("Need to check " + e1.getExpressionString() + " against PRMem of handler " + handlerClassName);
                                                            MSafeHandler tempHandler = new MSafeHandler(handlerClassName);
                                                            PRMem prMem = new PRMem(tempHandler);
                                                            if (mapping1 != null) {
                                                                for (RefCon rc1 : e1Rcs) {
                                                                    checked = true;
                                                                    if (!dominates(rc1, prMem)) {
                                                                        System.out.println("Checking to see if local variable " + e1.getExpressionString() + " of " + e2.getExpressionString() + " is safe in current env");
                                                                        System.out.print("  POSSIBLE MEMORY SAFETY VIOLATION - The field '" + e1.getExpressionString() + "' may reference an object stored in '");
                                                                        rc1.printRefCon();
                                                                        System.out.print("' when it should reside in '");
                                                                        prMem.printRefCon();
                                                                        System.out.println("' or higher");
                                                                        System.out.println("\nEnvironment that may be unsafe:");
                                                                        EnvUtil.printEnv(env);
                                                                    }
                                                                }
                                                            }
                                                            mapping1.checked();
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        if (check) {
                                            HashSet<RefCon> e2Rcs = mapping2.getRefCons();
                                            for (RefCon rc2 : e2Rcs) {
                                                for (RefCon rc1 : e1Rcs) {
                                                    checked = true;
                                                    if (!dominates(rc1, rc2)) {
                                                        checked = true;
                                                        System.out.println("Checking to see if field " + e1.getExpressionString() + " of " + e2.getExpressionString() + " is safe in current env");
                                                        System.out.print("  POSSIBLE MEMORY SAFETY VIOLATION - The field '" + e1.getExpressionString() + "' may reference an object stored in '");
                                                        rc1.printRefCon();
                                                        System.out.print("' when its parent '" + e2.getExpressionString() + "' resides in '");
                                                        rc2.printRefCon();
                                                        System.out.println("'");
                                                        System.out.println("\nEnvironment that may be unsafe:");
                                                        EnvUtil.printEnv(env);
                                                    }
                                                }
                                            }
                                        } else {
                                            checked = true;
                                        }
                                        mapping1.checked();
                                    }
                                }
                            }*/
                        }
                    }
//                    mapping1.checked();
                }
            }
//        }
    }



    public static void checkMethodPropertiesSafety(MethodProperties properties) {
//        for (MethodProperty property : properties.getProperties()) {
            MethodRefChange refChange = properties.getMethodRefChange();

            for (MethodRefMapping mapping : refChange.getMethodRefs()) {
                Expr e = mapping.getExpr();
                HashSet<MetaRefCon> mrcs = mapping.getMetaRefCons();
                HashSet<MetaRefCon> updatedMrcs = new HashSet<MetaRefCon>(0);
                for (MetaRefCon mrc : mrcs) {
//                    updatedMrcs.addAll(updateErcMetaRefCons(mrc, refChange));
                    for (MetaRefCon mrc1 : updateErcMetaRefCons(mrc, refChange)) {
                        boolean found = false;
                        for (MetaRefCon mrc2 : updatedMrcs) {
                            if (mrc2.getMetaRefConString().equals(mrc1.getMetaRefConString())) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            updatedMrcs.add(mrc1);
                        }
                    }
                }
                mapping.setMetaRefCons(updatedMrcs);
            }

//            System.out.println("Checking this method property:");
//            MethodPropertiesBuilder.printMethodProperty(property);

            for (MethodRefMapping mapping1 : refChange.getMethodRefs()) {
                if (mapping1.check()) {
                    Expr e1 = mapping1.getExpr();
                    HashSet<MetaRefCon> e1Mrcs = mapping1.getMetaRefCons();
                    if (e1Mrcs.size() > 0) {
                        boolean checked = false;
                        for (MethodRefMapping mapping2 : refChange.getMethodRefs()) {
                            Expr e2 = mapping2.getExpr();
                            if (EnvUtil.exprPrefixOfFieldAccess(e1, e2) && !(e1.equalTo(e2)) && (e1.getLength() == e2.getLength() - 1)) {
                                checked = true;
                                HashSet<MetaRefCon> e2Mrcs = mapping2.getMetaRefCons();
                                for (MetaRefCon mrc2 : e2Mrcs) {
                                    for (MetaRefCon mrc1 : e1Mrcs) {
                                        if (!safeMetaRefConMapping(mrc1, mrc2)) {
                                            System.out.println("Checking to see if field " + e1.getExpressionString() + " of " + e2.getExpressionString() + " is safe in current method properties");
                                            System.out.print("  POSSIBLE MEMORY SAFETY VIOLATION IN METHOD - The field '" + e1.getExpressionString() + "' may reference an object stored in '");
                                            mrc1.printMetaRefCon();
                                            System.out.print("' when its parent '" + e1.getExpressionString() + "' resides in '");
                                            mrc2.printMetaRefCon();
                                            System.out.println("'");
                                            System.out.println("\nMethod properties that may be unsafe:");
                                            MethodPropertiesBuilder.printMethodProperties(properties);
                                        }
                                    }
                                }
                                break;
                            }
                        }
/*
                        if (!checked) {
                            for (MethodRefMapping mapping2 : refChange.getMethodRefs()) {
                                Expr e2 = mapping2.getExpr();
                                if (EnvUtil.exprPrefixOfFieldAccess(e1, e2) && !(e1.equalTo(e2))) {
                                    HashSet<MetaRefCon> e2Mrcs = mapping2.getMetaRefCons();
                                    for (MetaRefCon mrc2 : e2Mrcs) {
                                        for (MetaRefCon mrc1 : e1Mrcs) {
                                            if (!safeMetaRefConMapping(mrc1, mrc2)) {
                                                System.out.println("Checking to see if field " + e1.getExpressionString() + " of " + e2.getExpressionString() + " is safe in current method properties");
                                                System.out.print("  POSSIBLE MEMORY SAFETY VIOLATION IN METHOD - The field '" + e1.getExpressionString() + "' may reference an object stored in '");
                                                mrc1.printMetaRefCon();
                                                System.out.print("' when its parent '" + e2.getExpressionString() + "' resides in '");
                                                mrc2.printMetaRefCon();
                                                System.out.println("'");
                                                System.out.println("\nMethod properties that may be unsafe:");
                                                MethodPropertiesBuilder.printMethodProperties(properties);
                                            }
                                        }
                                    }
                                }
                            }
                        }
*/
                        mapping1.checked();
                    }
                }
            }
//        }
    }



//    public static void checkMethodPropertiesSafetyMethod(MethodProperties properties, Method m, MetaRefCon currentMRC, Expr currentExpr) {
    public static void checkMethodPropertiesSafetyMethod(MethodProperties properties, ArrayList<Expr> vars, MetaRefCon currentMRC, Expr currentExpr) {
//        System.out.println("checkMethodPropertiesSafetyMethod");
//        for (MethodProperty property : properties.getProperties()) {
//            System.out.println("  checkingProperties");
//            MethodPropertiesBuilder.printMethodProperties(properties);
            MethodRefChange refChange = properties.getMethodRefChange();
//            System.out.println("    1");

            for (MethodRefMapping mapping : refChange.getMethodRefs()) {
                Expr e = mapping.getExpr();
                HashSet<MetaRefCon> mrcs = mapping.getMetaRefCons();
                HashSet<MetaRefCon> updatedMrcs = new HashSet<MetaRefCon>(0);
                boolean flag = false;
                ArrayList<MetaRefCon> toRemove = new ArrayList<MetaRefCon>(0);
                for (MetaRefCon mrc : mrcs) {
                    if (mrc instanceof Erc) {
                        Erc erc = (Erc) mrc;
                        if (mapping.getExpr().equalTo(erc.getExpr())) {
                            toRemove.add(erc);
                        } else {
                            flag = true;
                        }
                    }
                }
                for (MetaRefCon mrc : toRemove) {
                    mapping.removeMetaRefCon(mrc);
                }
                if (flag) {
                    for (MetaRefCon mrc : mrcs) {
//                        updatedMrcs.addAll(updateErcMetaRefCons(mrc, refChange));

                        for (MetaRefCon mrc1 : updateErcMetaRefCons(mrc, refChange)) {
                            boolean found = false;
                            for (MetaRefCon mrc2 : updatedMrcs) {
                                if (mrc2.getMetaRefConString().equals(mrc1.getMetaRefConString())) {
                                    found = true;
                                    break;
                                }
                            }
                            if (!found) {
                                updatedMrcs.add(mrc1);
                            }
                        }
                    }
                    mapping.setMetaRefCons(updatedMrcs);
                }
            }

//            System.out.println("Checking this method properties:");
//            MethodPropertiesBuilder.printMethodProperties(properties);

//            System.out.println("    2");
//            ArrayList<Expr> exprsToRemove = m.getVarsToRemove();
            ArrayList<Expr> exprsToRemove = vars;
            ArrayList<Expr> exprsToRemoveCurrent = new ArrayList<Expr>(0);
            for (Expr e : exprsToRemove) {
                exprsToRemoveCurrent.add(EnvUtil.mergeExprs(currentExpr, e));
            }
            for (MethodRefMapping mapping1 : refChange.getMethodRefs()) {
//                System.out.println("     3");
                if (mapping1.check()) {
                    Expr e1 = mapping1.getExpr();
                    HashSet<MetaRefCon> e1Mrcs = mapping1.getMetaRefCons();
                    if (e1Mrcs.size() > 0) {
//                        System.out.println("      4");
                        boolean foundRemove = false;
                        for (Expr e : exprsToRemoveCurrent) {
                            if (e.equalTo(e1)) {
                                foundRemove = true;
                                break;
                            }
                        }
                        if (foundRemove) {
//        System.out.println("Checking to see if field " + e1.getExpressionString() + " (to be removed) is safe in CurrenMRC = " + currentMRC.getMetaRefConString());
                            for (MetaRefCon mrc1 : e1Mrcs) {
                                if (!safeMetaRefConMapping(mrc1, currentMRC)) {
                                    System.out.println("Checking to see if local variable " + e1.getExpressionString() + " (to be removed) is safe in current method properties");
                                    System.out.print("  POSSIBLE MEMORY SAFETY VIOLATION IN METHOD - The variable '" + e1.getExpressionString() + "' may reference an object stored in '");
                                    mrc1.printMetaRefCon();
                                    System.out.print("' when it should reside in '");
                                    currentMRC.printMetaRefCon();
                                    System.out.println("' or higher");
                                    System.out.println("\nMethod properties that may be unsafe:");
                                    MethodPropertiesBuilder.printMethodProperties(properties);
                                }
                            }
                            mapping1.checked();
                        }/* else {
                            boolean checked = false;
                            for (MethodRefMapping mapping2 : refChange.getMethodRefs()) {
                                if (!checked) {
//                                    System.out.println("       5");
                                    Expr e2 = mapping2.getExpr();
                                    HashSet<MetaRefCon> e2Mrcs = mapping2.getMetaRefCons();
                                    if (e2Mrcs.size() > 0) {
                                        if (EnvUtil.exprPrefixOfFieldAccess(e2, e1) && !(e1.equalTo(e2)) && (e2.getLength() == e1.getLength() - 1)) {
//                System.out.println(" Checking to see if field " + e1.getExpressionString() + " of " + e2.getExpressionString() + " is safe in current method properties");
                                            for (MetaRefCon mrc2 : e2Mrcs) {
                                                for (MetaRefCon mrc1 : e1Mrcs) {
                                                    checked = true;
                                                    if (!safeMetaRefConMapping(mrc1, mrc2)) {
                                                        System.out.println("Checking to see if field " + e1.getExpressionString() + " of " + e2.getExpressionString() + " is safe in current method properties");
                                                        System.out.print("  POSSIBLE MEMORY SAFETY VIOLATION IN METHOD - The field '" + e1.getExpressionString() + "' may reference an object stored in '");
                                                        mrc1.printMetaRefCon();
                                                        System.out.print("' when its parent '" + e2.getExpressionString() + "' resides in '");
                                                        mrc2.printMetaRefCon();
                                                        System.out.println("'");
                                                        System.out.println("\nMethod properties that may be unsafe:");
                                                        MethodPropertiesBuilder.printMethodProperties(properties);
                                                    }
                                                }
                                            }
                                            mapping1.checked();
                                        }
                                    }
                                }
                            }
/*
                            if (!checked) {
//                                System.out.println("       6");
                                MethodRefMapping mapping2 = null;
                                int length = 0;
                                for (MethodRefMapping mapping3 : refChange.getMethodRefs()) {
                                    Expr e2 = mapping3.getExpr();
                                    if (EnvUtil.exprPrefixOfFieldAccess(e2, e1) && !(e1.equalTo(e2))) {
                                        if (mapping3.getMetaRefCons().size() > 0 && e2.getLength() > length) { 
                                            mapping2 = mapping3;
                                            length = e2.getLength();
                                        }
                                    }
                                }
                                if (mapping2 != null) {
                                    Expr e2 = mapping2.getExpr();
            System.out.println("Checking to see if field " + e1.getExpressionString() + " of " + e2.getExpressionString() + " is safe in current method properties");
                                    HashSet<MetaRefCon> e2Mrcs = mapping2.getMetaRefCons();
                                    for (MetaRefCon mrc2 : e2Mrcs) {
                                        for (MetaRefCon mrc1 : e1Mrcs) {
                                            if (!safeMetaRefConMapping(mrc1, mrc2)) {
                                                checked = true;
                                                System.out.println("Checking to see if field " + e1.getExpressionString() + " of " + e2.getExpressionString() + " is safe in current method properties");
                                                System.out.print("  POSSIBLE MEMORY SAFETY VIOLATION IN METHOD - The field '" + e1.getExpressionString() + "' may reference an object stored in '");
                                                mrc1.printMetaRefCon();
                                                System.out.print("' when its parent '" + e2.getExpressionString() + "' resides in '");
                                                mrc2.printMetaRefCon();
                                                System.out.println("'");
                                                System.out.println("\nMethod properties that may be unsafe:");
                                                MethodPropertiesBuilder.printMethodProperties(properties);
                                            }
                                        }
                                    }
                                    mapping1.checked();
                                }
                            }

                        }
*/
                    }
//                    mapping1.checked();
                }
            }
//            System.out.println("  checked");
//       }
    }


    // Checks to see if rc (refcon of object) dominates varRc (refcon of var)
    public static boolean dominates(RefCon rc, RefCon varRc) {
        if (rc instanceof Prim || rc instanceof IMem) {
            return true;
        } else if (rc instanceof MMem && !(varRc instanceof IMem)) {
            return true;
        } else if (rc instanceof TPMMem && !(varRc instanceof MMem && !(varRc instanceof IMem))) {
            TPMMem tpMMem = (TPMMem) rc;
            if (varRc instanceof TPMMem) {
                TPMMem tpMMem2 = (TPMMem) varRc;
                if (tpMMem.getDepth() <= tpMMem2.getDepth()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        } else if (rc instanceof PRMem && !(varRc instanceof IMem) && !(varRc instanceof MMem)) {
            return true;
        } else if (rc instanceof TPMem && !(varRc instanceof IMem) && !(varRc instanceof MMem) && !(varRc instanceof PRMem)) {
            TPMem tpMem = (TPMem) rc;
            if (varRc instanceof TPMem) {
                TPMem tpMem2 = (TPMem) varRc;
                if (tpMem.getDepth() <= tpMem2.getDepth()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }


    public static ArrayList<MetaRefCon> updateErcMetaRefCons(MetaRefCon mrc, MethodRefChange refChange) {
        ArrayList<MetaRefCon> result = new ArrayList<MetaRefCon>(0);
        if (mrc instanceof Erc) {
            Erc erc = (Erc) mrc;
            boolean found = false;
            for (MethodRefMapping mrp : refChange.getMethodRefs()) {
                if (mrp.getExpr().equalTo(erc.getExpr())) {
                    found = true;
                    HashSet<MetaRefCon> mrcs = mrp.getMetaRefCons();
                    for (MetaRefCon mrc1 : mrcs) {
                        result.add(mrc1);
                    }
                }
                break;
            }
            if (!found) {
                result.add(mrc);
            }
        } else {
            result.add(mrc);
        }
        return result;
    }


    // Checks to see if mrc1 (metarefcon of rhs) is safe mapped to mrc2 (metarefcon of lhs)
    public static boolean safeMetaRefConMapping(MetaRefCon mrc1, MetaRefCon mrc2) {
//        if (mrc1 instanceof Erc || mrc2 instanceof Erc || mrc1 instanceof Current) {
//            return true;
//        } else
        if (mrc1 instanceof CurrentPrivate && mrc2 instanceof Current) {
            return false;
        } else if (mrc1 instanceof CurrentPrivate && mrc2 instanceof CurrentPrivate) {
            CurrentPrivate cp1 = (CurrentPrivate) mrc1;
            CurrentPrivate cp2 = (CurrentPrivate) mrc2;
            if (cp1.getDepth() > cp2.getDepth()) {
                return false;
            } else {
                return true;
            }
        } else if (mrc1 instanceof Rcs && mrc2 instanceof Rcs) {
            Rcs rcs1 = (Rcs) mrc1;
            Rcs rcs2 = (Rcs) mrc2;
            for (RefCon rc1 : rcs1.getRefCons()) {
                for (RefCon rc2 : rcs2.getRefCons()) {
                    if (!dominates(rc1, rc2)) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return true;
        }
    }

    public static boolean refConSetsEqual(HashSet<RefCon> rcs1, HashSet<RefCon> rcs2) {
        boolean result = true;
        if (rcs1.size() == rcs2.size()) {
            for (RefCon rc1 : rcs1) {
                boolean found = false;
                for (RefCon rc2 : rcs2) {
                    if (rc1.equalTo(rc2)) {
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

    public static boolean metaRefConSetsEqual(HashSet<MetaRefCon> mrcs1, HashSet<MetaRefCon> mrcs2) {
        boolean result = true;
        if (mrcs1.size() == mrcs2.size()) {
            for (MetaRefCon mrc1 : mrcs1) {
                boolean found = false;
                for (MetaRefCon mrc2 : mrcs2) {
                    if (mrc1.equalTo(mrc2)) {
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

    private static void removePrimTypes(Environment env) {
        ShareRelation shareRelation = env.getShareRelation();
        RefSet refSet = env.getRefSet();
        ArrayList<Expr> toRemove = new ArrayList<Expr>(0);
        for (RefMapping mapping : refSet.getRefSet()) {
            if (mapping.getRefCons().size() == 1) {
                for (RefCon rc : mapping.getRefCons()) {
                    if (rc instanceof Prim) {
                        toRemove.add(mapping.getExpr());
                    }
                }
            }
        }
        for (Expr e : toRemove) {
            shareRelation.removeExprFromShares(e);
            refSet.removeExprFromRefSet(e);
        }
    }

    private static void removeEmptyRefs(Environment env) {
        RefSet refSet = env.getRefSet();
        ArrayList<RefMapping> toRemove = new ArrayList<RefMapping>(0);
        for (RefMapping mapping : refSet.getRefSet()) {
            if (mapping.getRefCons().size() == 0) {
                toRemove.add(mapping);
            }
        }
        for (RefMapping mapping : toRemove) {
            refSet.removeRefMapping(mapping);
        }
    }


}

package hijac.tools.scjmsafe.checker.environment;

import hijac.tools.scjmsafe.language.*;
import hijac.tools.scjmsafe.language.Commands.*;
import hijac.tools.scjmsafe.language.Expressions.*;
import hijac.tools.scjmsafe.language.RefCon.*;
import hijac.tools.scjmsafe.language.Method.*;

import hijac.tools.scjmsafe.checker.analysis.MethodPropertiesBuilder;

import hijac.tools.scjmsafe.translation.Util;
import hijac.tools.scjmsafe.checker.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;


public class EnvUtil {

    public static int LOOPCUTOFF = 1;

    ///////////////////////////////////////////////
    ////               Commands                ////
    ///////////////////////////////////////////////

    public static void analyseCommands(Environment env, ArrayList<Command> coms, Expr currentExpr, RefCon currentRC) {
        for (Command c : coms) {
            analyseCommand(env, c, currentExpr, currentRC);
        }
    }

    public static void analyseCommand(Environment env, Command com, Expr currentExpr, RefCon currentRC) {

        boolean print = true;

        if (com == null) {
            return;
        }

        if (com instanceof Assignment) {
            if (print) {
                System.out.println("--------------------------------- Assignment:");
                com.printCommand(0);
                System.out.println();
                System.out.println();
            }

            Assignment ass = (Assignment) com;
            calcEnvAssignment(env, ass, currentExpr, currentRC);
            if (print) {
                printEnv(env);
            }

        } else if (com instanceof Declaration) {
            if (print) {
                System.out.println("--------------------------------- Dec:");
                com.printCommand(0);
                System.out.println();
                System.out.println();
            }

            Declaration dec = (Declaration) com;
            addDecToEnv(env, dec, currentExpr, currentRC);
            if (print) {
                printEnv(env);
            }

        } else if (com instanceof DoWhile) {
            if (print) {
                System.out.println("--------------------------------- DoWhile:");
                com.printCommand(0);
                System.out.println();
                System.out.println();
            }

            DoWhile doWhile = (DoWhile) com;
//            analyseCommand(env, doWhile.getC1(), currentExpr, currentRC);
            analyseLoop(env, doWhile.getC1(), currentExpr, currentRC);

//            ArrayList<Expr> outOfScopeDecs = new ArrayList<Expr>(0);
//            calcOutOfScopeDecs(outOfScopeDecs, com, currentExpr);
//            removeOutOfScopeDecs(outOfScopeDecs, currentExpr, env);

        } else if (com instanceof EnterPrivateMemory) {
            if (print) {
                System.out.println("--------------------------------- EnterPrivateMemory:");
                com.printCommand(0);
                System.out.println();
                System.out.println();
            }

            EnterPrivateMemory enterPrivMem = (EnterPrivateMemory) com;
            MethodCall methodCall = enterPrivMem.getMethodCall();
            ArrayList<MethodSig> possibleMethodsSigs = methodCall.getPossibleMethods();
            ArrayList<Method> possibleMethods = MethodPropertiesBuilder.getMethodsFromSignatures(possibleMethodsSigs);
            applyPossibleMethods(possibleMethods, methodCall.getArgs(), env, currentExpr, methodCall.getExpr(), lowerRefCon(currentRC));
            if (print) {
                printEnv(env);
            }

        } else if (com instanceof ExecuteInAreaOf) {
            if (print) {
                System.out.println("--------------------------------- ExecuteInAreaOf:");
                com.printCommand(0);
                System.out.println();
                System.out.println();
            }

            ExecuteInAreaOf executeInAreaOf = (ExecuteInAreaOf) com;
            MethodCall methodCall = executeInAreaOf.getMethodCall();
            ArrayList<MethodSig> possibleMethodsSigs = methodCall.getPossibleMethods();
            ArrayList<Method> possibleMethods = MethodPropertiesBuilder.getMethodsFromSignatures(possibleMethodsSigs);
            applyPossibleMethodsEIAO(possibleMethods, methodCall.getArgs(), env, currentExpr, methodCall.getExpr(), executeInAreaOf.getMetaRefCon(), currentRC);
            if (print) {
                printEnv(env);
            }

        } else if (com instanceof ExecuteInOuterArea) {
            if (print) {
                System.out.println("--------------------------------- ExecuteInOuterArea:");
                com.printCommand(0);
                System.out.println();
                System.out.println();
            }

            ExecuteInOuterArea executeInOuterArea = (ExecuteInOuterArea) com;
            MethodCall methodCall = executeInOuterArea.getMethodCall();
            ArrayList<MethodSig> possibleMethodsSigs = methodCall.getPossibleMethods();
            ArrayList<Method> possibleMethods = MethodPropertiesBuilder.getMethodsFromSignatures(possibleMethodsSigs);
            applyPossibleMethods(possibleMethods, methodCall.getArgs(), env, currentExpr, methodCall.getExpr(), raiseRefCon(currentRC));
            if (print) {
                printEnv(env);
            }

        } else if (com instanceof GetMemoryArea) {
            if (print) {
                System.out.println("--------------------------------- GetMemoryArea:");
                com.printCommand(0);
                System.out.println();
                System.out.println();
            }

            GetMemoryArea getMemoryArea = (GetMemoryArea) com;
            analyseGetMemoryArea(env, getMemoryArea, currentExpr);

        } else if (com instanceof For) {
            if (print) {
                System.out.println("--------------------------------- For:");
                com.printCommand(0);
                System.out.println();
                System.out.println();
            }

            For forCom = (For) com;
            analyseCommand(env, forCom.getC1(), currentExpr, currentRC);

            analyseLoop(env, new Sequence(forCom.getC3(), forCom.getC2()), currentExpr, currentRC);

//            Environment clone = env.clone();
//            analyseCommand(clone, forCom.getC3(), currentExpr, currentRC);
//            analyseCommand(clone, forCom.getC2(), currentExpr, currentRC);

//            env.setEnvEntrys(envJoin(clone, env).getEnv());

//            ArrayList<Expr> outOfScopeDecs = new ArrayList<Expr>(0);
//            calcOutOfScopeDecs(outOfScopeDecs, com, currentExpr);
//            removeOutOfScopeDecs(outOfScopeDecs, currentExpr, env);
            if (print) {
                printEnv(env);
            }

        } else if (com instanceof If) {
            if (print) {
                System.out.println("--------------------------------- If:");
                com.printCommand(0);
                System.out.println();
                System.out.println();
            }

            If ifCom = (If) com;

            Environment clone1 = env.clone();
            analyseCommand(clone1, ifCom.getC1(), currentExpr, currentRC);

            Environment clone2 = env.clone();
            analyseCommand(clone2, ifCom.getC2(), currentExpr, currentRC);

            env.setEnvironment(envJoin(clone1, clone2));

//            ArrayList<Expr> outOfScopeDecs = new ArrayList<Expr>(0);
//            calcOutOfScopeDecs(outOfScopeDecs, com, currentExpr);
//            removeOutOfScopeDecs(outOfScopeDecs, currentExpr, env);

            if (print) {
                printEnv(env);
            }

        } else if (com instanceof MethodCall) {
            if (print) {
                System.out.println("--------------------------------- Method Call:");
                com.printCommand(0);
                System.out.println();
                System.out.println();
            }

            MethodCall methodCall = (MethodCall) com;
            ArrayList<MethodSig> possibleMethodsSigs = methodCall.getPossibleMethods();
            ArrayList<Method> possibleMethods = MethodPropertiesBuilder.getMethodsFromSignatures(possibleMethodsSigs);
            applyPossibleMethods(possibleMethods, methodCall.getArgs(), env, currentExpr, methodCall.getExpr(), currentRC);
            if (print) {
                printEnv(env);
            }

        } else if (com instanceof NewInstance) {
            if (print) {
                System.out.println("--------------------------------- NewInstance:");
                com.printCommand(0);
                System.out.println();
                System.out.println();
            }

            NewInstance newInstance = (NewInstance) com;
            calcEnvNewInstance(env, newInstance, currentExpr, currentRC);
            if (print) {
                printEnv(env);
            }

        } else if (com instanceof Scope) {
            Scope scope = (Scope) com;
            analyseCommand(env, scope.getCommand(), currentExpr, currentRC);

        } else if (com instanceof Sequence) {
            Sequence seq = (Sequence) com;
            analyseCommand(env, seq.getC1(), currentExpr, currentRC);
            analyseCommand(env, seq.getC2(), currentExpr, currentRC);

        } else if (com instanceof Skip) {
            if (print) {
                System.out.println("--------------------------------- Skip:");
                com.printCommand(0);
                System.out.println();
                System.out.println();
            }

        } else if (com instanceof Switch) {
            if (print) {
                System.out.println("--------------------------------- Switch:");
                com.printCommand(0);
                System.out.println();
                System.out.println();
            }

            Switch switchCom = (Switch) com;
            ArrayList<Command> commands = switchCom.getCommands();
            distEnvJoin(env, commands, currentExpr, currentRC);

//            ArrayList<Expr> outOfScopeDecs = new ArrayList<Expr>(0);
//            calcOutOfScopeDecs(outOfScopeDecs, com, currentExpr);
//            removeOutOfScopeDecs(outOfScopeDecs, currentExpr, env);

        } else if (com instanceof Try) {
            if (print) {
                System.out.println("--------------------------------- Try:");
                com.printCommand(0);
                System.out.println();
                System.out.println();
            }

            Try tryCom = (Try) com;
            Command tryCommand = tryCom.getTryCommand();
            ArrayList<Command> catchComs = tryCom.getCatchComs();
            Command finallyCom = tryCom.getFinallyCommand();

            analyseCommand(env, tryCommand, currentExpr, currentRC);
            distEnvJoin(env, catchComs, currentExpr, currentRC);
            analyseCommand(env, finallyCom, currentExpr, currentRC);

//            ArrayList<Expr> outOfScopeDecs = new ArrayList<Expr>(0);
//            calcOutOfScopeDecs(outOfScopeDecs, com, currentExpr);
//            removeOutOfScopeDecs(outOfScopeDecs, currentExpr, env);
            if (print) {
                printEnv(env);
            }

        } else if (com instanceof While) {
            if (print) {
                System.out.println("--------------------------------- While:");
                com.printCommand(0);
                System.out.println();
                System.out.println();
            }

            While whileCom = (While) com;
//            analyseCommand(env, whileCom.getC1(), currentExpr, currentRC);
            analyseLoop(env, whileCom.getC1(), currentExpr, currentRC);

//            ArrayList<Expr> outOfScopeDecs = new ArrayList<Expr>(0);
//            calcOutOfScopeDecs(outOfScopeDecs, com, currentExpr);
//            removeOutOfScopeDecs(outOfScopeDecs, currentExpr, env);

        } else {
            System.out.println("[ERROR] - Environment has not been updated with command : ");
            com.printCommand(2);
        }

    }




    public static void analyseLoop(Environment env, Command com, Expr currentExpr, RefCon rc) {

//        Environment clone = env.clone(); 

        System.out.println("Analysing loop");
        System.out.println();

        ArrayList<Environment> envList = new ArrayList<Environment>(0);

        boolean fixedPoint = false;
        int iteration = 0;
        while (!fixedPoint) {
            iteration++;

//            analyseCommand(clone, com, null, rc);
            analyseCommand(env, com, currentExpr, rc);

//            simplifyEnvironment(clone);

            System.out.println("Environment after " + iteration + " iterations");
            System.out.println();
//            printEnv(clone);
            printEnv(env);

//            if (envList.size() > 0 && envList.get(envList.size() - 1).equalTo(clone)) {
            if (envList.size() > 0 && envList.get(envList.size() - 1).equalTo(env)) {
                System.out.println("Environment are equal after " + iteration + " iterations");
                fixedPoint = true;

                System.out.println();
//                printEnv(clone);
                printEnv(env);
            }

            envList.add(env.clone());

            if (iteration >= LOOPCUTOFF && fixedPoint == false) {
                System.out.println("[ERROR] - No fixed point has been detected after " + iteration + " iterations.");
                fixedPoint = true;
            }
        }

//        env.setEnvironment(envJoin(clone, env));

    }








    ///////////////////////////////////////////////
    ////              Declarations             ////
    ///////////////////////////////////////////////

    public static void addDecToEnv(Environment env, Declaration dec, Expr currentExpr, RefCon currentRC) {
        Variable var = dec.getVar();
        Expr newLExpr = mergeExprs(currentExpr, var);

        ShareRelation shareRel = env.getShareRelation();
        Share newShare = new Share(newLExpr, newLExpr);
        shareRel.addShare(newShare);

        RefSet refSet = env.getRefSet();
        HashSet<RefCon> rcs = getDecRefCon(var.getVarType());
        RefMapping refMapping = new RefMapping(newLExpr, rcs);
        refSet.updateRefMapping(refMapping);
    }

    public static void addDecsToEnv(Environment env, ArrayList<Declaration> decs, Expr currentExpr, RefCon currentRC) {
        for (Declaration d : decs) {
            addDecToEnv(env, d, currentExpr, currentRC);
        }
    }


    public static HashSet<RefCon> getDecRefCon(VarType varType) {
        HashSet<RefCon> result = new HashSet<RefCon>(0);
        if (varType.isPrimitive()) {
            if (varType.isArray()) {

            } else {
                result.add(new Prim());
            }
        } else if (varType.isReference()) {

        } else {
            System.out.println("[WARNING] - Type of declaration (reference / primitive) has not been detected");
        }
        return result;
    }

    public static void calcOutOfScopeDecs(ArrayList<Expr> list, Command com, Expr currentExpr) {
        ArrayList<Expr> varsToRemove = MethodPropertiesBuilder.getDecs(com, currentExpr);
        for (Expr e : varsToRemove) {
            list.add(e);
        }
    }

    private static void removeOutOfScopeDecsMethod(Method m, Expr currentExpr, Environment env) {
        ArrayList<Expr> list = m.getVarsToRemove();
        for (Expr e : list) {
            Expr e2 = mergeExprs(currentExpr, e);
//            System.out.println("Removing expr : " + e2.getExpressionString() + " from environment");
            ShareRelation shareRelation = env.getShareRelation();
            shareRelation.removeExprFromShares(e2);
            RefSet refSet = env.getRefSet();
            refSet.removeExprFromRefSet(e2);
        }
//        simplifyEnvironment(env);
    }

    private static void removeOutOfScopeDecsMethod(ArrayList<Expr> vars, Expr currentExpr, Environment env) {
        for (Expr e : vars) {
            Expr e2 = mergeExprs(currentExpr, e);
//            System.out.println("Removing expr : " + e2.getExpressionString() + " from environment");
            ShareRelation shareRelation = env.getShareRelation();
            shareRelation.removeExprFromShares(e2);
            RefSet refSet = env.getRefSet();
            refSet.removeExprFromRefSet(e2);
        }
//        simplifyEnvironment(env);
    }

    public static void removeOutOfScopeDecs(ArrayList<Expr> list, Expr currentExpr, Environment env) {
        for (Expr e : list) {
//            System.out.println("Removing expr : " + e.getExpressionString() + " from environment");
            ShareRelation shareRelation = env.getShareRelation();
            shareRelation.removeExprFromShares(e);
            RefSet refSet = env.getRefSet();
            refSet.removeExprFromRefSet(e);
        }
//        simplifyEnvironment(env);
    }



    ///////////////////////////////////////////////
    ////              Assignment               ////
    ///////////////////////////////////////////////

    public static void calcEnvAssignment(Environment env, Assignment ass, Expr currentExpr, RefCon currentRC) {

        Expr lexpr = ass.getLExpr();
        Expr rexpr = ass.getRExpr();

        if (!(rexpr instanceof Val)) {
            envAssignmentAdd(env, ass, currentExpr);
        }
/*           
        } else if (lexpr instanceof Identifier) {
            if (lexpr instanceof ArrayElement) {
                ArrayElement ae = (ArrayElement) lexpr;

            } else if (lexpr instanceof Variable) {
                Variable var = (Variable) lexpr;
                envRemove(env, lexpr);
                envAssignmentAdd(env, ass);
                
            } else {
                System.out.println("[ERROR] - Assignment to identifier has not been added to the environment");
            }

        } else if (lexpr instanceof FieldAccess) {
            FieldAccess fa = (FieldAccess) lexpr;
            Identifier last = fa.getLast();
            if (last instanceof ArrayElement) {
                ArrayElement ae = (ArrayElement) last;

            } else if (last instanceof Variable) {
                Variable var = (Variable) last;

            } else {
                System.out.println("[ERROR] - Assignment to field access has not been added to the environment");
            }

        } else {
            System.out.println("[ERROR] - Assignment has not been identified and cannot be added to the environment");
        }
*/
    }

    public static void envAssignmentAdd(Environment env, Assignment ass, Expr currentExpr) {
        Expr lexpr = ass.getLExpr();
        Expr rexpr = ass.getRExpr();
        Expr newLExpr = mergeExprs(currentExpr, lexpr);
        Expr newRExpr;
        if (rexpr instanceof This) {
            newRExpr = currentExpr;
        } else {
            newRExpr = mergeExprs(currentExpr, rexpr);
        }
        HashSet<Share> sharesToAdd = new HashSet<Share>(0); 
        HashSet<RefMapping> refsToUpdate = new HashSet<RefMapping>(0);

        ShareRelation shareRelation = env.getShareRelation();

        ArrayList<Expr> equalExprs = new ArrayList<Expr>(0);
        if (lexpr instanceof FieldAccess || lexpr instanceof ArrayElement) {
            for (Share s : shareRelation.getShares()) {
                if (getFrontOfExpr(newLExpr).equalTo(s.getLExpr()) && !s.getLExpr().equalTo(s.getRExpr())) { 
//                    System.out.println("Assignment - LE : " + getFrontOfExpr(newLExpr).getExpressionString() + " is equal to " + s.getRExpr().getExpressionString());
//                    equalExprs.add(s.getRExpr());
                    addToEqualExprs(equalExprs, s.getRExpr());
                } else if (getFrontOfExpr(newLExpr).equalTo(s.getRExpr()) && !s.getLExpr().equalTo(s.getRExpr())) {
//                    System.out.println("Assignment - LE : " + getFrontOfExpr(newLExpr).getExpressionString() + " is equal to " + s.getLExpr().getExpressionString());
//                    equalExprs.add(s.getLExpr());
                    addToEqualExprs(equalExprs, s.getLExpr());
                }
            }
        }

        ArrayList<Expr> equalExprsRhs = new ArrayList<Expr>(0);
        for (Share s : shareRelation.getShares()) {
            if (newRExpr.equalTo(s.getLExpr()) && !s.getLExpr().equalTo(s.getRExpr()) && !exprPrefixOfFieldAccess(newLExpr, s.getRExpr())) { 
                addToEqualExprs(equalExprsRhs, s.getRExpr());
            } else if (newRExpr.equalTo(s.getRExpr()) && !s.getLExpr().equalTo(s.getRExpr()) && !exprPrefixOfFieldAccess(newLExpr, s.getLExpr())) {
                addToEqualExprs(equalExprsRhs, s.getLExpr());
            }/* else if (exprPrefixOfFieldAccess(s.getLExpr(), newLExpr)) {
                addToEqualExprs(equalExprs, sortShareAssignmentExpression(s.getRExpr(), s.getLExpr(), newLExpr));
            }  else if (exprPrefixOfFieldAccess(s.getRExpr(), newLExpr)) {
                addToEqualExprs(equalExprs, sortShareAssignmentExpression(s.getLExpr(), s.getRExpr(), newLExpr));
            }*/
        }

//        shareRelation.removeExprFromShares(newLExpr);
        Share newShare;
//            Share newShare = new Share(newLExpr, newLExpr);
//            sharesToAdd.add(newShare);
        if (!(rexpr instanceof Val) && !newLExpr.equalTo(newRExpr) && !exprPrefixOfFieldAccess(newLExpr, newRExpr)) {
            newShare = new Share(newLExpr, newRExpr);
            sharesToAdd.add(newShare);
        }
        for (Expr e1 : equalExprsRhs) {
            newShare = new Share(newLExpr, e1);
            sharesToAdd.add(newShare);
        }

        for (Expr e : equalExprs) {
//            newShare = new Share(getFrontOfExpr(newLExpr), e);
//            sharesToAdd.add(newShare);
//                newShare = new Share(e, newLExpr);
//                sharesToAdd.add(newShare);

            newShare = new Share(sortShareAssignmentExpression(e, newRExpr, newLExpr), newRExpr);
            if (!exprPrefixOfFieldAccess(newLExpr, newRExpr)) {
                sharesToAdd.add(newShare);
            }
            for (Expr e1 : equalExprsRhs) {
                newShare = new Share(sortShareAssignmentExpression(e, newRExpr, newLExpr), e1);
                sharesToAdd.add(newShare);
            }

//            newShare = new Share(e, newRExpr);    // TODO - Remove this - why is it here?
//            sharesToAdd.add(newShare);
//                newShare = new Share(newRExpr, e);
//                sharesToAdd.add(newShare);
        }

        RefSet refSet = env.getRefSet();
//        refSet.removeExprFromRefSet(newLExpr);
        RefMapping lExprRefMapping = refSet.getRefMapping(newLExpr);
        RefMapping rExprRefMapping = refSet.getRefMapping(newRExpr);
        HashSet<RefCon> refConSet;
        if (rExprRefMapping == null) {
            if (rexpr instanceof Val) {
                HashSet<RefCon> rexprRcs = new HashSet<RefCon>(0);
                rexprRcs.add(new Prim());
                refConSet = rexprRcs;
            } else {
                HashSet<RefCon> rexprRcs = new HashSet<RefCon>(0);
                refConSet = rexprRcs;
            }
        } else {
            refConSet = rExprRefMapping.getRefCons();
        }
        refsToUpdate.add(new RefMapping(newLExpr, refConSet));
        for (Expr e : equalExprs) {
            refsToUpdate.add(new RefMapping(sortShareAssignmentExpression(e, newRExpr, newLExpr), refConSet));
        }

        // This loop adds in any fields of an object on the rhs to the variable being assigned to on the lhs. For example, a = b would produce a.field if b had a field 'field'.
//        equalExprs.add(newLExpr);
        addToEqualExprs(equalExprs, newLExpr);
        for (Expr e : equalExprs) {
//            System.out.println("Analysing equal expression : " + e.getExpressionString());
            for (Share s : shareRelation.getShares()) {
//                System.out.println("  Analysing share : " + s.getShareString());
                if (exprPrefixOfFieldAccess(newRExpr, s.getLExpr()) && s.getLExpr().getLength() > newRExpr.getLength()) {
                    Expr newLhs = sortShareAssignmentExpression(e, newRExpr, s.getLExpr());
                    if (newLhs != null && !exprPrefixOfFieldAccess(newLExpr, newRExpr)) {
//                        System.out.println("    newLHS (a) = " + newLhs.getExpressionString());
                        sharesToAdd.add(new Share(newLhs, s.getRExpr()));
                        RefMapping mapping = refSet.getRefMapping(s.getRExpr());
                        HashSet<RefCon> refCons;
                        if (mapping != null) {
                            refCons = mapping.getRefCons();
    //                        newMapping = new RefMapping(newLhs, mapping.getRefCons());
                        } else {
                            HashSet<RefCon> rexprRcs = new HashSet<RefCon>(0);
                            refCons = rexprRcs;
    //                        newMapping = new RefMapping(newLhs, rexprRcs);
                        }
                        if (!newLhs.equalTo(newLExpr)) {
                            sharesToAdd.add(new Share(newLhs, newLhs));
                            refsToUpdate.add(new RefMapping(newLhs, refCons));
                        }
                    }
                } else if (exprPrefixOfFieldAccess(newRExpr, s.getRExpr()) && s.getRExpr().getLength() > newRExpr.getLength()) {   // TODO - Do I actually need this else if?
                    Expr newLhs = sortShareAssignmentExpression(e, newRExpr, s.getRExpr());
                    if (newLhs != null && !exprPrefixOfFieldAccess(newLExpr, newRExpr)) {
//                        System.out.println("    newLHS (b) = " + newLhs.getExpressionString());
                        sharesToAdd.add(new Share(newLhs, s.getLExpr()));
                        RefMapping mapping = refSet.getRefMapping(s.getLExpr());
                        HashSet<RefCon> refCons;
                        if (mapping != null) {
                            refCons = mapping.getRefCons();
    //                        newMapping = new RefMapping(newLhs, mapping.getRefCons());
                        } else {
                            HashSet<RefCon> rexprRcs = new HashSet<RefCon>(0);
                            refCons = rexprRcs;
    //                        newMapping = new RefMapping(newLhs, rexprRcs);
                        }
                        if (!newLhs.equalTo(newLExpr)) {
                            sharesToAdd.add(new Share(newLhs, newLhs));
                            refsToUpdate.add(new RefMapping(newLhs, refCons));
                        }
                    }
                }
            }
        }

        shareRelation.addShares(sharesToAdd);
        refSet.updateRefMappings(refsToUpdate);
        takeTransitiveClosure(env);
        CheckingUtil.checkMemorySafety(env);
    }





    public static void addToEqualExprs(ArrayList<Expr> equalExprs, Expr e) {
        boolean found = false;
        for (Expr exprInEqual : equalExprs) {
            if (exprInEqual.equalTo(e)) {
                found = true;
                break;
            }
        }
        if (!found) {
            equalExprs.add(e);
        }
    }

    public static void removeResultExprs(Environment env) {
        ShareRelation shareRelation = env.getShareRelation();
        HashSet<Share> shares = new HashSet<Share>(0);
        for (Share s : shareRelation.getShares()) {
            if (getFirstOfExpr(s.getLExpr()).getExpressionString().equals("Result") || getFirstOfExpr(s.getRExpr()).getExpressionString().equals("Result")) {
                shares.add(s);
            }
        }
        shareRelation.removeShares(shares);
    }

    public static void takeTransitiveClosure(Environment env) {
        ShareRelation shareRelation = env.getShareRelation();

        removeResultExprs(env);

        Environment store;
        do {
            store = env.clone();
            HashSet<Share> shares = new HashSet<Share>(0);
            for (Share s1 : shareRelation.getShares()) {
//                HashSet<Expr> emptySet = new HashSet<Expr>(0);
//                HashSet<Expr> matchingExprs = shareRelation.getAllEqualExprs(s1.getRExpr(), emptySet);
                HashSet<Expr> matchingExprs = new HashSet<Expr>(0);
                shareRelation.getAllEqualExprs(s1.getRExpr(), matchingExprs, 0);
//                System.out.println("Equal exprs to " + s1.getRExpr().getExpressionString() + " are:");
//                for (Expr e : matchingExprs) {
//                    System.out.println("  - " + e.getExpressionString());
//                }
                Iterator iter = matchingExprs.iterator();
                while (iter.hasNext()) {
                    Expr e = (Expr) iter.next();
                    if (!exprPrefixOfFieldAccess(s1.getLExpr(), e) && !exprPrefixOfFieldAccess(e, s1.getLExpr())) {
                        shares.add(new Share(s1.getLExpr(), e));
                    }
                }
            }
            shareRelation.addShares(shares);
        } while(!store.equalTo(env));

    }










    ///////////////////////////////////////////////
    ////             New Instance              ////
    ///////////////////////////////////////////////

    public static void calcEnvNewInstance(Environment env, NewInstance newInstance, Expr currentExpr, RefCon currentRC) {
        Expr lexpr = newInstance.getExpr();
        Expr newLExpr = mergeExprs(currentExpr, lexpr);
        MetaRefCon NImrc = newInstance.getMetaRefCon();
        if (NImrc instanceof Erc) {
            Erc erc = (Erc) NImrc;
            erc.setExpr(mergeExprs(currentExpr, erc.getExpr()));
        }
        VarType varType = newInstance.getVarType();
        String className = varType.getTypeName();
        ArrayList<Expr> params = newInstance.getParams();

        ShareRelation shareRel = env.getShareRelation();
//            shareRel.removeExprFromShares(newLExpr);
//            shareRel.addShare(new Share(newLExpr, newLExpr));

        RefSet refSet = env.getRefSet();
//            refSet.removeExprFromRefSet(newLExpr);
        HashSet<RefCon> rcs = getRefConsFromMRC(NImrc, currentRC, env, currentExpr);
//            RefMapping newRefMapping = new RefMapping(newLExpr, rcs);
        refSet.updateRefMapping(newLExpr, rcs);
//            refSet.updateRefMapping(newRefMapping);

        HashSet<Share> sharesToAdd = new HashSet<Share>(0); 

        ArrayList<Expr> equalExprs = new ArrayList<Expr>(0);
        if (lexpr instanceof FieldAccess || lexpr instanceof ArrayElement) {
            for (Share s : shareRel.getShares()) {
                if (!s.getLExpr().equalTo(s.getRExpr())) {
                    if (getFrontOfExpr(newLExpr).equalTo(s.getLExpr())) { 
    //                    System.out.println("New Instance - LE : " + getFrontOfExpr(newLExpr).getExpressionString() + " is equal to " + s.getRExpr().getExpressionString());
                        addToEqualExprs(equalExprs, s.getRExpr());
                    } else if (getFrontOfExpr(newLExpr).equalTo(s.getRExpr())) {
    //                    System.out.println("New Instance - LE : " + getFrontOfExpr(newLExpr).getExpressionString() + " is equal to " + s.getLExpr().getExpressionString());
                        addToEqualExprs(equalExprs, s.getLExpr());
                    } else if (exprPrefixOfFieldAccess(s.getLExpr(), newLExpr)) {
//                        System.out.println("New Instance - LE : " + newLExpr.getExpressionString() + " has a prefix " + s.getLExpr().getExpressionString());
//                        System.out.println("  Equal expression = " + sortShareAssignmentExpression(s.getRExpr(), s.getLExpr(), newLExpr).getExpressionString());
                        addToEqualExprs(equalExprs, sortShareAssignmentExpression(s.getRExpr(), s.getLExpr(), newLExpr));
                        sharesToAdd.add(new Share(sortShareAssignmentExpression(s.getRExpr(), s.getLExpr(), newLExpr), newLExpr));
                    }  else if (exprPrefixOfFieldAccess(s.getRExpr(), newLExpr)) {
//                        System.out.println("New Instance - LE : " + newLExpr.getExpressionString() + " has a prefix " + s.getRExpr().getExpressionString());
//                        System.out.println("  Equal expression = " + sortShareAssignmentExpression(s.getLExpr(), s.getRExpr(), newLExpr).getExpressionString());
                        addToEqualExprs(equalExprs, sortShareAssignmentExpression(s.getLExpr(), s.getRExpr(), newLExpr));
                        sharesToAdd.add(new Share(sortShareAssignmentExpression(s.getLExpr(), s.getRExpr(), newLExpr), newLExpr));
                    }
                }
            }
        }

//        for (Expr e : equalExprs) {
//            sharesToAdd.add(new Share(e, newLExpr));
//        }

        for (Expr e : equalExprs) {
//            System.out.println("NewInstance adding ref mapping : " + sortShareAssignmentExpression(e, getFrontOfExpr(newLExpr), newLExpr).getExpressionString());
            refSet.updateRefMapping(sortShareAssignmentExpression(e, getFrontOfExpr(newLExpr), newLExpr), rcs);
        }

        addToEqualExprs(equalExprs, newLExpr);

        for (Expr e : equalExprs) {
//            System.out.println("Analysing equal expression: " + e.getExpressionString());
            for (Share s : shareRel.getShares()) {
//                System.out.print("  Analysing share: ");
//                s.printShare();
                if (exprPrefixOfFieldAccess(newLExpr, s.getLExpr()) && s.getLExpr().getLength() > newLExpr.getLength()) {
                    Expr newLhs = sortShareAssignmentExpression(e, newLExpr, s.getLExpr());
                    if (newLhs != null && !exprPrefixOfFieldAccess(newLhs, s.getRExpr())) {
//                        System.out.println("    Adding share : " + newLhs.getExpressionString() + " -> " + s.getRExpr().getExpressionString());
                        sharesToAdd.add(new Share(newLhs, s.getRExpr()));
                        RefMapping mapping = refSet.getRefMapping(s.getRExpr());
                        HashSet<RefCon> refCons;
                        if (mapping != null) {
                            refCons = mapping.getRefCons();
                        } else {
                            HashSet<RefCon> rexprRcs = new HashSet<RefCon>(0);
                            refCons = rexprRcs;
                        }
                        if (!newLhs.equalTo(newLExpr)) {
//                            System.out.println("    Adding share : " + newLhs.getExpressionString() + " -> " + newLhs.getExpressionString());
                            sharesToAdd.add(new Share(newLhs, newLhs));
                            refSet.updateRefMapping(newLhs, refCons);
                        }
                    }
                } else if (exprPrefixOfFieldAccess(newLExpr, s.getRExpr()) && s.getRExpr().getLength() > newLExpr.getLength()) {
                    Expr newLhs = sortShareAssignmentExpression(e, newLExpr, s.getRExpr());
                    if (newLhs != null && !exprPrefixOfFieldAccess(newLhs, s.getLExpr())) {
//                        System.out.println("    Adding share : " + newLhs.getExpressionString() + " -> " + s.getLExpr().getExpressionString());
                        sharesToAdd.add(new Share(newLhs, s.getLExpr()));
                        RefMapping mapping = refSet.getRefMapping(s.getLExpr());
                        HashSet<RefCon> refCons;
                        if (mapping != null) {
                            refCons = mapping.getRefCons();
                        } else {
                            HashSet<RefCon> rexprRcs = new HashSet<RefCon>(0);
                            refCons = rexprRcs;
                        }
                        if (!newLhs.equalTo(newLExpr)) {
//                            System.out.println("    Adding share : " + newLhs.getExpressionString() + " -> " + newLhs.getExpressionString());
                            sharesToAdd.add(new Share(newLhs, newLhs));
                            refSet.updateRefMapping(newLhs, refCons);
                        }
                    }
                }
            }
        }
        shareRel.addShares(sharesToAdd);


        MSafeSuperClass newClass = SCJmSafeChecker.SCJmSafePROGRAM.getClass(className);
        if (SCJmSafeChecker.EXECUTIONPOINT == SCJmSafeChecker.ExecutionPoint.SAFELETGETSEQUENCER && SCJmSafeChecker.MSEQNAME.equals(className)) {
            System.out.println();
            System.out.println("Adding MissionSeq to list of classes to be checked");
            SCJmSafeChecker.classesToAnalyse.add(newClass);
            SCJmSafeChecker.exprsOfClasses.add(newLExpr);
            SCJmSafeChecker.MSEQEXPR = newLExpr;
        } else if (SCJmSafeChecker.EXECUTIONPOINT == SCJmSafeChecker.ExecutionPoint.SEQUENCERGETMISSION && SCJmSafeChecker.MISSIONNAMES.contains(className)) {
            System.out.println();
            System.out.println("Adding Mission to list of classes to be checked");
            SCJmSafeChecker.classesToAnalyse.add(newClass);
            SCJmSafeChecker.exprsOfClasses.add(newLExpr);
            SCJmSafeChecker.MISSIONEXPRS.add(newLExpr);
        } else if (SCJmSafeChecker.EXECUTIONPOINT == SCJmSafeChecker.ExecutionPoint.MISSIONINIT && SCJmSafeChecker.HANDLERNAMES.contains(className)) {
            System.out.println();
            System.out.println("Adding Handler to list of classes to be checked");
            SCJmSafeChecker.classesToAnalyse.add(newClass);
            SCJmSafeChecker.exprsOfClasses.add(newLExpr);
            SCJmSafeChecker.HANDLEREXPRS.add(newLExpr);
            MSafeHandler handler = (MSafeHandler) newClass;
            for (Declaration d : handler.getFields()) {
                Variable v = d.getVar();
                SCJmSafeChecker.HANDLERFIELDS.add(mergeExprs(newLExpr, v));
                System.out.println("Adding handler field : " + mergeExprs(newLExpr, v).getExpressionString());
            }
        }
        if (newClass != null) {
            ArrayList<Method> newClassConstrs = newClass.getConstr(params);
            if (newClassConstrs.size() > 0) {
                applyPossibleMethods(newClassConstrs, params, env, currentExpr, lexpr, currentRC);
            } else {
                System.out.println("[WARNING] - No constructor found for instantiation of new object");
            }
        } else {
            System.out.println("[WARNING] - Cannot find associated class with new instantiation");
        }
        takeTransitiveClosure(env);
        CheckingUtil.checkMemorySafety(env);
    }


    ///////////////////////////////////////////////
    ////             Method Calls              ////
    ///////////////////////////////////////////////

    public static void applyPossibleMethods(ArrayList<Method> methods, ArrayList<Expr> args, Environment env, Expr currentExpr, Expr lexpr, RefCon currentRC) {
//        System.out.println("Found " + methods.size() + " possible methods");
        Expr newCurrentExpr = mergeExprs(currentExpr, lexpr);
        if (methods.size() > 0) {
//            ArrayList<Environment> envList = new ArrayList<Environment>(0);
            ArrayList<Expr> vars = new ArrayList<Expr>(0);
            for (Method m : methods) {
//                Environment clone = env.clone();
                MethodProperties updatedMethodProperties = m.getProperties().clone();
                MethodPropertiesBuilder.swapParamsWithArgs(m, args, updatedMethodProperties);
                ArrayList<Expr> fields = m.getVisibleFields();
                applyMethodProperties(m, updatedMethodProperties, env, args, fields, currentExpr, lexpr, currentRC);
//                envList.add(clone);
//                CheckingUtil.checkMemorySafetyMethod(env, m, currentRC, newCurrentExpr);
//                removeOutOfScopeDecsMethod(m, newCurrentExpr, env);
                vars.addAll(m.getVarsToRemove());
            }
            takeTransitiveClosure(env);
            CheckingUtil.checkMemorySafetyMethod(env, vars, currentRC, newCurrentExpr);
            removeOutOfScopeDecsMethod(vars, newCurrentExpr, env);
//            env.setEnvironment(envList.get(0));
//            for (int i = 1; i < envList.size(); i++) {
//                for (EnvEntry entry : envList.get(i).getEnv()) {
//                    env.addEnvEntry(entry);
//                }
//            }
//            simplifyEnvironment(env);
            CheckingUtil.checkMemorySafety(env);
        }
    }

    public static void applyPossibleMethodsEIAO(ArrayList<Method> methods, ArrayList<Expr> args, Environment env, Expr currentExpr, Expr lexpr, MetaRefCon mrc, RefCon currentRC) {
        Expr newCurrentExpr = mergeExprs(currentExpr, lexpr);
        if (methods.size() > 0) {
//            ArrayList<Environment> envList = new ArrayList<Environment>(0);
            ArrayList<Expr> vars = new ArrayList<Expr>(0);
            for (Method m : methods) {
//                Environment clone = env.clone();
                MethodProperties updatedMethodProperties = m.getProperties().clone();
                MethodPropertiesBuilder.swapParamsWithArgs(m, args, updatedMethodProperties);
                ArrayList<Expr> fields = m.getVisibleFields();
                applyMethodPropertiesEIAO(m, updatedMethodProperties, env, args, fields, currentExpr, lexpr, mrc, currentRC);
//                envList.add(clone);
//                CheckingUtil.checkMemorySafetyMethod(env, m, currentRC, newCurrentExpr);
//                removeOutOfScopeDecsMethod(m, newCurrentExpr, env);
                vars.addAll(m.getVarsToRemove());
            }
            CheckingUtil.checkMemorySafetyMethod(env, vars, currentRC, newCurrentExpr);
            removeOutOfScopeDecsMethod(vars, newCurrentExpr, env);
//            env.setEnvEntrys(envList.get(0).getEnv());
//            for (int i = 1; i < envList.size(); i++) {
//                for (EnvEntry entry : envList.get(i).getEnv()) {
//                    env.addEnvEntry(entry);
//                }
//            }
//            simplifyEnvironment(env);
//            CheckingUtil.checkMemorySafety(env);
        }
    }


    public static void applyMethodProperties(Method m, MethodProperties toApply, Environment env, ArrayList<Expr> args, ArrayList<Expr> fields, Expr currentExpr, Expr lexpr, RefCon currentRC) {
//        ArrayList<EnvEntry> envEntryList = new ArrayList<EnvEntry>(0);
//        for (EnvEntry entry : env.getEnv()) {
//            for (MethodProperty mp : toApply.getProperties()) {
//                EnvEntry clone = entry.clone();
//                MethodProperty mpClone = mp.clone();
                applyPropertyToEnv(m, toApply, env, args, fields, currentExpr, lexpr, currentRC);
//                envEntryList.add(clone);
//            }
//        }
//        env.setEnvEntrys(envEntryList);
    }


    public static void applyMethodPropertiesEIAO(Method m, MethodProperties toApply, Environment env, ArrayList<Expr> args, ArrayList<Expr> fields, Expr currentExpr, Expr lexpr, MetaRefCon mrc, RefCon currentRC) {
//        ArrayList<EnvEntry> envEntryList = new ArrayList<EnvEntry>(0);
//        for (EnvEntry entry : env.getEnv()) {
            Erc erc = (Erc) mrc;
            erc.setExpr(mergeExprs(currentExpr, erc.getExpr()));
            for (RefCon rc : getRefConsFromMRC(erc, currentRC, env, currentExpr)) {
//                for (MethodProperty mp : toApply.getProperties()) {
//                    EnvEntry clone = entry.clone();
//                    MethodProperty mpClone = mp.clone();
                    applyPropertyToEnv(m, toApply, env, args, fields, currentExpr, lexpr, rc);
//                    envEntryList.add(clone);
//                }
            }
//        }
//        env.setEnvEntrys(envEntryList);
    }

    // Current Expr and LExpr are taken as arguments here because if the var is an accessible field or an argument, the LExpr is passed to getFrontOfExpr - this was changed from the entire currentExpr, because originally it did not handle both method calls such as method() and a.method() properly when the currentExpr was (for example) sequencer.mission
    public static void applyPropertyToEnv(Method m, MethodProperties toApply, Environment env, ArrayList<Expr> args, ArrayList<Expr> fields, Expr currentExpr, Expr lexpr, RefCon currentRC) {

        boolean print = false;

        ShareRelation existingShareRel = env.getShareRelation();
        RefSet existingRefChange = env.getRefSet();

        ShareRelation newShareRel = toApply.getMethodShareChange();
        MethodRefChange newRefChange = toApply.getMethodRefChange();

        ArrayList<Expr> equalExpr = new ArrayList<Expr>(0);
        ArrayList<ArrayList<Expr>> equalExprs = new ArrayList<ArrayList<Expr>>(0);
        int exprCount = 0;

        if (print) {
            System.out.println("To Apply:");
            MethodPropertiesBuilder.printMethodProperties(toApply);

            System.out.println("Env:");
            printEnv(env);
        }

        for (Share newS : newShareRel.getShares()) {
            boolean found = false;
            for (Expr e : args) {
//                if (getFirstOfExpr(newS.getLExpr()).equalTo(e)) {
                if (exprPrefixOfFieldAccess(e, newS.getLExpr())) {
                    found = true;
                    break;
                }
            }
            for (Expr e : fields) {
//                if (getFirstOfExpr(newS.getLExpr()).equalTo(e)) {
                if (getFirstOfExpr(newS.getLExpr()).equalTo(e)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                newS.setLExpr(mergeExprs(mergeExprs(currentExpr, lexpr), newS.getLExpr()));
            } else {
                newS.setLExpr(mergeExprs(mergeExprs(currentExpr, getFrontOfExpr(lexpr)), newS.getLExpr()));
            }
            found = false;
            for (Expr e : args) {
//                if (getFirstOfExpr(newS.getRExpr()).equalTo(e)) {
                if (exprPrefixOfFieldAccess(e, newS.getRExpr())) {
                    found = true;
                    break;
                }
            }
            for (Expr e : fields) {
//                if (getFirstOfExpr(newS.getRExpr()).equalTo(e)) {
                if (getFirstOfExpr(newS.getRExpr()).equalTo(e)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                newS.setRExpr(mergeExprs(mergeExprs(currentExpr, lexpr), newS.getRExpr()));
            } else {
                newS.setRExpr(mergeExprs(mergeExprs(currentExpr, getFrontOfExpr(lexpr)), newS.getRExpr()));
            }

            // Determines if the share may have additional expressions that may be equal to the lexpr
            boolean analyseEqual = false;
            if (!newS.getLExpr().equalTo(newS.getRExpr())) {
                if (newS.getLExpr() instanceof FieldAccess || newS.getLExpr() instanceof ArrayElement) {
                    analyseEqual = true;
                }
            }

//            System.out.println("Analysing share : " + newS.getShareString());
            if (analyseEqual) {
                equalExpr.add(newS.getLExpr());
                ArrayList<Expr> temp = new ArrayList<Expr>(0);                
                for (Share existingS : existingShareRel.getShares()) {
//                    System.out.println("  Comparing against : " + existingS.getShareString());
                    if (getFrontOfExpr(newS.getLExpr()).equalTo(existingS.getLExpr()) && !existingS.getLExpr().equalTo(existingS.getRExpr())) {
//                        System.out.println("MethodCall - LE : " + getFrontOfExpr(newS.getLExpr()).getExpressionString() + " is equal to " + existingS.getRExpr().getExpressionString());
//                        temp.add(existingS.getRExpr());
                        addToEqualExprs(temp, existingS.getRExpr());
                    } else if (getFrontOfExpr(newS.getLExpr()).equalTo(existingS.getRExpr()) && !existingS.getLExpr().equalTo(existingS.getRExpr())) {
//                        System.out.println("MethodCall - LE : " + getFrontOfExpr(newS.getLExpr()).getExpressionString() + " is equal to " + existingS.getLExpr().getExpressionString());
//                        temp.add(existingS.getLExpr());
                        addToEqualExprs(temp, existingS.getLExpr());
                    }
                }
                equalExprs.add(temp);
            }


        }

//        for (Share newS : newShareRel.getShares()) {
//            if (newS.getLExpr() != null) {
//                existingShareRel.removeExprFromShares(newS.getLExpr());
//            }
//        }

        for (Share newS : newShareRel.getShares()) {
            if (newS.getLExpr() != null && newS.getRExpr() != null) {
                Share updatedShare = new Share(newS.getLExpr(), newS.getRExpr());
                existingShareRel.addShare(updatedShare);
            }
            for (int i = 0; i < equalExpr.size(); i++) {
                if (equalExpr.get(i).equalTo(newS.getLExpr())) {
                    for (Expr e : equalExprs.get(i)) {
                        Share newShare = new Share(newS.getLExpr(), e);
                        existingShareRel.addShare(newShare);
//                        newShare = new Share(e, newS.getRExpr());
//                        existingShareRel.addShare(newShare);
                        newShare = new Share(sortShareAssignmentExpression(e, newS.getRExpr(), newS.getLExpr()), newS.getRExpr());
//                        System.out.println("Applying method properties, adding share : " + newShare.getShareString());
                        existingShareRel.addShare(newShare);
                    }
                }
                break;
            }
        }

        for (MethodRefMapping mrp : newRefChange.getMethodRefs()) {
            boolean found = false;
            for (Expr e : args) {
//                if (getFirstOfExpr(mrp.getExpr()).equalTo(e)) {
                if (exprPrefixOfFieldAccess(e, mrp.getExpr())) {
                    found = true;
                    break;
                }
            }
            for (Expr e : fields) {
//                if (getFirstOfExpr(mrp.getExpr()).equalTo(e)) {
                if (exprPrefixOfFieldAccess(e, mrp.getExpr())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                mrp.setExpr(mergeExprs(mergeExprs(currentExpr, lexpr), mrp.getExpr()));
            } else {
                mrp.setExpr(mergeExprs(mergeExprs(currentExpr, getFrontOfExpr(lexpr)), mrp.getExpr()));
            }
            for (MetaRefCon mrc : mrp.getMetaRefCons()) {
                if (mrc instanceof Erc) {
                    Erc erc = (Erc) mrc;
                    found = false;
                    for (Expr e : args) {
//                        if (getFirstOfExpr(erc.getExpr()).equalTo(e)) {
                        if (exprPrefixOfFieldAccess(e, erc.getExpr())) {
                            found = true;
                            break;
                        }
                    }
                    for (Expr e : fields) {
//                        if (getFirstOfExpr(erc.getExpr()).equalTo(e)) {
                        if (exprPrefixOfFieldAccess(e, erc.getExpr())) {
                            found = true;
                            break;
                        }
                    }
                    if (!found && erc.getExpr() instanceof This) {
                        if (currentExpr != null || lexpr != null) {
                            erc.setExpr(mergeExprs(currentExpr, lexpr));
                        }
                    } else if (!found) {
                        erc.setExpr(mergeExprs(mergeExprs(currentExpr, lexpr), erc.getExpr()));
                    } else {
                        erc.setExpr(mergeExprs(mergeExprs(currentExpr, getFrontOfExpr(lexpr)), erc.getExpr()));
                    }
                }
            }
        }

        if (print) {
            System.out.println("To Apply Updated:");
            MethodPropertiesBuilder.printMethodProperties(toApply);
        }


//        for (MethodRefMapping mrp : newRefChange.getMethodRefs()) {
//            if (mrp.getExpr() != null) {
//                existingRefChange.removeExprFromRefSet(mrp.getExpr());
//            }
//        }

        for (MethodRefMapping mrp : newRefChange.getMethodRefs()) {
            if (mrp.getExpr() != null) {
                HashSet<RefCon> updatedRefCons = new HashSet<RefCon>(0);
                for (MetaRefCon mrc : mrp.getMetaRefCons()) {
                    updatedRefCons.addAll(getRefConsFromMRC(mrc, currentRC, env, currentExpr));
                }
//                RefMapping updatedRefMapping = new RefMapping(mrp.getExpr(), updatedRefCons);
//                existingRefChange.updateRefMapping(updatedRefMapping);
                existingRefChange.updateRefMapping(mrp.getExpr(), updatedRefCons);
                for (int i = 0; i < equalExpr.size(); i++) {
                    if (equalExpr.get(i).equalTo(mrp.getExpr())) {
                        for (Expr e : equalExprs.get(i)) {
//                            existingRefChange.removeExprFromRefSet(e);
//                            existingRefChange.updateRefMapping(new RefMapping(e, updatedRefCons));
                            existingRefChange.updateRefMapping(e, updatedRefCons);
                        }
                        break;
                    }
                }
            }
        }

        // Add an empty entry to the refset for each share component if one doesn't exist.
        for (Share s : newShareRel.getShares()) {
            Expr e = s.getLExpr();
            boolean found = false;
            for (RefMapping mrp : existingRefChange.getRefSet()) {
                if (mrp.getExpr().equalTo(e)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                HashSet<RefCon> refCons = new HashSet<RefCon>(0);
                RefMapping newRefMapping = new RefMapping(e, refCons);
                existingRefChange.updateRefMapping(newRefMapping);
            }
/*
            e = s.getRExpr();
            found = false;
            for (RefMapping mrp : existingRefChange.getRefSet()) {
                if (mrp.getExpr().equalTo(e)) {
                    found = true;
                }
            }
            if (!found) {
                HashSet<RefCon> refCons = new HashSet<RefCon>(0);
                RefMapping newRefMapping = new MethodRefMapping(e, refCons);
                existingRefChange.updateRefMapping(newRefMapping);
            }
*/
        }

        HashSet<Share> sharesToAdd = new HashSet<Share>(0);
        HashSet<RefMapping> refsToUpdate = new HashSet<RefMapping>(0);
        for (Share s1 : newShareRel.getShares()) {
//            System.out.println("Analysing new share " + s1.getShareString());
            // If the lhs and rhs of the share are different:
            if (!s1.getLExpr().equalTo(s1.getRExpr())) {
                // Find the corresponding expr in the equalExpr list:
                for (int i = 0; i < equalExpr.size(); i++) {
                    if (equalExpr.get(i).equalTo(s1.getLExpr())) {
                        // If found, add it to the list of expressions that it is equal to
//                        equalExprs.get(i).add(s1.getLExpr());
                        addToEqualExprs(equalExprs.get(i), s1.getLExpr());
                        // Iterate over all the expressions that reference the same object as the lhs of the new share:
                        for (Expr e : equalExprs.get(i)) {
//                            System.out.println("  - Analysing for expr " + e.getExpressionString() + ", which is equal to " + s1.getLExpr().getExpressionString());
                            // Analyse all other existing shares:
                            for (Share s2 : existingShareRel.getShares()) {
//                                System.out.println("    - Analysing existing share " + s2.getShareString());
                                // If the existing share is a prefix of the new share:
                                if (exprPrefixOfFieldAccess(s1.getRExpr(), s2.getLExpr())) {
                                    // Get the new left expression:
                                    Expr newLhs = sortShareAssignmentExpression(e, s1.getRExpr(), s2.getLExpr());
                                    if (newLhs != null) {
//                                        System.out.println("      - Existing is a prefix of new, newLhs = " + newLhs.getExpressionString());
                                        sharesToAdd.add(new Share(newLhs, s2.getRExpr()));
//                                        sharesToAdd.add(new Share(s2.getRExpr(), newLhs));
                                        RefMapping mapping = existingRefChange.getRefMapping(s2.getRExpr());
                                        RefMapping newMapping;
                                        if (mapping != null) {
                                            newMapping = new RefMapping(newLhs, mapping.getRefCons());
                                        } else {
                                            HashSet<RefCon> rexprRcs = new HashSet<RefCon>(0);
                                            newMapping = new RefMapping(newLhs, rexprRcs);
                                        }
                                        if (!newLhs.equalTo(s1.getLExpr())) {
                                            sharesToAdd.add(new Share(newLhs, newLhs));
                                            refsToUpdate.add(newMapping);
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
            } else {
                // NewShare is the same on both sides (ie. an instantiation or declaration)
                // TODO - Also check that when updating the method properties with exprs that are to be removed that we don't remove information that was there before hand - we don't handle this properly in the implementation, but this is fine, because in the coding practice it states that you shouldn't reassign parameters in methods.
                for (Share s2 : existingShareRel.getShares()) {
//                    System.out.println("    - Analysing existing share " + s2.getShareString());
                    // If the existing share is a prefix of the new share:
                    Expr newField = null;
                    if (exprPrefixOfFieldAccess(s2.getLExpr(), s1.getLExpr())) {
//                        System.out.println("      - Existing share lhs " + s2.getLExpr().getExpressionString() + " is a prefix of new " + s1.getLExpr().getExpressionString());
                        newField = sortShareAssignmentExpression(s2.getRExpr(), s2.getLExpr(), s1.getLExpr());
//                        System.out.println("        - newExpr = " + newField.getExpressionString());
                    } else if (exprPrefixOfFieldAccess(s2.getRExpr(), s1.getLExpr())) {
//                        System.out.println("      - Existing share rhs " + s2.getRExpr().getExpressionString() + " is a prefix of new " + s1.getLExpr().getExpressionString());
                        newField = sortShareAssignmentExpression(s2.getLExpr(), s2.getRExpr(), s1.getLExpr());
//                        System.out.println("        - newExpr = " + newField.getExpressionString());
                    }
                    if (newField != null) {

                        sharesToAdd.add(new Share(newField, newField));
                        sharesToAdd.add(new Share(s1.getLExpr(), newField));

                        RefMapping mapping = existingRefChange.getRefMapping(s1.getLExpr());
                        RefMapping newMapping;
                        if (mapping != null) {
                            newMapping = new RefMapping(newField, mapping.getRefCons());
                            refsToUpdate.add(newMapping);
                        } else {
                            HashSet<RefCon> rexprRcs = new HashSet<RefCon>(0);
                            newMapping = new RefMapping(newField, rexprRcs);
                            refsToUpdate.add(newMapping);
                        }
//                        if (!newField.equalTo(s1.getLExpr())) {
//                            sharesToAdd.add(new Share(newField, newField));
//                            refsToAdd.add(newMapping);
//                        }
                    }
                }
            }
        }
        existingShareRel.addShares(sharesToAdd);
        existingRefChange.updateRefMappings(refsToUpdate);

        if (print) {
            System.out.println("Env Updated:");
            printEnv(env);
        }

    }


    public static void analyseGetMemoryArea(Environment env, GetMemoryArea getMemoryArea, Expr currentExpr) {
        Expr ref = getMemoryArea.getRef();
        if (ref != null) {
            Expr expr = getMemoryArea.getExpr();
            Expr newRef = mergeExprs(currentExpr, ref);
            Expr newExpr;
            if (expr instanceof This) {
                newExpr = currentExpr;
            } else {
                newExpr = mergeExprs(currentExpr, expr);
            }
            Erc erc = new Erc(newExpr);
//            for (EnvEntry entry : env.getEnv()) {
//                ShareRelation shareRel = env.getShareRelation();
//                shareRel.removeExprFromShares(newRef);
//                shareRel.addShare(new Share(newRef, newRef));

                RefSet refChange = env.getRefSet();
//                refChange.removeExprFromRefSet(newRef);
                HashSet<RefCon> refCons = getRefConsFromMRC(erc, null, env, currentExpr);
//                RefMapping refMapping = new RefMapping(newRef, refCons);
//                refChange.updateRefMapping(refMapping);
                refChange.updateRefMapping(newRef, refCons);
//            }
        }
    }


    public static Expr getFrontOfExpr(Expr e) {
        if (e instanceof FieldAccess) {
            FieldAccess fa = (FieldAccess) e;
            FieldAccess result = new FieldAccess();
            result.addElements(fa.getFront());
            return result;
        } else {
            return null;
        }
    }

    public static Expr getFirstOfExpr(Expr e) {
        if (e instanceof FieldAccess) {
            FieldAccess fa = (FieldAccess) e;
            return fa.getFirst();
        } else {
            return e;
        }
    }


    ///////////////////////////////////////////////
    ////           Add / Remove / Join         ////
    ///////////////////////////////////////////////


    public static void envRemove(Environment env, Expr expr) {
        envShareRelationRemove(env, expr);
        envRefSetRemove(env, expr);
    }

    public static void envShareRelationRemove(Environment env, Expr expr) {
//        for (EnvEntry entry : env.getEnv()) {
            ShareRelation shareRelation = env.getShareRelation();
            HashSet<Expr> matchingExprs = shareRelation.matchingExprsInShareRelation(expr);
            for (Expr e : matchingExprs) {
                shareRelation.removeExprFromShares(e);
            }
//        }
    }

    public static void envRefSetRemove(Environment env, Expr expr) {
//        for (EnvEntry entry : env.getEnv()) {
            RefSet refSet = env.getRefSet();
            HashSet<Expr> matchingExprs = refSet.matchingExprsInRefSet(expr);
            for (Expr e : matchingExprs) {
                refSet.removeExprFromRefSet(e);
            }
//        }
    }


    private static void distEnvJoin(Environment env, ArrayList<Command> coms, Expr currentExpr, RefCon currentRC) {
//        ArrayList<Environment> envList = new ArrayList<Environment>(0);
        for (Command c : coms) {
//            Environment clone = env.clone();
            analyseCommand(env, c, currentExpr, currentRC);
//            envList.add(clone);
        }
//        Environment result = envList.get(0);
//        for (int i = 1; i < envList.size(); i++) {
//            result = envJoin(result, envList.get(i));
//        }
//        env.setEnvEntrys(result.getEnv());
    }



    private static Environment envJoin(Environment env1, Environment env2) {
        Environment result = env1;
        result.getShareRelation().addShares(env2.getShareRelation().getShares());
        result.getRefSet().updateRefMappings(env2.getRefSet().getRefSet());
        return result;
    }

/*
    private static Environment envJoin(Environment env1, Environment env2) {
        Environment result = new Environment();
        if (env1.equalTo(env2) || env2.isEmpty()) {
            result = env1;
        } else if (env1.isEmpty()) {
            result = env2;
        } else {
            ArrayList<EnvEntry> joinedEntrys = new ArrayList<EnvEntry>(0);
            for (EnvEntry entry1 : env1.getEnv()) {
                ArrayList<EnvEntry> toMergeEntrys = new ArrayList<EnvEntry>(0);
                for (EnvEntry entry2 : env2.getEnv()) {
                    if (entry1.getShareRelation().canMergeWith(entry2.getShareRelation())) {
                        toMergeEntrys.add(entry2);
                        joinedEntrys.add(entry2);
                    }
                }
                if (toMergeEntrys.size() > 0) {
                    ArrayList<RefSet> refSets = new ArrayList<RefSet>(0);
                    refSets.add(entry1.getRefSet());
                    for (EnvEntry entry2 : toMergeEntrys) {
                        refSets.add(entry2.getRefSet());
                        entry1.getShareRelation().mergeWith(entry2.getShareRelation());
                    }
                    RefSet refSet = distRefSetJoin(refSets);
                    entry1.setRefSet(refSet);
                }
            }
            for (EnvEntry entry2 : joinedEntrys) {
                env2.removeEnvEntry(entry2);
            }
            for (EnvEntry entry2 : env2.getEnv()) {
                env1.addEnvEntry(entry2);
            }
            result = env1;
        }
        return result;
    }
*/

    public static RefSet distRefSetJoin(ArrayList<RefSet> refSets) {
        RefSet temp;
        if (refSets.size() > 0) {
            temp = refSets.get(0);
            for (int i = 1; i < refSets.size(); i++) {
                temp = refSetJoin(temp, refSets.get(i));
            }
        } else {
            System.out.println("[ERROR] - No RefSets to join in funcion DistRefSetJoin");
            temp = null;
        }
        return temp;
    }

    public static RefSet refSetJoin(RefSet rs1, RefSet rs2) {
        HashSet<Expr> allExprs = rs1.getExprs();
        allExprs.addAll(rs2.getExprs());
        RefSet result = new RefSet();
        for (Expr e : allExprs) {
            HashSet<RefCon> rcs1 = new HashSet<RefCon>(0);
            if (rs1.getRefMapping(e) != null) {
                rcs1 = rs1.getRefMapping(e).getRefCons();
            }
            HashSet<RefCon> rcs2 = new HashSet<RefCon>(0);
            if (rs2.getRefMapping(e) != null) {
                rcs2 = rs2.getRefMapping(e).getRefCons();
            }
            HashSet<RefCon> mergedRefCons = mergeRefCons(rcs1, rcs2);
            RefMapping newMapping = new RefMapping(e, mergedRefCons);
            result.updateRefMapping(newMapping);
        }
        return result;
    }

    public static HashSet<RefCon> mergeRefCons(HashSet<RefCon> rcs1, HashSet<RefCon> rcs2) {
        HashSet<RefCon> result = new HashSet<RefCon>(0);
        for (RefCon rc1 : rcs1) {
            boolean match = false;
            for (RefCon rc2 : rcs2) {
                if (rc1.equalTo(rc2)) {
                    match = true;
                    break;
                }
            }
            if (!match) {
                result.add(rc1);
            }
        }
        result.addAll(rcs2);
        return result;
    }



    ///////////////////////////////////////////////
    ////              Expressions              ////
    ///////////////////////////////////////////////

    // Checks to see if e1 is a prefix of e2
    public static boolean exprPrefixOfFieldAccess(Expr e1, Expr e2) {
//        System.out.println("Checking to see if " + e1.getExpressionString() + " is a prefix of " + e2.getExpressionString());
        boolean result = true;
        if (!e1.equalTo(e2)) {
            if (e1 == null || e1 instanceof This || e1 instanceof Val || e2 instanceof Val) {
                result = false;
            } else if (e1 instanceof Identifier) {
                if (e2 instanceof FieldAccess) {
                    FieldAccess expr = (FieldAccess) e2;
                    if (!e1.equalTo(expr.getFirst())) {
                        result = false;
                    }
                } else if (e2 instanceof Identifier) {
                    result = false;
                }
            } else if (e1 instanceof FieldAccess) {
                FieldAccess expr1 = (FieldAccess) e1;
                if (e2 instanceof FieldAccess) {
                    FieldAccess expr2 = (FieldAccess) e2;
                    if (expr2.getElements().size() >= expr1.getElements().size()) {
                        for (int i = 0; i < expr1.getElements().size(); i++) {
                            if (!expr1.getElements().get(i).equalTo(expr2.getElements().get(i))) {
                                result = false;
                            }
                        }
                    } else {
                        result = false;
                    }
                } else if (e2 instanceof Identifier) {
                    result = false;
                }
            }
        }
        return result;
    }

    public static Expr sortShareAssignmentExpression(Expr newLExpr, Expr newRExpr, Expr shareExpr) {
        Expr result = null;
        if (newRExpr instanceof Val || shareExpr instanceof Val) {
            return newLExpr;
        } else if (shareExpr instanceof FieldAccess && newRExpr instanceof FieldAccess) {
            FieldAccess fa1 = (FieldAccess) shareExpr;
            FieldAccess fa2 = (FieldAccess) newRExpr;
            result = new FieldAccess();
            if (newLExpr instanceof Val) {
                return newLExpr;
            } else if (newLExpr instanceof Identifier) {
                Identifier iden = (Identifier) newLExpr;
                ((FieldAccess) result).addElement(iden);
            } else if (newLExpr instanceof FieldAccess) {
                FieldAccess fa3 = (FieldAccess) newLExpr;
                ((FieldAccess) result).addElements(fa3.getElements());
            }
            for (int i = fa2.getElements().size(); i < fa1.getElements().size(); i++) {
                ((FieldAccess) result).addElement(fa1.getElements().get(i));
            }
            return result;
//        } else if (shareExpr instanceof FieldAccess && newRExpr instanceof Identifier) {
        } else if (shareExpr instanceof FieldAccess && !(newRExpr instanceof FieldAccess)) {
            FieldAccess fa1 = (FieldAccess) shareExpr;
            result = new FieldAccess();
            if (newLExpr instanceof Val) {
                return newLExpr;
            } else if (newLExpr instanceof Identifier) {
                Identifier iden = (Identifier) newLExpr;
                ((FieldAccess) result).addElement(iden);
            } else if (newLExpr instanceof FieldAccess) {
                FieldAccess fa2 = (FieldAccess) newLExpr;
                ((FieldAccess) result).addElements(fa2.getElements());
            }
            for (int i = 1; i < fa1.getElements().size(); i++) {
                ((FieldAccess) result).addElement(fa1.getElements().get(i));
            }
            return result;
        } else if (shareExpr instanceof Identifier && newRExpr instanceof FieldAccess) {
            return newLExpr;
        } else if (shareExpr instanceof Identifier && newRExpr instanceof Identifier) {
            return newLExpr;
        } else {
            return result;
        }
    }

    public static Expr mergeExprs(Expr e1, Expr e2) {
//        if (e1 != null && e2 != null) {
//            System.out.println("Merging expression " + e1.getExpressionString() + " with expression " + e2.getExpressionString());
//        }
        if (e2 == null) {
            return e1;
        } else if (e1 == null || e1.getExpressionString().equals("super") || e2 instanceof Null || e2 instanceof Val || e2.getExpressionString().equals("Result") || e2 instanceof This) {
            return e2;
        } else if (SCJmSafeChecker.STATICVARS.contains(e2.getExpressionString())) {
            return e2;
        } else if (e1 instanceof FieldAccess) {
            FieldAccess e3 = (FieldAccess) e1;
            FieldAccess result = new FieldAccess();
            result.addElements(e3.getElements());
            if (e2 instanceof FieldAccess) {
                FieldAccess e4 = (FieldAccess) e2;
                result.addElements(e4.getElements());
                return result;
            } else if (e2 instanceof ArrayElement) {
                Identifier e4 = (ArrayElement) e2;
                result.addElement(e4);
                return result;
            } else if (e2 instanceof Variable) {
                Identifier e4 = (Variable) e2;
                result.addElement(e4);
                return result;
            } else {
                System.out.println("[ERROR] - Failed to merge expressions. RHExpr = " + e2.toString());
                return null;
            }
        } else if (e1 instanceof Variable) {
            Variable e3 = (Variable) e1;
            FieldAccess result = new FieldAccess();
            result.addElement(e3);
            if (e2 instanceof FieldAccess) {
                FieldAccess e4 = (FieldAccess) e2;
                result.addElements(e4.getElements());
                return result;
            } else if (e2 instanceof ArrayElement) {
                Identifier e4 = (ArrayElement) e2;
                result.addElement(e4);
                return result;
            } else if (e2 instanceof Variable) {
                Identifier e4 = (Variable) e2;
                result.addElement(e4);
                return result;
            } else {
                System.out.println("[ERROR] - Failed to merge expressions " + e1.getExpressionString() + " and " + e2.getExpressionString());
                return null;
            }
        } else if (e1 instanceof ArrayElement) {
            ArrayElement e3 = (ArrayElement) e1;
            FieldAccess result = new FieldAccess();
            result.addElement(e3);
            if (e2 instanceof FieldAccess) {
                FieldAccess e4 = (FieldAccess) e2;
                result.addElements(e4.getElements());
                return result;
            } else if (e2 instanceof ArrayElement) {
                Identifier e4 = (ArrayElement) e2;
                result.addElement(e4);
                return result;
            } else if (e2 instanceof Variable) {
                Identifier e4 = (Variable) e2;
                result.addElement(e4);
                return result;
            } else {
                System.out.println("[ERROR] - Failed to merge expressions 3");
                return null;
            }
        } else {
            System.out.println("[ERROR] - Failed to merge expressions 4");
            return null;
        }
    }


    ///////////////////////////////////////////////
    ////                RefCons                ////
    ///////////////////////////////////////////////

    public static HashSet<RefCon> getRefConsFromMRC(MetaRefCon mrc, RefCon rc, Environment env, Expr currentExpr) {
        HashSet<RefCon> result = new HashSet<RefCon>(0);
        if (mrc instanceof Current) {
            result.add(rc);

        } else if (mrc instanceof CurrentPrivate) {
            CurrentPrivate currentPriv = (CurrentPrivate) mrc;
            int depth = currentPriv.getDepth();
            RefCon temp = rc;
            for (int i = depth; i >= 0; i--) {
                temp = lowerRefCon(temp);
            }
            result.add(temp);

        } else if (mrc instanceof CurrentPlus) {
            CurrentPlus currentPlus = (CurrentPlus) mrc;
            int depth = currentPlus.getDepth();
            RefCon temp = rc;
            for (int i = depth; i >= 0; i--) {
                temp = raiseRefCon(temp);
            }
            result.add(temp);

        } else if (mrc instanceof Erc) {
            Erc erc = (Erc) mrc;
            Expr e = erc.getExpr();
            if (e instanceof This) {
                e = currentExpr;
            }
            RefSet refSet = env.getRefSet();
            RefMapping mapping = refSet.getRefMapping(e);
            if (mapping != null) {
                result = mapping.getRefCons();
            } else {
                if (e instanceof Val) {
                    result.add(new Prim());
                } else {
//                    System.out.println("[ERROR] - Unable to find reference context for MetaRefCon Erc " + e.getExpressionString());
                }
            }

        } else if (mrc instanceof Rcs) {
            Rcs rcs = (Rcs) mrc;
            result.addAll(rcs.getRefCons());

        } else {
            System.out.println("[ERROR] - RefCon set cannot be determined from MetaRefCon");
        }
        return result;
    }


    public static RefCon lowerRefCon(RefCon currentRC) {

        RefCon result;

        if (currentRC instanceof MMem) {
            result = new TPMMem(0);

        } else if (currentRC instanceof TPMMem) {
            TPMMem tpMMem = (TPMMem) currentRC;
            int depth = tpMMem.getDepth();
            result = new TPMMem(depth + 1);

        } else if (currentRC instanceof PRMem) {
            PRMem prMem = (PRMem) currentRC;
            result = new TPMem(prMem.getHandler(), 0);

        } else if (currentRC instanceof TPMem) {
            TPMem tpMem = (TPMem) currentRC;
            result = new TPMem(tpMem.getHandler(), tpMem.getDepth() + 1);

        } else {
            result = null;
        }
        return result;
    }

    public static RefCon raiseRefCon(RefCon currentRC) {

        RefCon result;

        if (currentRC instanceof MMem) {
            result = new IMem();

        } else if (currentRC instanceof TPMMem) {
            TPMMem tpMMem = (TPMMem) currentRC;
            int depth = tpMMem.getDepth();
            if (depth > 0) {
                result = new TPMMem(depth - 1);
            } else {
                result = new MMem();
            }

        } else if (currentRC instanceof PRMem) {
            result = new MMem();

        } else if (currentRC instanceof TPMem) {
            TPMem tpMem = (TPMem) currentRC;
            int depth = tpMem.getDepth();
            if (depth > 0) {
                result = new TPMem(tpMem.getHandler(), depth - 1);
            } else {
                result = new PRMem(tpMem.getHandler());
            }

        } else {
            result = null;
        }
        return result;
    }











    ///////////////////////////////////////////////
    ////               Printing                ////
    ///////////////////////////////////////////////

    public static void printEnv(Environment env) {
        System.out.println("Environment print-out....");
        System.out.println("  Env:");
        ShareRelation shareRel = env.getShareRelation();
        RefSet refSet = env.getRefSet();
        shareRel.printShareRel();
        System.out.println("      -> ");
        refSet.printRefSet();
        System.out.println();
    }




    ///////////////////////////////////////////////
    ////             MSafeProgram              ////
    ///////////////////////////////////////////////

    public static MSafeClass findMSafeClass(String name) {
        HashSet<MSafeClass> classes = SCJmSafeChecker.SCJmSafePROGRAM.getClasses();
        MSafeClass result = null;
        for (MSafeClass c : classes) {
            if (c.getName().equals(name)) {
                result = c;
                break;
            }
        }
        return result;
    }


}

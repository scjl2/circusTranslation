package hijac.tools.scjmsafe.translation;

import hijac.tools.analysis.SCJAnalysis;

import hijac.tools.scjmsafe.language.*;
import hijac.tools.scjmsafe.language.Commands.*;
import hijac.tools.scjmsafe.language.Expressions.*;
import hijac.tools.scjmsafe.language.RefCon.*;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import com.sun.source.tree.*;
import com.sun.source.util.*;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.Modifier;



// Variable visitor specifically designed to extract the command sequence from a variable declaration
class VariableVisitor extends SimpleTreeVisitor<Command, Void> {

        @Override
        public Command visitVariable(VariableTree vt, Void p) {
            Set<Modifier> flags = vt.getModifiers().getFlags();
            ExpressionTree init = vt.getInitializer();
            Tree type = vt.getType();
            String typeString = type.toString();
            String decName = vt.getName().toString();
            Declaration dec = Util.getDeclaration(decName, type);
            Variable var = new Variable(decName, typeString);
            if (!typeString.equals(Util.getArrayTypeName(typeString))) {
//                typeString = Util.getArrayTypeName(typeString);
                var.getVarType().setArray();
                var.setType(typeString);
//                System.out.println("Setting array variable " + decName + " with type " + typeString);
            }

            if (flags.contains(Modifier.STATIC)) {
                MSafeProgram.addStaticDec(dec);
            }

            // If the declaration has an initialisation command:
            if (init == null) {
                Util.recordVariable(var);
                return dec;
            }

            if (init instanceof TypeCastTree) {
                TypeCastTree typeCast = (TypeCastTree) init;
                init = typeCast.getExpression();
            }

            Command initCommand;

            if (init instanceof NewArrayTree) {
                NewArrayTree array = (NewArrayTree) init;
//                String typeName = Util.getArrayTypeName(type.toString());
//                System.out.println("Array type is : " + type.toString());
//                var.setType(typeName);
//                var.setArray();
                initCommand = Util.getArrayNewInstance(var, array, typeString);

            } else if (init instanceof NewClassTree) {
                NewClassTree classTree = (NewClassTree) init;
                String cid = classTree.getIdentifier().toString();
                var.setType(cid);
                var.getVarType().setReference();

                MetaRefCon mrc = new Current();
                @SuppressWarnings("unchecked")
                List<ExpressionTree> args = (List<ExpressionTree>) classTree.getArguments();
                ArrayList<Expr> classArgs = new ArrayList<Expr>(0);

                NewInstance newInstance = new NewInstance(var, mrc, var.getVarType(), classArgs);
                ArrayList<Command> paramSequence = Util.extractParamCommand(args, classArgs);
                initCommand = Util.mergeSideEffectParamCommand(newInstance, paramSequence, new Skip());


            } else if (init instanceof MethodInvocationTree) {
                Command c1 = init.accept(new ExpressionVisitor(), null);

                if (c1 instanceof MethodCall) {
                    MethodCall methodCall = (MethodCall) c1;
                    ExprComPair pair = Util.fixUnknownMethodCallReturnDec(methodCall, c1);
                    Expr rexpr = pair.getExpr();
                    Assignment assignment = new Assignment(var, rexpr);
                    initCommand = new Sequence(pair.getCom(), assignment);

                } else if (c1 instanceof Sequence) {
                    Sequence seq = (Sequence) c1;
                    if (seq.getC2() instanceof MethodCall) {
                        MethodCall methodCall = (MethodCall) seq.getC2();
                        ExprComPair pair = Util.fixUnknownMethodCallReturnDec(methodCall, c1);
                        Expr rexpr = pair.getExpr();
                        Assignment assignment = new Assignment(var, rexpr);
                        initCommand = new Sequence(pair.getCom(), assignment);

                    } else if (seq.getC2() instanceof NewInstance) {
                        NewInstance newInstance = (NewInstance) seq.getC2();
                        newInstance.setExpr(var);
                        initCommand = new Sequence(seq.getC1(), newInstance);

                    } else if (seq.getC2() instanceof GetMemoryArea) {
                        GetMemoryArea getMemoryArea = (GetMemoryArea) seq.getC2();
                        getMemoryArea.setRef(var);
                        initCommand = new Sequence(seq.getC1(), getMemoryArea);

                    } else {
                        System.out.println("[ERROR] - Unable to extract method call from sequence in declaration");
                        initCommand = new Skip();
                    }

                } else if (c1 instanceof NewInstance) {
                    NewInstance newInstance = (NewInstance) c1;
                    newInstance.setExpr(var);
                    initCommand = newInstance;

                } else if (c1 instanceof GetMemoryArea) {
                    GetMemoryArea getMemoryArea = (GetMemoryArea) c1;
                    getMemoryArea.setRef(var);
                    initCommand = getMemoryArea;

                } else {
                    System.out.println("[ERROR] - Method call in declaration of variable " + decName + " is not of type MethodCall or Sequence");
                    initCommand = new Skip();
                }

            } else if (init instanceof ArrayAccessTree) {
                ArrayAccessTree arrayAccess = (ArrayAccessTree) init;
                ExpressionTree arrayExpr = arrayAccess.getExpression();
                Expr rexpr;
                Command c1;
                if (arrayExpr instanceof MethodInvocationTree) {
                    c1 = arrayExpr.accept(new ExpressionVisitor(), null);
                    if (c1 instanceof MethodCall) {
                        MethodCall methodCall = (MethodCall) c1;
                        ExprComPair pair = Util.fixUnknownMethodCallReturnDec(methodCall, c1);
                        rexpr = pair.getExpr();
                        c1 = pair.getCom();
                    } else if (c1 instanceof Sequence) {
                        Sequence seq = (Sequence) c1;
                        MethodCall methodCall = (MethodCall) seq.getC2();
                        ExprComPair pair = Util.fixUnknownMethodCallReturnDec(methodCall, seq);
                        rexpr = pair.getExpr();
                        c1 = pair.getCom();
                    } else {
                        System.out.println("[ERROR] - Method call in declaration of variable " + decName + " is not of type MethodCall or Sequence");
                        rexpr = new OtherExpr();
                    }
                } else {
                    c1 = arrayExpr.accept(new ExpressionVisitor(), null);
                    rexpr = Util.extractExpression(arrayAccess);
                }
                Command c2 = arrayAccess.getIndex().accept(new ExpressionVisitor(), null);
                Command sideEffect = Util.simplifyCommandPair(c1, c2);
                Assignment assignment = new Assignment(var, rexpr);
                if (sideEffect instanceof Skip) {
                    initCommand = assignment;
                } else {
                    initCommand = Util.simplifyCommandPair(sideEffect, assignment);
                }

            } else {
                // Initialisation of a delcaration that is not a new instance or method call
                Command sideEffect = init.accept(new ExpressionVisitor(), null);
                Expr rhs = Util.extractExpression(init);
                Assignment ass = new Assignment(var, rhs);
                if (sideEffect instanceof Skip) {
                    initCommand = ass;
                } else if (sideEffect instanceof MethodCall) {
                    MethodCall methodCall =  (MethodCall) sideEffect;
                    methodCall.addArg(var);
                    initCommand = methodCall;
                } else {
                    initCommand = new Sequence(sideEffect, ass);
                }
            }

            Util.recordVariable(var);

            if (flags.contains(Modifier.STATIC)) {
                MSafeProgram.addStaticInit(initCommand);
                return new Skip();
            } else {
                return new Sequence(dec, initCommand);
            }

        }

        @Override
        public Command defaultAction(Tree t, Void p) {
            Skip skip = new Skip();
            return skip;
        }

}



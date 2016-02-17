package hijac.tools.scjmsafe.translation;

import hijac.tools.analysis.SCJAnalysis;

import hijac.tools.scjmsafe.language.*;
import hijac.tools.scjmsafe.language.Commands.*;
import hijac.tools.scjmsafe.language.Expressions.*;
import hijac.tools.scjmsafe.language.RefCon.*;

import hijac.tools.scjmsafe.language.Method.MethodSig;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import com.sun.source.tree.*;
import com.sun.source.util.*;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.Modifier;


// Expression visitor that returns a command based on the expression found in SCJ
class ExpressionVisitor extends SimpleTreeVisitor<Command, Void> {

        @Override
        public Command visitExpressionStatement(ExpressionStatementTree tree, Void p) {

            Command result;
            ExpressionTree exprTree = tree.getExpression(); 
            result = visitExpression(exprTree);
            return result;
        }

        private Command visitExpression(ExpressionTree exprTree) {
            Command result;
            
            if (exprTree instanceof AnnotationTree) {
 //               System.out.println("AnnotationTree");
                result = new Skip();

            } else if (exprTree instanceof ArrayAccessTree) {
//                System.out.println("ArrayAccessTree");
                ArrayAccessTree expr = (ArrayAccessTree) exprTree;
                Command c1 = expr.getExpression().accept(new ExpressionVisitor(), null);
                Command c2 = expr.getIndex().accept(new ExpressionVisitor(), null);
                result = Util.simplifyCommandPair(c1, c2);

            } else if (exprTree instanceof AssignmentTree) {
                AssignmentTree ass = (AssignmentTree) exprTree;
//                System.out.println("Assignment - LExpr = " + ass.getVariable().toString() + ", RExpr = " + ass.getExpression().toString());
                ExpressionTree lhs = ass.getVariable();
                ExpressionTree rhs = ass.getExpression();
                Expr lexpr = Util.extractExpression(lhs);

                if (rhs instanceof TypeCastTree) {
                    TypeCastTree typeCast = (TypeCastTree) rhs;
                    rhs = typeCast.getExpression();
                }

                if (rhs instanceof UnaryTree) {
                    UnaryTree unaryTree = (UnaryTree) rhs;
                    rhs = unaryTree.getExpression();
                }

//                if (Util.extractExpression(rhs) instanceof Val) {
//                    result = new Skip();    // TODO
//                } else {
                    if (rhs instanceof NewArrayTree) {
                        NewArrayTree array = (NewArrayTree) rhs;
                        String typeName = array.getType().toString();
                        result = Util.getArrayNewInstance(lexpr, array, typeName);

                    } else if (rhs instanceof NewClassTree) {
                        NewClassTree classTree = (NewClassTree) rhs;
                        String cid = classTree.getIdentifier().toString();
                        VarType varType = new VarType();
                        varType.setReference();
                        varType.setTypeName(cid);
                        MetaRefCon mrc = new Current();
                        @SuppressWarnings("unchecked")
                        List<ExpressionTree> args = (List<ExpressionTree>) classTree.getArguments();
                        ArrayList<Expr> classArgs = new ArrayList<Expr>(0);
                        NewInstance newInstance = new NewInstance(lexpr, mrc, varType, classArgs);
                        ArrayList<Command> paramSequence = Util.extractParamCommand(args, classArgs);
                        result = Util.mergeSideEffectParamCommand(newInstance, paramSequence, new Skip());

                    } else if (rhs instanceof MethodInvocationTree) {
                        Command c1 = rhs.accept(new ExpressionVisitor(), null);
                        if (c1 instanceof MethodCall) {
                            MethodCall methodCall = (MethodCall) c1;
                            ExprComPair pair = Util.fixUnknownMethodCallReturnDec(methodCall, c1);
                            Expr rexpr = pair.getExpr();
                            Assignment assignment = new Assignment(lexpr, rexpr);
                            result = new Sequence(pair.getCom(), assignment);

                        } else if (c1 instanceof Sequence) {
                            Sequence seq = (Sequence) c1;
                            if (seq.getC2() instanceof MethodCall) {
                                MethodCall methodCall = (MethodCall) seq.getC2();
                                ExprComPair pair = Util.fixUnknownMethodCallReturnDec(methodCall, seq);
                                Expr rexpr = pair.getExpr();
                                Assignment assignment = new Assignment(lexpr, rexpr);
                                result = new Sequence(pair.getCom(), assignment);   // Despite the fact this is ignoring the first part of the sequence c1, it is fixed again by the fixUnknownMethodCallReturnDec function - not sure this is very pretty but it works for now.
                            } else if (seq.getC2() instanceof NewInstance) {
                                NewInstance newInstance = (NewInstance) seq.getC2();
                                newInstance.setExpr(lexpr);
                                result = new Sequence(seq.getC1(), newInstance);
                            } else if (seq.getC2() instanceof GetMemoryArea) {
                                GetMemoryArea getMemoryArea = (GetMemoryArea) seq.getC2();
                                getMemoryArea.setRef(lexpr);
                                result = new Sequence(seq.getC1(), getMemoryArea);
                            } else {
                                System.out.println("[ERROR] - Unable to extract method call from sequence in assignment");
                                result = new Skip();
                            }

                        } else if (c1 instanceof NewInstance) {
                            NewInstance newInstance = (NewInstance) c1;
                            newInstance.setExpr(lexpr);
                            result = newInstance;

                        } else if (c1 instanceof GetMemoryArea) {
                            GetMemoryArea getMemoryArea = (GetMemoryArea) c1;
                            getMemoryArea.setRef(lexpr);
                            result = getMemoryArea;

                        } else {
                            System.out.println("[ERROR] - Method call in Assignment to " + ass.getVariable().toString() + " is not of type MethodCall or Sequence");
                            result = new Skip();
                        }

                        // TODO - In assignments to method calls, we could check the type of the lhs if the return type of the method cannot be found - this would eliminate the need for "Unknown varX". Obviously this only works if the renaming of duplicates has been completed.


                    } else if (rhs instanceof ArrayAccessTree) {
                        ArrayAccessTree arrayAccess = (ArrayAccessTree) rhs;
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
                                System.out.println("[ERROR] - Method call in Assignment to " + ass.getVariable().toString() + " is not of type MethodCall or Sequence");
                                rexpr = new OtherExpr();
                            }
                        } else {
                            c1 = arrayExpr.accept(new ExpressionVisitor(), null);
                            rexpr = Util.extractExpression(arrayAccess);
                        }
                        Command c2 = arrayAccess.getIndex().accept(new ExpressionVisitor(), null);
                        Command sideEffect = Util.simplifyCommandPair(c1, c2);
                        Assignment assignment = new Assignment(lexpr, rexpr);
                        if (sideEffect instanceof Skip) {
                            result = assignment;
                        } else {
                            result = Util.simplifyCommandPair(sideEffect, assignment);
                        }

                    } else {
                        Expr rexpr = Util.extractExpression(rhs);
                        Command sideEffect = rhs.accept(new ExpressionVisitor(), null);
                        Assignment assignment = new Assignment(lexpr, rexpr);
                        if (sideEffect instanceof Skip) {
                            result = assignment;
                        } else {
                            result = Util.simplifyCommandPair(sideEffect, assignment);
                        }
                    }
//                }

            } else if (exprTree instanceof BinaryTree) {
//                System.out.println("BinaryTree");
                BinaryTree expr = (BinaryTree) exprTree;
                Command c1 = expr.getLeftOperand().accept(new ExpressionVisitor(), null);
                if (c1 instanceof MethodCall) {
                    MethodCall methodCall = (MethodCall) c1;
                    ExprComPair pair = Util.fixUnknownMethodCallReturnDec(methodCall, c1);
                    c1 = pair.getCom();
                }
                Command c2 = expr.getRightOperand().accept(new ExpressionVisitor(), null);
                if (c2 instanceof MethodCall) {
                    MethodCall methodCall = (MethodCall) c2;
                    ExprComPair pair = Util.fixUnknownMethodCallReturnDec(methodCall, c2);
                    c2 = pair.getCom();
                }
                result = Util.simplifyCommandPair(c1, c2);

            } else if (exprTree instanceof CompoundAssignmentTree) {
//                System.out.println("CompoundAssignmentTree");
                CompoundAssignmentTree expr = (CompoundAssignmentTree) exprTree;
                ExpressionTree lhs = expr.getVariable();
                ExpressionTree rhs = expr.getExpression();
                Expr lexpr = Util.extractExpression(lhs);
                Command sideEffects = rhs.accept(new ExpressionVisitor(), null);
                Assignment ass = new Assignment(lexpr, new Val());
                if (sideEffects instanceof Skip) {
                    result = ass;    // TODO
//                    result = new Skip();
                } else {
                    result = new Sequence(sideEffects, ass);
//                    result = sideEffects;
                }

            } else if (exprTree instanceof ConditionalExpressionTree) {
//                System.out.println("ConditionalExpressionTree");
                ConditionalExpressionTree expr = (ConditionalExpressionTree) exprTree;
                ExpressionTree ifExpr = expr.getCondition();
                Expr e1 = Util.extractExpression(ifExpr);
                Command c1 = expr.getTrueExpression().accept(new ExpressionVisitor(), null);
                Command c2 = expr.getFalseExpression().accept(new ExpressionVisitor(), null);
                result = new If(e1, c1, c2);

            } else if (exprTree instanceof ErroneousTree) {
//                System.out.println("ErroneousTree");
                result = new Skip();

            } else if (exprTree instanceof IdentifierTree) {
//                System.out.println("IdentifierTree");
                result = new Skip();

            } else if (exprTree instanceof InstanceOfTree) {
//                System.out.println("InstanceOfTree");
                InstanceOfTree expr = (InstanceOfTree) exprTree;
                result = expr.getExpression().accept(new ExpressionVisitor(), null);

            } else if (exprTree instanceof LiteralTree) {
//                System.out.println("LiteralTree");
                result = new Skip();

            } else if (exprTree instanceof MemberSelectTree) {
//                System.out.println("MemberSelectTree");
                MemberSelectTree expr = (MemberSelectTree) exprTree;
                result = expr.getExpression().accept(new ExpressionVisitor(), null);

            } else if (exprTree instanceof MethodInvocationTree) {
                MethodInvocationTree method = (MethodInvocationTree) exprTree;
                @SuppressWarnings("unchecked") // Casting method.getArguments, which is of type "? extends List<ExpressionTree>", to List<ExpressionTree> presents a [WARNING] at compile time
                List<ExpressionTree> args = (List<ExpressionTree>) method.getArguments();
                ExpressionTree methodExpr = method.getMethodSelect();
                String methodIdentifier = Util.extractMethodName(methodExpr);
//                System.out.println("Method Call: " + methodExpr.toString());


                // This part is used to extract any side effects in the left-hand side of the method call; for example, further method calls.
                Command sideEffect = methodExpr.accept(new ExpressionVisitor(), null);
                Expr lexpr;

                if (sideEffect instanceof MethodCall) {
//                    System.out.println("  Method call side effect found");
                    MethodCall methodCall = (MethodCall) sideEffect;

                    // This is used to catch the [ERROR] where the method has not been identified, but it must have a return type:
                    ExprComPair pair = Util.fixUnknownMethodCallReturnDec(methodCall, sideEffect);
                    lexpr = pair.getExpr();
                    sideEffect = pair.getCom();

                } else if (sideEffect instanceof Sequence) {
//                    System.out.println("  Sequence side effect found");
                    Sequence seq = (Sequence) sideEffect;
                    Command seqC2 = seq.getC2();
                    if (seqC2 instanceof MethodCall) {
                        MethodCall methodCall = (MethodCall) seqC2;
                        ExprComPair pair = Util.fixUnknownMethodCallReturnDec(methodCall, sideEffect);
                        lexpr = pair.getExpr();
                        sideEffect = pair.getCom();

                    } else if (seqC2 instanceof NewInstance) {
                        lexpr = ((NewInstance) seqC2).getExpr();

                    } else {
                        lexpr = Util.extractMethodLExpr(methodExpr);
                    }

                } else {
                    lexpr = Util.extractMethodLExpr(methodExpr);
                    
                }

                // List of arguments of the method call
                ArrayList<Expr> methodArgs = new ArrayList<Expr>(0);

                // List of commands from sideEffects of the arguments of the method call
                ArrayList<Command> paramSequence = Util.extractParamCommand(args, methodArgs);

                if (methodIdentifier.equals("executeInAreaOf")) {
                    Expr areaOf = methodArgs.get(0);
                    Erc erc = new Erc(areaOf);
                    Expr runnableClass = methodArgs.get(1);
                    ArrayList<Expr> runArgs = new ArrayList<Expr>(0);
                    ArrayList<MethodSig> possibleMethods = Util.findMethods(runnableClass, "run", runArgs);
                    MethodCall methodCall = new MethodCall(runnableClass, "run", runArgs);
                    methodCall.addPossibleMethods(possibleMethods);
                    ExecuteInAreaOf executeInAreaOf = new ExecuteInAreaOf(erc, methodCall);
                    result = Util.mergeSideEffectParamCommand(executeInAreaOf, paramSequence, sideEffect);

                } else if (methodIdentifier.equals("executeInOuterArea")) {
                    Expr runnableClass = methodArgs.get(0);
                    ArrayList<Expr> runArgs = new ArrayList<Expr>(0);
                    ArrayList<MethodSig> possibleMethods = Util.findMethods(runnableClass, "run", runArgs);
                    MethodCall methodCall = new MethodCall(runnableClass, "run", runArgs);
                    methodCall.addPossibleMethods(possibleMethods);
                    ExecuteInOuterArea executeInOuterArea = new ExecuteInOuterArea(methodCall);
                    result = Util.mergeSideEffectParamCommand(executeInOuterArea, paramSequence, sideEffect);

                } else if (methodIdentifier.equals("getMemoryArea")) {
                    Expr argument = methodArgs.get(0);
                    GetMemoryArea getMemoryArea = new GetMemoryArea(null, argument);
                    result = Util.mergeSideEffectParamCommand(getMemoryArea, paramSequence, sideEffect);

                } else if (methodIdentifier.equals("newArray")) {
                    Expr newClass = methodArgs.get(0);
                    String newClassString = fixNewInstanceClassName(newClass.getExpressionString());
                    VarType varType = new VarType(newClassString + "[]");
                    varType.setReference();
                    varType.setArray();
                    ArrayList<Expr> classArgs = new ArrayList<Expr>(0);
                    Erc erc = new Erc(lexpr);
                    NewInstance newInstance = new NewInstance(new Null(), erc, varType, classArgs);
                    result = Util.mergeSideEffectParamCommand(newInstance, paramSequence, sideEffect);

                } else if (methodIdentifier.equals("newInstance")) {
                    Expr newClass = methodArgs.get(0);
                    String newClassString = fixNewInstanceClassName(newClass.getExpressionString());
                    VarType varType = new VarType(newClassString);
                    varType.setReference();
                    ArrayList<Expr> classArgs = new ArrayList<Expr>(0);
                    Erc erc = new Erc(lexpr);
                    NewInstance newInstance = new NewInstance(new Null(), erc, varType, classArgs);
                    result = Util.mergeSideEffectParamCommand(newInstance, paramSequence, sideEffect);

                } else if (methodIdentifier.equals("enterPrivateMemory")) {
                    Expr runnableClass = methodArgs.get(1);
                    ArrayList<Expr> runArgs = new ArrayList<Expr>(0);
                    ArrayList<MethodSig> possibleMethods = Util.findMethods(runnableClass, "run", runArgs);
                    MethodCall methodCall = new MethodCall(runnableClass, "run", runArgs);
                    methodCall.addPossibleMethods(possibleMethods);
                    EnterPrivateMemory enterPrivateMemory = new EnterPrivateMemory(methodCall);
                    result = Util.mergeSideEffectParamCommand(enterPrivateMemory, paramSequence, sideEffect);

                } else {
                    
                    // Commands to add a results variable of the correct type to the method call
                    Tree methodReturnType = Util.findMethodType(lexpr, methodIdentifier, methodArgs);
                    ArrayList<MethodSig> possibleMethods = Util.findMethods(lexpr, methodIdentifier, methodArgs);
                    Declaration dec = null;
                    if (methodReturnType != null && !methodReturnType.toString().equals("void")) {
                        String methodVarName = Util.getTempName();
                        Variable methodVar = new Variable(methodVarName, Util.getMethodTypeName(methodReturnType, methodIdentifier));
                        methodVar.getVarType().setResultVar();
                        Util.recordVariable(methodVar);
                        dec = Util.getReturnTypeDec(methodVarName, methodReturnType, methodIdentifier);
                        methodArgs.add(methodVar);
                    }

                    // MethodCall object to represent method call
                    MethodCall methodCall = new MethodCall(lexpr, methodIdentifier, methodArgs);
                    methodCall.addPossibleMethods(possibleMethods);

                    // Result is a sequence of the dec and methodCall if the identified method has a return type
                    if (methodReturnType != null && !methodReturnType.toString().equals("void")) {
                        Command methodSeq = new Sequence(dec, methodCall);
                        result = Util.mergeSideEffectParamCommand(methodSeq, paramSequence, sideEffect);
                    } else {
                        result = Util.mergeSideEffectParamCommand(methodCall, paramSequence, sideEffect);
                    }
                }


            } else if (exprTree instanceof NewArrayTree) {
//                System.out.println("NewArrayTree");
                NewArrayTree array = (NewArrayTree) exprTree;
                Tree type = array.getType();
                String decName = Util.getTempName();
                Declaration dec = Util.getDeclaration(decName, type);
                String typeName;
                if (type != null) {
                    typeName = type.toString();
                } else {
                    typeName = "Unknown";
                }
                Variable var = new Variable(decName, typeName);
                Util.recordVariable(var);
                Command newInstance = Util.getArrayNewInstance(var, array, typeName);
                result = new Sequence(dec, newInstance);

            } else if (exprTree instanceof NewClassTree) {
//                System.out.println("NewClassTree");
                NewClassTree classTree = (NewClassTree) exprTree;
                String cid = classTree.getIdentifier().toString();
                String decName = Util.getTempName();
                Variable var = new Variable(decName, cid);
                var.getVarType().setReference();
                Declaration dec = new Declaration(var);
                Util.recordVariable(var);

                MetaRefCon mrc = new Current();
                @SuppressWarnings("unchecked")
                List<ExpressionTree> args = (List<ExpressionTree>) classTree.getArguments();
                ArrayList<Expr> classArgs = new ArrayList<Expr>(0);

                NewInstance newInstance = new NewInstance(var, mrc, var.getVarType(), classArgs);
                ArrayList<Command> paramSequence = Util.extractParamCommand(args, classArgs);
                result = Util.mergeSideEffectParamCommand(newInstance, paramSequence, dec);

            } else if (exprTree instanceof ParenthesizedTree) {
//                System.out.println("ParenthesizedTree");
                ParenthesizedTree expr = (ParenthesizedTree) exprTree;
                result = expr.getExpression().accept(new ExpressionVisitor(), null);

            } else if (exprTree instanceof TypeCastTree) {
//                System.out.println("TypeCastTree");
                TypeCastTree expr = (TypeCastTree) exprTree;
                result = expr.getExpression().accept(new ExpressionVisitor(), null);

            } else if (exprTree instanceof UnaryTree) {
//                System.out.println("UnaryTree");
                UnaryTree expr = (UnaryTree) exprTree;
                ExpressionTree e = expr.getExpression();
//                Expr lexpr = Util.extractExpression(e);
                Command sideEffects = e.accept(new ExpressionVisitor(), null);
//                Assignment ass = new Assignment(lexpr, new Val());
//                if (sideEffects instanceof Skip) {
//                    result = ass;
//                } else {
//                    result = new Sequence(sideEffects, ass);
//                }
                result = sideEffects;


            } else {
                System.out.println("[ERROR] - Expression not known");
                result = new Skip(); // Only used if expression is not detected
            }
            return result;
        }   


        @Override
        public Command defaultAction(Tree t, Void p) {
            Command result;
            if (t instanceof ExpressionTree) {
                result = visitExpression((ExpressionTree) t);
            } else {
                System.out.println("[ERROR] - Embedded expression has not been translated");
                result = super.defaultAction(t, p);
            }
            return result;
        }

        public String fixNewInstanceClassName(String s) {
            if (s.contains(".")) {
                int lastDot = s.lastIndexOf(".");
                String temp = s.substring(0, lastDot);
                if (temp.contains(".")) {
                    lastDot = temp.lastIndexOf(".");
                    temp = temp.substring(lastDot + 1);
                }
                return temp;
            } else {
                return s;
            }
        }

}








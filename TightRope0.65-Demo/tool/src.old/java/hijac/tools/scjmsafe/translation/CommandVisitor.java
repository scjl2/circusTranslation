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


// Command visitor that returns a Command object based on the command found in SCJ
class CommandVisitor extends SimpleTreeVisitor<Command, Void> {

        @Override
        public Command defaultAction(Tree t, Void p) {

            Command result;

//            System.out.print("Command \"" + t.toString() + "\"  -  ");
            if (t instanceof AssertTree) {
//                System.out.println("AssertTree");
                result = new Skip();

            } else if (t instanceof BlockTree) {
//                System.out.println("BlockTree");
                BlockTree block = (BlockTree) t;
                Command c1 = Util.getBlockCommand(block);
                result = new hijac.tools.scjmsafe.language.Commands.Scope(c1);

            } else if (t instanceof BreakTree) {
//                System.out.println("BreakTree");
                result = new Skip();

            } else if (t instanceof ClassTree) {
//                System.out.println("ClassTree");
                System.out.println("[ERROR] - Embedded Class found - Please refactor program to ensure no embedded classes are present before checking");
                result = new Skip();

            } else if (t instanceof ContinueTree) {
//                System.out.println("ContinueTree");
                result = new Skip();

            } else if (t instanceof DoWhileLoopTree) {
//                System.out.println("DoWhileLoopTree");
                DoWhileLoopTree tree = (DoWhileLoopTree) t;
                StatementTree statement = tree.getStatement();
                Command c1 = statement.accept(new CommandVisitor(), null);
                ExpressionTree condition = tree.getCondition();
                Expr e1 = Util.extractExpression(condition);
                Command c2 = condition.accept(new ExpressionVisitor(), null);
                Command c3 = new DoWhile(c1, e1);
                result = new Sequence(c3, c2);

            } else if (t instanceof EmptyStatementTree) {
//                System.out.println("EmptyStatementTree");
                result = new Skip();

            } else if (t instanceof EnhancedForLoopTree) {
//                System.out.println("EnhancedForLoopTree");
                EnhancedForLoopTree tree = (EnhancedForLoopTree) t;
                VariableTree var = tree.getVariable();
                Command varCom = var.accept(new VariableVisitor(), null);
                ExpressionTree e = tree.getExpression();
                Command sideEffect = e.accept(new ExpressionVisitor(), null);
                Expr expr = Util.extractExpression(e);
                StatementTree statement = tree.getStatement();
                Command statementCom = Util.extractBlock(statement);
                For forCommand = new For(varCom, expr, new Skip(), statementCom);
                if (sideEffect instanceof Skip) {
                    result = forCommand;
                } else {
                    result = new Sequence(sideEffect, forCommand);
                }

            } else if (t instanceof ExpressionStatementTree) {
                ExpressionStatementTree expr = (ExpressionStatementTree) t;
                result = expr.accept(new ExpressionVisitor(), null);

            } else if (t instanceof ForLoopTree) {
//                System.out.println("ForLoopTree");
                ForLoopTree tree = (ForLoopTree) t;
                @SuppressWarnings("unchecked")
                List<StatementTree> initializer = (List<StatementTree>) tree.getInitializer();
                Command c1 = Util.getStatementListCommand(initializer);
                Expr e1 = Util.extractExpression(tree.getCondition());
                Command sideEffect = tree.getCondition().accept(new ExpressionVisitor(), null);
                @SuppressWarnings("unchecked")
                List<ExpressionStatementTree> updates = (List<ExpressionStatementTree>) tree.getUpdate();
                Command c2 = Util.getExpressionStatementTreeCommand(updates);
                Command c3 = Util.extractBlock(tree.getStatement());
                For forCommand = new For(c1, e1, c2, c3);
                if (sideEffect instanceof Skip) {
                    result = forCommand;
                } else {
                    result = new Sequence(sideEffect, forCommand);
                }

            } else if (t instanceof IfTree) {
//                System.out.println("IfTree");
                IfTree ifTree = (IfTree) t;
                Expr e1;
                ExpressionTree expr = ifTree.getCondition();
                e1 = Util.extractExpression(expr);
                Command sideEffect = expr.accept(new ExpressionVisitor(), null);
                Command c1;
                StatementTree thenCom = ifTree.getThenStatement();
                c1 = Util.extractBlock(thenCom);
                Command c2;
                StatementTree elseCom = ifTree.getElseStatement();
                if (ifTree.getElseStatement() != null) { 
                    c2 = Util.extractBlock(elseCom);
                } else {
                    c2 = new Skip();
                }
                If ifCommand = new If(e1, c1, c2);
                if (sideEffect instanceof Skip) {
                    result = ifCommand;
                } else {
                    result = new Sequence(sideEffect, ifCommand);
                }

            } else if (t instanceof LabeledStatementTree) {
//                System.out.println("LabeledStatementTree");
                LabeledStatementTree tree = (LabeledStatementTree) t;
                StatementTree statement = tree.getStatement();
                Command c1 = tree.accept(new CommandVisitor(), null);
                result = c1;

            } else if (t instanceof ReturnTree) {
//                System.out.println("ReturnTree");
                ReturnTree tree = (ReturnTree) t;
                if (tree.getExpression() != null) {
                    ExpressionTree expression = tree.getExpression();
                    Variable resultVar = new Variable("Result", "Unknown");

                    if (expression instanceof TypeCastTree) {
                        TypeCastTree typeCast = (TypeCastTree) expression;
                        expression = typeCast.getExpression();
                    }

                    if (expression instanceof NewArrayTree) {
                        NewArrayTree array = (NewArrayTree) expression;
                        String typeName = array.getType().toString();
                        resultVar.setType(typeName);
                        Command newInstance = Util.getArrayNewInstance(resultVar, array, typeName);
                        result = newInstance;

                    } else if (expression instanceof NewClassTree) {
                        NewClassTree classTree = (NewClassTree) expression;
                        String cid = classTree.getIdentifier().toString();
                        resultVar.setType(cid);
                        resultVar.getVarType().setReference();

                        MetaRefCon mrc = new Current();
                        @SuppressWarnings("unchecked")
                        List<ExpressionTree> args = (List<ExpressionTree>) classTree.getArguments();
                        ArrayList<Expr> classArgs = new ArrayList<Expr>(0);

                        NewInstance newInstance = new NewInstance(resultVar, mrc, resultVar.getVarType(), classArgs);
                        ArrayList<Command> paramSequence = Util.extractParamCommand(args, classArgs);
                        result = Util.mergeSideEffectParamCommand(newInstance, paramSequence, new Skip());

                    } else if (expression instanceof MethodInvocationTree) {
                        Command com = expression.accept(new ExpressionVisitor(), null);

                        if (com instanceof MethodCall) {
                            MethodCall methodCall = (MethodCall) com;
                            ExprComPair pair = Util.fixUnknownMethodCallReturnDec(methodCall, com);
                            Expr rexpr = pair.getExpr();
                            resultVar.setType(((Variable) rexpr).getVarType().getTypeName());
                            Assignment ass = new Assignment(resultVar, rexpr);
                            result = new Sequence(pair.getCom(), ass);

                        } else if (com instanceof Sequence) {
                            Sequence seq = (Sequence) com;
                            MethodCall methodCall = (MethodCall) seq.getC2();
                            ExprComPair pair = Util.fixUnknownMethodCallReturnDec(methodCall, com);
                            Expr rexpr = pair.getExpr();
                            resultVar.setType(((Variable) rexpr).getVarType().getTypeName());
                            Assignment ass = new Assignment(resultVar, rexpr);
                            result = new Sequence(pair.getCom(), ass);

                        } else {
                            System.out.println("[ERROR] - Return of method call not translated correctly");
                            result = new Skip();
                        }

                    } else {
                        Expr rhs = Util.extractExpression(expression);
                        result = new Assignment(resultVar, rhs);
                    }

                } else {
                    result = new Skip();
                }

            } else if (t instanceof SwitchTree) {
//                System.out.println("SwitchTree");
                SwitchTree tree = (SwitchTree) t;
                ExpressionTree expression = tree.getExpression();
                Expr e1 = Util.extractExpression(expression);
                Command sideEffect = expression.accept(new ExpressionVisitor(), null);
                @SuppressWarnings("unchecked")
                List<CaseTree> cases = (List<CaseTree>) tree.getCases();
                ArrayList<Command> commands = new ArrayList<Command>(0);
                for (CaseTree ct : cases) {
                    if (ct.getExpression() != null) {
                        Expr caseExpr = Util.extractExpression(ct.getExpression());
                    } else {
                        Expr caseExpr = new OtherExpr();
                    }
                    @SuppressWarnings("unchecked")
                    List<StatementTree> statements = (List<StatementTree>) ct.getStatements();
                    commands.add(Util.getStatementListCommand(statements));
                }
                Switch switchCom = new Switch(e1, commands);
                if (sideEffect instanceof Skip) {
                    result = switchCom;
                } else {
                    result = new Sequence(sideEffect, switchCom);
                }

            } else if (t instanceof SynchronizedTree) {
//                System.out.println("SynchronizedTree");
                SynchronizedTree tree = (SynchronizedTree) t;
                BlockTree block = tree.getBlock();
                result = Util.getBlockCommand(block);

            } else if (t instanceof ThrowTree) {
//                System.out.println("ThrowTree");
                ThrowTree tree = (ThrowTree) t;
                ExpressionTree expression = tree.getExpression();
                result = expression.accept(new ExpressionVisitor(), null);

            } else if (t instanceof TryTree) {
//                System.out.println("TryTree");
                TryTree tree = (TryTree) t;
                BlockTree block = tree.getBlock();
                Command tryCom = Util.getBlockCommand(block);
                @SuppressWarnings("unchecked")
                List<CatchTree> catches = (List<CatchTree>) tree.getCatches();
                ArrayList<Command> catchExprs = new ArrayList<Command>(0);
                ArrayList<Command> catchComs = new ArrayList<Command>(0);
                for (CatchTree c : catches) {
                    catchExprs.add(c.getParameter().accept(new VariableVisitor(), null));
                    catchComs.add(Util.getBlockCommand(c.getBlock()));
                }
                BlockTree block2 = tree.getFinallyBlock();
                Command finallyCom = Util.getBlockCommand(block2);
                result = new Try(tryCom, catchExprs, catchComs, finallyCom);

            } else if (t instanceof VariableTree) {
//                System.out.println("VariableTree");
                VariableTree tree = (VariableTree) t;
                result = tree.accept(new VariableVisitor(), null);

            } else if (t instanceof WhileLoopTree) {
//                System.out.println("WhileLoopTree");
                WhileLoopTree tree = (WhileLoopTree) t;
                ExpressionTree condition = tree.getCondition();
                Expr e1 = Util.extractExpression(condition);
                Command c1 = condition.accept(new ExpressionVisitor(), null);
                StatementTree statement = tree.getStatement();
                Command c2 = Util.extractBlock(statement);
                Command c3 = new While(e1, c2);
                result = new Sequence(c3, c1);

            } else {
                System.out.println("[ERROR] - Command not known    -   " + t.toString());
                result = new Skip(); // Only used if command not detected
            }
            
            return result;
        }
}


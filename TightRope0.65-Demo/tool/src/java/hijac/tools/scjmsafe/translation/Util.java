package hijac.tools.scjmsafe.translation;

import hijac.tools.analysis.SCJAnalysis;

import hijac.tools.scjmsafe.language.*;
import hijac.tools.scjmsafe.language.Commands.*;
import hijac.tools.scjmsafe.language.Method.*;
import hijac.tools.scjmsafe.language.Expressions.*;
import hijac.tools.scjmsafe.language.RefCon.*;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import com.sun.source.tree.*;
import com.sun.source.util.*;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.Modifier;


public class Util {

    ///////////////////////////////////////////
    /////       EXTRACT EXPRESSION        /////
    ///////////////////////////////////////////

    public static Expr extractExpression(ExpressionTree exprTree) {

        Expr result;

        if (exprTree instanceof AnnotationTree) {
//            System.out.println("  AnnotationTree");
            result = new OtherExpr();

        } else if (exprTree instanceof ArrayAccessTree) {
//            System.out.println("  ArrayAccessTree");
            ArrayAccessTree expr = (ArrayAccessTree) exprTree;
            Expr lhs = extractExpression(expr.getExpression());

            if (lhs instanceof ArrayElement) {
                // This shouldn't be called I don't think
                result = (ArrayElement) lhs;
            } else if (lhs instanceof Variable) {
                Variable var = (Variable) lhs;
                ArrayElement array = new ArrayElement(var.getName(), var.getVarType().getTypeName());
                result = array;
            } else if (lhs instanceof FieldAccess) {
                FieldAccess fa = (FieldAccess) lhs;
                FieldAccess lhsFA = new FieldAccess(fa.getFront());
                Identifier last = fa.getLast();
                ArrayElement array;
                if (last instanceof Variable) {
                    Variable lastVar = (Variable) last;
                    array = new ArrayElement(lastVar.getName(), lastVar.getVarType().getTypeName());
                } else {
                    array = new ArrayElement(last.getName());
                }
                lhsFA.addElement(array); 
                result = lhsFA;
            } else {
                result = new OtherExpr();
            }

        } else if (exprTree instanceof AssignmentTree) {
//            System.out.println("  AssignmentTree");
            AssignmentTree ass = (AssignmentTree) exprTree;
            result = extractExpression(ass.getVariable());

        } else if (exprTree instanceof BinaryTree) {
//            System.out.println("  BinaryTree");
            result = new Val();

        } else if (exprTree instanceof CompoundAssignmentTree) {
//            System.out.println("  CompoundAssignmentTree");
            CompoundAssignmentTree ass = (CompoundAssignmentTree) exprTree;
            result = extractExpression(ass.getVariable());

        } else if (exprTree instanceof ConditionalExpressionTree) {
//            System.out.println("  ConditionalExpressionTree");
            result = new OtherExpr();

        } else if (exprTree instanceof ErroneousTree) {
//            System.out.println("  ErroneousTree");
            result = new OtherExpr();

        } else if (exprTree instanceof IdentifierTree) {
//            System.out.println("  IdentifierTree");
            IdentifierTree id = (IdentifierTree) exprTree;
            if (id.getName().toString().equals("this")) {
                result = new This();
            } else {
    //            System.out.println("Trying to find variable with name: " + id.getName().toString());
                Variable v = findVariable(id.getName().toString());
                if (v != null) {
    //                System.out.println("Found variable with name: " + v.getName() + " and type " + v.getType());
                    result = v;
                } else {
                    result = new Variable(id.getName().toString(), "Unknown");
                }
            }

        } else if (exprTree instanceof InstanceOfTree) {
//            System.out.println("  InstanceOfTree");
            result = new OtherExpr();

        } else if (exprTree instanceof LiteralTree) {
//            System.out.println("  LiteralTree");
            LiteralTree tree = (LiteralTree) exprTree;
            if (tree.getValue() == null) {
                result = new Null();
            } else {
                result = new Val();
            }

        } else if (exprTree instanceof MemberSelectTree) {
//            System.out.println("  MemberSelectTree");
            MemberSelectTree expr = (MemberSelectTree) exprTree;
            Expr lhs = extractExpression(expr.getExpression());

            Variable rhs;
//            System.out.println("Trying to find variable with name: " + expr.getIdentifier().toString());
            Variable v = findVariable(expr.getIdentifier().toString());
            if (v != null) {
//                System.out.println("Found variable with name: " + v.getName() + " and type " + v.getType());
                rhs = v;
            } else {
                rhs = new Variable(expr.getIdentifier().toString(), "Unknown");
            }

            if (lhs instanceof ArrayElement) {
                FieldAccess fa = new FieldAccess();
                fa.addElement((ArrayElement) lhs);
                fa.addElement(rhs);
                result = fa;
            } else if (lhs instanceof Variable) {
                FieldAccess fa = new FieldAccess();
                fa.addElement((Variable) lhs);
                fa.addElement(rhs);
                result = fa;
            } else if (lhs instanceof FieldAccess) {
                FieldAccess fa = (FieldAccess) lhs;
                fa.addElement(rhs);
                result = fa;

            } else if (lhs instanceof OtherExpr) {
                result = new OtherExpr();
//                result = rhs;
                System.out.println("    [ERROR] - Embedded OtherExpr found in MemberSelect");
            } else {
                result = new OtherExpr();
            }

        } else if (exprTree instanceof MethodInvocationTree) {
//            System.out.println("  MethodInvocationTree");
//            System.out.println("BINGO");
            result = new OtherExpr();

        } else if (exprTree instanceof NewArrayTree) {
//            System.out.println("  NewArrayTree");
            result = new OtherExpr();

        } else if (exprTree instanceof NewClassTree) {
//            System.out.println("  NewClassTree");
            result = new OtherExpr();

        } else if (exprTree instanceof ParenthesizedTree) { 
//            System.out.println("  ParenthesizedTree");
            ParenthesizedTree expr = (ParenthesizedTree) exprTree;
            result = extractExpression(expr.getExpression());

        } else if (exprTree instanceof TypeCastTree) {
//            System.out.println("  TypeCastTree");
            TypeCastTree expr = (TypeCastTree) exprTree;
            result = extractExpression(expr.getExpression());

        } else if (exprTree instanceof UnaryTree) {
//            System.out.println("  UnaryTree");
            UnaryTree expr = (UnaryTree) exprTree;
            result = extractExpression(expr.getExpression());

        } else {
            Variable var = new Variable(exprTree.toString(), "Unknown");
            result = var;
//            result = new OtherExpr();
            System.out.println("[ERROR] - Failed to extract expression - " + exprTree.toString());
        }

        return result;
    }


    ///////////////////////////////////////////
    /////             METHODS             /////
    ///////////////////////////////////////////

    public static String extractMethodName(ExpressionTree expr) {
        String result;
        if (expr instanceof MemberSelectTree) {
            MemberSelectTree memberSelect = (MemberSelectTree) expr;
            result = memberSelect.getIdentifier().toString();
        } else if (expr instanceof IdentifierTree) {
            IdentifierTree id = (IdentifierTree) expr;
            result = id.getName().toString();
        } else {
            result = "Unknown";
            System.out.println("[ERROR] - Failed to extract method name");
        }
        return result;
    }

    public static String extractMethodCallClass(Expr expr) {
        String result;
        if (expr instanceof FieldAccess) {
            FieldAccess fa = (FieldAccess) expr;
            expr = fa.getLast();
        }

        if (expr instanceof ArrayElement) {
            ArrayElement ae = (ArrayElement) expr;
            result = ae.getType();

        } else if (expr instanceof Variable) {
            Variable v = (Variable) expr;
            result = v.getVarType().getTypeName();

        } else {
            result = "Unknown";
        }

        return result;
    }

    public static Expr extractMethodLExpr(ExpressionTree expr) {
        Expr result;
        if (expr instanceof MemberSelectTree) {
            MemberSelectTree memberSelect = (MemberSelectTree) expr;
            result = extractExpression(memberSelect.getExpression());

        } else if (expr instanceof ParenthesizedTree) { 
            ParenthesizedTree paren = (ParenthesizedTree) expr;
            result = extractMethodLExpr(paren.getExpression());

        } else if (expr instanceof TypeCastTree) {
            TypeCastTree typeCast = (TypeCastTree) expr;
            result = extractMethodLExpr(typeCast.getExpression());

        } else if (expr instanceof IdentifierTree) {
            result = null;

        } else {
            result = extractExpression(expr);
        }
        return result;
    }

    public static Declaration getReturnTypeDec(String tempVarName, Tree returnType, String name) {
        if (returnType != null) {
            return getDeclaration(tempVarName, returnType);
        } else {
            String type = getSpecificMethodReturnType(name);
            Variable var = new Variable(tempVarName, type);
            return new Declaration(var);
        }
    }

    public static Method newDefaultMethod(String methodName, Tree returnType, BlockTree methodBody, List<VariableTree> parameters) {
//        System.out.println("Method name: " + methodName);
        Method method = new Method(methodName);

        for (VariableTree vt : parameters) {
            Variable param = new Variable(vt.getName().toString(), vt.getType().toString());
            recordVariable(param);
            method.addParam(param);
        }

        String returnTypeName = returnType.toString();
        if (!returnTypeName.equals("void")) {// && methodBody != null) { // TODO - By adding the constraint that methodBody != null, this can create problems, because the method should still have a return parameter even though it has no body.
            Variable result = new Variable("Result", returnTypeName);
            result.getVarType().setResultVar();
            method.addParam(result);
        }
//        System.out.println("Return type: " + returnTypeName);

        method.addBody(getBlockCommand(methodBody));
        method.addReturnType(returnType);
        method.addReturnTypeName(returnTypeName);

//        System.out.println();

        return method;
    }


    public static Method newConstr(String constrName, BlockTree constrBody, List<VariableTree> parameters) {
//        System.out.println("Constr");
        Method constr = new Method(constrName);

        for (VariableTree vt : parameters) {
            Variable param = new Variable(vt.getName().toString(), vt.getType().toString());
            recordVariable(param);
            constr.addParam(param);
        }

        constr.addBody(getBlockCommand(constrBody));
//        System.out.println();

        return constr;
    }



    public static ArrayList<MethodSig> methodDatabase = new ArrayList<MethodSig>(0);

    public static void addMethodToDatabase(String name, String classID, String classExtends, Tree type, ArrayList<String> params) {
        methodDatabase.add(new MethodSig(name, classID, classExtends, type, params));
    }

    public static String getMethodTypeName(Tree t, String name) {
        if (t != null) {
            return t.toString();
        } else {
            return getSpecificMethodReturnType(name);
        }
    }

    public static String getSpecificMethodReturnType(String name) {
        switch (name) {
            case "getMemoryArea" : {
                return "MemoryArea";
            }
            case "getCurrentMission" : {
                return "Mission";
            }
            default : {
                return "Unknown";
            }
        }
    }

    private static HashSet<String> ignoredMethods = new HashSet<String>(0);

    public static void populateIgnoredMethods() {
        ignoredMethods.add("println");
        ignoredMethods.add("toString");
        ignoredMethods.add("super");
    }

    public static Tree findMethodType(Expr lhs, String name, ArrayList<Expr> args) {

        if (ignoredMethods.contains(name)) {return null;}

        String className = extractMethodCallClass(lhs);
        if (!SCJmSafeTranslator.translatedClasses.contains(className) || SCJmSafeTranslator.ignoredClasses.contains(className)) {return null;}

        ArrayList<MethodSig> methods = findMethods(lhs, name, args);
        Tree type;
        if (methods.size() == 0) {
            type = null;
            System.out.println("[WARNING] - Method '" + name + "' not found");
            System.out.println();
        } else if (methods.size() > 1) {
            boolean same = true;
            for (int i = 1; i < methods.size(); i++) {
                if (!methods.get(i).getReturnType().toString().equals(methods.get(i - 1).getReturnType().toString())) {
                    same = false;
                }
            }
            if (same) {
                type = methods.get(0).getReturnType();
            } else {
                type = null;
                System.out.println("[WARNING] - Method '" + name + "' has multiple definitions with different return types - type cannot be inferred");
                System.out.println();
            }
        } else {
            type = methods.get(0).getReturnType();
        }
        return type;
    }

    public static ArrayList<MethodSig> findMethods(Expr lhs, String name, ArrayList<Expr> args) {
        ArrayList<String> argTypes = extractMethodArgTypes(args);
        String className = extractMethodCallClass(lhs);
        ArrayList<MethodSig> matches = new ArrayList<MethodSig>(0);

        if (ignoredMethods.contains(name)) {return matches;}

/*
        System.out.print("Looking for method '" + name + "'");
        if (args.size() > 0) {
            System.out.print(" with parameters (");
            for (int i = 0; i < args.size(); i++) {
                System.out.print("'");
                args.get(i).printExpression();
                System.out.print(" (" + argTypes.get(i) + ")'");
                if (i < args.size() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.print(")");
        }
        System.out.print(" in class '" + className + "'\n");
*/

        ArrayList<String> classes = new ArrayList<String>(0);
        for (MethodSig m : methodDatabase) {
            if (m.getClassName().equals(className)) {
                classes.add(className);
                classes.addAll(m.getDescendants());
            }
        }

        for (MethodSig m : methodDatabase) {
            if (classes.contains(m.getClassName()) || className.equals("Unknown")) {
                if (m.getName().equals(name) && m.getParamTypes().size() == args.size()) {
                    ArrayList<String> foundTypes = m.getParamTypes();
                    boolean match = true;
                    for (int i = 0; i < foundTypes.size(); i++) {
                        if (!foundTypes.get(i).equals(argTypes.get(i))) {
                            if (foundTypes.get(i).equals("Object") || argTypes.get(i).equals("Object") || argTypes.get(i).equals("Unknown")) {
                                if (args.get(i) instanceof Val && !(Arrays.asList(primitiveTypes).contains(foundTypes.get(i)))) {
                                    match = false;
                                }
                            } else {
                                match = false;
                            }
                        }
                    }
                    if (match) {
/*
                        System.out.print("Found method signature '" + m.getName() + "'");
                        if (m.getParamTypes().size() > 0) {
                            System.out.print(" with parameter types (");
                            for (int i = 0; i < m.getParamTypes().size(); i++) {
                                System.out.print(m.getParamTypes().get(i));
                                if (i < m.getParamTypes().size() - 1) {
                                    System.out.print(", ");
                                }
                            }
                            System.out.print(")");
                        }
                        System.out.print(" in class '" + m.getClassName() + "'");
                        if (!m.getClassExtends().equals("")) {
                            System.out.print(", which extends '" + m.getClassExtends() + "'");
                        }
                        System.out.println();
*/
                        if (!matches.contains(m)) {
                            matches.add(m);
                        }
                    }
                }
            }
        }
//        System.out.println();
        return matches;
    }

    static String[] primitiveTypes = {
        "int",
        "long",
        "double",
        "float",
        "char",
        "boolean",
        "String",
        "byte",
        "short",
    };

    public static ArrayList<String> extractMethodArgTypes(ArrayList<Expr> args) {
        ArrayList<String> result = new ArrayList<String>(0);
        for (Expr e : args) {
//            System.out.println("Analysing argument : '" + e.toString());
            if (e instanceof FieldAccess) {
                FieldAccess fa = (FieldAccess) e;
                e = fa.getLast();
            }

            if (e instanceof ArrayElement) {
                ArrayElement ae = (ArrayElement) e;
                result.add(ae.getType());

            } else if (e instanceof Variable) {
                Variable v = (Variable) e;
                result.add(v.getVarType().getTypeName());

            } else {
                result.add("Unknown");
            }
        }
        return result;
    }


    public static ExprComPair fixUnknownMethodCallReturnDec(MethodCall methodCall, Command c1) {

        ExprComPair result = new ExprComPair();
        Expr rexpr;
        Command com = c1;
        String specificName = getSpecificMethodReturnType(methodCall.getName());

        if (methodCall.getArgs().size() == 0 || !(methodCall.getArgs().get(methodCall.getArgs().size() - 1) instanceof Variable)) {
            String tempVarName = getTempName();
            rexpr = new Variable(tempVarName, specificName);
            methodCall.addArg(rexpr);
            Declaration dec = new Declaration((Variable) rexpr);
            com = new Sequence(dec, c1);
        } else {
            Variable lastVar = (Variable) methodCall.getArgs().get(methodCall.getArgs().size() - 1);
            if (lastVar.getVarType().isResultVar() == true) {
                rexpr = lastVar;
            } else {
                String tempVarName = getTempName();
                rexpr = new Variable(tempVarName, specificName);
                methodCall.addArg(rexpr);
                Declaration dec = new Declaration((Variable) rexpr);
                com = new Sequence(dec, c1);
            }
        }
        result.addExpr(rexpr);
        result.addCom(com);
        return result;
    }


    ///////////////////////////////////////////
    /////          ARGS / PARAMS          /////
    ///////////////////////////////////////////

    // TODO - Method calls inside method calls inside methods calls are not being transalted correctly
    //        Taken from method createMotions in TransientDetectorScopeEntry (CDx)
    //           byte[] var59;
    //           craft.getCallsign(var59);
    //           mkCallsignInPersistentScope(var59);
    //           state.put(var59, new_pos.x, new_pos.y, new_pos.z);

    public static ArrayList<Command> extractParamCommand(List<ExpressionTree> args, ArrayList<Expr> translatedArgs) {
        ArrayList<Command> result = new ArrayList<Command>(0);
        for (ExpressionTree a : args) {

            if (a instanceof TypeCastTree) {
                TypeCastTree typeCast = (TypeCastTree) a;
                a = typeCast.getExpression();
            }

            if (a instanceof BinaryTree) {
                BinaryTree binary = (BinaryTree) a;
                Command lhs = binary.getLeftOperand().accept(new ExpressionVisitor(), null);
                Command rhs = binary.getRightOperand().accept(new ExpressionVisitor(), null);
                Command com = Util.simplifyCommandPair(lhs, rhs);
                if (!(com instanceof Skip)) {
                    result.add(com);
                }
                translatedArgs.add(extractExpression(binary));

            } else if (a instanceof NewArrayTree) {
                NewArrayTree array = (NewArrayTree) a;
                Sequence seq = (Sequence) a.accept(new ExpressionVisitor(), null);
                result.add(seq);
                Expr expr = ((NewInstance) seq.getC2()).getExpr();
                translatedArgs.add(expr);

            } else if (a instanceof NewClassTree) {
                NewClassTree classTree = (NewClassTree) a;
                Sequence seq = (Sequence) a.accept(new ExpressionVisitor(), null);
                result.add(seq);
                Expr expr = ((NewInstance) seq.getC2()).getExpr();
                translatedArgs.add(expr);

            } else if (a instanceof MethodInvocationTree) {
                MethodInvocationTree methodTree = (MethodInvocationTree) a;
                Command com = a.accept(new ExpressionVisitor(), null);
                ParamCommandMethodCheck(com, result, translatedArgs);

            } else if (a instanceof MemberSelectTree) {
                MemberSelectTree memberSelectTree = (MemberSelectTree) a;
                Command com = a.accept(new ExpressionVisitor(), null);
                if (com instanceof MethodCall || com instanceof Sequence) {
                    ParamCommandMethodCheck(com, result, translatedArgs);
                } else {
                    translatedArgs.add(extractExpression(a));
                }

            } else {
                translatedArgs.add(extractExpression(a));
            }
        }
        return result;
    }




    public static void ParamCommandMethodCheck(Command com, ArrayList<Command> result, ArrayList<Expr> translatedArgs) {
        if (com instanceof MethodCall) {
            MethodCall methodCall = (MethodCall) com;
            Variable tempVar;
            ExprComPair pair = Util.fixUnknownMethodCallReturnDec(methodCall, com);
            tempVar = (Variable) pair.getExpr();
            translatedArgs.add(tempVar);
            result.add(pair.getCom());
            
        } else if (com instanceof Sequence) {
            Sequence seq = (Sequence) com;
            MethodCall methodCall = (MethodCall) seq.getC2();
            ExprComPair pair = Util.fixUnknownMethodCallReturnDec(methodCall, seq);
            Variable tempVar = (Variable) pair.getExpr();
            translatedArgs.add(tempVar);
            result.add(pair.getCom());
            
        } else { }
    }



    public static Command mergeSideEffectParamCommand(Command com, ArrayList<Command> paramSequence, Command sideEffect) {
        Command result;
        Command paramCommand = createSingleCommand(paramSequence);
        if (sideEffect instanceof Skip) {
            if (paramSequence.size() == 0) {
                result = com;
            } else {
                if (com instanceof Sequence) {
                    Sequence seq = (Sequence) com;
                    result = new Sequence(new Sequence(paramCommand, seq.getC1()), seq.getC2());
                } else {
                    result = new Sequence(paramCommand, com);
                }
            }
        } else {
            if (paramSequence.size() == 0) {
                if (com instanceof Sequence) {
                    Sequence seq = (Sequence) com;
                    result = new Sequence(new Sequence(sideEffect, seq.getC1()), seq.getC2());
                } else {
                    result = new Sequence(sideEffect, com);
                }
            } else {
                if (com instanceof Sequence) {
                    Sequence seq = (Sequence) com;
                    result = new Sequence(new Sequence(new Sequence(sideEffect, paramCommand), seq.getC1()), seq.getC2());
                } else {
                    result = new Sequence(new Sequence(sideEffect, paramCommand), com);
                }
            }
        }
        return result;
    }

    ///////////////////////////////////////////
    /////           DECLARATION           /////
    ///////////////////////////////////////////

    public static Declaration getDeclaration(String name, Tree type) {

            String decTypeName;
//            if (type.getKind().equals(Tree.Kind.ARRAY_TYPE)) {
//                ArrayTypeTree arrayTree = (ArrayTypeTree) type;
//                decTypeName = arrayTree.getType().toString();
//            } else {
//                decTypeName = type.toString();
//            }

            if (type != null) {
                decTypeName = getTypeString(type);
            } else {
                decTypeName = "Unknown";
            }

            Variable var = new Variable(name, decTypeName);
            if (type != null) {
                setVarType(var, type);
            }

            Declaration dec = new Declaration(var);
            return dec;

    }

    public static String getTypeString(Tree type) {
        String result;
        if (type.getKind().equals(Tree.Kind.MEMBER_SELECT)) {
            MemberSelectTree mst = (MemberSelectTree) type;
            result = mst.getIdentifier().toString();
        } else {
            result = type.toString();
        }
        return result;
    }

    public static void setVarType(Variable var, Tree type) {
        if (type.getKind().equals(Tree.Kind.PRIMITIVE_TYPE)) {
            var.getVarType().setPrimitive();

        } else if (type.getKind().equals(Tree.Kind.ARRAY_TYPE)) {
            ArrayTypeTree arrayTree = (ArrayTypeTree) type;
            var.getVarType().setArray();
            Tree arrayType = arrayTree.getType();
            if (arrayType == null) {
                System.out.println("[ERROR] - Cannot determine type of array");
            } else {
                setVarType(var, arrayType);
            }

        } else if (type.getKind().equals(Tree.Kind.IDENTIFIER)) {
            var.getVarType().setReference();

        } else if (type.getKind().equals(Tree.Kind.MEMBER_SELECT)) {
            var.getVarType().setReference();

        } else {
            System.out.println("[ERROR] - Declaration of type " + var.getVarType().getTypeName() + " cannot be determined");
        }
    }

    private static ArrayList<Variable> variables = new ArrayList<Variable>(0);

    public static void recordVariable(Variable v) {
//        System.out.println("Recording variable : '" + v.getName() + "', with type : '" + v.getVarType().getTypeName() + "'");
        variables.add(v);
    }

    public static Variable findVariable(String name) {
        ArrayList<Variable> results = new ArrayList<Variable>(0);
        int count = 0;
        for (Variable v : variables) {
            if (v.getName().equals(name)) {
                count++;
                results.add(v);
            }
        }
        if (count > 1) {
            boolean match = true;
            Variable first = results.get(0);
            for (Variable v : results) {
                if (!first.compareTypeAgainst(v)) {
                    match = false;
                }
            }
            if (match) {
                return first;
            } else {
                return new Variable(name, "Unknown");
            }
        } else if (count == 1) {
            return results.get(0);
        } else {
            return new Variable(name, "Unknown");
        }
    }

    public static void resetVariables() {
        variables = new ArrayList<Variable>(0);
    }


    ///////////////////////////////////////////
    /////             COMMANDS            /////
    ///////////////////////////////////////////

    public static Command extractBlock(StatementTree command) {
        if (command instanceof BlockTree) {
            BlockTree comBlock = (BlockTree) command;
            return Util.getBlockCommand(comBlock);
        } else {
            return command.accept(new CommandVisitor(), null);
        }
    }


    public static Command getBlockCommand(BlockTree tree) {
        if (tree != null) {
            @SuppressWarnings("unchecked")
            List<StatementTree> statements = (List<StatementTree>) tree.getStatements();
            return getStatementListCommand(statements);
        } else {
            return new Skip();
        }
    }


    public static Command getExpressionStatementTreeCommand(List<ExpressionStatementTree> expressions) {
        ArrayList<Command> commands = new ArrayList<Command>(0);
        for (ExpressionStatementTree est : expressions) {
            commands.add(est.getExpression().accept(new ExpressionVisitor(), null));
        }
        return createSingleCommand(commands);
    }

    public static Command getStatementListCommand(List<StatementTree> statements) {
        ArrayList<Command> commands = new ArrayList<Command>(0);
        for (StatementTree st : statements) {
            commands.add(st.accept(new CommandVisitor(), null));
        }
        return createSingleCommand(commands);
    }

    public static Command createSingleCommand(ArrayList<Command> commands) {
        Command c1;
        Command c2;
        Command result;
        if (commands.size() > 1) {
            c1 = commands.get(0);
            commands.remove(0);
            c2 = createSingleCommand(commands);
            result = new Sequence(c1, c2);
        } else if (commands.size() == 1) {
            c1 = commands.get(0);
            result = c1;
        } else {
            c1 = new Skip();
            result = c1;
        }
        return result;
    }



    public static Command getArrayNewInstance(Expr lexpr, NewArrayTree array, String typeName) {
        MetaRefCon mrc = new Current();

        @SuppressWarnings("unchecked")
        List<ExpressionTree> inits = (List<ExpressionTree>) array.getInitializers();
        ArrayList<Expr> arrayInits = new ArrayList<Expr>(0);
        ArrayList<Command> sideEffects = new ArrayList<Command>(0);
        if (inits != null) {
            sideEffects = extractParamCommand(inits, arrayInits);
        }
        
//        if (typeName.equals("Unknown") && arrayInits.size() > 0) {
//            System.out.println("1");
//            for (Expr e : arrayInits) {
//            System.out.println("2");
//                Variable v;
//                if (e instanceof Variable) {
//            System.out.println("3 - " + e.toString());
//                    v = (Variable) e;
//                    v = findVariable(e.toString());
//                    if (v != null) {
//                        System.out.println("Analysing array type based on parameters... " + v.getType());
//                    }
//                }
//            }
//        }

//        if (typeName.equals("Unknown") && arrayInits.size() > 0) {
//            for (Expr e : arrayInits) {
//                String s = extractMethodCallClass(e);
//                System.out.println(s);
//            }
//        }

        VarType varType = new VarType();
        varType.setArray();
        varType.setReference();
        Tree type = array.getType();
        if (type == null) {
            if (inits == null) {
                System.out.println("[ERROR] - Cannot determine type of array");
            } else if (inits.get(0).getKind().equals(Tree.Kind.IDENTIFIER)) {
                varType.setReference();
            } else {
                varType.setPrimitive();
            }
        } else {
//            setVarType(decType, type); // TODO - This is now broken?
        }
        
        NewInstance newInstance;
        if (lexpr instanceof Variable) {    // TODO - Expand this so that the variable at the tail of the lexpr is assigned the varType
            Variable var = (Variable) lexpr;
//            var.setVarType(varType);
            newInstance = new NewInstance(lexpr, mrc, var.getVarType(), arrayInits);    // TODO - I *think* this is the one to use for all left expressions - see todo above.
        } else {
            newInstance = new NewInstance(lexpr, mrc, varType, arrayInits);
        }
        return mergeSideEffectParamCommand(newInstance, sideEffects, new Skip());
    }


    public static String getArrayTypeName(String name) {
        String result;
        result = name.replace("[]", "");
        return result;
    }


/*    public static NewInstance getClassNewInstance(Expr lexpr, NewClassTree newClass) {
        MetaRefCon mrc = new Current();
        String type = newClass.getIdentifier().toString();
        @SuppressWarnings("unchecked")
        List<ExpressionTree> args = (List<ExpressionTree>) newClass.getArguments();
        ArrayList<Expr> classArgs = new ArrayList<Expr>(0);
        return new NewInstance(lexpr, mrc, type, classArgs);
    }
*/


    public static Command simplifyCommandPair(Command c1, Command c2) {
        Command result;
        if (c1 instanceof Skip) {
            result = c2;
        } else if (c2 instanceof Skip) {
            result = c1;
        } else {
            result = new Sequence(c1, c2);
        }
        return result;
    }

    ///////////////////////////////////////////
    /////            TEMP VAR             /////
    ///////////////////////////////////////////

    private static int tempVal = 0;

    public static String getTempName() {
        String result = "var";
        result += tempVal;
        tempVal++;
        return result;
    }






    ///////////////////////////////////////////
    /////            HANDLERS             /////
    ///////////////////////////////////////////

    private static ArrayList<String> handlerClasses = new ArrayList<String>(0);

    public static void addHandler(String handlerName) {
        handlerClasses.add(handlerName);
    }

    public static boolean isHandler(String handlerName) {
        for (String s : handlerClasses) {
            if (s.equals(handlerName)) {
                return true;
            }
        }
        return false;
    }


    public static void scanForHandlers(MSafeMission mission, BlockTree tree) {
        if (tree != null) {
            @SuppressWarnings("unchecked")
            List<StatementTree> statements = (List<StatementTree>) tree.getStatements();
            for (StatementTree s : statements) {
                scanStatementForHandler(mission, s);
            }
        }
    }


    public static void scanStatementForHandler(MSafeMission mission, StatementTree s) {
        if (s instanceof BlockTree) {
            BlockTree block = (BlockTree) s;
            for (StatementTree st : block.getStatements()) {
                scanStatementForHandler(mission, st);
            }

        } else if (s instanceof DoWhileLoopTree) {
            DoWhileLoopTree tree = (DoWhileLoopTree) s;
            StatementTree statement = tree.getStatement();
            scanStatementForHandler(mission, statement);

        } else if (s instanceof EnhancedForLoopTree) {
            EnhancedForLoopTree tree = (EnhancedForLoopTree) s;
            StatementTree statement = tree.getStatement();
            scanStatementForHandler(mission, statement);

        } else if (s instanceof ExpressionStatementTree) {
            ExpressionStatementTree expr = (ExpressionStatementTree) s;
            scanExpressionForHandler(mission, expr.getExpression());

        } else if (s instanceof ForLoopTree) {
            ForLoopTree tree = (ForLoopTree) s;
            scanStatementForHandler(mission, tree.getStatement());

        } else if (s instanceof IfTree) {
            IfTree ifTree = (IfTree) s;
            StatementTree thenCom = ifTree.getThenStatement();
            scanStatementForHandler(mission, thenCom);
            StatementTree elseCom = ifTree.getElseStatement();
            if (elseCom != null) { 
                scanStatementForHandler(mission, elseCom);
            }

        } else if (s instanceof LabeledStatementTree) {
            LabeledStatementTree tree = (LabeledStatementTree) s;
            StatementTree statement = tree.getStatement();
            scanStatementForHandler(mission, statement);

//        } else if (s instanceof ReturnTree) {
//            ReturnTree tree = (ReturnTree) s;
//            if (tree.getExpression() != null) {
//                ExpressionTree expression = tree.getExpression();
//            }

        } else if (s instanceof SwitchTree) {
            SwitchTree tree = (SwitchTree) s;
            @SuppressWarnings("unchecked")
            List<CaseTree> cases = (List<CaseTree>) tree.getCases();
            for (CaseTree ct : cases) {
                @SuppressWarnings("unchecked")
                List<StatementTree> statements = (List<StatementTree>) ct.getStatements();
                for (StatementTree st : statements) {
                    scanStatementForHandler(mission, st);
                }
            }

        } else if (s instanceof SynchronizedTree) {
            SynchronizedTree tree = (SynchronizedTree) s;
            BlockTree block = tree.getBlock();
            for (StatementTree st : block.getStatements()) {
                scanStatementForHandler(mission, st);
            }

//        } else if (s instanceof ThrowTree) {
//            ThrowTree tree = (ThrowTree) s;
//            ExpressionTree expression = tree.getExpression();
//            result = expression.accept(new ExpressionVisitor(), null);

        } else if (s instanceof TryTree) {
            TryTree tree = (TryTree) s;
            BlockTree block = tree.getBlock();
            for (StatementTree st : block.getStatements()) {
                scanStatementForHandler(mission, st);
            }
            @SuppressWarnings("unchecked")
            List<CatchTree> catches = (List<CatchTree>) tree.getCatches();
            for (CatchTree c : catches) {
                for (StatementTree st : c.getBlock().getStatements()) {
                    scanStatementForHandler(mission, st);
                }
            }
            BlockTree block2 = tree.getFinallyBlock();
            if (block2 != null) {
                for (StatementTree st : block2.getStatements()) {
                    scanStatementForHandler(mission, st);
                }
            }

        } else if (s instanceof VariableTree) {
            VariableTree tree = (VariableTree) s;
            scanExpressionForHandler(mission, tree.getInitializer());

        } else if (s instanceof WhileLoopTree) {
            WhileLoopTree tree = (WhileLoopTree) s;
            StatementTree statement = tree.getStatement();
            scanStatementForHandler(mission, statement);
        }
    }



    public static void scanExpressionForHandler(MSafeMission mission, ExpressionTree exprTree) {

        if (exprTree instanceof AssignmentTree) {
            AssignmentTree ass = (AssignmentTree) exprTree;
            ExpressionTree rhs = ass.getExpression();
            scanExpressionForHandler(mission, rhs);

        } else if (exprTree instanceof ConditionalExpressionTree) {
            ConditionalExpressionTree expr = (ConditionalExpressionTree) exprTree;
            scanExpressionForHandler(mission, expr.getTrueExpression());
            scanExpressionForHandler(mission, expr.getFalseExpression());

        } else if (exprTree instanceof MethodInvocationTree) {
            MethodInvocationTree method = (MethodInvocationTree) exprTree;
            ExpressionTree methodName = method.getMethodSelect();
            @SuppressWarnings("unchecked")
            List<ExpressionTree> args = (List<ExpressionTree>) method.getArguments();
            String methodIdentifier = extractMethodName(methodName);
            Expr lexpr = extractMethodLExpr(methodName);
            ArrayList<Expr> methodArgs = new ArrayList<Expr>(0);
            ArrayList<Command> paramSequence = extractParamCommand(args, methodArgs);
            Tree methodReturnType = findMethodType(lexpr, methodIdentifier, methodArgs);
            if (methodReturnType != null && !methodReturnType.toString().equals("void")) {
                if (isHandler(getMethodTypeName(methodReturnType, methodIdentifier))) {
                    mission.addHandler(getMethodTypeName(methodReturnType, methodIdentifier));
                }
            }

        } else if (exprTree instanceof NewArrayTree) {
            NewArrayTree array = (NewArrayTree) exprTree;
            if (array.getInitializers() != null) {
                for (ExpressionTree e : array.getInitializers()) {
                    scanExpressionForHandler(mission, e);
                }
            }

        } else if (exprTree instanceof NewClassTree) {
            NewClassTree classTree = (NewClassTree) exprTree;
            String cid = classTree.getIdentifier().toString();
            if (isHandler(cid)) {
                mission.addHandler(cid);
            }

        } else if (exprTree instanceof ParenthesizedTree) {
            ParenthesizedTree expr = (ParenthesizedTree) exprTree;
            scanExpressionForHandler(mission, expr.getExpression());

        } else if (exprTree instanceof TypeCastTree) {
            TypeCastTree expr = (TypeCastTree) exprTree;
            scanExpressionForHandler(mission, expr.getExpression());
        }
    }


}

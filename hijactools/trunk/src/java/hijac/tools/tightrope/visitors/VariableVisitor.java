package hijac.tools.tightrope.visitors;

import hijac.tools.application.TightRope;
import hijac.tools.tightrope.environments.ClassEnv;
import hijac.tools.tightrope.environments.ObjectEnv;
import hijac.tools.tightrope.environments.ParadigmEnv;
import hijac.tools.tightrope.environments.ProgramEnv;
import hijac.tools.tightrope.environments.VariableEnv;
import hijac.tools.tightrope.utils.Debugger;
import hijac.tools.tightrope.utils.TightRopeTransUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Name;

import com.sun.source.tree.AnnotatedTypeTree;
import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.ArrayAccessTree;
import com.sun.source.tree.ArrayTypeTree;
import com.sun.source.tree.AssertTree;
import com.sun.source.tree.AssignmentTree;
import com.sun.source.tree.BinaryTree;
import com.sun.source.tree.BlockTree;
import com.sun.source.tree.BreakTree;
import com.sun.source.tree.CaseTree;
import com.sun.source.tree.CatchTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.CompoundAssignmentTree;
import com.sun.source.tree.ConditionalExpressionTree;
import com.sun.source.tree.ContinueTree;
import com.sun.source.tree.DoWhileLoopTree;
import com.sun.source.tree.EmptyStatementTree;
import com.sun.source.tree.EnhancedForLoopTree;
import com.sun.source.tree.ErroneousTree;
import com.sun.source.tree.ExpressionStatementTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.ForLoopTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.IfTree;
import com.sun.source.tree.ImportTree;
import com.sun.source.tree.InstanceOfTree;
import com.sun.source.tree.IntersectionTypeTree;
import com.sun.source.tree.LabeledStatementTree;
import com.sun.source.tree.LambdaExpressionTree;
import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.MemberReferenceTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.ModifiersTree;
import com.sun.source.tree.NewArrayTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.ParameterizedTypeTree;
import com.sun.source.tree.ParenthesizedTree;
import com.sun.source.tree.PrimitiveTypeTree;
import com.sun.source.tree.ReturnTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.SwitchTree;
import com.sun.source.tree.SynchronizedTree;
import com.sun.source.tree.ThrowTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.TreeVisitor;
import com.sun.source.tree.TryTree;
import com.sun.source.tree.TypeCastTree;
import com.sun.source.tree.TypeParameterTree;
import com.sun.source.tree.UnaryTree;
import com.sun.source.tree.UnionTypeTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.tree.WhileLoopTree;
import com.sun.source.tree.WildcardTree;

public class VariableVisitor implements TreeVisitor<Map<Name, Tree>, Boolean>
{
  // TODO This returns Names and Trees, it should return Names and
  // TypeKinds....but how?

  private final ProgramEnv programEnv;
  private ObjectEnv objectEnv;
  private ClassEnv classEnv;

  private final static ArrayList<String> GENERIC_PARADIGM_TYPES = ParadigmEnv
      .getGenericParadigmTypes();

  public VariableVisitor(ProgramEnv programEnv, ObjectEnv objectEnv)
  {
    this.programEnv = programEnv;
    this.objectEnv = objectEnv;
    classEnv = objectEnv.getClassEnv();
  }

  public VariableVisitor(ProgramEnv programEnv)
  {
    this.programEnv = programEnv;
  }

  @Override
  public Map<Name, Tree> visitAnnotatedType(AnnotatedTypeTree arg0, Boolean addToEnv)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Name, Tree> visitAnnotation(AnnotationTree arg0, Boolean addToEnv)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Name, Tree> visitArrayAccess(ArrayAccessTree arg0, Boolean addToEnv)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Name, Tree> visitArrayType(ArrayTypeTree arg0, Boolean addToEnv)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Name, Tree> visitAssert(AssertTree arg0, Boolean addToEnv)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Name, Tree> visitAssignment(AssignmentTree arg0, Boolean addToEnv)
  {
    Debugger.log("+++ Var Visitor: Assignment-> " + arg0);

    Map<Name, Tree> returnMap = null;

    ExpressionTree variable = arg0.getVariable();
    Name varName = null;
    Tree expression = arg0.getExpression();
    Tree expressionTree = null;

    if (variable instanceof IdentifierTree)
    {
      varName = ((IdentifierTree) variable).getName();
    }
    if (variable instanceof MemberSelectTree)
    {
      varName = ((MemberSelectTree) variable).getIdentifier();
    }

    if (expression instanceof NewClassTree)
    {
      expressionTree = ((NewClassTree) expression).getIdentifier();
    }

    if (expression instanceof IdentifierTree)
    {
      // expressionTree = expression;

      final VariableEnv variableWeHave = objectEnv.getVariable(varName);
      if (variableWeHave != null)
      {
        if (variableWeHave.isPrimitive())
        {
          expressionTree = expression;
        }
        else if (!(objectEnv.getName().toString().contains(expression.toString())))
        {
          // TODO HACKY! need to get what kind of ID here!
          final String variableInitAndInput = "?" + varName.toString() + "In";
          //
          // objectEnv.addVariable(varName.toString(),
          // "MissionID",
          // variableInitAndInput,
          // variableInitAndInput);

          classEnv.addVariableInit(varName.toString(), variableInitAndInput, true);
        }
      }
    }

    if (expression instanceof LiteralTree)
    {
      expressionTree = expression;
    }

    if (varName != null & expressionTree != null)
    {
      returnMap = new HashMap<Name, Tree>();
      returnMap.put(varName, expressionTree);
      if (objectEnv != null && addToEnv == true)
      {
        Debugger.log("Adding Var Init: name=" + varName + " init="
            + expressionTree.toString());

        String expressionString = expressionTree.toString();
        if (expression instanceof LiteralTree)
        {
          expressionString = TightRopeTransUtils.encodeLiteral((LiteralTree) expression);
        }
        classEnv.addVariableInit(varName.toString(), expressionString, true);

      }
    }

    return returnMap;

  }

  @Override
  public Map<Name, Tree> visitBinary(BinaryTree arg0, Boolean addToEnv)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Name, Tree> visitBlock(BlockTree arg0, Boolean addToEnv)
  {

    HashMap<Name, Tree> returnMap = new HashMap<Name, Tree>();

    List<? extends StatementTree> statements = arg0.getStatements();

    Iterator<? extends StatementTree> i = statements.iterator();

    while (i.hasNext())
    {
      StatementTree st = i.next();

      Map<Name, Tree> statementReturn = st.accept(this, addToEnv);

      if (statementReturn != null)
      {

        // Set<Name> returnMapKeys = returnMap.keySet();

        for (Name n : statementReturn.keySet())
        {
          Debugger.log("Block adding " + n + "->" + statementReturn.get(n)
              + " to returnMap");

          returnMap.put(n, statementReturn.get(n));

        }

      }

    }

    return returnMap;

  }

  @Override
  public Map<Name, Tree> visitBreak(BreakTree arg0, Boolean addToEnv)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Name, Tree> visitCase(CaseTree arg0, Boolean addToEnv)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Name, Tree> visitCatch(CatchTree arg0, Boolean addToEnv)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Name, Tree> visitClass(ClassTree arg0, Boolean addToEnv)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Name, Tree> visitCompilationUnit(CompilationUnitTree arg0, Boolean addToEnv)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Name, Tree> visitCompoundAssignment(CompoundAssignmentTree arg0,
      Boolean addToEnv)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Name, Tree> visitConditionalExpression(ConditionalExpressionTree arg0,
      Boolean addToEnv)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Name, Tree> visitContinue(ContinueTree arg0, Boolean addToEnv)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Name, Tree> visitDoWhileLoop(DoWhileLoopTree arg0, Boolean addToEnv)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Name, Tree> visitEmptyStatement(EmptyStatementTree arg0, Boolean addToEnv)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Name, Tree> visitEnhancedForLoop(EnhancedForLoopTree arg0, Boolean addToEnv)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Name, Tree> visitErroneous(ErroneousTree arg0, Boolean addToEnv)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Name, Tree> visitExpressionStatement(ExpressionStatementTree arg0,
      Boolean addToEnv)
  {
    return arg0.getExpression().accept(this, addToEnv);

  }

  @Override
  public Map<Name, Tree> visitForLoop(ForLoopTree arg0, Boolean addToEnv)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Name, Tree> visitIdentifier(IdentifierTree arg0, Boolean addToEnv)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Name, Tree> visitIf(IfTree arg0, Boolean addToEnv)
  {
    HashMap<Name, Tree> returnMap = new HashMap<Name, Tree>();

    Map<Name, Tree> tempMap = arg0.getThenStatement().accept(this, addToEnv);

    // List<? extends StatementTree> statements = arg0.getStatements();
    if (tempMap != null)
    {
      returnMap.putAll(tempMap);
    }

    if (arg0.getElseStatement() != null)
    {
      tempMap = arg0.getElseStatement().accept(this, addToEnv);

      if (tempMap != null)
      {
        returnMap.putAll(tempMap);
      }
    }

    return returnMap;
  }

  @Override
  public Map<Name, Tree> visitImport(ImportTree arg0, Boolean addToEnv)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Name, Tree> visitInstanceOf(InstanceOfTree arg0, Boolean addToEnv)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Name, Tree> visitIntersectionType(IntersectionTypeTree arg0, Boolean addToEnv)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Name, Tree> visitLabeledStatement(LabeledStatementTree arg0, Boolean addToEnv)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Name, Tree> visitLambdaExpression(LambdaExpressionTree arg0, Boolean addToEnv)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Name, Tree> visitLiteral(LiteralTree arg0, Boolean addToEnv)
  {

    Map<Name, Tree> returnMap = new HashMap<Name, Tree>();

    // returnMap.put(null, (Tree) arg0.);

    return returnMap;

  }

  @Override
  public Map<Name, Tree> visitMemberReference(MemberReferenceTree arg0, Boolean addToEnv)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Name, Tree> visitMemberSelect(MemberSelectTree arg0, Boolean addToEnv)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Name, Tree> visitMethod(MethodTree arg0, Boolean addToEnv)
  {
    Debugger.log("Method Tree = " + arg0 + " and it's body is " + arg0.getBody());
    if (arg0.getName().contentEquals("<init>"))
    {
      return arg0.getBody().accept(this, true);
    }
    if (arg0.getBody() == null)
    {
      // Assume it's an abstract or an interface method
      return new HashMap<Name, Tree>();
    }

    else
    {

      return arg0.getBody().accept(this, false);
    }

  }

  @Override
  public Map<Name, Tree> visitMethodInvocation(MethodInvocationTree arg0, Boolean addToEnv)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Name, Tree> visitModifiers(ModifiersTree arg0, Boolean addToEnv)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Name, Tree> visitNewArray(NewArrayTree arg0, Boolean addToEnv)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Name, Tree> visitNewClass(NewClassTree arg0, Boolean addToEnv)
  {
    Debugger.log("*** VarVisitor: New Class Tree");

    // List<? extends ExpressionTree> args = arg0.getArguments();

    // TODO WHAT TO DO?

    return null;
  }

  @Override
  public Map<Name, Tree> visitOther(Tree arg0, Boolean addToEnv)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Name, Tree> visitParameterizedType(ParameterizedTypeTree arg0,
      Boolean addToEnv)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Name, Tree> visitParenthesized(ParenthesizedTree arg0, Boolean addToEnv)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Name, Tree> visitPrimitiveType(PrimitiveTypeTree arg0, Boolean addToEnv)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Name, Tree> visitReturn(ReturnTree arg0, Boolean addToEnv)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Name, Tree> visitSwitch(SwitchTree arg0, Boolean addToEnv)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Name, Tree> visitSynchronized(SynchronizedTree arg0, Boolean addToEnv)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Name, Tree> visitThrow(ThrowTree arg0, Boolean addToEnv)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Name, Tree> visitTry(TryTree arg0, Boolean addToEnv)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Name, Tree> visitTypeCast(TypeCastTree arg0, Boolean addToEnv)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Name, Tree> visitTypeParameter(TypeParameterTree arg0, Boolean addToEnv)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Name, Tree> visitUnary(UnaryTree arg0, Boolean addToEnv)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Name, Tree> visitUnionType(UnionTypeTree arg0, Boolean addToEnv)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Name, Tree> visitVariable(VariableTree arg0, Boolean addToEnv)
  {

    Debugger.log("Var Visitor: Variable for " + arg0);

    HashMap<Name, Tree> returnMap = new HashMap<Name, Tree>();

    Debugger.log("-> Name = " + arg0.getName() + " Type = " + arg0.getType() + " Init = "
        + arg0.getInitializer());

    Name varName = arg0.getName();

    Tree varType = arg0.getType();

    String init = "";
    if (arg0.getInitializer() != null)
    {
      Map<Name, Tree> initialiser = arg0.getInitializer().accept(this, addToEnv);
      Debugger.log("/*/* Init of " + arg0.getName() + " is = " + initialiser);
      init = arg0.getInitializer().toString();
      Debugger.log("/*/* Init of " + arg0.getName() + " is now = " + init);
    }

    returnMap.put(varName, varType);

    assert (varName != null);

    if (classEnv != null && addToEnv == true
        && (!TightRope.getProgramEnv().containsNonParadigmObject(varName.toString())))
    {
      Debugger.log("var Visitor If");
      if (varType.getKind() == Tree.Kind.PRIMITIVE_TYPE)
      {
        Debugger.log("var Visitor Primitive Type and init = " + init);
        VariableEnv v = new VariableEnv(TightRopeTransUtils.encodeName(varName),
            TightRopeTransUtils.encodeType(varType), init, true);
        v.setProgramType(init);
        v.setInit(init);
        Debugger.log("Adding v: " + v.toString());
        classEnv.addVariable(v);

        if (objectEnv.getVariables().isEmpty())
        {
          objectEnv.addVariable("this", objectEnv.getName() + "Class", "\\circnew "
              + objectEnv.getName() + "Class()", false);
        }

      }
      else if ((!(objectEnv.getName().toString().contains(varType.toString()))))
      {
        Debugger.log("var Visitor var type not this Object'ss name");
        {
          Debugger.log("*/*/ New Parameter for " + classEnv.getName() + " with name= "
              + TightRopeTransUtils.encodeName(varName) + " type = " + varType.toString()
              + " programType = " + varType.toString() + " and primitve = " + false);

          String encodedName = TightRopeTransUtils.encodeName(varName);
          String varTypeString = varType.toString();

          if (GENERIC_PARADIGM_TYPES.contains(varTypeString))
          {
            // String varTypefromName =
            // WordUtils.capitalize(encodedName) + "ID";
          }
          else
          {

          }

          if (addToEnv)
          {
            Debugger.log("*/*/ Adding Parameter " + encodedName + " to ObjectEnv "
                + objectEnv.getName() + " of type "
                + objectEnv.getClass().getCanonicalName());

            boolean stillAddToEnv = true;
            for (VariableEnv v : objectEnv.getProcParameters())
            {
              if (v.getName().equals(encodedName))
              {
                stillAddToEnv = false;
              }
            }

            if (stillAddToEnv)
            {
              VariableEnv v = new VariableEnv(encodedName, varTypeString, init, false);
              v.setProgramType(TightRopeTransUtils.encodeType(varType));

              classEnv.addVariable(v);

              if (objectEnv.getVariables().isEmpty())
              {
                objectEnv.addVariable("this", objectEnv.getName() + "Class", "\\circnew "
                    + objectEnv.getName() + "Class()", false);
              }
            }

          }

        }
      }
      else if (programEnv.getSchedulable(varName) != null)
      {
        Debugger.log("Var Visitor var name is a schedulable");
      }
      else
      {
        Debugger.log("var Visitor add var to Object Env");

        classEnv.addVariable("\\circreftype " + varName.toString() + "Class",
            varType.toString() + "Class", "\\circnew " + varType.toString() + "Class()",
            false);
      }
    }
    return returnMap;
  }

  @Override
  public Map<Name, Tree> visitWhileLoop(WhileLoopTree arg0, Boolean addToEnv)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Name, Tree> visitWildcard(WildcardTree arg0, Boolean addToEnv)
  {
    // TODO Auto-generated method stub
    return null;
  }
}
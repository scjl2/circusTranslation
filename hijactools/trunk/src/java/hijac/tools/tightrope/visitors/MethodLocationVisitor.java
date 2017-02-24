package hijac.tools.tightrope.visitors;

import hijac.tools.tightrope.utils.MethodDestinationE;

import java.util.HashMap;

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

//TODO Finish this for each relevant tree kind.
/**
 * This visitor checks a method to see where it should go: in the process or in
 * the class that represents the object in which it resides. It assumes that the
 * default result is that method should go in the process, so as soon as it
 * finds a reason for the method to go in the class it returns with that result.
 * 
 * @author Matt Luckcuck
 * 
 */
public class MethodLocationVisitor implements TreeVisitor<MethodDestinationE, Boolean>
{
  /**
   * Variable Map of the class this method is in
   */
  private HashMap<Name, Tree> varMap;

  public MethodLocationVisitor(HashMap<Name, Tree> varMap)
  {
    this.varMap = varMap;
  }

  @Override
  public MethodDestinationE visitAnnotatedType(AnnotatedTypeTree arg0, Boolean arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MethodDestinationE visitAnnotation(AnnotationTree arg0, Boolean arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MethodDestinationE visitArrayAccess(ArrayAccessTree arg0, Boolean arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MethodDestinationE visitArrayType(ArrayTypeTree arg0, Boolean arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MethodDestinationE visitAssert(AssertTree arg0, Boolean arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MethodDestinationE visitAssignment(AssignmentTree arg0, Boolean arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MethodDestinationE visitBinary(BinaryTree arg0, Boolean arg1)
  {
    MethodDestinationE methDest = arg0.getLeftOperand().accept(this, arg1);

    if (methDest == MethodDestinationE.CLASS)
    {
      return methDest;
    }

    methDest = arg0.getRightOperand().accept(this, arg1);

    if (methDest == MethodDestinationE.CLASS)
    {
      return methDest;
    }

    return MethodDestinationE.PROCESS;
  }

  @Override
  public MethodDestinationE visitBlock(BlockTree arg0, Boolean arg1)
  {
    for (StatementTree st : arg0.getStatements())
    {
      MethodDestinationE methDest = st.accept(this, arg1);
      if (methDest != null && methDest == MethodDestinationE.CLASS)
      {
        return methDest;
      }
    }

    return MethodDestinationE.PROCESS;
  }

  @Override
  public MethodDestinationE visitBreak(BreakTree arg0, Boolean arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MethodDestinationE visitCase(CaseTree arg0, Boolean arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MethodDestinationE visitCatch(CatchTree arg0, Boolean arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MethodDestinationE visitClass(ClassTree arg0, Boolean arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MethodDestinationE visitCompilationUnit(CompilationUnitTree arg0, Boolean arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MethodDestinationE visitCompoundAssignment(CompoundAssignmentTree arg0,
      Boolean arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MethodDestinationE visitConditionalExpression(ConditionalExpressionTree arg0,
      Boolean arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MethodDestinationE visitContinue(ContinueTree arg0, Boolean arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MethodDestinationE visitDoWhileLoop(DoWhileLoopTree arg0, Boolean arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MethodDestinationE visitEmptyStatement(EmptyStatementTree arg0, Boolean arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MethodDestinationE visitEnhancedForLoop(EnhancedForLoopTree arg0, Boolean arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MethodDestinationE visitErroneous(ErroneousTree arg0, Boolean arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MethodDestinationE visitExpressionStatement(ExpressionStatementTree arg0,
      Boolean arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MethodDestinationE visitForLoop(ForLoopTree arg0, Boolean arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MethodDestinationE visitIdentifier(IdentifierTree arg0, Boolean arg1)
  {
    if (varMap.containsKey(arg0.getName()))
    {
      return MethodDestinationE.CLASS;
    }
    else
    {
      return MethodDestinationE.PROCESS;
    }
  }

  @Override
  public MethodDestinationE visitIf(IfTree arg0, Boolean arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MethodDestinationE visitImport(ImportTree arg0, Boolean arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MethodDestinationE visitInstanceOf(InstanceOfTree arg0, Boolean arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MethodDestinationE visitIntersectionType(IntersectionTypeTree arg0, Boolean arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MethodDestinationE visitLabeledStatement(LabeledStatementTree arg0, Boolean arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MethodDestinationE visitLambdaExpression(LambdaExpressionTree arg0, Boolean arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MethodDestinationE visitLiteral(LiteralTree arg0, Boolean arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MethodDestinationE visitMemberReference(MemberReferenceTree arg0, Boolean arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MethodDestinationE visitMemberSelect(MemberSelectTree arg0, Boolean arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MethodDestinationE visitMethod(MethodTree arg0, Boolean arg1)
  {
//    if (arg0.getBody() != null)
//    {
      return arg0.getBody().accept(this, arg1);
//    }
//    else
//    {
//      return null;
//    }
  }

  @Override
  public MethodDestinationE visitMethodInvocation(MethodInvocationTree arg0, Boolean arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MethodDestinationE visitModifiers(ModifiersTree arg0, Boolean arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MethodDestinationE visitNewArray(NewArrayTree arg0, Boolean arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MethodDestinationE visitNewClass(NewClassTree arg0, Boolean arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MethodDestinationE visitOther(Tree arg0, Boolean arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MethodDestinationE visitParameterizedType(ParameterizedTypeTree arg0,
      Boolean arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MethodDestinationE visitParenthesized(ParenthesizedTree arg0, Boolean arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MethodDestinationE visitPrimitiveType(PrimitiveTypeTree arg0, Boolean arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MethodDestinationE visitReturn(ReturnTree arg0, Boolean arg1)
  {
    return arg0.getExpression().accept(this, arg1);
  }

  @Override
  public MethodDestinationE visitSwitch(SwitchTree arg0, Boolean arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MethodDestinationE visitSynchronized(SynchronizedTree arg0, Boolean arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MethodDestinationE visitThrow(ThrowTree arg0, Boolean arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MethodDestinationE visitTry(TryTree arg0, Boolean arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MethodDestinationE visitTypeCast(TypeCastTree arg0, Boolean arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MethodDestinationE visitTypeParameter(TypeParameterTree arg0, Boolean arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MethodDestinationE visitUnary(UnaryTree arg0, Boolean arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MethodDestinationE visitUnionType(UnionTypeTree arg0, Boolean arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MethodDestinationE visitVariable(VariableTree arg0, Boolean arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MethodDestinationE visitWhileLoop(WhileLoopTree arg0, Boolean arg1)
  {
    MethodDestinationE methDest = arg0.getCondition().accept(this, arg1);

    if (methDest == MethodDestinationE.CLASS)
    {
      return methDest;
    }
    else
    {
      return arg0.getStatement().accept(this, arg1);
    }

  }

  @Override
  public MethodDestinationE visitWildcard(WildcardTree arg0, Boolean arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

}
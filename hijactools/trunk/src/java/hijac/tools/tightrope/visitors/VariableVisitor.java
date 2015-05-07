package hijac.tools.tightrope.visitors;

import hijac.tools.tightrope.environments.ProgramEnv;

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

	private Tree save;
	private ArrayList<Name> returns = new ArrayList<Name>();
	private ClassTree tree;

	private Iterator<StatementTree> i ;
	private ProgramEnv programEnv;


	public VariableVisitor(ProgramEnv programEnv)
	{
		this.programEnv = programEnv;
	}

	@Override
	public Map<Name, Tree> visitAnnotatedType(AnnotatedTypeTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Name, Tree> visitAnnotation(AnnotationTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Name, Tree> visitArrayAccess(ArrayAccessTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Name, Tree> visitArrayType(ArrayTypeTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Name, Tree> visitAssert(AssertTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Name, Tree> visitAssignment(AssignmentTree arg0, Boolean arg1)
	{

		System.out.println("Var Visitor: Assignment");
		System.out.println("-> " + arg0);
//		System.out
//				.println("-> variable.kind = " + arg0.getVariable().getKind());
//		System.out.println("-> Expression = " + arg0.getExpression());
//		System.out.println("-> expression.kind = "
//				+ arg0.getExpression().getKind());
		Map<Name, Tree> returnMap = new HashMap<Name, Tree>();

		// Map<Name, Tree> expressionMap = arg0.getExpression().accept(this,
		// false);
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

		if(expression instanceof IdentifierTree)
		{
			expressionTree = expression;
		}
		
		if(varName != null & expressionTree != null)
		{
			System.out.println("Adding to Var Map");
			returnMap.put(varName, expressionTree);
		}
	
		
		return returnMap;

	}

	@Override
	public Map<Name, Tree> visitBinary(BinaryTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Name, Tree> visitBlock(BlockTree arg0, Boolean arg1)
	{

		System.out.println("Var Visitor: Block");
		System.out.println(arg0);
		HashMap<Name, Tree> returnMap = new HashMap<Name, Tree>();

		List<? extends StatementTree> statements = arg0.getStatements();

		Iterator<? extends StatementTree> i = statements.iterator();

		while (i.hasNext())
		{
			StatementTree st = i.next();
			System.out.println("Var Visitor Block: st kind = " + st.getKind());
			Map<Name, Tree> statementReturn = st.accept(this, false);
			// System.out.println("Var Visitor: Block: in while");
			if (statementReturn != null)
			{
				System.out.println("Var Visitor: Block: Returned Adding");
				returnMap.putAll(statementReturn);
			} else
			{
				System.out.println("Var Visitor: Block: Returned Null");
			}
		}

		return returnMap;

	}

	@Override
	public Map<Name, Tree> visitBreak(BreakTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Name, Tree> visitCase(CaseTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Name, Tree> visitCatch(CatchTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Name, Tree> visitClass(ClassTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Name, Tree> visitCompilationUnit(CompilationUnitTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Name, Tree> visitCompoundAssignment(CompoundAssignmentTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Name, Tree> visitConditionalExpression(
			ConditionalExpressionTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Name, Tree> visitContinue(ContinueTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Name, Tree> visitDoWhileLoop(DoWhileLoopTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Name, Tree> visitEmptyStatement(EmptyStatementTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Name, Tree> visitEnhancedForLoop(EnhancedForLoopTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Name, Tree> visitErroneous(ErroneousTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Name, Tree> visitExpressionStatement(
			ExpressionStatementTree arg0, Boolean arg1)
	{

		System.out.println("Var Visitor: Expression Statement Tree");
		return arg0.getExpression().accept(this, false);

	}

	@Override
	public Map<Name, Tree> visitForLoop(ForLoopTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Name, Tree> visitIdentifier(IdentifierTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Name, Tree> visitIf(IfTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Name, Tree> visitImport(ImportTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Name, Tree> visitInstanceOf(InstanceOfTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Name, Tree> visitIntersectionType(IntersectionTypeTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Name, Tree> visitLabeledStatement(LabeledStatementTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Name, Tree> visitLambdaExpression(LambdaExpressionTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Name, Tree> visitLiteral(LiteralTree arg0, Boolean arg1)
	{

		Map<Name, Tree> returnMap = new HashMap<Name, Tree>();

		// returnMap.put(null, (Tree) arg0.);

		return returnMap;

	}

	@Override
	public Map<Name, Tree> visitMemberReference(MemberReferenceTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Name, Tree> visitMemberSelect(MemberSelectTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Name, Tree> visitMethod(MethodTree arg0, Boolean arg1)
	{

		return arg0.getBody().accept(this, false);

	}

	@Override
	public Map<Name, Tree> visitMethodInvocation(MethodInvocationTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Name, Tree> visitModifiers(ModifiersTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Name, Tree> visitNewArray(NewArrayTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Name, Tree> visitNewClass(NewClassTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Name, Tree> visitOther(Tree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Name, Tree> visitParameterizedType(ParameterizedTypeTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Name, Tree> visitParenthesized(ParenthesizedTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Name, Tree> visitPrimitiveType(PrimitiveTypeTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Name, Tree> visitReturn(ReturnTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Name, Tree> visitSwitch(SwitchTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Name, Tree> visitSynchronized(SynchronizedTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Name, Tree> visitThrow(ThrowTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Name, Tree> visitTry(TryTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Name, Tree> visitTypeCast(TypeCastTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Name, Tree> visitTypeParameter(TypeParameterTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Name, Tree> visitUnary(UnaryTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Name, Tree> visitUnionType(UnionTypeTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Name, Tree> visitVariable(VariableTree arg0, Boolean arg1)
	{

		System.out.println("Var Visitor: Variable for " + arg0);

		HashMap<Name, Tree> r = new HashMap<Name, Tree>();

		System.out.println("-> Name = " + arg0.getName() + " Type = "
				+ arg0.getType());

		r.put(arg0.getName(), arg0.getType());

		// System.out.println("+++ r is null : " + r == null + " +++");


		return r;
	}

	@Override
	public Map<Name, Tree> visitWhileLoop(WhileLoopTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Name, Tree> visitWildcard(WildcardTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
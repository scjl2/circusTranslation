package hijac.tools.tightrope.visitors;

import hijac.tools.application.TightRope;
import hijac.tools.tightrope.environments.VariableEnv;

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

public class ReturnVisitor implements TreeVisitor<ArrayList<String>, Boolean>
{

	// private Tree save;
	private ArrayList<String> returns = new ArrayList<String>();
	// private ClassTree tree;
	// private Iterator<StatementTree> i ;
	// private ProgramEnv programEnv;

	private List<VariableEnv> varMap;

	public ReturnVisitor(List<VariableEnv> varMap)
	{
		this.varMap = varMap;

	}

	public ReturnVisitor()
	{
		this.varMap = new ArrayList<VariableEnv>();

	}

	@Override
	public ArrayList<String> visitBlock(BlockTree arg0, Boolean arg1)
	{
		System.out.println("Return Visitor: Visiting Block Tree");

		List<? extends StatementTree> s = arg0.getStatements();

		Iterator<? extends StatementTree> j = s.iterator();

		while (j.hasNext())
		{
			StatementTree st = j.next();
			if (st instanceof ReturnTree)
			{
				return (st.accept(this, false));
			}
		}

		return null;

	}

	@Override
	public ArrayList<String> visitIdentifier(IdentifierTree arg0,
			Boolean returnExpression)
	{

		System.out.println("Return Visitor: visiting Identifier Tree: "
				+ arg0.getName());

		 ArrayList<String> idReturn = new ArrayList<String>();
		if (!returnExpression)
		{
			for (VariableEnv v : varMap)
			{
				if (v.getVariableName().equals(arg0.getName()))
				{
					idReturn.add(v.getVariableName());
					return idReturn;
				}
			}
			return null;
		}
		else
		{
			idReturn.add(arg0.getName().toString());
			return idReturn;
		}
//		return returns;
	}

	@Override
	public ArrayList<String> visitIf(IfTree arg0, Boolean arg1)
	{
		System.out.println("Return Visistor: visiting if tree "
				+ arg0.getCondition());
		System.out.println("+++ Size of Returns = " + returns.size() + " +++");
		ArrayList<StatementTree> branches = new ArrayList<StatementTree>();

		branches.add(arg0.getThenStatement());

		if (arg0.getElseStatement() != null)
		{
			branches.add(arg0.getElseStatement());
		}

		 ArrayList<String> ifRetunrn = new ArrayList<String>();

		for (StatementTree s : branches)
		{
			System.out.println("Visiting " + s.getKind() + " branch");
			// this may trigger a mission being added to returns (eg above) so
			// we get the same one twice...but it might not happen.
			ArrayList<String> names = s.accept(this, false);

			if (names != null)
			{
				System.out.println("+++ size of names = " + names.size()
						+ " +++");
				for (String n : names)
				{
					System.out.println("+++ names returned = " + n + " +++");
				}

				ifRetunrn.addAll(names);
			}
			else
			{
				System.out.println("+++ twas a null return +++ ");
			}

		}

		return ifRetunrn;
	}

	@Override
	public ArrayList<String> visitMethod(MethodTree arg0, Boolean arg1)
	{
		System.out.println("Return Tree: Visintg method tree");

		List<? extends StatementTree> s = arg0.getBody().getStatements();

		Iterator<? extends StatementTree> j = s.iterator();

		 ArrayList<String> methodReturns = new ArrayList<String>();
		 ArrayList<String> tempReturns = null;

		while (j.hasNext())
		{
			 tempReturns = new ArrayList<String>();
			StatementTree st = j.next();
			System.out.println("Return Visitor: " + st + " is a "
					+ st.getKind());
			
			
			if (st instanceof ExpressionStatementTree)
			{
				System.out.println("Expression Statement");
				st.accept(this, false);
			}

			if (st instanceof ReturnTree)
			{
				System.out.println("Founs Return Tree");
				tempReturns = (st.accept(this, false));

			}

			if (st instanceof IfTree)
			{
				System.out.println("Found If Tree");
				tempReturns =(st.accept(this, false));

			}

		}
		
		if (tempReturns != null)
		{
//			System.out.println("\t \t+++ Temp Returns = " + tempReturns);
			methodReturns.addAll(tempReturns);
//			System.out
//					.println("\t \t+++ Method Returns Now = " + methodReturns);
		}

		return methodReturns;
	}

	@Override
	public ArrayList<String> visitNewClass(NewClassTree arg0,
			Boolean returnExpression)
	{

		System.out.println("Return Visitor: New Clss Tree");

		if (returnExpression)
		{
			return (arg0.getIdentifier().accept(this, true));
		}
		else
		{
			return (arg0.getIdentifier().accept(this, false));
		}

		
	}

	@Override
	public ArrayList<String> visitReturn(ReturnTree arg0, Boolean arg1)
	{

		System.out.println("Return Visitor: visiting return tree " + arg0);
		System.out.println("-> " + arg0.getExpression().getKind());

		return arg0.getExpression().accept(this, true);
	}

	@Override
	public ArrayList<String> visitMethodInvocation(MethodInvocationTree arg0,
			Boolean arg1)
	{
		System.out.println("Return Visitor: method Invocation Found : " + arg0);

		return returns;
	}

	@Override
	public ArrayList<String> visitTypeCast(TypeCastTree arg0, Boolean arg1)
	{
		System.out.println("Return Visitor: Type Cast Tree " + arg0);
		// System.out.println("-> Expression= " + arg0.getExpression());
		// System.out.println("-> Expression.getKind = "
		// + arg0.getExpression().getKind());
		// System.out.println("-> Expression..getName = "
		// + ((IdentifierTree) arg0.getExpression()).getName());

		Name n = ((IdentifierTree) arg0.getExpression()).getName();

		// System.out.println("-> n = " + n);

		if (varMap != null)
		{
			// System.out.println("varmap = " + varMap.toString());
			// System.out.println("-> getVariable = " + varMap.get(n));

			VariableEnv retreivedVar = null;

			for (VariableEnv var : varMap)
			{
				if (var.getVariableName().equals(n.toString()))
				{
					retreivedVar = var;
				}
			}

			// Tree t = (varMap.get(n));

			// System.out.println("-> t = " + t);

			// ArrayList<String> tcReturns = new ArrayList<String>();

			if (n != null)
			{
				// System.out.println("T type= " + t.getKind());
				// IdentifierTree id = (IdentifierTree) t;
				// returns.add(id.getName());

				returns.add(retreivedVar.getVariableName());
			}
			// return arg0.getExpression().accept(this, arg1);
			return returns;
		}
		else
		{
			System.out.println("Return Visitor Returning Null");
			return returns;
		}

	}

	@Override
	public ArrayList<String> visitAnnotatedType(AnnotatedTypeTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> visitAnnotation(AnnotationTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> visitArrayAccess(ArrayAccessTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> visitArrayType(ArrayTypeTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> visitAssert(AssertTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> visitAssignment(AssignmentTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> visitBinary(BinaryTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> visitBreak(BreakTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> visitCase(CaseTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> visitCatch(CatchTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> visitClass(ClassTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> visitCompilationUnit(CompilationUnitTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> visitCompoundAssignment(
			CompoundAssignmentTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> visitConditionalExpression(
			ConditionalExpressionTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> visitContinue(ContinueTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> visitDoWhileLoop(DoWhileLoopTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> visitEmptyStatement(EmptyStatementTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> visitEnhancedForLoop(EnhancedForLoopTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> visitErroneous(ErroneousTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> visitExpressionStatement(
			ExpressionStatementTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> visitForLoop(ForLoopTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> visitImport(ImportTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> visitInstanceOf(InstanceOfTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> visitIntersectionType(IntersectionTypeTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> visitLabeledStatement(LabeledStatementTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> visitLambdaExpression(LambdaExpressionTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> visitLiteral(LiteralTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> visitMemberReference(MemberReferenceTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> visitMemberSelect(MemberSelectTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> visitModifiers(ModifiersTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> visitNewArray(NewArrayTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> visitOther(Tree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> visitParameterizedType(ParameterizedTypeTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> visitParenthesized(ParenthesizedTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> visitPrimitiveType(PrimitiveTypeTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> visitSwitch(SwitchTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> visitSynchronized(SynchronizedTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> visitThrow(ThrowTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> visitTry(TryTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> visitTypeParameter(TypeParameterTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> visitUnary(UnaryTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> visitUnionType(UnionTypeTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> visitVariable(VariableTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> visitWhileLoop(WhileLoopTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> visitWildcard(WildcardTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
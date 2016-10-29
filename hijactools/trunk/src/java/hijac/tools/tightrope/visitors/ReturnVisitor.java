package hijac.tools.tightrope.visitors;

import hijac.tools.tightrope.environments.MethodEnv;
import hijac.tools.tightrope.utils.Debugger;

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

public class ReturnVisitor implements TreeVisitor<ArrayList<Name>, Boolean>
{
	private ArrayList<Name> returns = new ArrayList<Name>();

	private Map<Name, Tree> varMap;
	
	private MethodEnv methodEnv = null;

	public ReturnVisitor(Map<Name, Tree> varMap)
	{

		assert (varMap != null);
		this.varMap = varMap;

	}
	
	public ReturnVisitor(Map<Name, Tree> varMap, MethodEnv methodEnv)
	{
	  this.methodEnv = methodEnv;
	}

	public ReturnVisitor()
	{
		this.varMap = new HashMap<Name, Tree>();
	}

	@Override
	public ArrayList<Name> visitBlock(BlockTree arg0, Boolean arg1)
	{
		Debugger.log("Return Visitor: Visiting Block Tree");

		List<? extends StatementTree> s = arg0.getStatements();

		Iterator<? extends StatementTree> j = s.iterator();

		while (j.hasNext())
		{
			StatementTree st = j.next();
			if (st instanceof ReturnTree)
			{
				return st.accept(this, false);
			}
		}

		return null;

	}

	@Override
	public ArrayList<Name> visitIdentifier(IdentifierTree arg0, Boolean returnExpression)
	{

		Debugger.log("Return Visitor: visiting Identifier Tree: " + arg0.getName()
				+ " arg kind= " + arg0.getKind());

		// This adds a mission to returns

		ArrayList<Name> idReturn = new ArrayList<Name>();
		if (!returnExpression)
		{
			Name t = ((IdentifierTree) varMap.get(arg0.getName())).getName();

			
			idReturn.add(t);
			return idReturn;
		}
		else
		{
			

			Debugger.log("visitID varMap=" + this.varMap.toString());
		
			Name n = arg0.getName();
			Tree t = varMap.get(n);

			if(t != null)
			{

			Name returnName = null;
			if (t instanceof ParameterizedTypeTree)
			{
				returnName = ((IdentifierTree) ((ParameterizedTypeTree) t).getType())
						.getName();
			}
			else if (t instanceof IdentifierTree)
			{
				returnName = ((IdentifierTree) t).getName();
			}

			if (returnName != null)
			{
				idReturn.add(returnName);
			}

			}
			else
			{
				idReturn.add(arg0.getName());
			}
			
			return idReturn;
		}
	}

	@Override
	public ArrayList<Name> visitIf(IfTree arg0, Boolean arg1)
	{
		Debugger.log("Return Visistor: visiting if tree " + arg0.getCondition());
		Debugger.log("+++ Size of Returns = " + returns.size() + " +++");
		ArrayList<StatementTree> branches = new ArrayList<StatementTree>();

		branches.add(arg0.getThenStatement());

		if (arg0.getElseStatement() != null)
		{
			branches.add(arg0.getElseStatement());
		}

		ArrayList<Name> ifRetunrn = new ArrayList<Name>();

		for (StatementTree s : branches)
		{
			Debugger.log("Visiting " + s.getKind() + " branch");
			// this may trigger a mission being added to returns (eg above) so
			// we get the same one twice...but it might not happen.
			ArrayList<Name> names = s.accept(this, false);

			if (names != null)
			{
				Debugger.log("+++ size of names = " + names.size() + " +++");
				for (Name n : names)
				{
					Debugger.log("+++ names returned = " + n + " +++");
				}

				ifRetunrn.addAll(names);
			}
			else
			{
				Debugger.log("+++ twas a null return +++ ");
			}

		}

		return ifRetunrn;
	}

	@Override
	public ArrayList<Name> visitMethod(MethodTree arg0, Boolean arg1)
	{
		Debugger.log("Return Tree: Visintg method tree");
		
		if(arg0.getBody() == null)
		{
		  //assume it's an abstract or interface method
		  return new ArrayList<Name>();
		}
		
		
		List<? extends StatementTree> s = arg0.getBody().getStatements();

		Iterator<? extends StatementTree> j = s.iterator();

		ArrayList<Name> methodReturns = new ArrayList<Name>();
		ArrayList<Name> tempReturns = null;

		while (j.hasNext())
		{
			tempReturns = new ArrayList<Name>();
			StatementTree st = j.next();
			Debugger.log("Return Visitor: " + st + " is a " + st.getKind());

			if (st instanceof ExpressionStatementTree)
			{
				Debugger.log("Expression Statement");
				st.accept(this, false);
			}

			if (st instanceof ReturnTree)
			{
				Debugger.log("Founds Return Tree");
				tempReturns = st.accept(this, false);

			}

			if (st instanceof IfTree)
			{
				Debugger.log("Found If Tree");
				tempReturns = st.accept(this, false);

			}

			if (st instanceof VariableTree)
			{
				Debugger.log("Found Variable Tree");

				st.accept(this, false);
			}
			
		}

		if (tempReturns != null)
		{
			Debugger.log("\t \t+++ Temp Returns = " + tempReturns);
			methodReturns.addAll(tempReturns);
			Debugger.log("\t \t+++ Method Returns Now = " + methodReturns);
		}

		Debugger.log("\t \t+++ Final Method Returns = " + methodReturns);
		return methodReturns;
	}

	@Override
	public ArrayList<Name> visitNewClass(NewClassTree arg0, Boolean returnExpression)
	{

		
		Debugger.log("Return Visitor: New Clss Tree");
		Debugger.log(arg0);
		if (returnExpression)
		{
			return arg0.getIdentifier().accept(this, true);
		}
		else
		{
			return arg0.getIdentifier().accept(this, false);
		}
	}

	@Override
	public ArrayList<Name> visitReturn(ReturnTree arg0, Boolean arg1)
	{
		Debugger.log("Return Visitor: visiting return tree " + arg0);
		Debugger.log("-> " + arg0.getExpression().getKind());
		return arg0.getExpression().accept(this, true);
	}

	@Override
	public ArrayList<Name> visitTypeCast(TypeCastTree arg0, Boolean arg1)
	{
		Debugger.log("Return Visitor: Type Cast Tree " + arg0);
		Debugger.log("-> Expression= " + arg0.getExpression());
		Debugger.log("-> Expression.getKind = " + arg0.getExpression().getKind());
		Debugger.log("-> Expression..getName = "
				+ ((IdentifierTree) arg0.getExpression()).getName());

		Name n = ((IdentifierTree) arg0.getExpression()).getName();

		Debugger.log("-> n = " + n);

		if (varMap != null)
		{
			Debugger.log("visitTypeCast varmap = " + varMap.toString());
			Debugger.log("-> getVariable = " + varMap.get(n));
			Tree t = (varMap.get(n));

			Debugger.log("-> t = " + t);

			ArrayList<Name> tcReturns = new ArrayList<Name>();

			if (n != null)
			{
				Debugger.log("T type= " + t.getKind());
				IdentifierTree id = (IdentifierTree) t;
			
				tcReturns.add(id.getName());
			}
			return tcReturns;
		}
		else
		{
			Debugger.log("Return Visitor Returning Null");
			return null;
		}

	}

	@Override
	public ArrayList<Name> visitVariable(VariableTree arg0, Boolean arg1)
	{
		Debugger.log("Return Visitor: Variable Tree");
		Name n = arg0.getName();
		Tree t = arg0.getType();

		varMap.size();

		varMap.put(n, t);
		Debugger.log("visitVariable varMap=" + varMap.toString());

		return null;
	}

	@Override
	public ArrayList<Name> visitMethodInvocation(MethodInvocationTree arg0, Boolean arg1)
	{
		Debugger.log("Return Visitor: method Invocation Found : " + arg0);

		return null;
	}

	@Override
	public ArrayList<Name> visitAssignment(AssignmentTree arg0, Boolean arg1)
	{
		Debugger.log("Return Visitor: Assignment " + arg0);
		return null;
	}

	@Override
	public ArrayList<Name> visitExpressionStatement(ExpressionStatementTree arg0,
			Boolean arg1)
	{
		return arg0.getExpression().accept(this, false);
	}

	@Override
	public ArrayList<Name> visitAnnotatedType(AnnotatedTypeTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitAnnotation(AnnotationTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitArrayAccess(ArrayAccessTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitArrayType(ArrayTypeTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitAssert(AssertTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitBinary(BinaryTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitBreak(BreakTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitCase(CaseTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitCatch(CatchTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitClass(ClassTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitCompilationUnit(CompilationUnitTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitCompoundAssignment(CompoundAssignmentTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitConditionalExpression(ConditionalExpressionTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitContinue(ContinueTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitDoWhileLoop(DoWhileLoopTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitEmptyStatement(EmptyStatementTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitEnhancedForLoop(EnhancedForLoopTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitErroneous(ErroneousTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitForLoop(ForLoopTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitImport(ImportTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitInstanceOf(InstanceOfTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitIntersectionType(IntersectionTypeTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitLabeledStatement(LabeledStatementTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitLambdaExpression(LambdaExpressionTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitLiteral(LiteralTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitMemberReference(MemberReferenceTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitMemberSelect(MemberSelectTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitModifiers(ModifiersTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitNewArray(NewArrayTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitOther(Tree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitParameterizedType(ParameterizedTypeTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitParenthesized(ParenthesizedTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitPrimitiveType(PrimitiveTypeTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitSwitch(SwitchTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitSynchronized(SynchronizedTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitThrow(ThrowTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitTry(TryTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitTypeParameter(TypeParameterTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitUnary(UnaryTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitUnionType(UnionTypeTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitWhileLoop(WhileLoopTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitWildcard(WildcardTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
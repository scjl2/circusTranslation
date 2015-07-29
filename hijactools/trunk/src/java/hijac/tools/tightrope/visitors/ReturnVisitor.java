package hijac.tools.tightrope.visitors;

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

//	private Tree save;
	private ArrayList<Name> returns = new ArrayList<Name>();
//	private ClassTree tree;
//	private Iterator<StatementTree> i ;
//	private ProgramEnv programEnv;


	private Map<Name, Tree> varMap;
	
	public ReturnVisitor(Map<Name, Tree> varMap)
	{
		this.varMap = varMap;

	}
	
	public ReturnVisitor()
	{
		this.varMap = new HashMap<Name, Tree>();

	}
	
//	public ReturnVisitor(ClassTree tree)
//	{
//		this.tree = tree;
//		
//		List<StatementTree> members = (List<StatementTree>) tree.getMembers();
//		System.out.println("MS Visitor members: " + members);
//
//		i = members.iterator();
//	}
//	
//	public ArrayList<Name> getReturns()
//	{
//		while (i.hasNext())
//		{
//			Tree tlst = i.next();
//			
//			tlst.accept(this, false);
//		}
//		
//		return returns;
//	}

	@Override
	public ArrayList<Name> visitBlock(BlockTree arg0, Boolean arg1)
	{
		System.out.println("Return Visitor: Visiting Block Tree");

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

		System.out.println("Return Visitor: visiting Identifier Tree: "+ arg0.getName());
		
		//This adds a mission to returns
		
		ArrayList<Name> idReturn = new ArrayList<Name>();
		if(!returnExpression)
		{
			Name t = ((IdentifierTree) varMap.get(arg0.getName())).getName();
			
//			returns.add(t);
			idReturn.add(t);
			return idReturn;
		}
		else
		{
//			ArrayList<Name> ids = new ArrayList<Name>(); 
			idReturn.add(arg0.getName());
			return idReturn;
		}
	}

	@Override
	public ArrayList<Name> visitIf(IfTree arg0, Boolean arg1)
	{
		System.out.println("Return Visistor: visiting if tree " + arg0.getCondition());
		System.out.println("+++ Size of Returns = " + returns.size() + " +++");
		ArrayList<StatementTree> branches = new ArrayList<StatementTree>();

		branches.add(arg0.getThenStatement());
		branches.add(arg0.getElseStatement());
		
		ArrayList<Name> ifRetunrn = new ArrayList<Name>();
		
		for (StatementTree s : branches)
		{			
			System.out.println("Visiting " + s.getKind() + " branch");
			//this may trigger a mission being added to returns (eg above) so we get the same one twice...but it might not happen.
			ArrayList<Name> names = s.accept(this, false);
						
			if (names != null)
			{
				System.out.println("+++ size of names = " + names.size() + " +++");
				for(Name n : names)
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
	public ArrayList<Name> visitMethod(MethodTree arg0, Boolean arg1)
	{
		System.out.println("Return Tree: Visintg method tree");

		List<? extends StatementTree> s = arg0.getBody().getStatements();

		Iterator<? extends StatementTree> j = s.iterator();
		
		ArrayList<Name> methodReturns = new ArrayList<Name>();
		ArrayList<Name> tempReturns = null;
		
		
		while (j.hasNext())
		{
			tempReturns = new ArrayList<Name>();
			StatementTree st = j.next();
			System.out.println("Return Visitor: " + st + " is a " + st.getKind());
			
			if (st instanceof ExpressionStatementTree)
			{
				System.out.println("Expression Statement");
				st.accept(this, false);
			}
			
			if (st instanceof ReturnTree)
			{
				System.out.println("Founs Return Tree");
				tempReturns =  st.accept(this, false);
				
				
			}
			
			if (st instanceof IfTree)
			{
				System.out.println("Found If Tree");
				tempReturns =  st.accept(this, false);
				
			}
//			
			
			
//			if( st instanceof ExpressionStatementTree)
//			{				
//				System.out.println("Found Expression Statement");
//				return st.accept(this, false);
//			}
		}
		
		if(tempReturns != null)
		{
			System.out.println("\t \t+++ Temp Returns = " + tempReturns );
			methodReturns.addAll(tempReturns);
			System.out.println("\t \t+++ Method Returns Now = " + methodReturns );
		}
		
		System.out.println("\t \t+++ Final Method Returns = " + methodReturns );
		return methodReturns;
	}

	@Override 
	public ArrayList<Name> visitNewClass(NewClassTree arg0, Boolean returnExpression)
	{

		// ((IdentifierTree) ((NewClassTree) ((ReturnTree)
		// arg0).getExpression()).getIdentifier()).getName() ;
		System.out.println("Return Visitor: New Clss Tree");
		System.out.println(arg0);
		if(returnExpression)
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

		System.out.println("Return Visitor: visiting return tree " + arg0);
		System.out.println("-> " + arg0.getExpression().getKind());
		return arg0.getExpression().accept(this, true);
	}


	@Override
	public ArrayList<Name> visitAnnotatedType(AnnotatedTypeTree arg0,
			Boolean arg1)
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
	public ArrayList<Name> visitAssignment(AssignmentTree arg0, Boolean arg1)
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
	public ArrayList<Name> visitCompilationUnit(CompilationUnitTree arg0,
			Boolean arg1)
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
	public ArrayList<Name> visitConditionalExpression(
			ConditionalExpressionTree arg0, Boolean arg1)
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
	public ArrayList<Name> visitEmptyStatement(EmptyStatementTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitEnhancedForLoop(EnhancedForLoopTree arg0,
			Boolean arg1)
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
	public ArrayList<Name> visitExpressionStatement(
			ExpressionStatementTree arg0, Boolean arg1)
	{
		return arg0.getExpression().accept(this, false);
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
	public ArrayList<Name> visitIntersectionType(IntersectionTypeTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitLabeledStatement(LabeledStatementTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitLambdaExpression(LambdaExpressionTree arg0,
			Boolean arg1)
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
	public ArrayList<Name> visitMemberReference(MemberReferenceTree arg0,
			Boolean arg1)
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
	public ArrayList<Name> visitMethodInvocation(MethodInvocationTree arg0,
			Boolean arg1)
	{
		System.out.println("Return Visitor: method Invocation Found : " + arg0);
		
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
	public ArrayList<Name> visitParameterizedType(ParameterizedTypeTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitParenthesized(ParenthesizedTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitPrimitiveType(PrimitiveTypeTree arg0,
			Boolean arg1)
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
	public ArrayList<Name> visitTypeCast(TypeCastTree arg0, Boolean arg1)
	{
		System.out.println("Return Visitor: Type Cast Tree " + arg0);
		System.out.println("-> Expression= " + arg0.getExpression() );
		System.out.println("-> Expression.getKind = " + arg0.getExpression().getKind());
		System.out.println("-> Expression..getName = " + ((IdentifierTree) arg0.getExpression()).getName());
		
		Name n = ((IdentifierTree) arg0.getExpression()).getName();
		
		System.out.println("-> n = " + n);

		System.out.println("-> getVariable = " + varMap.get(n));
		Tree t = ( varMap.get(n));

		
		System.out.println("-> t = " + t);
		
		ArrayList<Name> tcReturns = new ArrayList<Name>();
		
		if (n != null)
		{
			System.out.println("T type= " + t.getKind());
			IdentifierTree id = (IdentifierTree) t;
//			returns.add(id.getName());
			tcReturns.add(id.getName());
		}
		
//		return arg0.getExpression().accept(this, arg1);
		return tcReturns;

	}

	@Override
	public ArrayList<Name> visitTypeParameter(TypeParameterTree arg0,
			Boolean arg1)
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
	public ArrayList<Name> visitVariable(VariableTree arg0, Boolean arg1)
	{
		System.out.println("Return Visitor: Variable Tree");
//		programEnv.addVariable(arg0.getName(), arg0.getType());

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
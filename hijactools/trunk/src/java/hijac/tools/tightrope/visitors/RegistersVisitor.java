package hijac.tools.tightrope.visitors;

import hijac.tools.analysis.SCJAnalysis;
import hijac.tools.tightrope.environments.FrameworkEnv;
import hijac.tools.tightrope.environments.FrameworkEnv.schedulableType;
import hijac.tools.tightrope.environments.ProgramEnv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;

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

 public class RegistersVisitor implements TreeVisitor<ArrayList<Name> , Void>
   {
	 
	private HashMap<Name, Tree> variables = new HashMap<Name, Tree>();
	ArrayList<Name> schedulables =  new ArrayList<Name>();
	
	ProgramEnv programEnv;
	SCJAnalysis analysis;
	
	public RegistersVisitor(ProgramEnv programEnv, SCJAnalysis analysis)
	{
		this.programEnv = programEnv;
		this.analysis = analysis;
	}
	@Override
	public ArrayList<Name> visitAnnotatedType(AnnotatedTypeTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Name> visitAnnotation(AnnotationTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Name> visitArrayAccess(ArrayAccessTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Name> visitArrayType(ArrayTypeTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Name> visitAssert(AssertTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Name> visitAssignment(AssignmentTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Name> visitBinary(BinaryTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Name> visitBlock(BlockTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Name> visitBreak(BreakTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Name> visitCase(CaseTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Name> visitCatch(CatchTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Name> visitClass(ClassTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Name> visitCompilationUnit(CompilationUnitTree arg0,
			Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Name> visitCompoundAssignment(CompoundAssignmentTree arg0,
			Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Name> visitConditionalExpression(
			ConditionalExpressionTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Name> visitContinue(ContinueTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Name> visitDoWhileLoop(DoWhileLoopTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Name> visitEmptyStatement(EmptyStatementTree arg0,
			Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Name> visitEnhancedForLoop(EnhancedForLoopTree arg0,
			Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Name> visitErroneous(ErroneousTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Name> visitExpressionStatement(ExpressionStatementTree arg0, Void arg1)
	{
		return arg0.getExpression().accept(this, null);
//		if(((ExpressionStatementTree) st).getExpression() instanceof MethodInvocationTree)
//			{
//				System.out.println("Mission Visitor: Found Method Invocation");
//				MethodInvocationTree mit = (MethodInvocationTree) ((ExpressionStatementTree) st).getExpression();
//				
//				System.out.println(mit.getMethodSelect());
//				System.out.println(mit.getMethodSelect().getKind());
//				
//				if (mit.getMethodSelect() instanceof MemberSelectTree)
//				{
//					MemberSelectTree mst = (MemberSelectTree) mit.getMethodSelect();
//					
//					if(mst.getIdentifier().contentEquals("register"))
//					{
////						schedulables
//						
//						
//						schedulables.add( (Name) variables.get(((MethodTree) mst.getExpression()).getName()) );
//						
//					}
//					//get identifier
//					//if register then add
//				}
//			}
	}
	@Override
	public ArrayList<Name> visitForLoop(ForLoopTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public  ArrayList<Name> visitIdentifier(IdentifierTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Name> visitIf(IfTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Name> visitImport(ImportTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Name> visitInstanceOf(InstanceOfTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Name> visitIntersectionType(IntersectionTypeTree arg0,
			Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Name> visitLabeledStatement(LabeledStatementTree arg0,
			Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Name> visitLambdaExpression(LambdaExpressionTree arg0,
			Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Name> visitLiteral(LiteralTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Name> visitMemberReference(MemberReferenceTree arg0,
			Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Name> visitMemberSelect(MemberSelectTree arg0, Void arg1)
	{
				
		if(arg0.getIdentifier().contentEquals("register"))
		{
			System.out.println("Registers Visitor: Returning " + (Name) variables.get(((MethodTree) arg0.getExpression()).getName()));
//			ArrayList<Name> a = new ArrayList<Name>();
//					a.add( (Name) variables.get(((MethodTree) arg0.getExpression()).getName()) );
			Name name = (Name) variables.get(((MethodTree) arg0.getExpression()).getName());
			
			
			
			programEnv.addSchedulable(getSchedulableType(name) , name)	;	
					
			
				
		}
		return null;
	}
	
	private schedulableType getSchedulableType(Name name)
	{
		TypeElement element = null;
		
		for(TypeElement t : analysis.getTypeElements())
		{
			if(t.getSimpleName() == name)
			{
				element = t;
			}
		}
		
		if(element.getSuperclass().toString().contains("ManagedThread"))
		{
			return FrameworkEnv.schedulableType.MT;
		}
	
		return null;
	}
	@Override
	public ArrayList<Name> visitMethod(MethodTree arg0, Void arg1)
	{
		MethodTree o = arg0;
		System.out.println(o.getName());

		if (o.getName().contentEquals("initialize"))
		{
			System.out.println("Mission Visitor: J iterator");
			List<StatementTree> s = (List<StatementTree>) o.getBody().getStatements();

			Iterator<StatementTree> j = s.iterator();

			
			//iterate through the statements in the Init method
			while (j.hasNext())
			{
				StatementTree st =  j.next();
				
				System.out.println("Register Visistor: j = "+ st.getKind());
				
//				ArrayList<Name> name = 
						st.accept(this, null);
				
//				if(name != null)
//				{
//					schedulables.add(name);
//				}
//				
//				if (st instanceof ExpressionStatementTree)
//				{
//					if(((ExpressionStatementTree) st).getExpression() instanceof MethodInvocationTree)
//					{
//						System.out.println("Mission Visitor: Found Method Invocation");
//						MethodInvocationTree mit = (MethodInvocationTree) ((ExpressionStatementTree) st).getExpression();
//						
//						System.out.println(mit.getMethodSelect());
//						System.out.println(mit.getMethodSelect().getKind());
//						
//						if (mit.getMethodSelect() instanceof MemberSelectTree)
//						{
//							MemberSelectTree mst = (MemberSelectTree) mit.getMethodSelect();
//							
//							if(mst.getIdentifier().contentEquals("register"))
//							{
////								schedulables
//								
//								
//								schedulables.add( (Name) variables.get(((MethodTree) mst.getExpression()).getName()) );
//								
//							}
//							//get identifier
//							//if register then add
//						}
//					}
//				}
				
				
				
				
			}

		}
		return schedulables;
	}
	@Override
	public ArrayList<Name> visitMethodInvocation(MethodInvocationTree arg0, Void arg1)
	{
		
		System.out.println("Registers Visitor: Method Invocation Tree");
		
		return arg0.getMethodSelect().accept(this, null);
//		MethodInvocationTree mit = (MethodInvocationTree) ((ExpressionStatementTree) st).getExpression();
//		
//		System.out.println(mit.getMethodSelect());
//		System.out.println(mit.getMethodSelect().getKind());
//		
//		if (mit.getMethodSelect() instanceof MemberSelectTree)
//		{
//			MemberSelectTree mst = (MemberSelectTree) mit.getMethodSelect();
//			
//			if(mst.getIdentifier().contentEquals("register"))
//			{
////				schedulables
//				
//				
//				schedulables.add( (Name) variables.get(((MethodTree) mst.getExpression()).getName()) );
//				
//			}
//			//get identifier
//			//if register then add
//		}
	}
	@Override
	public ArrayList<Name> visitModifiers(ModifiersTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Name> visitNewArray(NewArrayTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Name> visitNewClass(NewClassTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Name> visitOther(Tree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Name> visitParameterizedType(ParameterizedTypeTree arg0,
			Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Name> visitParenthesized(ParenthesizedTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Name> visitPrimitiveType(PrimitiveTypeTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Name> visitReturn(ReturnTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Name> visitSwitch(SwitchTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public  ArrayList<Name> visitSynchronized(SynchronizedTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Name> visitThrow(ThrowTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Name> visitTry(TryTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Name> visitTypeCast(TypeCastTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Name> visitTypeParameter(TypeParameterTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Name> visitUnary(UnaryTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Name> visitUnionType(UnionTypeTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Name> visitVariable(VariableTree arg0, Void arg1)
	{
		System.out.println("Registers Visitor: Variable Tree Found");
		System.out.println("-> " + arg0.toString());
		
		variables.put( arg0.getName(), arg0.getType() );
		
		return null;
	}
	@Override
	public ArrayList<Name> visitWhileLoop(WhileLoopTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<Name> visitWildcard(WildcardTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	} 

	
	   
   }
package hijac.tools.tightrope.visitors;

import hijac.tools.analysis.SCJAnalysis;
import hijac.tools.tightrope.environments.MissionEnv;
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

public class RegistersVisitor implements TreeVisitor<Name, Void>
{
	private MissionEnv missionEnv;
//	private static SCJAnalysis analysis;

	public RegistersVisitor(MissionEnv missionEnv, SCJAnalysis analysis)
	{
		this.missionEnv = missionEnv;
//		RegistersVisitor.analysis = analysis;

	}

	// private schedulableType getSchedulableType(Name name)
	// {
	// TypeElement element = null;
	//
	// for (TypeElement t : analysis.getTypeElements())
	// {
	// if (t.getSimpleName() == name)
	// {
	// element = t;
	// }
	// }
	//
	// if (element.getSuperclass().toString().contains("ManagedThread"))
	// {
	// return FrameworkEnv.schedulableType.MT;
	// }
	//
	// return null;
	// }

	@Override
	public Name visitAnnotatedType(AnnotatedTypeTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name visitAnnotation(AnnotationTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name visitArrayAccess(ArrayAccessTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name visitArrayType(ArrayTypeTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name visitAssert(AssertTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name visitAssignment(AssignmentTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name visitBinary(BinaryTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name visitBlock(BlockTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name visitBreak(BreakTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name visitCase(CaseTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name visitCatch(CatchTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name visitClass(ClassTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name visitCompilationUnit(CompilationUnitTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name visitCompoundAssignment(CompoundAssignmentTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name visitConditionalExpression(ConditionalExpressionTree arg0,
			Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name visitContinue(ContinueTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name visitDoWhileLoop(DoWhileLoopTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name visitEmptyStatement(EmptyStatementTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name visitEnhancedForLoop(EnhancedForLoopTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name visitErroneous(ErroneousTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name visitExpressionStatement(ExpressionStatementTree arg0, Void arg1)
	{
		return arg0.getExpression().accept(this, null);
		// if(((ExpressionStatementTree) st).getExpression() instanceof
		// MethodInvocationTree)
		// {
		// System.out.println("Mission Visitor: Found Method Invocation");
		// MethodInvocationTree mit = (MethodInvocationTree)
		// ((ExpressionStatementTree) st).getExpression();
		//
		// System.out.println(mit.getMethodSelect());
		// System.out.println(mit.getMethodSelect().getKind());
		//
		// if (mit.getMethodSelect() instanceof MemberSelectTree)
		// {
		// MemberSelectTree mst = (MemberSelectTree) mit.getMethodSelect();
		//
		// if(mst.getIdentifier().contentEquals("register"))
		// {
		// // schedulables
		//
		//
		// schedulables.add( (Name) variables.get(((MethodTree)
		// mst.getExpression()).getName()) );
		//
		// }
		// //get identifier
		// //if register then add
		// }
		// }
	}

	@Override
	public Name visitForLoop(ForLoopTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name visitIdentifier(IdentifierTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name visitIf(IfTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name visitImport(ImportTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name visitInstanceOf(InstanceOfTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name visitIntersectionType(IntersectionTypeTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name visitLabeledStatement(LabeledStatementTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name visitLambdaExpression(LambdaExpressionTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name visitLiteral(LiteralTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name visitMemberReference(MemberReferenceTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name visitMemberSelect(MemberSelectTree arg0, Void arg1)
	{

		if (arg0.getIdentifier().contentEquals("register"))
		{
			System.out.println("Rgisters Visitor: " + arg0);
			System.out.println("Rgisters Visitor: Expression "
					+ arg0.getExpression());
			System.out.println("Rgisters Visitor: Identifier "
					+ arg0.getIdentifier());

			if(arg0.getExpression() instanceof IdentifierTree)
			{
			
				IdentifierTree identifier = (IdentifierTree) arg0.getExpression();
			System.out.println("Registers Visitor: getExpression().getName() "
					+ (identifier.getName()));

			System.out.println("Registers Visitor: Returning "
					+ missionEnv.getVariable(identifier.getName()));
			// ArrayList<Name> a = new ArrayList<Name>();
			// a.add( (Name) variables.get(((MethodTree)
			// arg0.getExpression()).getName()) );
			Name name = 
					((IdentifierTree) missionEnv.getVariable(identifier.getName())).getName();

			// programEnv.addSchedulable(getSchedulableType(name), name);

			return name;
			}
			
			if(arg0.getExpression() instanceof NewClassTree)
			{
				NewClassTree newClass = (NewClassTree) arg0.getExpression();
				
				System.out.println("Registers Visitor: getExpression().getName() "
						+ (((IdentifierTree) newClass.getIdentifier()).getName()));
				
				System.out.println("Registers Visitor: Returning "
						+ missionEnv.getVariable(((IdentifierTree) newClass.getIdentifier()).getName()));
				
				Name name = (((IdentifierTree) newClass.getIdentifier()).getName());
				
//						((IdentifierTree) programEnv.getVariable(((IdentifierTree) newClass.getIdentifier()).getName())).getName();

				return name;
			}

		}
		return null;
	}

	@Override
	public Name visitMethod(MethodTree arg0, Void arg1)
	{
		System.out.println("Registers Visitor: Method Tree");

		// MethodTree o = arg0;
		// System.out.println(o.getName());
		//
		// if (o.getName().contentEquals("initialize"))
		// {
		// System.out.println("Mission Visitor: J iterator");
		// List<StatementTree> s = (List<StatementTree>) o.getBody()
		// .getStatements();
		//
		// Iterator<StatementTree> j = s.iterator();
		//
		// // iterate through the statements in the Init method
		// while (j.hasNext())
		// {
		// StatementTree st = j.next();
		//
		// System.out.println("Register Visistor: j = " + st.getKind());
		//
		// // ArrayList<Name> name =
		// st.accept(this, null);
		//
		// // if(name != null)
		// // {
		// // schedulables.add(name);
		// // }
		// //
		// // if (st instanceof ExpressionStatementTree)
		// // {
		// // if(((ExpressionStatementTree) st).getExpression() instanceof
		// // MethodInvocationTree)
		// // {
		// // System.out.println("Mission Visitor: Found Method Invocation");
		// // MethodInvocationTree mit = (MethodInvocationTree)
		// // ((ExpressionStatementTree) st).getExpression();
		// //
		// // System.out.println(mit.getMethodSelect());
		// // System.out.println(mit.getMethodSelect().getKind());
		// //
		// // if (mit.getMethodSelect() instanceof MemberSelectTree)
		// // {
		// // MemberSelectTree mst = (MemberSelectTree)
		// // mit.getMethodSelect();
		// //
		// // if(mst.getIdentifier().contentEquals("register"))
		// // {
		// // // schedulables
		// //
		// //
		// // schedulables.add( (Name) variables.get(((MethodTree)
		// // mst.getExpression()).getName()) );
		// //
		// // }
		// // //get identifier
		// // //if register then add
		// // }
		// // }
		// // }
		//
		// }
		//
		// }
		return null;
	}

	@Override
	public Name visitMethodInvocation(MethodInvocationTree arg0, Void arg1)
	{

		System.out.println("Registers Visitor: Method Invocation Tree");

		return arg0.getMethodSelect().accept(this, null);
		// MethodInvocationTree mit = (MethodInvocationTree)
		// ((ExpressionStatementTree) st).getExpression();
		//
		// System.out.println(mit.getMethodSelect());
		// System.out.println(mit.getMethodSelect().getKind());
		//
		// if (mit.getMethodSelect() instanceof MemberSelectTree)
		// {
		// MemberSelectTree mst = (MemberSelectTree) mit.getMethodSelect();
		//
		// if(mst.getIdentifier().contentEquals("register"))
		// {
		// // schedulables
		//
		//
		// schedulables.add( (Name) variables.get(((MethodTree)
		// mst.getExpression()).getName()) );
		//
		// }
		// //get identifier
		// //if register then add
		// }
	}

	@Override
	public Name visitModifiers(ModifiersTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name visitNewArray(NewArrayTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name visitNewClass(NewClassTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name visitOther(Tree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name visitParameterizedType(ParameterizedTypeTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name visitParenthesized(ParenthesizedTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name visitPrimitiveType(PrimitiveTypeTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name visitReturn(ReturnTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name visitSwitch(SwitchTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name visitSynchronized(SynchronizedTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name visitThrow(ThrowTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name visitTry(TryTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name visitTypeCast(TypeCastTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name visitTypeParameter(TypeParameterTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name visitUnary(UnaryTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name visitUnionType(UnionTypeTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name visitVariable(VariableTree arg0, Void arg1)
	{
		System.out.println("Registers Visitor: Variable Tree Found");
//		missionEnv.addVariable(arg0.getName().toString(), arg0.getType().toString(), "");
		
		return null;
	}

	@Override
	public Name visitWhileLoop(WhileLoopTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name visitWildcard(WildcardTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
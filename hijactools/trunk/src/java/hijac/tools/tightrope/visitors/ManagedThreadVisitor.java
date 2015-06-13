package hijac.tools.tightrope.visitors;

import java.util.ArrayList;
import java.util.HashMap;

import hijac.tools.analysis.SCJAnalysis;
import hijac.tools.tightrope.environments.ProgramEnv;

import javax.lang.model.element.Modifier;
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
import com.sun.source.tree.Tree.Kind;
import com.sun.source.tree.TreeVisitor;
import com.sun.source.tree.TryTree;
import com.sun.source.tree.TypeCastTree;
import com.sun.source.tree.TypeParameterTree;
import com.sun.source.tree.UnaryTree;
import com.sun.source.tree.UnionTypeTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.tree.WhileLoopTree;
import com.sun.source.tree.WildcardTree;

public class ManagedThreadVisitor implements TreeVisitor<ArrayList<Name>, Void>
{
	// Going to use this to test for synchronised methods

	// private HashMap<Name, Tree> variables = new HashMap<Name, Tree>();

	// ArrayList<Name> schedulables = new ArrayList<Name>();

	ProgramEnv programEnv;
	SCJAnalysis analysis;
	HashMap<Name, Tree> variables;
	String packagePrefix;

	public ManagedThreadVisitor(ProgramEnv programEnv, SCJAnalysis analysis,
			HashMap<Name, Tree> vars, String packagePrefix)
	{
		this.programEnv = programEnv;
		this.analysis = analysis;
		this.variables = vars;
		this.packagePrefix = packagePrefix;

		System.out.println("*** Variables ***");
		System.out.println(variables);
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
		System.out.println("*** mtVisitor block *** ");
		ArrayList<Name> blockReturns = new ArrayList<Name>();

		for (StatementTree st : arg0.getStatements())
		{
			System.out.println("*** st type = " + st.getKind() + " *** ");
			ArrayList<Name> tmp = st.accept(this, arg1);
			if (tmp != null)
			{
				blockReturns.addAll(tmp);
			}
		}

		return blockReturns;
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
	public ArrayList<Name> visitExpressionStatement(
			ExpressionStatementTree arg0, Void arg1)
	{
		System.out.println(" *** mtVisitor expression");
		return arg0.getExpression().accept(this, arg1);
	}

	@Override
	public ArrayList<Name> visitForLoop(ForLoopTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitIdentifier(IdentifierTree arg0, Void arg1)
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitMethod(MethodTree arg0, Void arg1)
	{
		System.out.println("*** mtVisitor method ***");
		ArrayList<Name> methReturns = new ArrayList<Name>();

		for (StatementTree st : arg0.getBody().getStatements())
		{
			ArrayList<Name> tmp = st.accept(this, arg1);
			if (tmp != null)
			{
				methReturns.addAll(tmp);
			}
		}

		return methReturns;
	}

	@Override
	public ArrayList<Name> visitMethodInvocation(MethodInvocationTree arg0,
			Void arg1)
	{
		System.out.println("+++ Method Invocation: "
				+ arg0.getMethodSelect().toString());

		if (arg0.getMethodSelect().getKind() == Kind.MEMBER_SELECT)
		{
			MemberSelectTree method = (MemberSelectTree) arg0.getMethodSelect();
			// get the name of the method we are calling...
			Name methodName = method.getIdentifier();
			// ...and the variable we're calling it on
			ExpressionTree classNameExpression = method.getExpression();

			if (classNameExpression instanceof IdentifierTree)
			{

				IdentifierTree classNameIdentifier = (IdentifierTree) classNameExpression;
				// get the name of the variable we're calling a method on, as a
				// Name
				Name className = classNameIdentifier.getName();

				// get the type of the variable, from the variables map
				Tree classTree = variables.get(className);

				if (classTree instanceof IdentifierTree)
				{
					// get the Name of the actual class, not just the variable
					// name
					className = ((IdentifierTree) classTree).getName();
				}

				// get the full name of the class...
				String fullName = packagePrefix + className;
				// find the TypeElement for the class, using the full name
				TypeElement type = analysis.ELEMENTS.getTypeElement(fullName);
				// and then find the ClassTree for that class, using the
				// TypeElement (phew!)
				if (type != null)
				{
					ClassTree ct = analysis.TREES.getTree(type);
					ArrayList<Name> tmp = new ArrayList<Name>();

					for (Tree t : ct.getMembers())
					{
						if (t instanceof MethodTree)
						{
							System.out.println("*** methodTree *** ");
							MethodTree mt = (MethodTree) t;
							if (mt.getName().contentEquals(methodName))
							{
								System.out.println("*** found the method *** ");
								if (mt.getModifiers().getFlags()
										.contains(Modifier.SYNCHRONIZED))
								{
									System.out
											.println("*** and it's synchronised *** ");
									tmp.add(methodName);

								}
							}
						}
					}

					return tmp;
				}
			}
		}

		return null;
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
	public ArrayList<Name> visitSynchronized(SynchronizedTree arg0, Void arg1)
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
		System.out.println("*** mtVisitor try *** ");

		return arg0.getBlock().accept(this, arg1);
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
		System.out.println("*** mtVisitor variable ***");
		if(arg0.getInitializer() != null)
		{
			return arg0.getInitializer().accept(this, arg1);
		}
		else
		{
			return null;
		}
	}

	@Override
	public ArrayList<Name> visitWhileLoop(WhileLoopTree arg0, Void arg1)
	{
		System.out.println("*** mtVisitor while *** ");
		return arg0.getStatement().accept(this, arg1);
	}

	@Override
	public ArrayList<Name> visitWildcard(WildcardTree arg0, Void arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
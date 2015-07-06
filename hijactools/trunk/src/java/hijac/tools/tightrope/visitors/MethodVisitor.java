package hijac.tools.tightrope.visitors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.Name;
import javax.lang.model.type.TypeKind;

import hijac.tools.tightrope.environments.MethodEnv;

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
/**
 * Visitor for capturing the name, return type, parameters and return value
 * of a method and returning these as a <code>MethodEnv</code>
 * @author Matt Luckcuck
 *
 */

//TODO This may need some more context, i.e. the Env that it will reside in
public class MethodVisitor implements TreeVisitor<MethodEnv, Boolean>
{
	MethodEnv methodEnv;

	@Override
	public MethodEnv visitAnnotatedType(AnnotatedTypeTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitAnnotation(AnnotationTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitArrayAccess(ArrayAccessTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitArrayType(ArrayTypeTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitAssert(AssertTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitAssignment(AssignmentTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitBinary(BinaryTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitBlock(BlockTree arg0, Boolean arg1)
	{
		System.out.println("+++ Method Visitor: Block +++");
		
		//TODO Make this work, depends on what structure I'm adding the statements to
		for(StatementTree st :arg0.getStatements())
		{
			//TODO Something
		}
		
		return null;
	}

	@Override
	public MethodEnv visitBreak(BreakTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitCase(CaseTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitCatch(CatchTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitClass(ClassTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitCompilationUnit(CompilationUnitTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitCompoundAssignment(CompoundAssignmentTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitConditionalExpression(ConditionalExpressionTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitContinue(ContinueTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitDoWhileLoop(DoWhileLoopTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitEmptyStatement(EmptyStatementTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitEnhancedForLoop(EnhancedForLoopTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitErroneous(ErroneousTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitExpressionStatement(ExpressionStatementTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitForLoop(ForLoopTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitIdentifier(IdentifierTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitIf(IfTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitImport(ImportTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitInstanceOf(InstanceOfTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitIntersectionType(IntersectionTypeTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitLabeledStatement(LabeledStatementTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitLambdaExpression(LambdaExpressionTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitLiteral(LiteralTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitMemberReference(MemberReferenceTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitMemberSelect(MemberSelectTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	
	//This should be the entry and exit point, since I'm translating a method
	@SuppressWarnings("unchecked")
	@Override
	public MethodEnv visitMethod(MethodTree mt, Boolean arg1)
	{
		//get name
		Name methodName = mt.getName();
		
		//get return type
		// TODO This needs to now actually figure out WHAT is
				// returned and return it too
		Tree returnType = mt.getReturnType();
		TypeKind returnTypeKind = TypeKind.ERROR;

		if (returnType instanceof PrimitiveTypeTree)
		{
			returnTypeKind = ((PrimitiveTypeTree) mt.getReturnType())
					.getPrimitiveTypeKind();
		}
		
		//return values
		ArrayList<Name> returnsValues = mt.accept(
				new ReturnVisitor(null), null);
		
		
		@SuppressWarnings("rawtypes")
		Map parameters = new HashMap();
		for (VariableTree vt : mt.getParameters())
		{
			parameters.put(vt.getName().toString(), vt.getType());
		}
		
		
		//get body
		Object body = mt.getBody().accept(this, arg1);
		
		
		return new MethodEnv(methodName, returnTypeKind, returnsValues, parameters, body);
	}

	@Override
	public MethodEnv visitMethodInvocation(MethodInvocationTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitModifiers(ModifiersTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitNewArray(NewArrayTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitNewClass(NewClassTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitOther(Tree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitParameterizedType(ParameterizedTypeTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitParenthesized(ParenthesizedTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitPrimitiveType(PrimitiveTypeTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitReturn(ReturnTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitSwitch(SwitchTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitSynchronized(SynchronizedTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitThrow(ThrowTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitTry(TryTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitTypeCast(TypeCastTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitTypeParameter(TypeParameterTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitUnary(UnaryTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitUnionType(UnionTypeTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitVariable(VariableTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitWhileLoop(WhileLoopTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodEnv visitWildcard(WildcardTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
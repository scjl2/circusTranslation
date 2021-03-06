package hijac.tools.tightrope.visitors;

import java.util.HashMap;

import hijac.tools.analysis.SCJAnalysis;
import hijac.tools.application.TightRope;
import hijac.tools.tightrope.environments.MissionEnv;
import hijac.tools.tightrope.environments.VariableEnv;
import hijac.tools.tightrope.utils.Debugger;

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
	private HashMap<Name, Tree> initVarMap;

	public RegistersVisitor(MissionEnv missionEnv, SCJAnalysis analysis)
	{
		this.missionEnv = missionEnv;
	}

	public RegistersVisitor(MissionEnv missionEnv, SCJAnalysis analysis,
			HashMap<Name, Tree> initVarMap)
	{
		this.missionEnv = missionEnv;
		this.initVarMap = initVarMap;
	}

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

		Name name = null;

		final Name memberSelected = arg0.getIdentifier();
		if (memberSelected.contentEquals("register"))
		{
			Debugger.log("Rgisters Visitor: " + arg0);
			final ExpressionTree expression = arg0.getExpression();
			Debugger.log("Rgisters Visitor: Expression " + expression);
			Debugger.log("Rgisters Visitor: Identifier " + memberSelected);

			if (expression instanceof IdentifierTree)
			{

				IdentifierTree expressionIdentifierTree = (IdentifierTree) expression;
				Debugger.log("Registers Visitor: getExpression().getName() "
								+ (expressionIdentifierTree.getName()));

				Debugger.log("Registers Visitor: Returning "
						+ missionEnv.getVariable(expressionIdentifierTree
								.getName()));
				// ArrayList<Name> a = new ArrayList<Name>();
				// a.add( (Name) variables.get(((MethodTree)
				// arg0.getExpression()).getName()) );
				Name identifirerName = expressionIdentifierTree.getName();

				VariableEnv vEnv = missionEnv.getVariable(identifirerName);

				if (vEnv != null)
				{
					name = ((IdentifierTree) vEnv).getName();
				}
				else
				{
					assert (initVarMap != null);

					if (initVarMap.containsKey(identifirerName))
					{
						Debugger.log("*** initVarMap returns " +initVarMap.get(identifirerName));
						
						
						
						
						for(TypeElement te : TightRope.ANALYSIS.getTypeElements())
						{
							if(te.getSimpleName().contentEquals(initVarMap.get(identifirerName).toString()))
							{
							name = te.getSimpleName() ;
							}
						}
						
						
					}
				}

				// programEnv.addSchedulable(getSchedulableType(name), name);

				return name;
			}

			if (expression instanceof NewClassTree)
			{
				NewClassTree newClass = (NewClassTree) expression;

				Debugger.log("Registers Visitor: getExpression().getName() "
								+ (((IdentifierTree) newClass.getIdentifier())
										.getName()));

				Debugger.log("Registers Visitor: Returning "
						+ missionEnv.getVariable(((IdentifierTree) newClass
								.getIdentifier()).getName()));

				name = (((IdentifierTree) newClass.getIdentifier()).getName());

				
				return name;
			}

		}
		return null;
	}

	@Override
	public Name visitMethod(MethodTree arg0, Void arg1)
	{
		Debugger.log("Registers Visitor: Method Tree");

		
	
		return null;
	}

	@Override
	public Name visitMethodInvocation(MethodInvocationTree arg0, Void arg1)
	{

		Debugger.log("Registers Visitor: Method Invocation Tree");

		return arg0.getMethodSelect().accept(this, null);
		
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
		Debugger.log("Registers Visitor: Variable Tree Found");
		// missionEnv.addVariable(arg0.getName().toString(),
		// arg0.getType().toString(), "");

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
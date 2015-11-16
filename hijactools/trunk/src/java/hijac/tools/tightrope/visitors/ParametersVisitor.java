package hijac.tools.tightrope.visitors;

import hijac.tools.application.TightRope;
import hijac.tools.tightrope.environments.ClassEnv;
import hijac.tools.tightrope.environments.ObjectEnv;
import hijac.tools.tightrope.environments.ParadigmEnv;
import hijac.tools.tightrope.environments.ProgramEnv;
import hijac.tools.tightrope.environments.VariableEnv;

import java.util.ArrayList;
import java.util.List;

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

public class ParametersVisitor implements
		TreeVisitor<VariableEnv, VariableEnv>
{
	// TODO This returns Names and Trees, it should return Names and
	// TypeKinds....but how?

	// TODO CLEAN UP
	// private Tree save;
	// private ArrayList<Name> returns = new ArrayList<Name>();
	// private ClassTree tree;

	// private Iterator<StatementTree> i ;
	private final ProgramEnv programEnv;
	private ObjectEnv objectEnv;
	private ClassEnv classEnv;

	private final static ArrayList<String> GENERIC_PARADIGM_TYPES = ParadigmEnv
			.getGenericParadigmTypes();

	public ParametersVisitor(ProgramEnv programEnv,
			ObjectEnv whereTheVariableNameIs, ClassEnv classEnv)
	{
		super();
		this.programEnv = programEnv;
		this.objectEnv = whereTheVariableNameIs;
		this.classEnv = classEnv;
	}

	@Override
	public VariableEnv visitVariable(VariableTree arg0, VariableEnv param)
	{
		System.out.println("Params Visitor: Variable " + arg0.toString());
		for (TypeElement et : TightRope.ANALYSIS.getTypeElements())
		{
			System.out.println("et.simpleName = " + et.getSimpleName()
					+ "\t\t arg0.name = " + arg0.getName());
			if (et.getSimpleName().toString().equals(arg0.getType().toString()))
			{
				ExpressionTree initialiser = arg0.getInitializer();

				if (initialiser != null)
				{

					return initialiser.accept(this, param);
				}

			}
		}

		return null;

		// Name varName = arg0.getName();
		//
		// Tree varType = arg0.getType();
		//
		// String encodedName = NewTransUtils.encodeName(varName);
		// String varTypeString = varType.toString();
		//
		// String init = "";
		// if (arg0.getInitializer() != null)
		// {
		// // Map<Name, Tree> initialiser = arg0.getInitializer().accept(this,
		// arg1);
		// // System.out.println("/*/* Init of " + arg0.getName() + " is = " +
		// initialiser);
		// // init = arg0.getInitializer().toString();
		//
		// param = arg0.getInitializer().accept(this, param);
		// }
		//
		//
		//
		//
		// if (GENERIC_PARADIGM_TYPES.contains(varTypeString))
		// {
		// // String varTypefromName = WordUtils
		// // .capitalize(encodedName) + "ID";
		// param.setVariableName(encodedName);
		//
		// objectEnv.addParameter(encodedName, varTypeString,
		// varType.toString(), false, init);
		//
		// }
		// else
		// {
		// objectEnv.addParameter(encodedName, varTypeString,
		// varType.toString() + "ID", false, init);
		// }
		//
		// System.out.println("*/*/ Adding Parameter " + encodedName +
		// " to ObjectEnv " +objectEnv.getName() + "of type " +
		// objectEnv.getClass().getCanonicalName());
		//
		//
		//
		// return param;
	}

	@Override
	public VariableEnv visitNewClass(NewClassTree arg0, VariableEnv arg1)
	{
		System.out.println("Params Visitor: New Class " + arg0.toString());
		ArrayList<VariableEnv> returnList = new ArrayList<VariableEnv>();

		List<? extends VariableTree> initParams = null;

		// System.out.println("Expression = " + etc.toString());

		//
		// ///WHAT THE HELL DOES THIS DO??
		// //for each type element in the program...
		// for (TypeElement te : TightRope.ANALYSIS.getTypeElements())
		// {
		//
		// System.out.println("te.simpleName = " + te.getSimpleName().toString()
		// + "\t\t arg0.id = " +arg0.getIdentifier().toString() ) ;
		// // ...if the type element equals the identifier of this new class...
		// if (te.getSimpleName().toString()
		// .equals(arg0.getIdentifier().toString()))
		// {
		// //...for each tree in the members of this type element...
		// for (Tree t : TightRope.ANALYSIS.TREES.getTree(te).getMembers())
		// {
		// //...if it's a method
		// if (t instanceof MethodTree)
		// {
		// MethodTree mt = (MethodTree) t;
		// //...if its the constructor
		// if(mt.getName().contentEquals("<init>"))
		// {
		// initParams = mt.getParameters();
		// break;
		// //get all the params and for each...
		//
		// }
		//
		// // System.out.println("v.getType = " + v.getType() +
		// " \t\t arg0.getIdentifier() = " + arg0.getIdentifier().toString());
		//
		// }
		// }
		// }
		// }

		VariableEnv v = new VariableEnv();
		v.setVariableName(arg0.getIdentifier().toString());
		returnList.add(v);

		// }

		// System.out.println("returnList = " + returnList.toString());
		return v;
	}


	@Override
	public VariableEnv visitIdentifier(IdentifierTree arg0,
			VariableEnv arg1)
	{
		
		System.out.println("Param Visitor: Identifier");
		if (arg1 != null)
		{

			arg1.setVariableName(arg0.getName().toString());
			return null;
		}
		else
		{
			VariableEnv v = new VariableEnv();
			
			v.setVariableName(arg0.getName().toString());
			
			List<VariableEnv> params = new ArrayList<VariableEnv>();
			params.add(v);
			return v;
		}

		
	}



	@Override
	public VariableEnv visitLiteral(LiteralTree arg0, VariableEnv arg1)
	{
		System.out.println("Param Visitor : Literal");
		
		if (arg1 != null)
		{
		if (arg0 != null)
		{
			Object value = arg0.getValue();
			if (value != null)
			{
				if (value.toString().equals("this"))
				{
					arg1.setVariableType("Probably Mission");
				}
				else
				{
					// ?
				}
				arg1.setVariableInit(arg0.getValue());
			}
		}
		return null;
		}
		else
		{
			if (arg0 != null)
			{
				Object value = arg0.getValue();
				if (value != null)
				{
				
					VariableEnv v = new VariableEnv();
					
					if (value.toString().equals("this"))
					{
						v.setVariableType("Probably Mission");
					}
					else
					{
						// ?
					}
					v.setVariableInit(arg0.getValue());
					
					List<VariableEnv> params = new ArrayList<VariableEnv> ();
					params.add(v);
					return v;
					
				}
			}
		}
		return null;
	}



	@Override
	public VariableEnv visitMethodInvocation(MethodInvocationTree arg0,
			VariableEnv arg1)
	{
		assert (arg1 != null);

		arg1.setVariableName(arg0.getMethodSelect().toString());

		return null;
	}

	@Override
	public VariableEnv visitAnnotatedType(AnnotatedTypeTree arg0,
			VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitAnnotation(AnnotationTree arg0, VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitArrayAccess(ArrayAccessTree arg0, VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitArrayType(ArrayTypeTree arg0, VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitAssert(AssertTree arg0, VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitAssignment(AssignmentTree arg0, VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitBinary(BinaryTree arg0, VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitBlock(BlockTree arg0, VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitBreak(BreakTree arg0, VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitCase(CaseTree arg0, VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitCatch(CatchTree arg0, VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitClass(ClassTree arg0, VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitCompilationUnit(CompilationUnitTree arg0,
			VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitCompoundAssignment(CompoundAssignmentTree arg0,
			VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitConditionalExpression(
			ConditionalExpressionTree arg0, VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitContinue(ContinueTree arg0, VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitDoWhileLoop(DoWhileLoopTree arg0, VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitEmptyStatement(EmptyStatementTree arg0,
			VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitEnhancedForLoop(EnhancedForLoopTree arg0,
			VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitErroneous(ErroneousTree arg0, VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitExpressionStatement(ExpressionStatementTree arg0,
			VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitForLoop(ForLoopTree arg0, VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitIf(IfTree arg0, VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitImport(ImportTree arg0, VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitInstanceOf(InstanceOfTree arg0, VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitIntersectionType(IntersectionTypeTree arg0,
			VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitLabeledStatement(LabeledStatementTree arg0,
			VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitLambdaExpression(LambdaExpressionTree arg0,
			VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitMemberReference(MemberReferenceTree arg0,
			VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitMemberSelect(MemberSelectTree arg0, VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitMethod(MethodTree arg0, VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitModifiers(ModifiersTree arg0, VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitNewArray(NewArrayTree arg0, VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitOther(Tree arg0, VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitParameterizedType(ParameterizedTypeTree arg0,
			VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitParenthesized(ParenthesizedTree arg0,
			VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitPrimitiveType(PrimitiveTypeTree arg0,
			VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitReturn(ReturnTree arg0, VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitSwitch(SwitchTree arg0, VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitSynchronized(SynchronizedTree arg0, VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitThrow(ThrowTree arg0, VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitTry(TryTree arg0, VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitTypeCast(TypeCastTree arg0, VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitTypeParameter(TypeParameterTree arg0,
			VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitUnary(UnaryTree arg0, VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitUnionType(UnionTypeTree arg0, VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitWhileLoop(WhileLoopTree arg0, VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitWildcard(WildcardTree arg0, VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

}

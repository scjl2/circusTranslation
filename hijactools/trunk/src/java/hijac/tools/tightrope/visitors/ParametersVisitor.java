package hijac.tools.tightrope.visitors;

import hijac.tools.application.TightRope;
import hijac.tools.tightrope.environments.ClassEnv;
import hijac.tools.tightrope.environments.ObjectEnv;
import hijac.tools.tightrope.environments.ParadigmEnv;
import hijac.tools.tightrope.environments.ProgramEnv;
import hijac.tools.tightrope.environments.VariableEnv;

import java.util.ArrayList;
import java.util.HashMap;
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

public class ParametersVisitor implements TreeVisitor<VariableEnv, VariableEnv>
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
	private List<VariableEnv> varMap;

	private String originClass;

	private final static ArrayList<String> GENERIC_PARADIGM_TYPES = ParadigmEnv
			.getGenericParadigmTypes();

	public ParametersVisitor(ProgramEnv programEnv,
			ObjectEnv whereTheVariableNameIs, ClassEnv classEnv,
			String originClass, List<VariableEnv> varMap)
	{
		super();
		this.programEnv = programEnv;
		this.objectEnv = whereTheVariableNameIs;
		this.classEnv = classEnv;
		this.originClass = originClass;
		this.varMap = varMap;
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

	}

	@Override
	public VariableEnv visitNewClass(NewClassTree arg0, VariableEnv arg1)
	{
		System.out.println("Params Visitor: New Class " + arg0.toString());
		// ArrayList<VariableEnv> returnList = new ArrayList<VariableEnv>();

		// List<? extends VariableTree> initParams = null;

		ExpressionTree identifierTree = arg0.getIdentifier();
		String identifiterTree = identifierTree.toString();

		List<? extends ExpressionTree> args = arg0.getArguments();

		VariableEnv v = new VariableEnv();
		if (identifiterTree.equals("PriorityParameters"))
		{
			assert (args.size() == 1);

			ExpressionTree arg = args.get(0);
			System.out.println("arg " + arg.toString() + " in PriParams is "
					+ arg.getKind());

			// System.out.println(programEnv.getObjectEnv(originClass).getVariables().toString());
			// System.out.println(((ParadigmEnv)
			// programEnv.getObjectEnv(originClass)).getClassEnv().getVariables().toString());

			if (arg instanceof IdentifierTree)
			{
				// if (varMap.get(((IdentifierTree) arg).getName()) != null)
				// {
				// Tree varThing = varMap
				// .get(((IdentifierTree) arg).getName());
				//
				// v.setProgramType(varThing.toString());
				// }
				// else
				// {
				VariableEnv vTemp = arg.accept(this, null);
				v.setProgramType(vTemp.getProgramType());
				// }
			}
			else if (arg instanceof LiteralTree)
			{
				v.setProgramType(arg.toString());
			}
		}
		else if (identifiterTree.equals("PeriodicParameters"))
		{

			ExpressionTree time = args.get(0);

			String start = "", period = "", deadline = "", deadlineMissHandler = "";

			ExpressionTree startTree, periodTree, deadlineTree, deadlineMissTree;

			if (args.size() == 2)
			{

				// Assuming time is either Relative or Absolute Time
				if (time instanceof NewClassTree)
				{
					List<? extends ExpressionTree> timeParams = ((NewClassTree) time)
							.getArguments();

					if (timeParams.size() >= 2)
					{
						startTree = timeParams.get(0);
						start = startTree.toString();

						periodTree = timeParams.get(1);
						period = periodTree.toString();

						deadline = "NULL";
						deadlineMissHandler = "nullSchedulableId";

					}
					else
					{
						startTree = timeParams.get(0);
						start = startTree.toString();

						periodTree = timeParams.get(1);
						period = periodTree.toString();

						deadlineTree = timeParams.get(2);
						deadline = deadlineTree.toString();

						if (deadline.equals("null"))
						{
							deadline = "NULL";
						}

						deadlineMissTree = timeParams.get(3);
						deadlineMissHandler = deadlineMissTree.toString();

						if (deadlineMissHandler.equals("null"))
						{
							deadlineMissHandler = "nullSchedulableId";
						}
					}
				}
			}
			else
			{
				ExpressionTree deadlineMisHandlerTree = args.get(1);
				deadlineMissHandler = deadlineMisHandlerTree.toString();

				if (start.equals("null"))
				{
					start = "NULL";
				}

				if (period.equals("null"))
				{
					period = "NULL";
				}

				if (deadlineMissHandler.equals("null"))
				{
					deadlineMissHandler = "nullSchedulableId";
				}
			}

			v.setProgramType("(" + start + "," + period + "," + deadline + ","
					+ deadlineMissHandler + ")");

		}
		else if (identifiterTree.equals("AperiodicParameters"))
		{
			// assert(args.size() == 2 || args.size() == 0);
			String deadline = "", deadlineMisHandler = "";

			if (args.size() == 2)
			{
				ExpressionTree deadlineTree = args.get(0);
				deadline = deadlineTree.toString();

				ExpressionTree deadlineMisHandlerTree = args.get(1);
				deadlineMisHandler = deadlineMisHandlerTree.toString();

			}
			else
			{
				deadline = "NULL";
				deadlineMisHandler = "nullSchedulableId";
			}

			v.setProgramType("(" + deadline + "," + deadlineMisHandler + ")");
		}
		else
		{
			v.setProgramType(arg0.getIdentifier().toString());
		}

		v.setVariableName(arg0.getIdentifier().toString());

		return v;
	}

	@Override
	public VariableEnv visitIdentifier(IdentifierTree arg0, VariableEnv arg1)
	{
		System.out.println("Param Visitor: Identifier");

		{
			VariableEnv v = new VariableEnv();

			System.out.println("arg = " + arg0.getName().toString());
			if (arg0.getName().toString().equals("this"))
			{
				v.setVariableType(originClass + "ID");
				v.setProgramType(originClass + "ID");
			}
			else
			{
				Name varName = arg0.getName();

				ObjectEnv originObject = programEnv.getObjectEnv(originClass);

//				Tree varTree = varMap.get(varName);
			
				
				boolean doneIt = false;
						
						for(VariableEnv otherVar: varMap)
						{
							if(otherVar.getVariableName().equals(varName.toString()))
							{
								String varTreeString = otherVar.getVariableType();
								
								if (varTreeString.equals("StorageParameters"))
								{
									System.out.println("Ignored Arg " + varName);
									return null;
								}

								ObjectEnv parameterObject = programEnv
										.getObjectEnv(varTreeString);

								if (parameterObject instanceof ParadigmEnv)
								{
									v.setVariableName(arg0.getName().toString() + "ID");
									v.setProgramType(arg0.getName().toString() + "ID");
								}
								else if (originObject.getVariable(varTreeString) != null)
								{
									VariableEnv thisVar = originObject
											.getVariable(varTreeString);

									v.setProgramType(thisVar.getVariableInit().toString());
								}
								else//In the VarMap, but it's type isn't a ParadigmEnv and its name isn't a variable in the OriginObject 
								{
									v.setVariableName(arg0.getName().toString());
									
									
									
									v.setProgramType("A"+arg0.getName().toString());
								}
								doneIt = true;
							}
						}
				
				if(! doneIt)
				{
					System.out.println("not done it");

					VariableEnv theVar = originObject.getVariable(varName);

					if (theVar == null)
					{
						theVar = ((ParadigmEnv) originObject).getClassEnv()
								.getVariable(varName);
					}

					System.out.println("originObject = "
							+ originObject.getName());

					System.out.println(theVar);
					if(theVar != null)
					{
					theVar.setProgramType(theVar.getVariableInit().toString());

					v = theVar;
					}
				}
			}

			return v;
		}

	}

	@Override
	public VariableEnv visitLiteral(LiteralTree arg0, VariableEnv arg1)
	{
		System.out.println("Param Visitor : Literal");

		if (arg0 != null)
		{
			Object value = arg0.getValue();
			if (value != null)
			{
				// Ignoring all string params
				if (value instanceof String)
				{
					return null;
				}

				VariableEnv v = new VariableEnv();

				v.setVariableName(originClass);

				VariableEnv objVar = objectEnv.getVariable(value.toString());
				if (objVar != null)
				{
					String varType = (objVar.getVariableType());
					if (varType.equals("StorageParameters"))
					{
						System.out.println("Ignored Arg " + varType);

					}
					else
					{
						v.setProgramType(varType);
					}
				}

				v.setVariableInit(arg0.getValue());

				System.out.println("Returning " + v.toString()
						+ " from literal");
				return v;

			}
		}

		System.out.println("Returning null form Literal");
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

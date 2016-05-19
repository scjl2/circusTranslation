package hijac.tools.tightrope.visitors;

import hijac.tools.application.TightRope;
import hijac.tools.tightrope.environments.ObjectEnv;
import hijac.tools.tightrope.environments.ParadigmEnv;
import hijac.tools.tightrope.environments.ProgramEnv;
import hijac.tools.tightrope.environments.VariableEnv;
import hijac.tools.tightrope.utils.Debugger;
import hijac.tools.tightrope.utils.TightRopeTransUtils;

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
	private final ProgramEnv programEnv;
	private ObjectEnv objectEnv;
	private HashMap<Name, Tree> varMap;

	private String originClass;
	private String nameOfClass;

	public ParametersVisitor(ProgramEnv programEnv, ObjectEnv whereTheVariableNameIs,
			ObjectEnv classEnv, String originClass, HashMap<Name, Tree> varMap)
	{
		super();
		this.programEnv = programEnv;
		this.objectEnv = whereTheVariableNameIs;
		this.originClass = originClass;
		this.varMap = varMap;
	}

	@Override
	public VariableEnv visitVariable(VariableTree arg0, VariableEnv param)
	{
		Debugger.log("Params Visitor: Variable " + arg0.toString());
		for (TypeElement et : TightRope.ANALYSIS.getTypeElements())
		{
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
		Debugger.log("Params Visitor: New Class " + arg0.toString());
		

		ExpressionTree identifierTree = arg0.getIdentifier();
		String identifiterTree = identifierTree.toString();

		List<? extends ExpressionTree> args = arg0.getArguments();

		VariableEnv v = new VariableEnv();
		if (identifiterTree.equals("PriorityParameters"))
		{
			assert (args.size() == 1);

			ExpressionTree arg = args.get(0);


			// TODO This doesn't work. As a limitation of the tool, it only
			// works if you have a param that is a literal
			if (arg instanceof IdentifierTree)
			{
				
				VariableEnv vTemp = arg.accept(this, null);
				Debugger.log("vTemp = " + vTemp.toString());
				
				//TODO FOx this, attempted to only set the thread priority if 'needsThread' is true, 
				//which should be set when the MethodBodyVisitor finds a call to wait or notify.
//				
					programEnv.setThreadPriority(nameOfClass, vTemp.getProgramType());
//			
			}
			else if (arg instanceof LiteralTree)
			{
				
				
				//TODO FOx this, attempted to only set the thread priority if 'needsThread' is true, 
				//which should be set when the MethodBodyVisitor finds a call to wait or notify.

					programEnv.setThreadPriority(nameOfClass, arg.toString());

			}

			return null;
		}
		else if (identifiterTree.equals("PeriodicParameters"))
		{
			// ExpressionTree time = args.get(0);

			String start = "NULL", period = "NULL", deadline = "NULL", deadlineMissHandler = "nullSchedulableId";

			ExpressionTree startTree, periodTree, deadlineTree, deadlineMissTree;

			if (args.size() == 2)
			{
				startTree = args.get(0);
				periodTree = args.get(1);

				// Assuming time is either Relative or Absolute Time
				if (startTree instanceof NewClassTree)
				{
					List<? extends ExpressionTree> timeParams = ((NewClassTree) startTree)
							.getArguments();

					if (timeParams.size() >= 2)
					{
						start = "time(" + timeParams.get(0) + "," + timeParams.get(1)
								+ ")";
					}
				}
				else
				{
					if (startTree.toString().equals("null"))
					{
						start = "NULL";
					}
					else
					{
						start = startTree.toString();
					}
				}

				if (periodTree instanceof NewClassTree)
				{
					List<? extends ExpressionTree> timeParams = ((NewClassTree) periodTree)
							.getArguments();

					if (timeParams.size() >= 2)
					{
						period = "time(" + timeParams.get(0) + "," + timeParams.get(1)
								+ ")";
					}

				}
				else
				{
					if (periodTree.toString().equals("null"))
					{
						period = "NULL";
					}
					else
					{
						period = periodTree.toString();
					}
				}

			}
			else
			{
				startTree = args.get(0);
				periodTree = args.get(1);

				// Assuming time is either Relative or Absolute Time
				if (startTree instanceof NewClassTree)
				{
					List<? extends ExpressionTree> timeParams = ((NewClassTree) startTree)
							.getArguments();

					if (timeParams.size() >= 2)
					{
						start = "time(" + timeParams.get(0) + "," + timeParams.get(1)
								+ ")";

					}

				}
				else
				{
					if (startTree.toString().equals("null"))
					{
						start = "NULL";
					}
					else
					{
						start = startTree.toString();
					}
				}

				if (periodTree instanceof NewClassTree)
				{
					List<? extends ExpressionTree> timeParams = ((NewClassTree) periodTree)
							.getArguments();

					if (timeParams.size() >= 2)
					{
						period = "time(" + timeParams.get(0) + "," + timeParams.get(1)
								+ ")";
					}

				}
				else
				{
					if (periodTree.toString().equals("null"))
					{
						period = "NULL";
					}
					else
					{
						period = periodTree.toString();
					}
				}

				deadlineTree = args.get(2);

				if (deadlineTree instanceof NewClassTree)
				{
					List<? extends ExpressionTree> timeParams = ((NewClassTree) deadlineTree)
							.getArguments();

					if (timeParams.size() >= 2)
					{
						deadline = "time(" + timeParams.get(0) + "," + timeParams.get(1)
								+ ")";
					}
				}
				else
				{
					if (deadlineTree.toString().equals("null"))
					{
						deadline = "NULL";
					}
					else
					{
						deadline = deadlineTree.toString();
					}
				}

				deadlineMissTree = args.get(3);
				deadlineMissHandler = deadlineMissTree.toString();

				if (deadlineMissHandler.equals("null"))
				{
					deadlineMissHandler = "nullSchedulableId";
				}
			}

			v.setType("PeriodicParameters");
			v.setProgramType("(" + start + "," + period + "," + deadline + ","
					+ deadlineMissHandler + ")");

		}
		else if (identifiterTree.equals("AperiodicParameters"))
		{
			// assert(args.size() == 2 || args.size() == 0);
			String deadline = "NULL", deadlineMissHandler = "nullSchedulableId";

			if (args.size() == 2)
			{
				ExpressionTree deadlineTree = args.get(0);

				if (deadlineTree instanceof NewClassTree)
				{
					List<? extends ExpressionTree> timeParams = ((NewClassTree) deadlineTree)
							.getArguments();

					if (timeParams.size() >= 2)
					{
						deadline = "time(" + timeParams.get(0) + "," + timeParams.get(1)
								+ ")";

					}
					else
					{
						deadline = deadlineTree.toString();
					}
				}
				ExpressionTree deadlineMisHandlerTree = args.get(1);
				deadlineMissHandler = deadlineMisHandlerTree.toString();

			}

			v.setType("AperiodicParameters");
			v.setProgramType("(" + deadline + "," + deadlineMissHandler + ")");
		}
		else
		{
			v.setProgramType(TightRopeTransUtils.encodeType(arg0.getIdentifier()).toString());
		}

		v.setName(arg0.getIdentifier().toString());

		return v;
	}

	@Override
	public VariableEnv visitIdentifier(IdentifierTree arg0, VariableEnv arg1)
	{
		{
			VariableEnv v = new VariableEnv();
			String idString = arg0.getName().toString();

			if (idString.equals("this"))
			{
				v.setType("ID");

				v.setProgramType(originClass + "ID");
				
			}
			else
			{
				Name varName = arg0.getName();

				ObjectEnv originObject = programEnv.getObjectEnv(originClass);

				Tree varTree = varMap.get(varName);


				if (varTree != null)
				{
					String varTreeString = varTree.toString();

					if (varTreeString.equals("StorageParameters"))
					{
						
						return null;
					}

					ObjectEnv parameterObject = programEnv.getObjectEnv(varTreeString);

					if (parameterObject instanceof ParadigmEnv)
					{
						v.setName(arg0.getName().toString() + "ID");

						v.setProgramType(arg0.getName().toString() + "ID");
					}
					else if (originObject.getVariable(varName) != null)
					{
						VariableEnv thisVar = originObject.getVariable(varTreeString);

						v.setProgramType(thisVar.getInit().toString());
					}
					else
					// In the VarMap, but it's type isn't a ParadigmEnv and its
					// name isn't a variable in the OriginObject
					{
						v.setName(arg0.getName().toString());

						v.setProgramType("A" + arg0.getName().toString());
					}
				}
				else
				{
					VariableEnv theVar = originObject.getVariable(varName);

					if (theVar == null)
					{
						
						theVar = ((ParadigmEnv) originObject).getClassEnv().getVariable(
								varName);
					}

				
					v = theVar;
				}
			}

			return v;
		}

	}

	@Override
	public VariableEnv visitLiteral(LiteralTree arg0, VariableEnv arg1)
	{

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

				v.setName(originClass);

				VariableEnv objVar = objectEnv.getVariable(value.toString());
				if (objVar != null)
				{
					String varType = (objVar.getType());
					if (varType.equals("StorageParameters"))
					{

					}
					else
					{
						v.setProgramType("T"+varType);
					}
				}

				v.setInit(arg0.getValue());

				return v;

			}
		}

		return null;
	}

	@Override
	public VariableEnv visitMethodInvocation(MethodInvocationTree arg0, VariableEnv arg1)
	{
		assert (arg1 != null);

		arg1.setName(arg0.getMethodSelect().toString());

		return null;
	}

	@Override
	public VariableEnv visitAnnotatedType(AnnotatedTypeTree arg0, VariableEnv arg1)
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
	public VariableEnv visitCompilationUnit(CompilationUnitTree arg0, VariableEnv arg1)
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
	public VariableEnv visitConditionalExpression(ConditionalExpressionTree arg0,
			VariableEnv arg1)
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
	public VariableEnv visitEmptyStatement(EmptyStatementTree arg0, VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitEnhancedForLoop(EnhancedForLoopTree arg0, VariableEnv arg1)
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
	public VariableEnv visitIntersectionType(IntersectionTypeTree arg0, VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitLabeledStatement(LabeledStatementTree arg0, VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitLambdaExpression(LambdaExpressionTree arg0, VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitMemberReference(MemberReferenceTree arg0, VariableEnv arg1)
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
	public VariableEnv visitParameterizedType(ParameterizedTypeTree arg0, VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitParenthesized(ParenthesizedTree arg0, VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitPrimitiveType(PrimitiveTypeTree arg0, VariableEnv arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableEnv visitReturn(ReturnTree arg0, VariableEnv arg1)
	{

		return arg0.getExpression().accept(this, arg1);
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
	public VariableEnv visitTypeParameter(TypeParameterTree arg0, VariableEnv arg1)
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

	public void setName(String nameOfClassBeingTranslated)
	{
		this.nameOfClass = nameOfClassBeingTranslated;

	}

}

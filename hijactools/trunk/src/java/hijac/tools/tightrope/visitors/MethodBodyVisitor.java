package hijac.tools.tightrope.visitors;

import hijac.tools.application.TightRopeTest;
import hijac.tools.modelgen.circus.templates.CircusTemplateFactory;
import hijac.tools.modelgen.circus.templates.CircusTemplates;
import hijac.tools.modelgen.circus.visitors.MethodVisitorContext;
import hijac.tools.tightrope.environments.MethodEnv;
import hijac.tools.tightrope.environments.ObjectEnv;
import hijac.tools.tightrope.generators.NewActionMethodModel;
import hijac.tools.tightrope.generators.NewSCJApplication;
import hijac.tools.tightrope.utils.NewTransUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javacutils.TreeUtils;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.ArrayAccessTree;
import com.sun.source.tree.AssertTree;
import com.sun.source.tree.AssignmentTree;
import com.sun.source.tree.BinaryTree;
import com.sun.source.tree.BlockTree;
import com.sun.source.tree.BreakTree;
import com.sun.source.tree.CaseTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompoundAssignmentTree;
import com.sun.source.tree.ConditionalExpressionTree;
import com.sun.source.tree.EmptyStatementTree;
import com.sun.source.tree.ErroneousTree;
import com.sun.source.tree.ExpressionStatementTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.ForLoopTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.IfTree;
import com.sun.source.tree.LabeledStatementTree;
import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.ParenthesizedTree;
import com.sun.source.tree.PrimitiveTypeTree;
import com.sun.source.tree.ReturnTree;
import com.sun.source.tree.SwitchTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.TreeVisitor;
import com.sun.source.tree.TryTree;
import com.sun.source.tree.UnaryTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.tree.WhileLoopTree;
import com.sun.source.util.SimpleTreeVisitor;

/**
 * This class visits a method and returns a String representing its body.
 * 
 * Adapted from <code>hijac.tools.modelgen.circus.visitors.AMethodVisitor</code>
 * .
 * 
 * @author Matt Luckcuck
 */

public class MethodBodyVisitor extends
		SimpleTreeVisitor<String, MethodVisitorContext>
{

	private static final String RET_FALSE = "ret := \\false";
	private static final String RET_TRUE = "ret := \\true";
	private static final String TYPE_TEMPLATE = "L2Type.ftl";
	private static final String EXPR_TEMPLATE = "L2Expr.ftl";
	private static final String STMT_TEMPLATE = "L2Stmt.ftl";

	private static final List<MethodEnv> MISSION_API_METHODS = new ArrayList<MethodEnv>();
	/**
	 * Used to store values for 'future' calls to methods of the visitor
	 */
	private static final HashMap<String, Object> timeMachine = new HashMap<String, Object>();

	private MethodEnv methodEnv = null;
	private LiteralTree trueLiteral = null;
	private LiteralTree falseLiteral = null;
	private ObjectEnv object;

	protected final NewSCJApplication CONTEXT;

	/**
	 * Constructor for the Method Body Visitor
	 * 
	 * @param context
	 *            The NewSCJApplication object for this application
	 * @param object
	 *            The object in which the method on which we are operating
	 *            resides
	 */
	public MethodBodyVisitor(NewSCJApplication context, ObjectEnv object)
	{
		super(NewTransUtils.FAILED_RESULT);
		assert context != null;
		CONTEXT = context;
		this.object = object;

		initAPIMeths();

	}

	private void initAPIMeths()
	{
		MISSION_API_METHODS.add(new MethodEnv("getMission", "Mission", true));
		MISSION_API_METHODS.add(new MethodEnv("getSequencer",
				"MissionSequencer", true));
		MISSION_API_METHODS
				.add(new MethodEnv("missionMemorySize", "int", true));
		MISSION_API_METHODS.add(new MethodEnv("requestTermination", "boolean",
				true));
		MISSION_API_METHODS.add(new MethodEnv("terminationPending", "boolean",
				true));

	}

	/**
	 * Constructor for the Method Body Visitor
	 * 
	 * @param context
	 *            The NewSCJApplication object for this application
	 * @param object
	 *            The object in which the method on which we are operating
	 *            resides
	 * @param env
	 *            The environment for the method of which we exploring the body
	 */
	public MethodBodyVisitor(NewSCJApplication context, ObjectEnv object,
			MethodEnv env)
	{
		super(NewTransUtils.FAILED_RESULT);
		assert context != null;
		CONTEXT = context;
		this.object = object;
		this.methodEnv = env;

		initAPIMeths();
	}

	/**
	 * Constructs the trueLiteral and falseLiteral LiteralTrees if they have not
	 * been constructed before.
	 */
	public void lazyLiteralConstructor()
	{
		if (trueLiteral == null)
		{
			trueLiteral = new LiteralTree()
			{

				@SuppressWarnings("unchecked")
				@Override
				public <R, D> R accept(TreeVisitor<R, D> arg0, D arg1)
				{
					// TODO Auto-generated method stub
					return (R) RET_TRUE;
				}

				@Override
				public Kind getKind()
				{
					// TODO Auto-generated method stub
					return Kind.BOOLEAN_LITERAL;
				}

				@Override
				public Object getValue()
				{
					// TODO Auto-generated method stub
					return true;
				}

			};
		}

		if (falseLiteral == null)
		{
			falseLiteral = new LiteralTree()
			{

				@SuppressWarnings("unchecked")
				@Override
				public <R, D> R accept(TreeVisitor<R, D> arg0, D arg1)
				{
					// TODO Auto-generated method stub
					return (R) RET_FALSE;
				}

				@Override
				public Kind getKind()
				{
					// TODO Auto-generated method stub
					return Kind.BOOLEAN_LITERAL;
				}

				@Override
				public Object getValue()
				{
					// TODO Auto-generated method stub
					return false;
				}

			};
		}
	}

	public void initMacroModel(Tree node, MethodVisitorContext ctxt)
	{
		/*
		 * Importantly we have to duplicate the context as we are going to
		 * change the macro model and the context is assumed to be immutable.
		 */
		ctxt.MACRO_MODEL = ctxt.MACRO_MODEL.copy();
		ctxt.MACRO_MODEL.put("NODE", node);
		ctxt.MACRO_MODEL.put("CTXT", ctxt);

		if (methodEnv != null)
		{
			ctxt.MACRO_MODEL.put("TRANS", new NewActionMethodModel(CONTEXT,
					object, methodEnv));
		}
		else
		{
			ctxt.MACRO_MODEL.put("TRANS", new NewActionMethodModel(CONTEXT,
					object));
		}
	}

	protected String doMacroCall(Tree node, MethodVisitorContext ctxt,
			String file, String name, Object... args)
	{
		initMacroModel(node, ctxt);

		CircusTemplates templates = CONTEXT.TEMPLATES;

		CircusTemplateFactory factory = templates.FACTORY;
		System.out.println("doMacroCall node = " + node);
		return factory.doMacroCall(ctxt.MACRO_MODEL, file, name, args);
	}

	protected String callStmtMacro(Tree node, MethodVisitorContext ctxt,
			String name, Object... args)
	{
		return doMacroCall(node, ctxt, STMT_TEMPLATE, name, args);
	}

	protected String callExprMacro(Tree node, MethodVisitorContext ctxt,
			String name, Object... args)
	{
		return doMacroCall(node, ctxt, EXPR_TEMPLATE, name, args);
	}

	protected String callTypeMacro(Tree node, MethodVisitorContext ctxt,
			String name, Object... args)
	{
		return doMacroCall(node, ctxt, TYPE_TEMPLATE, name, args);
	}

	@Override
	public String visitAnnotation(AnnotationTree node, MethodVisitorContext ctxt)
	{
		/*
		 * TODO: Raise a proper translation error below instead of an assertion
		 * failure. This still needs to be designed in the view of visitors.
		 */
		throw new AssertionError("Unexpected annotation AST node.");
	}

	@Override
	public String visitArrayAccess(ArrayAccessTree node,
			MethodVisitorContext ctxt)
	{
		return callExprMacro(node, ctxt, "ArrayAccess", node.getExpression(),
				node.getIndex());
	}

	@Override
	public String visitAssert(AssertTree node, MethodVisitorContext ctxt)
	{
		/* Should we check that we are not inside an expression? */
		return callStmtMacro(node, ctxt, "Assert", node.getCondition());
	}

	@Override
	public String visitAssignment(AssignmentTree node, MethodVisitorContext ctxt)
	{
		/* Should we check that we are not inside an expression? */
		System.out.println("/// AssignmentTree node = " + node);
		System.out.println("/// AssignmentTree variable = "
				+ node.getVariable());
		System.out.println("/// AssignmentTree expression = "
				+ node.getExpression());

		if (node.getExpression() instanceof MethodInvocationTree)
		{
			MethodInvocationTree mit = (MethodInvocationTree) node
					.getExpression();

			if (isSyncMethod(mit))
			{
				return visitMethodInvocation(mit, ctxt);
			}
			else
			{
				return callStmtMacro(node, ctxt, "Assignment",
						node.getVariable(), node.getExpression());
			}
		}
		else
		{
			if (object.getVariable(node.getVariable().toString()) == null)
			{
				return "this~.~" + node.getVariable().toString() + " :="
						+ node.getExpression();
			}
			else
			{
				return callStmtMacro(node, ctxt, "Assignment",
						node.getVariable(), node.getExpression());
			}
		}
	}

	@Override
	public String visitBinary(BinaryTree node, MethodVisitorContext ctxt)
	{
		/* What if the operands are boolean? Equality raises issues. */
		return callExprMacro(node, ctxt, "Binary", node.getLeftOperand(),
				node.getRightOperand());
	}

	@Override
	public String visitBlock(BlockTree node, MethodVisitorContext ctxt)
	{
		return callStmtMacro(node, ctxt, "Block", node.getStatements());
	}

	@Override
	public String visitBreak(BreakTree node, MethodVisitorContext ctxt)
	{
		/* TODO: Check that we are inside a switch statement here. */
		return callStmtMacro(node, ctxt, "Break");
	}

	@Override
	public String visitCase(CaseTree node, MethodVisitorContext ctxt)
	{
		/* TODO: Validate that the case is terminated with a break. */
		return callStmtMacro(node, ctxt, "Case", node.getExpression(),
				node.getStatements());
	}

	@Override
	public String visitCompoundAssignment(CompoundAssignmentTree node,
			MethodVisitorContext ctxt)
	{
		// System.out.println("/// CompoundAssignmentTree node = " + node);

		/* Should we check that we are not inside an expression? */
		return callStmtMacro(node, ctxt, "CompoundAssignment",
				node.getVariable(), node.getExpression());
	}

	@Override
	public String visitConditionalExpression(ConditionalExpressionTree node,
			MethodVisitorContext ctxt)
	{
		return callExprMacro(node, ctxt, "ConditionalExpression",
				node.getCondition(), node.getTrueExpression(),
				node.getFalseExpression());
	}

	@Override
	public String visitEmptyStatement(EmptyStatementTree node,
			MethodVisitorContext ctxt)
	{
		return callStmtMacro(node, ctxt, "EmptyStatement");
	}

	@Override
	public String visitErroneous(ErroneousTree node, MethodVisitorContext ctxt)
	{
		throw new AssertionError("Erroneous AST node encountered.");
	}

	@Override
	public String visitExpressionStatement(ExpressionStatementTree node,
			MethodVisitorContext ctxt)
	{
		/* Are those nodes permissible? Do a bit more investigation here. */

		// TODO HACKERY!
		if (node.toString().contains("Console"))
		{
			System.out.println("*** Console so ignoring *** ");
			return "";
		}
		else
		{
			return callStmtMacro(node, ctxt, "ExpressionStatement",
					node.getExpression());
		}
	}

	@Override
	public String visitForLoop(ForLoopTree node, MethodVisitorContext ctxt)
	{
		return callStmtMacro(node, ctxt, "ForLoop", node.getInitializer(),
				node.getCondition(), node.getStatement(), node.getUpdate());
	}

	@Override
	public String visitIdentifier(IdentifierTree node, MethodVisitorContext ctxt)
	{

		return callExprMacro(node, ctxt, "Identifier", node.getName());
	}

	@Override
	public String visitIf(IfTree node, MethodVisitorContext ctxt)
	{
		return callStmtMacro(node, ctxt, "If", node.getCondition(),
				node.getThenStatement(), node.getElseStatement());
	}

	@Override
	public String visitLabeledStatement(LabeledStatementTree node,
			MethodVisitorContext ctxt)
	{
		return callStmtMacro(node, ctxt, "LabeledStatement",
				node.getStatement());
	}

	@Override
	public String visitLiteral(LiteralTree node, MethodVisitorContext ctxt)
	{
		/* Should we do the translation in a template macro here too? */
		// System.out.println("///Literal ");
		// This is supposed to cater for null id values, but is a bit hacky...

		if (methodEnv != null)
		{
			String returnType = methodEnv.getReturnType();

			if (node.getKind() == Tree.Kind.NEW_CLASS
					|| node.getKind() == Tree.Kind.IDENTIFIER
					|| node.getKind() == Tree.Kind.NULL_LITERAL)
			{
				if (returnType.contains("MissionID"))
				{

					return "nullMissionId";
				}
				else if (returnType.contains("SchedulableId"))
				{
					return "nullSchedulableId";
				}
				else
				{
					System.out.println("/// String is null");
					return NewTransUtils.encodeLiteral(node);
				}
			}
			else
			{

				System.out.println("///  kind is not new class or identifier");
				return NewTransUtils.encodeLiteral(node);
			}

		}
		else
		{

			System.out.println("/// methodEnv is null ");
			return NewTransUtils.encodeLiteral(node);
		}
	}

	@Override
	public String visitMemberSelect(MemberSelectTree node,
			MethodVisitorContext ctxt)
	{
		// Name identifier = node.getIdentifier();
		// Name objectEnvName = object.getName();
		// StringBuilder sb = new StringBuilder();
		//
		// if(identifier.contentEquals("notify"))
		// {
		// sb.append("notify~.~");
		// sb.append(objectEnvName.toString());
		// sb.append("Object");
		// sb.append("~?~thread \\then ");
		// sb.append("\\\\");
		// sb.append("\\Skip");
		//
		// return sb.toString() ;
		// }
		// else if (identifier.contentEquals("wait"))
		// {
		// sb.append("waitCall~.~");
		// sb.append(objectEnvName.toString());
		// sb.append("Object");
		// sb.append("~?~thread \\then");
		// sb.append("\\\\");
		// sb.append("waitRet~.~");
		// sb.append(objectEnvName.toString());
		// sb.append("Object");
		// sb.append("~?~thread \\then");
		// sb.append("\\\\");
		// sb.append("\\Skip");
		//
		// return sb.toString() ;
		// }
		// else
		// {

		/* Are MemberSelect nodes also used for selecting methods too? */
		return callExprMacro(node, ctxt, "MemberSelect", node.getExpression(),
				node.getIdentifier());
		// }
	}

	@Override
	public String visitMethod(MethodTree node, MethodVisitorContext ctxt)
	{
		return visit(node.getBody(), ctxt);
	}

	@Override
	public String visitMethodInvocation(MethodInvocationTree node,
			MethodVisitorContext ctxt)
	{
		System.out.println("/// methodInvocation node = " + node);

		ExpressionTree methodSelect = node.getMethodSelect();
		Name identifier = null;

		if (methodSelect instanceof MemberSelectTree)
		{

			MemberSelectTree mst = (MemberSelectTree) methodSelect;
			identifier = mst.getIdentifier();
		}
		else if (methodSelect instanceof IdentifierTree)
		{
			IdentifierTree it = (IdentifierTree) methodSelect;

			identifier = it.getName();
		}

		Name objectEnvName = object.getName();
		StringBuilder sb = new StringBuilder();

		if (identifier != null)
		{
			if (identifier.contentEquals("notify"))
			{
				object.addParent("ObjectChan");
				object.addParent("ObjectIds");
				object.addParent("ThreadIds");

				sb.append("notify~.~");
				sb.append(objectEnvName.toString());
				sb.append("Object");
				sb.append("~!~thread \\then ");
				sb.append(" \\\\ ");
				sb.append("\\Skip");

				return sb.toString();
			}
			else if (identifier.contentEquals("wait"))
			{
				object.addParent("ObjectChan");
				object.addParent("ObjectIds");
				object.addParent("ThreadIds");

				sb.append("waitCall~.~");
				sb.append(objectEnvName.toString());
				sb.append("Object");
				sb.append("~!~thread \\then");
				sb.append(" \\\\ ");
				sb.append("waitRet~.~");
				sb.append(objectEnvName.toString());
				sb.append("Object");
				sb.append("~!~thread \\then");
				sb.append(" \\\\ ");
				sb.append("\\Skip");

				return sb.toString();
			}
			else if (isNotMyMethod(node))
			{
				MethodEnv method = getMethodEnvBeingCalled(node);
				if (method.isAPIMethod())
				{
					Name nodeName = getObjectEnvOfMethod(methodSelect)
							.getName();
					for (TypeElement t : CONTEXT.getAnalysis()
							.getTypeElements())
					{
						if (t.getSimpleName().contentEquals(nodeName))
						{
							String name = t.getSuperclass().toString();
							name = name.substring(name.lastIndexOf('.') + 1);

							object.addParent(name + "MethChan");
						}
					}

					if (method.isSynchronised())
					{
						object.addParent("ObjectIds");
						object.addParent("ThreadIds");
					}

				}
				else
				{
					object.addParent(getObjectEnvOfMethod(methodSelect)
							.getName().toString() + "MethChan");
					if (method.isSynchronised())
					{
						object.addParent("ObjectIds");
						object.addParent("ThreadIds");
					}
				}

				// System.out.println("!// method being called (returned) = " +
				// method.getMethodName());

				String returnString = method.getReturnType();
				List<? extends ExpressionTree> parameters = node.getArguments();

				sb.append(identifier);
				sb.append("Call");
				sb.append("~.~");
				sb.append(((MemberSelectTree) node.getMethodSelect())
						.getExpression().toString());
				if (method.isSynchronised())
				{
					sb.append("~.~");
					sb.append(objectEnvName.toString());
					sb.append("Thread");
				}

				if (!parameters.isEmpty())
				{
					for (ExpressionTree s : parameters)
					{

						if (s instanceof IdentifierTree)
						{
							sb.append("~!~");
							sb.append(((IdentifierTree) s).getName().toString());
						}
						else if (s instanceof LiteralTree)
						{
							if (s.getKind().equals(Tree.Kind.STRING_LITERAL))
							{

							}
							else
							{
								sb.append(((LiteralTree) s).getValue()
										.toString());
							}
						}
					}
				}
				sb.append("\\then \\\\");

				sb.append(identifier);
				sb.append("Ret");
				sb.append("~.~");
				sb.append(((MemberSelectTree) node.getMethodSelect())
						.getExpression().toString());
				if (method.isSynchronised())
				{
					sb.append("~.~");
					sb.append(objectEnvName.toString());
					sb.append("Thread");
				}

				System.out.println("!// !returnString.contains('null') =  "
						+ (!returnString.contains("null")));
				System.out.println("!// returnString =  " + returnString);
				if (!returnString.contains("null"))
				{

					sb.append("~?~");
					System.out
							.println("!// return string not 'null', getting key: "
									+ identifier.toString()
									+ " value: "
									+ timeMachine.get(identifier.toString()));

					sb.append(timeMachine.get(identifier.toString()));
					sb.append("\\then \\\\");

				}
				else
				{
					sb.append("\\then \\\\");
					sb.append("\\Skip");
				}

				return sb.toString();
			}
			else
			{
				ExecutableElement method = TreeUtils.elementFromUse(node);
				List<? extends VariableElement> params = method.getParameters();
				List<ExpressionTree> arguments = new ArrayList<ExpressionTree>();
				for (int index = 0; index < params.size(); index++)
				{
					if (!CONTEXT.ANNOTS.isInteractionCode(params.get(index))
							&& !CONTEXT.ANNOTS.isIgnored(params.get(index)))
					{
						arguments.add(node.getArguments().get(index));
					}
				}
				/*
				 * Infer the method here that is called and pass to it the
				 * macro.
				 */
				/* Question: Is that generally feasible? */
				return callExprMacro(node, ctxt, "MethodInvocation",
						node.getMethodSelect(), arguments);

			}
		}
		else
		{
			System.out
					.println("*** Identifier was null, returning empty string***");
			return "";
		}
		//
		// TODO if the object in which the method I'm invoking resides is an API
		// app process, translate to call/ret comms
		// String returnString = "";
		// ExpressionTree et = node.getMethodSelect();
		// MemberSelectTree mst = null;
		// if (et instanceof MemberSelectTree)
		// {
		// mst = (MemberSelectTree) et;
		// }

		// TODO what I need to get here is the MethodEnv (or similar) of the
		// method we're calling so I know it's parameters and return value.

		// TypeElement te =
		// CONTEXT.getAnalysis().getTypeElement("scjlevel2examples.flatbuffer.FlatBufferMission");

		// object = TightRopeTest.getProgramEnv().getObjectEnv();
		// //
		// ((NewCircusTemplates) CONTEXT.getTemplates()).setObjectEnv(object);
		// ObjectEnv objectWhereMethodResides = null;
		// String returnType = "";
		// String methodName = mst.getIdentifier().toString();
		//
		// for (VariableEnv v : object.getVariables())
		// {
		// if (v.getVariableName().contentEquals(
		// mst.getExpression().toString()))
		// {
		//
		// objectWhereMethodResides = TightRopeTest.getProgramEnv()
		// .getObjectEnv(v.getVariableType().toString());
		//
		// MethodEnv methodWeAreCalling;
		// for (MethodEnv m : ((ParadigmEnv) objectWhereMethodResides)
		// .getMeths())
		// {
		// if (m.getMethodName().contentEquals(methodName))
		// {
		// methodWeAreCalling = m;
		//
		// returnType = "~?~" + methodWeAreCalling.getReturnType();
		// }
		// }
		//
		// }
		// }

		// returnString = methodName + "Call";
		// returnString += "\\then \\\\";
		//
		// returnString += methodName + "Ret" + returnType;
		// returnString += "\\then \\Skip";
		// //
		// // returnString = node.get
		//
		// return returnString;
	}

	private boolean isNotMyMethod(MethodInvocationTree node)
	{
		// MethodEnv method = getMethodEnvBeingCalled(node);

		ExpressionTree methodSelect = node.getMethodSelect();

		if (methodSelect instanceof MemberSelectTree)
		{

			MemberSelectTree mst = (MemberSelectTree) methodSelect;

			ExpressionTree et = mst.getExpression();
			String expressionString = et.toString();

			if (expressionString.contentEquals(object.getName().toString()))

			{

				return false;
			}
			else
			{
				System.out.println("/// not my method, it's from "
						+ expressionString);
				return true;
			}

		}
		else if (methodSelect instanceof IdentifierTree)
		{
			System.out.println("// I can just call it, so it's my method");
			return false;
		}
		else
		{
			System.out.println("/// not sure if it's my method");
			return false;
		}

	}

	private boolean isSyncMethod(MethodInvocationTree node)
	{
		if (getMethodEnvBeingCalled(node) != null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	private MethodEnv getMethodEnvBeingCalled(MethodInvocationTree node)
	{
		ExpressionTree methodSelect = node.getMethodSelect();
		Name identifier = null;
		if (methodSelect instanceof MemberSelectTree)
		{

			MemberSelectTree mst = (MemberSelectTree) methodSelect;

			System.out.println("/// node.getMethodSelect = " + mst.toString());

			System.out.println("/// node...getExpression = "
					+ mst.getExpression().toString());

			System.out.println("/// node...getIdenitifer = "
					+ mst.getIdentifier().toString());

			identifier = mst.getIdentifier();

			ObjectEnv o = getObjectEnvOfMethod(methodSelect);

			System.out.println("/// o is " + o.getName());

			return getMethodEnvFromObject(identifier, o);
		}
		else if (methodSelect instanceof IdentifierTree)
		{
			IdentifierTree it = (IdentifierTree) methodSelect;

			identifier = it.getName();

			return getMethodEnvFromObject(identifier, object);
		}

		return null;

	}

	private ObjectEnv getObjectEnvOfMethod(ExpressionTree methodSelect)
	{
		// MemberSelectTree mst = (MemberSelectTree) node.getMethodSelect();

		ExpressionTree expression = methodSelect;// .getExpression();
		// = mst.getExpression();

		// System.out.println("/// node.getMethodSelect = " + mst.toString());
		//
		// System.out.println("/// node...getExpression = "
		// + expression.toString());
		// System.out
		// .println("/// node expression type = " + expression.getKind());
		//
		// System.out.println("/// node...getIdenitifer = "
		// + mst.getIdentifier().toString());

		// while(expression instanceof MemberSelectTree)
		// {
		// System.out.println("/// Expression: " + expression +
		// " Is a MemberSelectTree");
		//
		// expression = ((MemberSelectTree) expression).getExpression();
		// }

		if (expression instanceof MemberSelectTree)
		{
			expression = ((MemberSelectTree) methodSelect).getExpression();

			String varType = "";
			final boolean objectNotNull = object != null;
			System.out.println("/// objectNotNull = " + objectNotNull);
			if (objectNotNull)
			{
				System.out.println("/// object name = " + object.getName());

				// For all the type elements in the program...
				for (TypeElement t : CONTEXT.getAnalysis().getTypeElements())
				{
					System.out.println("///** t.getSimpleName() = "
							+ t.getSimpleName());

					// ...if the name of the type equals the name of the object
					// we're in...
					if (t.getSimpleName().contentEquals(object.getName()))
					{

						// ... then that's our class tree!
						ClassTree ct = CONTEXT.getAnalysis().TREES.getTree(t);

						List<Tree> members = (List<Tree>) ct.getMembers();
						Iterator<Tree> i = members.iterator();

						// iterate through all the members of our class...
						while (i.hasNext())
						{
							Tree tree = i.next();

							if (tree instanceof VariableTree)
							{
								VariableTree vt = (VariableTree) tree;

								System.out.println("/// vt.getName = "
										+ vt.getName());
								// / ... to find the variable with the same name
								// as
								// the expression in our method call
								// i.e. expression.methodCall()
								if (vt.getName().contentEquals(
										expression.toString()))
								{
									Tree typeTree = vt.getType();

									System.out.println("/// typeTree = "
											+ typeTree);

									if (typeTree instanceof IdentifierTree)
									{

										IdentifierTree it = (IdentifierTree) typeTree;
										// And finally get the TYPE (as a
										// String) of
										// the variable we are calling the
										// method of
										varType = it.getName().toString();
									}
								}
								else
								{
									System.out
											.println("/// vt not equal to the expression. vt = "
													+ vt
													+ " and expresison = "
													+ expression);
								}
							}
						}
					}
				}

				System.out.println("/// varType = " + varType);

				// Then we get the Type Element of the variable we're calling
				// the
				// method on
				for (TypeElement t : CONTEXT.getAnalysis().getTypeElements())
				{
					System.out.println("!// t = " + t.getSimpleName());

					final Name simpleName = t.getSimpleName();

					System.out.println("/// t simpleName = " + simpleName);
					if (simpleName.contentEquals(varType))
					{
						// Get the object env of the class, that represents the
						// variable we're calling the method on
						ObjectEnv o = TightRopeTest.getProgramEnv()
								.getObjectEnv(simpleName);
						return o;
					}
				}
			}
		}

		return null;
	}

	private MethodEnv getMethodEnvFromObject(Name identifier, ObjectEnv o)
	{
		List<MethodEnv> methods = new ArrayList<MethodEnv>();
		methods.addAll(o.getSyncMeths());
		methods.addAll(o.getMeths());
		methods.addAll(MISSION_API_METHODS);

		// TODO This falls over for API methods...
		for (MethodEnv mEnv : methods)
		{
			System.out.println("/// mEnv.getMethodName = "
					+ mEnv.getMethodName());
			if (mEnv.getMethodName().contentEquals(identifier))
			{
				// Then get the method env of the method we're
				// calling.
				return mEnv;
			}
		}
		return null;
	}

	@Override
	public String visitNewClass(NewClassTree node, MethodVisitorContext ctxt)
	{
		ExpressionTree et = node.getIdentifier();
		IdentifierTree identifier = null;

		if (et instanceof IdentifierTree)
		{
			identifier = (IdentifierTree) et;
			Name n = identifier.getName();

			for (TypeElement elem : CONTEXT.ANALYSIS.getTypeElements())
			{
				if (elem.getSimpleName().contentEquals(n))
				{
					if (elem.getSuperclass().toString().contains("Mission"))
					{
						// if the thing we are returning is a mission then get
						// its ID...somehow
						// so far
						return n.toString();
					}

				}
			}
		}
		else
		{

			ExecutableElement ctor = TreeUtils.elementFromUse(node);
			List<? extends VariableElement> params = ctor.getParameters();
			List<ExpressionTree> arguments = new ArrayList<ExpressionTree>();
			for (int index = 0; index < params.size(); index++)
			{
				if (!CONTEXT.ANNOTS.isInteractionCode(params.get(index))
						&& !CONTEXT.ANNOTS.isIgnored(params.get(index)))
				{
					arguments.add(node.getArguments().get(index));
				}
			}
			return callExprMacro(node, ctxt, "NewClass", node.getIdentifier(),
					arguments);
		}
		return DEFAULT_VALUE;
	}

	@Override
	public String visitParenthesized(ParenthesizedTree node,
			MethodVisitorContext ctxt)
	{
		/* Does the JDK parser retain information on bracketing in the AST? */
		return callExprMacro(node, ctxt, "Parenthesized", node.getExpression());
	}

	@Override
	public String visitPrimitiveType(PrimitiveTypeTree node,
			MethodVisitorContext ctxt)
	{
		return callTypeMacro(node, ctxt, "PrimitiveType",
				node.getPrimitiveTypeKind());
	}

	@Override
	public String visitReturn(ReturnTree node, MethodVisitorContext ctxt)
	{
		ExpressionTree et = node.getExpression();

		if (et instanceof BinaryTree)
		{
			lazyLiteralConstructor();

			BinaryTree bt = (BinaryTree) et;
			return callStmtMacro(node, ctxt, "If", bt, trueLiteral,
					falseLiteral);
		}
		else
		{
			return callStmtMacro(node, ctxt, "Return", et);
		}
	}

	@Override
	public String visitSwitch(SwitchTree node, MethodVisitorContext ctxt)
	{
		return callStmtMacro(node, ctxt, "Switch", node.getExpression(),
				node.getCases());
	}

	@Override
	public String visitUnary(UnaryTree node, MethodVisitorContext ctxt)
	{
		/* What if the operand is boolean? Equality raises issues. */
		return callExprMacro(node, ctxt, "Unary", node.getExpression());
	}

	@Override
	public String visitVariable(VariableTree node, MethodVisitorContext ctxt)
	{
		System.out.println("/// VariableTree node = " + node);
		ExpressionTree initializer = node.getInitializer();

		if (initializer instanceof MethodInvocationTree)
		{
			MethodInvocationTree mit = (MethodInvocationTree) node
					.getInitializer();

			if (isNotMyMethod(mit))
			{
				System.out.println("!// not my method, variable, putting key: "
						+ ((MemberSelectTree) mit.getMethodSelect())
								.getIdentifier().toString() + " value: "
						+ node.getName().toString());

				timeMachine.putIfAbsent(((MemberSelectTree) mit
						.getMethodSelect()).getIdentifier().toString(), node
						.getName().toString());

				return visitMethodInvocation(mit, ctxt);
			}
			else
			{
				return callStmtMacro(node, ctxt, "Variable", node.getName(),
						NewTransUtils.encodeType(node.getType()),
						initializer);
			}
		}
		else if(initializer instanceof LiteralTree) {
			return "\\circvar " + node.getName() + " : "
					+ NewTransUtils.encodeType(node.getType())
					+ " \\circspot " + node.getName() + " :=~"
					+ initializer.toString();
		}
		else
		{
						
			if (object.getVariable(initializer.toString()) != null)
			{
				return "\\circvar " + node.getName() + " : "
						+ NewTransUtils.encodeType(node.getType())
						+ " \\circspot " + node.getName() + " := this~.~"
						+ initializer.toString();
			}
			else
			{
				return callStmtMacro(node, ctxt, "Variable", node.getName(),
						NewTransUtils.encodeType(node.getType()),
						initializer);
			}
		}
	}

	@Override
	public String visitTry(TryTree node, MethodVisitorContext ctxt)
	{
		return callStmtMacro(node, ctxt, "Block", node.getBlock()
				.getStatements());
	}

	@Override
	public String visitWhileLoop(WhileLoopTree node, MethodVisitorContext ctxt)
	{
		return callStmtMacro(node, ctxt, "WhileLoop", node.getCondition(),
				node.getStatement());
	}
}
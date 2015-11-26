package hijac.tools.tightrope.visitors;

import hijac.tools.tightrope.environments.ClassEnv;
import hijac.tools.tightrope.environments.ObjectEnv;
import hijac.tools.tightrope.environments.ParadigmEnv;
import hijac.tools.tightrope.environments.ProgramEnv;
import hijac.tools.tightrope.environments.VariableEnv;
import hijac.tools.tightrope.utils.NewTransUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Name;

import org.apache.commons.lang3.text.WordUtils;

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
import com.sun.source.tree.TreeVisitor;
import com.sun.source.tree.TryTree;
import com.sun.source.tree.TypeCastTree;
import com.sun.source.tree.TypeParameterTree;
import com.sun.source.tree.UnaryTree;
import com.sun.source.tree.UnionTypeTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.tree.WhileLoopTree;
import com.sun.source.tree.WildcardTree;

public class VariableVisitor implements TreeVisitor<List<VariableEnv>, Boolean>
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

	public VariableVisitor(ProgramEnv programEnv, ObjectEnv objectEnv)
	{
		this.programEnv = programEnv;
		this.objectEnv = objectEnv;
		classEnv = ((ParadigmEnv) objectEnv).getClassEnv();
	}

	public VariableVisitor(ProgramEnv programEnv)
	{
		 this.programEnv = programEnv;
	}

	
	@Override
	public List<VariableEnv> visitAssignment(AssignmentTree arg0, Boolean addToEnv)
	{

		// System.out.println("Var Visitor: Assignment");
		// System.out.println("-> " + arg0);

		List<VariableEnv> returnMap = null;

		ExpressionTree variable = arg0.getVariable();
		Name varName = null;
		Tree expression = arg0.getExpression();
		Tree expressionTree = null;

		if (variable instanceof IdentifierTree)
		{
			varName = ((IdentifierTree) variable).getName();
		}
		if (variable instanceof MemberSelectTree)
		{
			varName = ((MemberSelectTree) variable).getIdentifier();
		}

		if (expression instanceof NewClassTree)
		{
			expressionTree = ((NewClassTree) expression).getIdentifier();
		}

		if (expression instanceof IdentifierTree)
		{
			// expressionTree = expression;

			final VariableEnv variableWeHave = objectEnv.getVariable(varName);
			if (variableWeHave != null)
			{
				if (variableWeHave.isPrimitive())
				{
					expressionTree = expression;
				}
				else

				if (!(objectEnv.getName().toString().contains(expression
						.toString())))
				{
					// TODO HACKY! need to get what kind of ID here!
					final String variableInitAndInput = "?"
							+ varName.toString() + "In";
					//
					// objectEnv.addVariable(varName.toString(),
					// "MissionID",
					// variableInitAndInput,
					// variableInitAndInput);

					objectEnv.addVariableInit(varName.toString(),
							variableInitAndInput, true);

				}
			}
		}

		if (expression instanceof LiteralTree)
		{
			expressionTree = expression;
			
		}

		if (varName != null & expressionTree != null)
		{
			// System.out.println("Adding to Var Map");
			returnMap = new ArrayList<VariableEnv>();
			returnMap.add(new VariableEnv(varName.toString(), expressionTree.toString(), (Object)expressionTree, false));
			if (objectEnv != null && addToEnv == true)
			{
				objectEnv.addVariableInit(NewTransUtils.encodeName(varName),
						expressionTree);
				// objectEnv.addVariableInit(varName.toString(),
				// "?"+varName.toString()+"In");

			}
		}

		return returnMap;

	}

	

	@Override
	public List<VariableEnv> visitBlock(BlockTree arg0, Boolean addToEnv)
	{

		// System.out.println("Var Visitor: Block");
		// System.out.println(arg0);
		List<VariableEnv> returnMap = new ArrayList<VariableEnv>();

		List<? extends StatementTree> statements = arg0.getStatements();

		Iterator<? extends StatementTree> i = statements.iterator();

		while (i.hasNext())
		{
			StatementTree st = i.next();
			// System.out.println("Var Visitor Block: st kind = " +
			// st.getKind());
			List<VariableEnv> statementReturn = st.accept(this, addToEnv);

			if (statementReturn != null)
			{
				// System.out.println("Var Visitor: Block: Returned Adding");
				returnMap.addAll(statementReturn);
			}
			// else
			// {
			// System.out.println("Var Visitor: Block: Returned Null");
			// }
		}

		return returnMap;

	}

	
	
	@Override
	public List<VariableEnv> visitNewClass(NewClassTree arg0, Boolean addToEnv)
	{
		System.out.println("*** VarVisitor: New Class Tree");
		
		List<? extends ExpressionTree> args = arg0.getArguments();
		
		//TODO WHAT TO DO?
				
				
		return null;
	}

	

	@Override //return VariableEnv
	public List<VariableEnv> visitVariable(VariableTree arg0, Boolean addToEnv)
	{

		System.out.println("Var Visitor: Variable for " + arg0);

		List<VariableEnv> returnMap = new ArrayList<VariableEnv>();

		System.out.println("-> Name = " + arg0.getName() + " Type = "
				+ arg0.getType() + " Init = " + arg0.getInitializer());

		Name varName = arg0.getName();

		Tree varType = arg0.getType();
		
		String init = "";
		if (arg0.getInitializer() != null)
		{
			List<VariableEnv> initialiser = arg0.getInitializer().accept(this, addToEnv);
			System.out.println("/*/* Init of " + arg0.getName() + " is = " + initialiser);
			init = arg0.getInitializer().toString();
		}

		VariableEnv v = new VariableEnv();
		v.setVariableName(varName.toString());
		v.setVariableType(varType.toString());
		
		returnMap.add(v);
		
		assert (varName != null);
	//	assert (programEnv != null);
		
		if (classEnv != null && addToEnv == true)
		{
			System.out.println("var Visitor If");
			if (varType.getKind() == Tree.Kind.PRIMITIVE_TYPE)
			{
				System.out.println("var Visitor Primitive Type");
				classEnv.addVariable(NewTransUtils.encodeName(varName),
						NewTransUtils.encodeType(varType), init, true);
			}
			else if ((!(objectEnv.getName().toString().contains(varType
					.toString()))))
			{
				System.out.println("var Visitor var type not Objdect name");
				// if (programEnv.getSchedulable(varName) != null)
				{
					System.out.println("*/*/ New Parameter for "
							+ classEnv.getName() + " with name= "
							+ NewTransUtils.encodeName(varName) + " type = "
							+ varType.toString() + " programType = "
							+ varType.toString() + " and primitve = " + false);

					String encodedName = NewTransUtils.encodeName(varName);
					String varTypeString = varType.toString();

					if (GENERIC_PARADIGM_TYPES.contains(varTypeString))
					{
						String varTypefromName = WordUtils
								.capitalize(encodedName) + "ID";
						
////						objectEnv.addParameter(encodedName, varTypeString,
//								varTypefromName, false, init);

					}
					else
					{
//						objectEnv.addParameter(encodedName, varTypeString,
//								varType.toString() + "ID", false, init);
					}
					
					System.out.println("*/*/ Adding Parameter " + encodedName + " to ObjectEnv " +objectEnv.getName() + "of type " + objectEnv.getClass().getCanonicalName());
				}
			}
			else if (programEnv.getSchedulable(varName.toString()) != null)
			{
				System.out.println("Var Visitor var name is a schedulable");
			}
			else
			{
				System.out.println("var Visitor add var to Object Env");

				classEnv.addVariable("\\circreftype " + varName.toString()
						+ "Class", varType.toString() + "Class", "\\circnew "
						+ varType.toString() + "Class()", false);
			}
		}
		return returnMap;
	}

	@Override
	public List<VariableEnv> visitAnnotatedType(AnnotatedTypeTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitAnnotation(AnnotationTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitArrayAccess(ArrayAccessTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitArrayType(ArrayTypeTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitAssert(AssertTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitBinary(BinaryTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitBreak(BreakTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitCase(CaseTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitCatch(CatchTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitClass(ClassTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitCompilationUnit(CompilationUnitTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitCompoundAssignment(
			CompoundAssignmentTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitConditionalExpression(
			ConditionalExpressionTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitContinue(ContinueTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitDoWhileLoop(DoWhileLoopTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitEmptyStatement(EmptyStatementTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitEnhancedForLoop(EnhancedForLoopTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitErroneous(ErroneousTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitExpressionStatement(
			ExpressionStatementTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitForLoop(ForLoopTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitIdentifier(IdentifierTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitIf(IfTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitImport(ImportTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitInstanceOf(InstanceOfTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitIntersectionType(IntersectionTypeTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitLabeledStatement(LabeledStatementTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitLambdaExpression(LambdaExpressionTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitLiteral(LiteralTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitMemberReference(MemberReferenceTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitMemberSelect(MemberSelectTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitMethod(MethodTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitMethodInvocation(MethodInvocationTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitModifiers(ModifiersTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitNewArray(NewArrayTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitOther(Tree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitParameterizedType(ParameterizedTypeTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitParenthesized(ParenthesizedTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitPrimitiveType(PrimitiveTypeTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitReturn(ReturnTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitSwitch(SwitchTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitSynchronized(SynchronizedTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitThrow(ThrowTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitTry(TryTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitTypeCast(TypeCastTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitTypeParameter(TypeParameterTree arg0,
			Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitUnary(UnaryTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitUnionType(UnionTypeTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitWhileLoop(WhileLoopTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VariableEnv> visitWildcard(WildcardTree arg0, Boolean arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	
}
package hijac.tools.modelgen.circus.visitors;

import freemarker.template.TemplateModelException;

import hijac.tools.modelgen.circus.SCJApplication;
import hijac.tools.modelgen.circus.datamodel.MacroModel;
import hijac.tools.modelgen.circus.templates.CircusTemplateFactory;
import hijac.tools.modelgen.circus.utils.TransUtils;

import javacutils.TreeUtils;


import com.sun.source.tree.*;
import com.sun.source.util.SimpleTreeVisitor;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

/**
 * Visitor that translates active methods into Circus actions.
 *
 * @author Frank Zeyda
 * @version $Revision$
 */
public class AMethodVisitor extends
      SimpleTreeVisitor<String, MethodVisitorContext> {

   protected final SCJApplication CONTEXT;

   public AMethodVisitor(SCJApplication context) {
      super(TransUtils.FAILED_RESULT);
      assert context != null;
      CONTEXT = context;
   }

   public void initMacroModel(Tree node, MethodVisitorContext ctxt) {
      /* Importantly we have to duplicate the context as we are going to
       * change the macro model and the context is assumed to be immutable. */
      ctxt.MACRO_MODEL = ctxt.MACRO_MODEL.copy();
      ctxt.MACRO_MODEL.put("NODE", node);
      ctxt.MACRO_MODEL.put("CTXT", ctxt);
      ctxt.MACRO_MODEL.put("TRANS", CONTEXT.TEMPLATES.FACTORY.ACTION_METHOD);
   }

   protected String doMacroCall(Tree node, MethodVisitorContext ctxt,
         String file, String name, Object... args) {
      initMacroModel(node, ctxt);
      return CONTEXT.TEMPLATES.FACTORY.doMacroCall(
         ctxt.MACRO_MODEL, file, name, args);
   }

   protected String callStmtMacro(Tree node, MethodVisitorContext ctxt,
         String name, Object... args) {
      return doMacroCall(node, ctxt, "Stmt.ftl", name, args);
   }

   protected String callExprMacro(Tree node, MethodVisitorContext ctxt,
         String name, Object... args) {
      return doMacroCall(node, ctxt, "Expr.ftl", name, args);
   }

   protected String callTypeMacro(Tree node, MethodVisitorContext ctxt,
         String name, Object... args) {
      return doMacroCall(node, ctxt, "Type.ftl", name, args);
   }

   @Override
   public String visitAnnotation(
         AnnotationTree node, MethodVisitorContext ctxt) {
      /* TODO: Raise a proper translation error below instead of an assertion
       * failure. This still needs to be designed in the view of visitors. */
      throw new AssertionError("Unexpected annotation AST node.");
   }

   @Override
   public String visitArrayAccess(
         ArrayAccessTree node, MethodVisitorContext ctxt) {
      return callExprMacro(node, ctxt,
         "ArrayAccess", node.getExpression(), node.getIndex());
   }

   @Override
   public String visitAssert(
         AssertTree node, MethodVisitorContext ctxt) {
      /* Should we check that we are not inside an expression? */
      return callStmtMacro(node, ctxt, "Assert", node.getCondition());
   }

   @Override
   public String visitAssignment(
         AssignmentTree node, MethodVisitorContext ctxt) {
      /* Should we check that we are not inside an expression? */
      return callStmtMacro(node, ctxt,
         "Assignment", node.getVariable(), node.getExpression());
   }

   @Override
   public String visitBinary(
         BinaryTree node, MethodVisitorContext ctxt) {
      /* What if the operands are boolean? Equality raises issues. */
      return callExprMacro(node, ctxt,
         "Binary", node.getLeftOperand(), node.getRightOperand());
   }

   @Override
   public String visitBlock(
         BlockTree node, MethodVisitorContext ctxt) {
      return callStmtMacro(node, ctxt, "Block", node.getStatements());
   }

   @Override
   public String visitBreak(
         BreakTree node, MethodVisitorContext ctxt) {
      /* TODO: Check that we are inside a switch statement here. */
      return callStmtMacro(node, ctxt, "Break");
   }

   @Override
   public String visitCase(
         CaseTree node, MethodVisitorContext ctxt) {
      /* TODO: Validate that the case is terminated with a break. */
      return callStmtMacro(node, ctxt,
         "Case", node.getExpression(), node.getStatements());
   }

   @Override
   public String visitCompoundAssignment(
         CompoundAssignmentTree node, MethodVisitorContext ctxt) {
      /* Should we check that we are not inside an expression? */
      return callStmtMacro(node, ctxt,
         "CompoundAssignment", node.getVariable(), node.getExpression());
   }

   @Override
   public String visitConditionalExpression(
         ConditionalExpressionTree node, MethodVisitorContext ctxt) {
      return callExprMacro(node, ctxt,
         "ConditionalExpression", node.getCondition(),
         node.getTrueExpression(), node.getFalseExpression());
   }

   @Override
   public String visitEmptyStatement(
         EmptyStatementTree node, MethodVisitorContext ctxt) {
      return callStmtMacro(node, ctxt, "EmptyStatement");
   }

   @Override
   public String visitErroneous(
         ErroneousTree node, MethodVisitorContext ctxt) {
      throw new AssertionError("Erroneous AST node encountered.");
   }

   @Override
   public String visitExpressionStatement(
         ExpressionStatementTree node, MethodVisitorContext ctxt) {
      /* Are those nodes permissible? Do a bit more investigation here. */
      return callStmtMacro(node, ctxt,
         "ExpressionStatement", node.getExpression());
   }

   @Override
   public String visitForLoop(
         ForLoopTree node, MethodVisitorContext ctxt) {
      return callStmtMacro(node, ctxt,
         "ForLoop", node.getInitializer(), node.getCondition(),
         node.getStatement(), node.getUpdate());
   }

   @Override
   public String visitIdentifier(
         IdentifierTree node, MethodVisitorContext ctxt) {
      return callExprMacro(node, ctxt, "Identifier", node.getName());
   }

   @Override
   public String visitIf(
         IfTree node, MethodVisitorContext ctxt) {
      return callStmtMacro(node, ctxt,
         "If", node.getCondition(),
         node.getThenStatement(), node.getElseStatement());
   }

   @Override
   public String visitLabeledStatement(
         LabeledStatementTree node, MethodVisitorContext ctxt) {
      return callStmtMacro(node, ctxt,
         "LabeledStatement", node.getStatement());
   }

   @Override
   public String visitLiteral(
         LiteralTree node, MethodVisitorContext ctxt) {
      /* Should we do the translation in a template macro here too? */
      return TransUtils.encodeLiteral(node);
   }

   @Override
   public String visitMemberSelect(
         MemberSelectTree node, MethodVisitorContext ctxt) {
      /* Are MemberSelect nodes also used for selecting methods too? */
      return callExprMacro(node, ctxt,
         "MemberSelect", node.getExpression(), node.getIdentifier());
   }

   @Override
   public String visitMethod(
         MethodTree node, MethodVisitorContext ctxt) {
      return visit(node.getBody(), ctxt);
   }

   @Override
   public String visitMethodInvocation(
         MethodInvocationTree node, MethodVisitorContext ctxt) {
      ExecutableElement method = TreeUtils.elementFromUse(node);
      List<? extends VariableElement> params = method.getParameters();
      List<ExpressionTree> arguments = new ArrayList<ExpressionTree>();
      for (int index = 0; index < params.size(); index++) {
         if (!CONTEXT.ANNOTS.isInteractionCode(params.get(index)) &&
            !CONTEXT.ANNOTS.isIgnored(params.get(index))) {
            arguments.add(node.getArguments().get(index));
         }
      }
      /* Infer the method here that is called and pass to it the macro. */
      /* Question: Is that generally feasible? */
      return callExprMacro(node, ctxt,
         "MethodInvocation", node.getMethodSelect(), arguments);
   }

   @Override
   public String visitNewClass(
         NewClassTree node, MethodVisitorContext ctxt) {
      ExecutableElement ctor = TreeUtils.elementFromUse(node);
      List<? extends VariableElement> params = ctor.getParameters();
      List<ExpressionTree> arguments = new ArrayList<ExpressionTree>();
      for (int index = 0; index < params.size(); index++) {
         if (!CONTEXT.ANNOTS.isInteractionCode(params.get(index)) &&
            !CONTEXT.ANNOTS.isIgnored(params.get(index))) {
            arguments.add(node.getArguments().get(index));
         }
      }
      return callExprMacro(node, ctxt,
         "NewClass", node.getIdentifier(), arguments);
   }

   @Override
   public String visitParenthesized(
         ParenthesizedTree node, MethodVisitorContext ctxt) {
      /* Does the JDK parser retain information on bracketing in the AST? */
      return callExprMacro(node, ctxt,
         "Parenthesized", node.getExpression());
   }

   @Override
   public String visitPrimitiveType(
         PrimitiveTypeTree node, MethodVisitorContext ctxt) {
      return callTypeMacro(node, ctxt,
         "PrimitiveType", node.getPrimitiveTypeKind());
   }

   @Override
   public String visitReturn(
         ReturnTree node, MethodVisitorContext ctxt) {
      return callStmtMacro(node, ctxt, "Return", node.getExpression());
   }

   @Override
   public String visitSwitch(
         SwitchTree node, MethodVisitorContext ctxt) {
      return callStmtMacro(node, ctxt,
         "Switch", node.getExpression(), node.getCases());
   }

   @Override
   public String visitUnary(
         UnaryTree node, MethodVisitorContext ctxt) {
      /* What if the operand is boolean? Equality raises issues. */
      return callExprMacro(node, ctxt, "Unary", node.getExpression());
   }

   @Override
   public String visitVariable(
         VariableTree node, MethodVisitorContext ctxt) {
      return callStmtMacro(node, ctxt,
         "Variable", node.getName(), node.getInitializer());
   }

   @Override
   public String visitWhileLoop(
         WhileLoopTree node, MethodVisitorContext ctxt) {
      return callStmtMacro(node, ctxt,
         "WhileLoop", node.getCondition(), node.getStatement());
   }
}

package hijac.tools.tightrope.visitors;

import hijac.tools.application.TightRope;
import hijac.tools.modelgen.circus.templates.CircusTemplateFactory;
import hijac.tools.modelgen.circus.templates.CircusTemplates;
import hijac.tools.modelgen.circus.visitors.MethodVisitorContext;
import hijac.tools.tightrope.environments.AperiodicEventHandlerEnv;
import hijac.tools.tightrope.environments.ClassEnv;
import hijac.tools.tightrope.environments.ManagedThreadEnv;
import hijac.tools.tightrope.environments.MethodEnv;
import hijac.tools.tightrope.environments.MissionEnv;
import hijac.tools.tightrope.environments.MissionSequencerEnv;
import hijac.tools.tightrope.environments.ObjectEnv;
import hijac.tools.tightrope.environments.OneShotEventHandlerEnv;
import hijac.tools.tightrope.environments.ParadigmEnv;
import hijac.tools.tightrope.environments.PeriodicEventHandlerEnv;
import hijac.tools.tightrope.environments.SafeletEnv;
import hijac.tools.tightrope.environments.StructureEnv;
import hijac.tools.tightrope.generators.NewActionMethodModel;
import hijac.tools.tightrope.generators.NewSCJApplication;
import hijac.tools.tightrope.utils.Debugger;
import hijac.tools.tightrope.utils.TightRopeTransUtils;
import hijac.tools.tightrope.utils.TightRopeString;
import hijac.tools.tightrope.utils.TightRopeString.LATEX;
import hijac.tools.tightrope.utils.TightRopeString.Location;

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
import com.sun.source.tree.StatementTree;
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
 * 
 * 
 * @author Matt Luckcuck
 */

public class MethodBodyVisitor extends SimpleTreeVisitor<String, MethodVisitorContext>
{

  /**
   * Used to store values for 'future' calls to methods of the visitor
   */
  private static final HashMap<String, Object> timeMachine = new HashMap<String, Object>();

  private MethodEnv methodEnv = null;
  private LiteralTree trueLiteral = null;
  private LiteralTree falseLiteral = null;
  private ObjectEnv object;

  private final static ArrayList<String> GENERIC_PARADIGM_TYPES = ParadigmEnv
      .getGenericParadigmTypes();

  protected final NewSCJApplication CONTEXT;
  private String varType;
  // private boolean classEnv;
  private boolean putSkip = false;

  // private NonParadigmBuilder nonParadigmBuilder;

  /**
   * Constructor for the Method Body Visitor
   * 
   * @param context
   *          The NewSCJApplication object for this application
   * @param object
   *          The object in which the method on which we are operating resides
   */
  public MethodBodyVisitor(NewSCJApplication context, ObjectEnv object)
  {
    super(TightRopeTransUtils.FAILED_RESULT);
    assert context != null;
    CONTEXT = context;
    this.object = object;

  }

  /**
   * Constructor for the Method Body Visitor
   * 
   * @param context
   *          The NewSCJApplication object for this application
   * @param object
   *          The object in which the method on which we are operating resides
   * @param env
   *          The environment for the method of which we exploring the body
   */
  public MethodBodyVisitor(NewSCJApplication context, ObjectEnv object, MethodEnv env)
  {
    super(TightRopeTransUtils.FAILED_RESULT);
    assert context != null;
    CONTEXT = context;
    this.object = object;
    this.methodEnv = env;

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
          return (R) LATEX.RET_TRUE;
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
          return (R) LATEX.RET_FALSE;
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
     * Importantly we have to duplicate the context as we are going to change
     * the macro model and the context is assumed to be immutable.
     */
    ctxt.MACRO_MODEL = ctxt.MACRO_MODEL.copy();
    ctxt.MACRO_MODEL.put("NODE", node);
    ctxt.MACRO_MODEL.put("CTXT", ctxt);

    if (methodEnv != null)
    {
      ctxt.MACRO_MODEL.put("TRANS", new NewActionMethodModel(CONTEXT, object, methodEnv));
    }
    else
    {
      ctxt.MACRO_MODEL.put("TRANS", new NewActionMethodModel(CONTEXT, object));
    }
  }

  protected String doMacroCall(Tree node, MethodVisitorContext ctxt, String file,
      String name, Object... args)
  {
    initMacroModel(node, ctxt);

    CircusTemplates templates = CONTEXT.TEMPLATES;

    CircusTemplateFactory factory = templates.FACTORY;
    Debugger.log("doMacroCall node = " + node);
    return factory.doMacroCall(ctxt.MACRO_MODEL, file, name, args);
  }

  protected String callStmtMacro(Tree node, MethodVisitorContext ctxt, String name,
      Object... args)
  {
    return doMacroCall(node, ctxt, Location.STMT_TEMPLATE, name, args);
  }

  protected String callExprMacro(Tree node, MethodVisitorContext ctxt, String name,
      Object... args)
  {
    return doMacroCall(node, ctxt, Location.EXPR_TEMPLATE, name, args);
  }

  protected String callTypeMacro(Tree node, MethodVisitorContext ctxt, String name,
      Object... args)
  {
    return doMacroCall(node, ctxt, Location.TYPE_TEMPLATE, name, args);
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
  public String visitArrayAccess(ArrayAccessTree node, MethodVisitorContext ctxt)
  {
    return callExprMacro(node, ctxt, "ArrayAccess", node.getExpression(), node.getIndex());
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
    Debugger.log("/// AssignmentTree node = " + node);
    Debugger.log("/// AssignmentTree variable = " + node.getVariable());
    Debugger.log("/// AssignmentTree expression = " + node.getExpression());

    // methodAccessesVariable();

    final String nodeVariableString = node.getVariable().toString();

    if (node.getExpression() instanceof MethodInvocationTree)
    {
      Debugger.log("visitAssignment node = " + node);
      MethodInvocationTree mit = (MethodInvocationTree) node.getExpression();

      if (isSyncMethod(mit))
      {
        return visitMethodInvocation(mit, ctxt);
      }
      else
      {
        // TODO fix this, it's outputting " " which is BADNESS 90000
        // return "this~.~" + node.getVariable().toString() + " :="
        // + node.getExpression() ;
        Debugger.log("node.getExpression: " + node.getExpression());
        if (node.getExpression().toString().contains("~?~"))
        {
          return visitMethodInvocation(mit, ctxt) + LATEX.NEW_LINE + nodeVariableString
              + LATEX.ASSIGN + node.getExpression();
        }
        else
        {
          // This was trying to add `this' to bufferEmpty
          if(object.hasClass() )
          {
            for(MethodEnv me : object.getClassEnv().getMeths())
            {
              if(me.getName().toString().equals(mit))
              {
                return "this~.~"+callStmtMacro(node, ctxt, "Assignment", node.getVariable(),
                    node.getExpression());
              }
            }
          }
          
          return callStmtMacro(node, ctxt, "Assignment", node.getVariable(),
              node.getExpression());
        }
      }
    }
    else
    {
      ExpressionTree expressiontree = node.getExpression();
      String expression = "";

      if (expressiontree instanceof LiteralTree)
      {
        expression = TightRopeTransUtils.encodeLiteral((LiteralTree) expressiontree);
      }
      else
      {
        expression = expressiontree.toString();
      }

      if (object instanceof ParadigmEnv)
      {
        ObjectEnv p = (ObjectEnv) object;
        ClassEnv cE = p.getClassEnv();
        if (cE != null)
        {
          Debugger.log(cE);
          if (cE.getVariable(nodeVariableString) != null)
          {

            if (node.getExpression() instanceof LiteralTree)
            {
              expression = TightRopeTransUtils.encodeLiteral((LiteralTree) node
                  .getExpression());
            }

            return "this~.~" + nodeVariableString + LATEX.ASSIGN + expression;
          }
        }
        else
        {
          Debugger.log("cE was null");
        }

      }

      if (object.getVariable(nodeVariableString) == null)
      {
        return "this~.~" + nodeVariableString + LATEX.ASSIGN + expression;
      }
      else
      {
        return callStmtMacro(node, ctxt, "Assignment", node.getVariable(), expression);
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
    Debugger.log("Visit Block, node= " + node.toString() + " and num of statements = "
        + node.getStatements().size());

    List<String> processedBlockStatements = new ArrayList<String>();
    // processedBlockStatements.addAll(node.getStatements());

    Iterator<? extends StatementTree> iter = node.getStatements().iterator();
    StatementTree st = null;

    while (iter.hasNext())
    {
      st = iter.next();

      Debugger.log("/// st=" + st.toString() + " st.kind=" + st.getKind());
      // This seemed to block whole blocks that contain a 'Console' from being
      // captured.
      // if (st instanceof ExpressionStatementTree)
      //
      // {
      // if (!(node.toString().contains("Console") ||
      // node.toString().contains(
      // "System")))
      // {
      // String statementString = st.accept(this, ctxt);
      //
      // processedBlockStatements.add(statementString);
      // }
      // }
      // else
      {

        String statementString = st.accept(this, ctxt);

        processedBlockStatements.add(statementString);
      }

    }

    return callStmtMacro(node, ctxt, "Block", processedBlockStatements);
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
    return callStmtMacro(node, ctxt, "Case", node.getExpression(), node.getStatements());
  }

  @Override
  public String visitCompoundAssignment(CompoundAssignmentTree node,
      MethodVisitorContext ctxt)
  {
    // Debugger.log("/// CompoundAssignmentTree node = " + node);

    /* Should we check that we are not inside an expression? */
    return callStmtMacro(node, ctxt, "CompoundAssignment", node.getVariable(),
        node.getExpression());
  }

  @Override
  public String visitConditionalExpression(ConditionalExpressionTree node,
      MethodVisitorContext ctxt)
  {
    return callExprMacro(node, ctxt, "ConditionalExpression", node.getCondition(),
        node.getTrueExpression(), node.getFalseExpression());
  }

  @Override
  public String visitEmptyStatement(EmptyStatementTree node, MethodVisitorContext ctxt)
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

    Debugger.log("Expression Statement, node = " + node);

    // TODO HACKERY!
    if (node.toString().contains("Console") || node.toString().contains("System"))
    {
      Debugger.log("*** Console so ignoring *** ");
      return "";
    }
    else
    {
      return callStmtMacro(node, ctxt, "ExpressionStatement", node.getExpression());
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
    Name nodeName = node.getName();

    StructureEnv framework = TightRope.getProgramEnv().getStructureEnv();

    ObjectEnv o = framework.getObjectEnv(nodeName.toString());
    String id = "";
    if (o != null)
    {
      id = o.getId();
      Debugger.log(id);
      assert (false);
      return callExprMacro(node, ctxt, "Identifier", id);
    }

    // if(object.getVariable(nodeName) != null)
    // {
    // methodAccessesVariable();
    // }

    return callExprMacro(node, ctxt, "Identifier", nodeName);

  }

  @Override
  public String visitIf(IfTree node, MethodVisitorContext ctxt)
  {
    return callStmtMacro(node, ctxt, "If", node.getCondition(), node.getThenStatement(),
        node.getElseStatement());
  }

  @Override
  public String visitLabeledStatement(LabeledStatementTree node, MethodVisitorContext ctxt)
  {
    return callStmtMacro(node, ctxt, "LabeledStatement", node.getStatement());
  }

  @Override
  public String visitLiteral(LiteralTree node, MethodVisitorContext ctxt)
  {
    /* Should we do the translation in a template macro here too? */

    // This is supposed to cater for null id values, but is a bit hacky...

    if (methodEnv != null)
    {
      String returnType = methodEnv.getReturnType();

      if (node.getKind() == Tree.Kind.NEW_CLASS || node.getKind() == Tree.Kind.IDENTIFIER
          || node.getKind() == Tree.Kind.NULL_LITERAL)
      {
        if (returnType.contains("MissionID"))
        {
          return hijac.tools.tightrope.utils.TightRopeString.Name.NULL_M_ID;
        }
        else if (returnType.contains("SchedulableId"))
        {
          return hijac.tools.tightrope.utils.TightRopeString.Name.NULL_S_ID;
        }
        else
        {
          Debugger.log("/// String is null");
          return TightRopeTransUtils.encodeLiteral(node);
        }
      }
      else
      {

        Debugger.log("///  kind is not new class or identifier");
        return TightRopeTransUtils.encodeLiteral(node);
      }

    }
    else
    {

      Debugger.log("/// methodEnv is null ");
      return TightRopeTransUtils.encodeLiteral(node);
    }
  }

  @Override
  public String visitMemberSelect(MemberSelectTree node, MethodVisitorContext ctxt)
  {
    Debugger.log("/// member Select, node = " + node);

    /* Are MemberSelect nodes also used for selecting methods too? */
    return callExprMacro(node, ctxt, "MemberSelect", node.getExpression(),
        node.getIdentifier());
  }

  @Override
  public String visitMethod(MethodTree node, MethodVisitorContext ctxt)
  {
    return visit(node.getBody(), ctxt);
  }

  @Override
  public String visitMethodInvocation(MethodInvocationTree node, MethodVisitorContext ctxt)
  {
    Debugger.log("/// methodInvocation node = " + node);

    ExpressionTree methodSelect = node.getMethodSelect();

    String output = "";
    Name identifier = null;
    Debugger.log("methodSelect = " + methodSelect + " and type = "
        + methodSelect.getKind());

    if (methodSelect instanceof MemberSelectTree)
    {
      ExpressionTree expresison = ((MemberSelectTree) methodSelect).getExpression();

      Debugger.log("expression = " + expresison + " and type = " + expresison.getKind());
      if (expresison instanceof MethodInvocationTree)
      {
        // This is for if the method call is: o.meth1().meth2();
        output = visitMethodInvocation((MethodInvocationTree) expresison, ctxt);
        output += "\\\\ ";

        Debugger.log("Output = " + output);

        methodSelect = ((MethodInvocationTree) expresison).getMethodSelect();

        identifier = ((MemberSelectTree) methodSelect).getIdentifier();

        Debugger.log("/*/* methodSelect = " + methodSelect);
      }
    }

    if (methodSelect instanceof MemberSelectTree)
    {
      MemberSelectTree mst = (MemberSelectTree) methodSelect;

      if (mst.getExpression().toString().startsWith("System"))
      {
        // return LATEX.SKIP;
        return "";
      }

      identifier = mst.getIdentifier();
    }
    else if (methodSelect instanceof IdentifierTree)
    {
      IdentifierTree it = (IdentifierTree) methodSelect;

      identifier = it.getName();
    }

    // String objectID = object.getObjectId();
    StringBuilder sb = new StringBuilder();

    if (identifier != null)
    {
      if (identifier.contentEquals("notify"))
      {
        object.setNeedsThread(true);

        object.addParent("ObjectChan");
        object.addParent("ObjectIds");
        object.addParent("ThreadIds");

        sb.append("notify" + LATEX.DOT);
        sb.append(object.getObjectId());
        sb.append(LATEX.SHRIEK + "thread " + LATEX.THEN);
        sb.append(LATEX.NEW_LINE);
        sb.append(LATEX.SKIP);

        timeMachine.put("methodCall", false);

        if (putSkip)
        {
          sb.append(TightRopeString.LATEX.SKIP);
        }
        return sb.toString();
      }
      else if (identifier.contentEquals("wait"))
      {
        object.setNeedsThread(true);

        object.addParent("ObjectChan");
        object.addParent("ObjectIds");
        object.addParent("ThreadIds");

        sb.append("waitCall" + LATEX.DOT);
        sb.append(object.getObjectId());
        sb.append(LATEX.DOT + "thread " + LATEX.THEN);
        sb.append(LATEX.NEW_LINE);
        sb.append("waitRet~.~");
        sb.append(object.getObjectId());
        sb.append(LATEX.DOT + "thread " + LATEX.THEN);
        sb.append(LATEX.NEW_LINE);
        sb.append(LATEX.SKIP);

        timeMachine.put("methodCall", false);

        if (putSkip)
        {
          sb.append(TightRopeString.LATEX.SKIP);
        }
        return sb.toString();
      }
      else if (identifier.contentEquals("release"))
      {
        sb.append(identifier);
        sb.append("Call");

        sb.append(LATEX.DOT);
        sb.append(((MemberSelectTree) node.getMethodSelect()).getExpression().toString());

        {
          sb.append(LATEX.DOT);
          // sb.append("TESTE"+objectID.toString());
          sb.append(object.getId());
        }
        sb.append(LATEX.THEN);
        sb.append(LATEX.NEW_LINE);
        sb.append(LATEX.SKIP);

        return sb.toString();
      }

      else if (isNotMyMethod(methodSelect))
      {
        MethodEnv method = getMethodEnvBeingCalled(methodSelect);
        method.setExternalAppMethod(true);

        // final String methodName = mt.getName().toString();
        // assert(false);
        // ((MemberSelectTree)
        // node.getMethodSelect()).getExpression().toString();
        // boolean notIgnoredMethod =
        // ((!identifier.contentEquals("release"))
        // && (!identifier.contentEquals("requestTermination")) &&
        // (!identifier
        // .contentEquals("terminationPending")));

        final boolean notIgnoredMethod = !((identifier.contentEquals("<init>"))
            || (identifier.contentEquals("handleAsyncEvent"))
            || (identifier.contentEquals("run"))
            || (identifier.contentEquals("initialize"))
            || (identifier.contentEquals("missionMemorySize"))
            || (identifier.contentEquals("main"))
            || (identifier.contentEquals("initializeApplication"))
            || (identifier.contentEquals("cleanUp"))
            || (identifier.contentEquals("immortalMemorySize"))
            || (identifier.contentEquals("getSequencer"))
            || (identifier.contentEquals("getNextMission"))
            || (identifier.contentEquals("release")) || (identifier
            .contentEquals("requestTermination")));

        Debugger.log("/*/*NotIg Id=" + identifier + "  notIg=" + notIgnoredMethod);

        if (notIgnoredMethod)
        {
          // TODO I think here is where I need to check the class
          // methods map for the *actual* location of the method I'm
          // calling...I think
          String loc = varType.toString();

          if (TightRope.getProgramEnv().containsNonParadigmObject(loc))
          {
            loc += "ID";
          }

          TightRope.getProgramEnv().addBinderMethodEnv(method, loc, object.getId(),
              object.getIdType());
        }

        if (method.isAPIMethod())
        {
          ObjectEnv oEnv = getObjectEnvOfMethod(methodSelect);
          Debugger.log(oEnv);

          Name nodeName = oEnv.getName();

          for (TypeElement t : CONTEXT.getAnalysis().getTypeElements())
          {
            Debugger.log(t.getSimpleName());
            Debugger.log(nodeName);
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

            if (object != null)
            {
              TightRope.getProgramEnv().addThreadIdName(object.getName().toString());
            }
          }

        }
        else
        {
          object.addParent(getObjectEnvOfMethod(methodSelect).getName().toString()
              + "MethChan");
          if (method.isSynchronised())
          {
            object.addParent("ObjectIds");
            object.addParent("ThreadIds");

            if (object != null)
            {
              TightRope.getProgramEnv().addThreadIdName(object.getName().toString());
            }
          }
        }

        String returnString = method.getReturnType();
        List<? extends ExpressionTree> parameters = node.getArguments();

        if (notIgnoredMethod)
        {
          sb.append(TightRopeString.Name.BINDER);
        }
        sb.append(identifier);
        sb.append("Call");

        sb.append(LATEX.DOT);

        String methodSelectExpr = ((MemberSelectTree) node.getMethodSelect())
            .getExpression().toString();
        Debugger.log("MemberSelect Expression " + methodSelectExpr);
        if (TightRope.getProgramEnv().containsNonParadigmObject(methodSelectExpr))
        {
          sb.append(methodSelectExpr + "ID");
        }
        else
        {
          sb.append(methodSelectExpr);
        }

        if ((!method.isAPIMethod()) || (method.getName().contains("requestTermination")))

        {
          sb.append(LATEX.DOT);
          // sb.append("TESTE"+objectID.toString());
          sb.append(object.getId());
        }

        final String threadId = object.getThreadId();
        if (method.isSynchronised())
        {
          sb.append(LATEX.DOT);
          sb.append(threadId);
        }

        if (!parameters.isEmpty())
        {
          for (ExpressionTree s : parameters)
          {
            sb.append(LATEX.SHRIEK);
            if (s instanceof IdentifierTree)
            {
              sb.append(((IdentifierTree) s).getName().toString());
            }
            else if (s instanceof LiteralTree)
            {
              if (s.getKind().equals(Tree.Kind.STRING_LITERAL))
              {

              }
              else
              {
                sb.append(((LiteralTree) s).getValue().toString());
              }
            }
          }
        }
        sb.append(LATEX.THEN + LATEX.NEW_LINE);

        if (notIgnoredMethod)
        {
          sb.append(hijac.tools.tightrope.utils.TightRopeString.Name.BINDER);
        }
        sb.append(identifier);
        sb.append("Ret");
        sb.append(LATEX.DOT);

        // String methodSelectExpr = ((MemberSelectTree)
        // node.getMethodSelect()).getExpression().toString();
        Debugger.log("MemberSelect Expression " + methodSelectExpr);
        if (TightRope.getProgramEnv().containsNonParadigmObject(methodSelectExpr))
        {
          sb.append(methodSelectExpr + "ID");
        }
        else
        {
          sb.append(methodSelectExpr);
        }

        if ((!method.isAPIMethod()) || (method.getName().contains("requestTermination")))
        {
          sb.append(LATEX.DOT);
          // sb.append(objectID.toString());
          sb.append(object.getId());
        }

        if (method.isSynchronised())
        {
          sb.append(LATEX.DOT);
          sb.append(threadId);
        }

        Debugger.log("!// !returnString.contains('null') =  "
            + (!returnString.contains("null")));
        Debugger.log("!// returnString =  " + returnString);
        if (!returnString.contains("null"))
        {

          sb.append("~?~");
          final Object identifierString = timeMachine.get(identifier.toString());
          Debugger.log("!// return string not 'null', getting key: "
              + identifier.toString() + " value: " + identifierString);

          sb.append(identifier.toString());
          timeMachine.put("variableIdentifier", identifier);
          sb.append(LATEX.THEN + LATEX.NEW_LINE);
          // This adds a skip when not needed, somtimes.
          sb.append(LATEX.SKIP);

        }
        else
        {
          sb.append(LATEX.THEN + LATEX.NEW_LINE);
          sb.append(LATEX.SKIP);
        }

        timeMachine.put("methodCall", false);

        if (putSkip)
        {
          sb.append(TightRopeString.LATEX.SKIP);
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
          if (TightRope.useAnnotations())
          {
            if (!CONTEXT.ANNOTS.isInteractionCode(params.get(index))
                && !CONTEXT.ANNOTS.isIgnored(params.get(index)))
            {
              arguments.add(node.getArguments().get(index));
            }
          }
          else
          {
            arguments.add(node.getArguments().get(index));
          }
        }

        timeMachine.put("methodCall", true);
        /*
         * (FRANK) Infer the method here that is called and pass to it the
         * macro.
         */
        /* Question: Is that generally feasible? */
        return output
            + callExprMacro(node, ctxt, "MethodInvocation", node.getMethodSelect(),
                arguments);

      }
    }
    else
    {
      Debugger.log("*** Identifier was null, returning empty string***");
      return "";
    }

  }

  private boolean isNotMyMethod(ExpressionTree methodSelect)
  {
    // MethodEnv method = getMethodEnvBeingCalled(node);

    // ExpressionTree methodSelect = node.getMethodSelect();

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
        Debugger.log("/// not my method, it's from " + expressionString);
        return true;
      }

    }
    else if (methodSelect instanceof IdentifierTree)
    {
      Debugger.log("// I can just call it, so it's my method");
      return false;
    }
    else
    {
      Debugger.log("/// not sure if it's my method");
      return false;
    }

  }

  private boolean isSyncMethod(MethodInvocationTree node)
  {
    MethodEnv m = getMethodEnvBeingCalled(node.getMethodSelect());
    
    if (m != null && m.isSynchronised())
    {
      return true;
    }
    else
    {
      return false;
    }

  }

  private MethodEnv getMethodEnvBeingCalled(ExpressionTree methodSelect)
  {
    // ExpressionTree methodSelect = node.getMethodSelect();
    Name identifier = null;

    ArrayList<MethodEnv> APIMethods = new ArrayList<MethodEnv>();
    // APIMethods.addAll(MISSION_API_METHODS);
    // APIMethods.addAll(EVENT_HANDLER_API_METHODS);

    String methodName = "";
    if (methodSelect instanceof MemberSelectTree)
    {
      methodName = ((MemberSelectTree) methodSelect).getIdentifier().toString();
    }
    else if (methodSelect instanceof IdentifierTree)
    {
      methodName = ((IdentifierTree) methodSelect).getName().toString();
    }

    for (MethodEnv me : APIMethods)
    {
      if (me.getName().contains(methodName))
      {
        Debugger.log("getMethodEnv returning method env for " + me.getName());
        return me;
      }
    }

    if (methodSelect instanceof MemberSelectTree)
    {

      MemberSelectTree mst = (MemberSelectTree) methodSelect;

      Debugger.log("/// node.getMethodSelect = " + mst.toString());

      Debugger.log("/// node...getExpression = " + mst.getExpression().toString());

      Debugger.log("/// node...getIdenitifer = " + mst.getIdentifier().toString());

      identifier = mst.getIdentifier();

      Debugger.log("/// identifier = " + identifier);

      // if (isAPIMethod(node, methodSelect))
      // {
      // return new MethodEnv(identifier.toString(), "void", true);
      // }
      // else
      {
        ObjectEnv o = getObjectEnvOfMethod(methodSelect);
        Debugger.log("methodSelect = " + methodSelect);
        Debugger.log("/// o is " + o.getName());

        return getMethodEnvFromObject(identifier, o);
      }
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

    ObjectEnv returnObject = null;
    ExpressionTree expression = methodSelect;

    // Doesn't cater to API objects and methods
    if (expression instanceof MemberSelectTree)
    {

      IdentifierTree identifier = null;

      expression = ((MemberSelectTree) methodSelect).getExpression();

      while (identifier == null)
      {
        Debugger.log("/// Expression = " + expression + " its kind = "
            + expression.getKind());

        if (expression instanceof MemberSelectTree)
        {
          expression = ((MemberSelectTree) expression).getExpression();
        }
        else if (expression instanceof MethodInvocationTree)
        {
          expression = ((MethodInvocationTree) expression).getMethodSelect();
        }
        else if (expression instanceof IdentifierTree)
        {
          identifier = (IdentifierTree) expression;
        }

      }

      Name varType = null;
      String IDString = "";
      final boolean objectNotNull = object != null;
      Debugger.log("/// objectNotNull = " + objectNotNull);
      if (objectNotNull)
      {
        Debugger.log("/// object name = " + object.getName());

        // For all the type elements in the program...
        for (TypeElement t : CONTEXT.getAnalysis().getTypeElements())
        {
          Debugger.log("///** t.getSimpleName() = " + t.getSimpleName());

          // ...if the name of the type equals the name of the object
          // we're in...
          if (t.getSimpleName().contentEquals(object.getName()))
          {
            // ... then that's our class tree!
            ClassTree ct = CONTEXT.getAnalysis().TREES.getTree(t);

            @SuppressWarnings("unchecked")
            List<Tree> members = (List<Tree>) ct.getMembers();
            Iterator<Tree> i = members.iterator();

            // iterate through all the members of our class...
            while (i.hasNext())
            {
              Tree tree = i.next();

              if (tree instanceof VariableTree)
              {
                VariableTree vt = (VariableTree) tree;

                Debugger.log("/// vt.getName = " + vt.getName());

                // / ... to find the variable with the same name
                // as the expression in our method call
                // i.e. expression.methodCall()
                if (vt.getName().contentEquals(identifier.toString()))
                {

                  Tree typeTree = vt.getType();

                  Debugger.log("/// typeTree = " + typeTree);

                  if (typeTree instanceof IdentifierTree)
                  {

                    IdentifierTree it = (IdentifierTree) typeTree;
                    for (TypeElement typeElem : CONTEXT.getAnalysis().getTypeElements())
                    {
                      if (typeElem.getSimpleName().contentEquals(vt.getType().toString()))
                      {
                        ClassTree classTree = CONTEXT.getAnalysis().TREES
                            .getTree(typeElem);

                        assert (classTree != null);

                        final Tree classTreeExtendsClause = classTree.getExtendsClause();

                        if (classTreeExtendsClause != null)
                        {

                          if (classTreeExtendsClause.toString().contains(
                              TightRopeString.ParadigmName.MISSION))
                          {
                            IDString = TightRopeString.Name.MID;
                          }
                          else if ((classTreeExtendsClause.toString()
                              .contains(TightRopeString.ParadigmName.MANAGED_THREAD))
                              || ct
                                  .getExtendsClause()
                                  .toString()
                                  .contains(
                                      TightRopeString.ParadigmName.APERIODIC_EVENT_HANDLER)
                              || ct
                                  .getExtendsClause()
                                  .toString()
                                  .contains(
                                      TightRopeString.ParadigmName.PERIODIC_EVENT_HANDLER)
                              || ct
                                  .getExtendsClause()
                                  .toString()
                                  .contains(
                                      TightRopeString.ParadigmName.ONE_SHOT_EVENT_HANDLER)
                              || ct
                                  .getExtendsClause()
                                  .toString()
                                  .contains(
                                      TightRopeString.ParadigmName.MISSION_SEQUENCER))
                          {
                            IDString = TightRopeString.Name.SID;
                          }
                        }
                      }
                    }

                    // And finally get the TYPE (as a
                    // String) of
                    // the variable we are calling the
                    // method of

                    varType = it.getName();

                    // String varTypeString =
                    // varType.toString();
                  }
                }
                else
                {
                  Debugger.log("/// vt not equal to the expression. vt = " + vt
                      + " and expresison = " + identifier);
                }
              }
            }
          }
        }

        this.varType = varType + IDString;
        Debugger.log("/// varType = " + varType);

        // Then we get the Type Element of the variable we're calling
        // the method on
        if (varIsGenericParadigm(varType))
        {
          Debugger.log("var is Generic Paradigm");
          returnObject = genericParadigm(varType);
          Debugger.log("returnObject = " + returnObject);
        }
        else
        {
          for (TypeElement t : CONTEXT.getAnalysis().getTypeElements())
          {
            Debugger.log("!// t = " + t.getSimpleName());

            final Name simpleName = t.getSimpleName();

            Debugger.log("/// t simpleName = " + simpleName);
            if (simpleName.contentEquals(varType))
            {
              // Get the object env of the class, that represents
              // the
              // variable we're calling the method on
              Debugger.log("getting Object Env of " + simpleName);
              ObjectEnv o = TightRope.getProgramEnv().getObjectEnv(simpleName);
              returnObject = o;
            }
          }
        }
      }
    }

    Debugger.log("returning returnObject = " + returnObject);
    return returnObject;
  }

  private ObjectEnv genericParadigm(Name varType)
  {
    Debugger.log("In genericParadigm, varType = " + varType);
    ObjectEnv returnObject;

    switch (varType.toString())
    {
      case "Safelet":
        Debugger.log("Triggered Safelet");
        returnObject = new SafeletEnv();

        break;
      case "Mission":
        Debugger.log("Triggered Mission");
        returnObject = new MissionEnv();
        break;
      case "MissionSequencer":
        Debugger.log("Triggered MissionSequencer");
        returnObject = new MissionSequencerEnv();
        break;
      case "AperiodicEventHandler":
        Debugger.log("Triggered AperiodicEventHandler");
        returnObject = new AperiodicEventHandlerEnv();
        break;
      case "OneShotEventHandler":
        Debugger.log("Triggered OneShotEventHandler");
        returnObject = new OneShotEventHandlerEnv();
        break;
      case "PeriodicEventHandler":
        Debugger.log("Triggered PeriodicEventHandler");
        returnObject = new PeriodicEventHandlerEnv();
        break;
      case "ManagedThread":
        Debugger.log("Triggered ManagedThread");
        returnObject = new ManagedThreadEnv();
        break;
      default:
        Debugger.log("Triggered null");
        returnObject = null;
        break;
    }

    returnObject.setName(varType);
    return returnObject;
  }

  private boolean varIsGenericParadigm(Name varType)
  {
    if (GENERIC_PARADIGM_TYPES.contains(varType.toString()))
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  private MethodEnv getMethodEnvFromObject(Name identifier, ObjectEnv o)
  {
    List<MethodEnv> methods = new ArrayList<MethodEnv>();
    methods.addAll(o.getSyncMeths());
    methods.addAll(o.getMeths());
    // methods.addAll(MISSION_API_METHODS);
    // methods.addAll(EVENT_HANDLER_API_METHODS);

    // TODO This falls over for API methods...
    for (MethodEnv mEnv : methods)
    {
      Debugger.log("/// mEnv.getMethodName = " + mEnv.getName() + " and identifier = "
          + identifier);
      if (mEnv.getName().contentEquals(identifier))
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
          String superClass = elem.getSuperclass().toString();

          if (superClass.contains("Mission"))
          {
            // if the thing we are returning is a mission then get
            // its ID...somehow
            // so far
            return n.toString() + TightRopeString.Name.MID;
          }

          // if (superClass.contains(""))

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
      return callExprMacro(node, ctxt, "NewClass", node.getIdentifier(), arguments);
    }
    return DEFAULT_VALUE;
  }

  @Override
  public String visitParenthesized(ParenthesizedTree node, MethodVisitorContext ctxt)
  {
    /* Does the JDK parser retain information on bracketing in the AST? */
    return callExprMacro(node, ctxt, "Parenthesized", node.getExpression());
  }

  @Override
  public String visitPrimitiveType(PrimitiveTypeTree node, MethodVisitorContext ctxt)
  {
    return callTypeMacro(node, ctxt, "PrimitiveType", node.getPrimitiveTypeKind());
  }

  @Override
  public String visitReturn(ReturnTree node, MethodVisitorContext ctxt)
  {
    ExpressionTree et = node.getExpression();

    if (et instanceof BinaryTree)
    {
      lazyLiteralConstructor();

      BinaryTree bt = (BinaryTree) et;
      return callStmtMacro(node, ctxt, "If", bt, trueLiteral, falseLiteral);
    }
    else
    {
      return callStmtMacro(node, ctxt, "Return", et);
    }
  }

  @Override
  public String visitSwitch(SwitchTree node, MethodVisitorContext ctxt)
  {
    return callStmtMacro(node, ctxt, "Switch", node.getExpression(), node.getCases());
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
    Debugger.log("/// VariableTree node = " + node);
    ExpressionTree initializer = node.getInitializer();

    if (initializer instanceof MethodInvocationTree)
    {
      MethodInvocationTree mit = (MethodInvocationTree) node.getInitializer();

      if (isNotMyMethod(mit.getMethodSelect()))
      {
        final MemberSelectTree memberSelectTree = (MemberSelectTree) mit
            .getMethodSelect();
        Debugger.log("!// not my method, variable, putting key: "
            + memberSelectTree.getIdentifier().toString() + " value: "
            + node.getName().toString());

        timeMachine.putIfAbsent(memberSelectTree.getIdentifier().toString(), node
            .getName().toString());

        return visitMethodInvocation(mit, ctxt) + "\\circvar " + node.getName() + " : "
            + TightRopeTransUtils.encodeType(node.getType()) + " \\circspot "
            + node.getName() + " :=~" + memberSelectTree.getIdentifier().toString();// +
        // "\\circseq ";
      }
      else
      {
        return callStmtMacro(node, ctxt, "Variable", node.getName(),
            TightRopeTransUtils.encodeType(node.getType()), initializer);
      }
    }
    else if (initializer instanceof LiteralTree)
    {
      final LiteralTree initLiteral = (LiteralTree) initializer;

      String initString = TightRopeTransUtils.encodeLiteral(initLiteral).toString();
      if (object.getVariable(initString) != null)
      {
        initString = "this~.~" + TightRopeTransUtils.encodeLiteral(initLiteral);
      }
      // initString = "TEST";
      return "\\circvar " + node.getName() + " : "
          + TightRopeTransUtils.encodeType(node.getType()) + " \\circspot "
          + node.getName() + " :=~" + initString;// + "\\circseq ";
    }
    else
    {

      if (object instanceof ParadigmEnv)
      {
        ClassEnv classEnv = ((ObjectEnv) object).getClassEnv();
        if (classEnv.getVariable(initializer.toString()) != null)
        {
          return "\\circvar " + node.getName() + " : "
              + TightRopeTransUtils.encodeType(node.getType()) + " \\circspot "
              + node.getName() + " := this~.~" + initializer.toString();// +
                                                                        // "\\circseq ";
        }
        return "\\circvar " + node.getName() + " : "
            + TightRopeTransUtils.encodeType(node.getType()) + " \\circspot "
            + node.getName() + " := this~.~" + initializer.toString();// +
        // "\\circseq ";
      }
      else if (object.getVariable(initializer.toString()) == null)
      {
        return callStmtMacro(node, ctxt, "Variable", node.getName(),
            TightRopeTransUtils.encodeType(node.getType()), initializer);
      }
      else
      {
        return "\\circvar " + node.getName() + " : "
            + TightRopeTransUtils.encodeType(node.getType()) + " \\circspot "
            + node.getName() + " := this~.~" + initializer.toString();// +
        // "\\circseq ";
      }
    }

  }

  @Override
  public String visitTry(TryTree node, MethodVisitorContext ctxt)
  {
    // return callStmtMacro(node, ctxt, "Block",
    // node.getBlock().getStatements());
    String tryBlock = "";

    Iterator<? extends StatementTree> iter = node.getBlock().getStatements().iterator();
    StatementTree st = null;

    while (iter.hasNext())
    {
      st = iter.next();

      tryBlock += st.accept(this, ctxt);

      if (iter.hasNext())
      {
        tryBlock += TightRopeString.LATEX.NEW_LINE;
      }

    }
    //
    // if (!tryBlock.contains("Skip"))
    // {
    // tryBlock += TightRopeString.LATEX.SKIP;
    // }

    return tryBlock;
  }

  @Override
  public String visitWhileLoop(WhileLoopTree node, MethodVisitorContext ctxt)
  {
    ExpressionTree condition = node.getCondition();
    Debugger.log("/// WHile Loop condition = " + condition.toString() + " kind = "
        + condition.getKind());

    // TODO HACKY, just checks for a ( to get if it's a method invocation.
    if (condition.toString().contains("("))
    {
      String conditionTrans = visit(condition, ctxt);
      conditionTrans = conditionTrans.replace("\\Skip", "");

      String conditionString = "";
      Debugger.log("/// conditionTrans = " + conditionTrans);

      boolean isStillMethodCall = (boolean) timeMachine.get("methodCall");
      if (isStillMethodCall)
      {

        conditionString = "\\circvar loopVar : \\boolean \\circspot loopVar :=~"
            + conditionTrans + "\\circseq \\\\";

        // return callStmtMacro(node, ctxt, "WhileLoopMethCond",
        // conditionString, node.getStatement());
      }
      else
      {
        // TODO SUPER HACKY! But I've now forgotten what it does.
        conditionTrans = conditionTrans.substring(1, conditionTrans.length() - 1);
        // Debugger.log("/// conditionTrans sub = " +
        // conditionTrans);

        boolean negative = conditionTrans.startsWith("\\lnot");

        if (negative)
        {
          conditionTrans = conditionTrans.substring(5);

          conditionString = conditionTrans

          + " \\circvar loopVar : \\boolean \\circspot loopVar :=~" + " (\\lnot "
              + timeMachine.get("variableIdentifier") + ") \\circseq \\\\";
        }
        else
        {
          conditionString = conditionTrans

          + " \\circvar loopVar : \\boolean \\circspot loopVar :=~"
              + timeMachine.get("variableIdentifier") + "\\circseq \\\\";
        }

      }

      return callStmtMacro(node, ctxt, "WhileLoopMethCond", conditionString,
          node.getStatement());
    }
    else
    {
      return callStmtMacro(node, ctxt, "WhileLoop", condition, node.getStatement());
    }
  }

  // public void setNonPBuilder(NonParadigmBuilder nonParadigmBuilder)
  // {
  // this.nonParadigmBuilder = nonParadigmBuilder;
  // }

  // public void methodAccessesVariable()
  // {
  // Debugger.log("Method Accesses Variable");
  // if(nonParadigmBuilder != null)
  // {
  // Debugger.log("nonPBuilder not null");
  // // nonParadigmBuilder.setAccessesVariable(true);
  // }
  // }
}

package hijac.tools.tightrope.builders;

import hijac.tools.analysis.SCJAnalysis;
import hijac.tools.tightrope.environments.MethodEnv;
import hijac.tools.tightrope.environments.ObjectEnv;
import hijac.tools.tightrope.environments.ProgramEnv;
import hijac.tools.tightrope.environments.SafeletEnv;
import hijac.tools.tightrope.environments.VariableEnv;
import hijac.tools.tightrope.utils.TightRopeTransUtils;
import hijac.tools.tightrope.visitors.MethodVisitor;
import hijac.tools.tightrope.visitors.ReturnVisitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.PrimitiveTypeTree;
import com.sun.source.tree.ReturnTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.Trees;

public class SafeletBuilder extends ParadigmBuilder
{

	private static ReturnVisitor returnVisitor = new ReturnVisitor();

	private Trees trees;

	private SafeletEnv safeletEnv;

	public SafeletBuilder(ProgramEnv programEnv, SCJAnalysis analysis,
			EnvironmentBuilder environmentBuilder)
	{
		super(analysis, programEnv, environmentBuilder);

		trees = analysis.TREES;
		analysis.getCompilationUnits();
		analysis.getTypeElements();

		safeletEnv = programEnv.getSafelet();
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Name> build(TypeElement e)
	{
		MethodVisitor methodVisitor = new MethodVisitor(analysis, safeletEnv);
		ClassTree ct = trees.getTree(e);

		HashMap<Name, Tree> varMap = getVariables(e, safeletEnv);

		List<StatementTree> members = (List<StatementTree>) ct.getMembers();
		Iterator<StatementTree> i = members.iterator();
		MethodTree getSequencer = null;
		while (i.hasNext())
		{
			Object obj = i.next();

			if (obj instanceof MethodTree)
			{
				MethodTree mt = (MethodTree) obj;

				Tree returnType = mt.getReturnType();
				final boolean isGetSequencerMethod = mt.getName().contentEquals(
						"getSequencer");
				final boolean isSyncMethod = mt.getModifiers().getFlags()
						.contains(Modifier.SYNCHRONIZED);
				final boolean notIgnoredMethod = !(isGetSequencerMethod
						|| mt.getName().contentEquals("<init>") || mt.getName()
						.contentEquals("getLevel") || mt.getName().contentEquals("immortalMemorySize"));

				if (returnType instanceof PrimitiveTypeTree)
				{
					((PrimitiveTypeTree) mt.getReturnType()).getPrimitiveTypeKind();
				}
				// ArrayList<Name> returns =
				
				mt.accept(new ReturnVisitor(varMap), null);

				@SuppressWarnings("rawtypes")
				Map paramMap = new HashMap();
				for (VariableTree vt : mt.getParameters())
				{
					paramMap.put(vt.getName().toString(), vt.getType());
				}

				if (mt.getName().contentEquals("<init>"))
				{
					extractProcessParameters(mt, safeletEnv);
				}
				else if (mt.getName().contentEquals("initializeApplication"))
				{
					safeletEnv.addInitMethod(methodVisitor.visitMethod(mt, false,varMap));
				}
				else if (isSyncMethod)
				{
					safeletEnv.setObjectId(safeletEnv.getName().toString());
					MethodEnv m = methodVisitor.visitMethod(mt, false, varMap);
					setMethodAccess(m, mt);
				
					safeletEnv.getClassEnv().addSyncMeth(m);
				}
				else if (notIgnoredMethod)
				{
					MethodEnv m = methodVisitor.visitMethod(mt, false, varMap);
					setMethodAccess(m, mt);
					safeletEnv.addMeth(m);
				}

				if (isGetSequencerMethod)
				{
					getSequencer = mt;

				}
			}
		}
		// /////////////////////////////////////////////////////
		if (getSequencer != null)
		{
			List<StatementTree> s = (List<StatementTree>) getSequencer.getBody()
					.getStatements();

			@SuppressWarnings("rawtypes")
			Iterator j = s.iterator();

			while (j.hasNext())
			{
				StatementTree st = (StatementTree) j.next();

				if (st instanceof ReturnTree)
				{
				  //TODO Doesn't handle the safelet returning multiple sequencers
					return st.accept(returnVisitor, null);
				}
			}
		}

		return null;
	}

	@Override
	public void addParents()
	{		
	}

	//TODO merge this with MethodVisitor.getConstructorParameters()
  protected void extractProcessParameters(MethodTree methodTree, ObjectEnv object)
  {
  	for (VariableTree vt : methodTree.getParameters())
  	{
  
  		VariableEnv parameter = new VariableEnv();
  
  		parameter.setName(vt.getName().toString());
  		parameter.setType(TightRopeTransUtils.encodeType(vt.getType()));
  		parameter.setProgramType(TightRopeTransUtils.encodeType(vt.getType()));
  
  		final boolean ignoredParameter = parameter.getType().endsWith("Parameters")
  				|| parameter.getType().equals("String");
  
  		if (!ignoredParameter)
  		{
  			object.addProcParameter(parameter);
  		}
  
  	}
  }
}
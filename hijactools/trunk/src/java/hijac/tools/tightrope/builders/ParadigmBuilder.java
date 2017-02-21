package hijac.tools.tightrope.builders;

import hijac.tools.analysis.SCJAnalysis;
import hijac.tools.tightrope.environments.MethodEnv;
import hijac.tools.tightrope.environments.ObjectEnv;
import hijac.tools.tightrope.environments.ProgramEnv;
import hijac.tools.tightrope.visitors.VariableVisitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.ModifiersTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.Tree;

public abstract class ParadigmBuilder
{
	protected static ProgramEnv programEnv;
	protected static SCJAnalysis analysis;
	protected EnvironmentBuilder environmentBuilder;

	public enum IDType
	{
		MissionID, SchedulableID
	};
	
	protected ParadigmBuilder()
	{
	  
	}

	public ParadigmBuilder(SCJAnalysis analysis, ProgramEnv programEnv,
			EnvironmentBuilder environmentBuilder)
	{
		this.analysis = analysis;
		this.programEnv = programEnv;
		this.environmentBuilder = environmentBuilder;
	}

	public abstract ArrayList<Name> build(TypeElement paradigmTypeElement);

	public abstract void addParents();
	
	protected abstract void extractProcessParameters(MethodTree methodTree, ObjectEnv object);

	protected HashMap<Name, Tree> getVariables(TypeElement arg0, ObjectEnv objectEnv)
	{
		HashMap<Name, Tree> varMap = new HashMap<Name, Tree>();

		VariableVisitor varVisitor;

		assert (objectEnv != null);

		varVisitor = new VariableVisitor(programEnv, objectEnv);

		ClassTree ct = analysis.TREES.getTree(arg0);
		List<? extends Tree> members = ct.getMembers();
		Iterator<? extends Tree> i = members.iterator();

		while (i.hasNext())
		{
			Tree s = i.next();
			// TODO if this is only ever going to return one value at a time
			// then it shouldn't be a map
			HashMap<Name, Tree> m = (HashMap<Name, Tree>) s.accept(varVisitor, true);

			// TODO this is a bit of a hack...
			for (Name n : m.keySet())
			{			
				 varMap.put(n, m.get(n));
			}
		}		
		return varMap;
	}

	protected void addDeferredParameters(List<StatementTree> methodStatements,
			HashMap<Name, Tree> varMap, ObjectEnv env)
	{
		Iterator<StatementTree> methodStatementsIterator;
		StatementTree methodStatementTree;
		methodStatementsIterator = methodStatements.iterator();

		while (methodStatementsIterator.hasNext())
		{
			methodStatementTree = methodStatementsIterator.next();
			// TODO Should only add the right trees I suppose..
			environmentBuilder.addDeferredParam(methodStatementTree, env.getName()
					.toString(), varMap);
		}
	}

	protected  void setMethodAccess(MethodEnv m, MethodTree o)
  {
  	ModifiersTree modTree = o.getModifiers();
  	Set<Modifier> flags = modTree.getFlags();
  
  	// m.setSynchronised(flags.contains(Modifier.SYNCHRONIZED));
  
  	if (flags.contains(Modifier.PUBLIC))
  	{
  		m.setAccess(MethodEnv.AccessModifier.PUBLIC);
  	}
  	else if (flags.contains(Modifier.PRIVATE))
  	{
  		m.setAccess(MethodEnv.AccessModifier.PRIVATE);
  	}
  	else if (flags.contains(Modifier.PROTECTED))
  	{
  		m.setAccess(MethodEnv.AccessModifier.PROTECTED);
  	}
  }

	
}
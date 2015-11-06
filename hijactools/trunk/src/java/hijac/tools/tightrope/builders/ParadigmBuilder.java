package hijac.tools.tightrope.builders;

import hijac.tools.analysis.SCJAnalysis;
import hijac.tools.tightrope.environments.ObjectEnv;
import hijac.tools.tightrope.environments.ProgramEnv;
import hijac.tools.tightrope.visitors.VariableVisitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.Tree;

public abstract class ParadigmBuilder
{
	 protected ProgramEnv programEnv;
	 protected static SCJAnalysis analysis;
	
	 public ParadigmBuilder(SCJAnalysis analysis, ProgramEnv programEnv)
	 {
		 this.analysis = analysis;
		 this.programEnv = programEnv;
	 }
	 
	public abstract ArrayList<Name> build(TypeElement paradigmTypeElement);
	
	protected HashMap<Name, Tree> getVariables(TypeElement arg0,
			ObjectEnv objectEnv)
	{
		HashMap<Name, Tree> varMap = new HashMap<Name, Tree>();

		VariableVisitor varVisitor;

		assert(objectEnv != null);
		if (objectEnv != null)
		{
			varVisitor = new VariableVisitor(programEnv, objectEnv);
		}
		else
		{
			varVisitor = new VariableVisitor(programEnv);
		}

		ClassTree ct = analysis.TREES.getTree(arg0);
		List<? extends Tree> members = ct.getMembers();
		Iterator<? extends Tree> i = members.iterator();

		while (i.hasNext())
		{
			Tree s = i.next();
			// TODO if this is only ever going to return one value at a time
			// then it shouldn't be a map
			HashMap<Name, Tree> m = (HashMap<Name, Tree>) s.accept(varVisitor,
					true);
			assert (m != null);
			System.out.println("getVariables m = " + m);
			// TODO this is a bit of a hack...

			for (Name n : m.keySet())
			{
				// System.out.println("\t*** Name = " + n + " Type = "
				// + m.get(n) + " Kind = " + m.get(n).getKind());
				varMap.putIfAbsent(n, m.get(n));
			}
		}
		System.out.println("getVariables varMap = " + varMap);
		return varMap;

	}
}
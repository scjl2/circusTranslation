package hijac.tools.tightrope.environments;

import hijac.tools.analysis.SCJAnalysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.lang.model.element.Name;

import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;

public class ProgramEnv
{
	FrameworkEnv frameworkEnv;
	List<NonParadigmEnv> nonParadigmObjectEnvs;
	private HashMap<Name, Tree> variables = new HashMap<Name, Tree>();

	public ProgramEnv(SCJAnalysis context)
	{
		this.frameworkEnv = new FrameworkEnv();
		this.nonParadigmObjectEnvs = new ArrayList<NonParadigmEnv>();
	}

	public void addSafelet(Name safelet)
	{
		frameworkEnv.addSafelet(safelet);
	}

	public void addTopLevelMissionSequencer(Name topLevelMissionSequencer)
	{
		frameworkEnv.addTopLevelMissionSequencer(topLevelMissionSequencer);
	}
	
	public void addMission(Name mission)
	{
		frameworkEnv.addMission(mission);
	}

	public FrameworkEnv getFrameworkEnv()
	{
		return frameworkEnv;
	}

	public void getMethod(String methodName)
	{
		//		ClassTree ct = getSafelet().getClassTree();

		//		List<StatementTree> members = (List<StatementTree>) ct.getMembers();

		Iterator<MethodTree> i = getSafelet().getMeths().iterator();
		while (i.hasNext())
		{
			MethodTree o = i.next();

			if (o.getName().contentEquals(methodName))
			{
				//explore it
				System.out.println(o.getName());
				System.out.println(o);
			}
		}

	}



	public List<NonParadigmEnv> getNonParadigmObjectEnvs()
	{
		return nonParadigmObjectEnvs;
	}

	public ParadigmEnv getSafelet()
	{
		return frameworkEnv.getControlTier().getSafeletEnv();
	}

	public void output()
	{
		System.out.println("*******************");
		System.out.println("Program Environment");
		System.out.println("-------------------");
		System.out.println("Framework Environment");
		System.out.println("_____________________");
		System.out.println(frameworkEnv.toString());
		System.out.println("----------------------");
		System.out.println("Non-Framework Environments");
		System.out.println("___________________________");
		System.out.println(nonParadigmObjectEnvs.toString());
		System.out.println("--------------------------------");
		System.out.println("Variables");
		System.out.println("---------");
		for(Name n : variables.keySet())
		{
			System.out.println(n + " -> " + variables.get(n));
		}

	}

	public void addSchedulable(SchedulableTypeE type, Name name)
	{
		frameworkEnv.addSchedulable(type, name);
		
	}

	public void addVariable(Name name, Tree type)
	{
		variables.put(name, type);
	}
	
	public Tree getVariable(Name name)
	{
		return variables.get(name);
	}
	
	public void newTier()
	{
		// TODO Auto-generated method stub
		
	}

	public void newCluster()
	{
		// TODO Auto-generated method stub
		
	}

//	public void setFrameworkEnv(FrameworkEnv frameworkEnv)
//	{
//		this.frameworkEnv = frameworkEnv;
//	}
//
//	public void setNonParadigmObjectEnvs(
//			List<NonParadigmEnv> nonParadigmObjectEnvs)
//	{
//		this.nonParadigmObjectEnvs = nonParadigmObjectEnvs;
//	}

}

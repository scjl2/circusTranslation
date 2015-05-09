package hijac.tools.tightrope.environments;

import hijac.tools.analysis.SCJAnalysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Name;

import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;

public class ProgramEnv
{
	FrameworkEnv structureEnv;
	List<NonParadigmEnv> nonParadigmObjectEnvs;
	private HashMap<Name, Tree> variables = new HashMap<Name, Tree>();

	public ProgramEnv(SCJAnalysis context)
	{
		this.structureEnv = new FrameworkEnv();
		this.nonParadigmObjectEnvs = new ArrayList<NonParadigmEnv>();
	}

	public void addSafelet(Name safelet)
	{
		structureEnv.addSafelet(safelet);
	}

	public void addTopLevelMissionSequencer(Name topLevelMissionSequencer)
	{
		structureEnv.addTopLevelMissionSequencer(topLevelMissionSequencer);
	}

	public void addMission(Name mission)
	{
		structureEnv.addMission(mission);
	}

	public FrameworkEnv getFrameworkEnv()
	{
		return structureEnv;
	}

	public void getMethod(String methodName)
	{
		// ClassTree ct = getSafelet().getClassTree();

		// List<StatementTree> members = (List<StatementTree>) ct.getMembers();

		Iterator<MethodTree> i = getSafelet().getMeths().iterator();
		while (i.hasNext())
		{
			MethodTree o = i.next();

			if (o.getName().contentEquals(methodName))
			{
				// explore it
				System.out.println(o.getName());
				System.out.println(o);
			}
		}

	}

	public List<NonParadigmEnv> getNonParadigmObjectEnvs()
	{
		return nonParadigmObjectEnvs;
	}

	public SafeletEnv getSafelet()
	{
		return structureEnv.getControlTier().getSafeletEnv();
	}

	public void output()
	{
		System.out.println("*******************");
		System.out.println("Program Environment");
		System.out.println("-------------------");
		System.out.println("Framework Environment");
		System.out.println("_____________________");
		System.out.println(structureEnv.toString());
		System.out.println("----------------------");
		System.out.println("Non-Framework Environments");
		System.out.println("___________________________");
		System.out.println(nonParadigmObjectEnvs.toString());
		System.out.println("--------------------------------");
		System.out.println("Variables");
		System.out.println("---------");
		for (Name n : variables.keySet())
		{
			System.out.println(n + " -> " + variables.get(n));
		}

	}

	public void addSchedulable(SchedulableTypeE type, Name name)
	{
		structureEnv.addSchedulable(type, name);
	}

	public boolean containsScheudlable(Name name)
	{
		return structureEnv.containsSchedulable(name);
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
		System.out.println("+++ New Tier +++");
		System.out.println();
		
		structureEnv.newTier();
	}

	public void newCluster()
	{
		System.out.println("+++ New Cluster +++");
		System.out.println();
		
		structureEnv.newCluster();
	}

	public ArrayList<TopLevelMissionSequencerEnv> getTopLevelMissionSequencers()
	{
		ArrayList<TopLevelMissionSequencerEnv> returnList = new ArrayList<TopLevelMissionSequencerEnv>();
				
		returnList.add(structureEnv.getControlTier().getTopLevelMissionSequencerEnv());
		
		return returnList;
	}

	public ArrayList<MissionEnv> getMissions()
	{
		return structureEnv.getMissions();
	}

	public void addMissionSequencerMission(Name tlms, Name n)
	{
		structureEnv.addMissionSequencerMission(tlms, n);
		
	}

	public ArrayList<ManagedThreadEnv> getManagedThreads()
	{
		return structureEnv.getManagedThreads();
	}

	public Map geNetworkMap()
	{
		return structureEnv.getNetworkMap();
	}

	// public void setFrameworkEnv(FrameworkEnv frameworkEnv)
	// {
	// this.frameworkEnv = frameworkEnv;
	// }
	//
	// public void setNonParadigmObjectEnvs(
	// List<NonParadigmEnv> nonParadigmObjectEnvs)
	// {
	// this.nonParadigmObjectEnvs = nonParadigmObjectEnvs;
	// }

}

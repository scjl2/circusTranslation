package hijac.tools.tightrope.environments;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Name;

import com.sun.source.tree.ClassTree;

public class ProgramEnv
{
	FrameworkEnv frameworkEnv;
	List<NonParadigmEnv> nonParadigmObjectEnvs;
	List<ClassTree> context;

	public ProgramEnv()
	{
		this.frameworkEnv = new FrameworkEnv();
		this.nonParadigmObjectEnvs = new ArrayList<NonParadigmEnv>();
		this.context = new ArrayList<ClassTree>();
	}

	public FrameworkEnv getFrameworkEnv()
	{
		return frameworkEnv;
	}

	public void setFrameworkEnv(FrameworkEnv frameworkEnv)
	{
		this.frameworkEnv = frameworkEnv;
	}

	public List<NonParadigmEnv> getNonParadigmObjectEnvs()
	{
		return nonParadigmObjectEnvs;
	}

	public void setNonParadigmObjectEnvs(
			List<NonParadigmEnv> nonParadigmObjectEnvs)
	{
		this.nonParadigmObjectEnvs = nonParadigmObjectEnvs;
	}

	public List<ClassTree> getContext()
	{
		return context;
	}

	public void setContext(List<ClassTree> context)
	{
		this.context = context;
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

	}

	public void addSafelet(Name safelet)
	{
		frameworkEnv.addSafelet(safelet);
	}

	public void addTopLevelMissionSequencer(Name topLevelMissionSequencer)
	{
		frameworkEnv.addTopLevelMissionSequencer(topLevelMissionSequencer);
	}

}

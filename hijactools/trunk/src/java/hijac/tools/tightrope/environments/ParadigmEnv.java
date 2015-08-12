package hijac.tools.tightrope.environments;

import hijac.tools.tightrope.environments.EnvironmentBuilder.ClassEnv;

import java.util.ArrayList;
import java.util.List;


public class ParadigmEnv extends ObjectEnv
{
	private ClassEnv classEnv;

	public ParadigmEnv()
	{
		super();

		meths = new ArrayList<MethodEnv>();
		syncMeths = new ArrayList<MethodEnv>();

	}

	public List<MethodEnv> getMeths()
	{
		return meths;
	}

	public void addClassEnv(ClassEnv classEnv)
	{
		this.classEnv = classEnv;
	}

	public ClassEnv getClassEnv()
	{
		return classEnv;
	}
}

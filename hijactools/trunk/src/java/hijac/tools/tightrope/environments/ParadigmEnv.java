package hijac.tools.tightrope.environments;

import hijac.tools.tightrope.environments.EnvironmentBuilder.ClassEnv;

import java.util.ArrayList;


public class ParadigmEnv extends ObjectEnv
{
	private ClassEnv classEnv;

	public ParadigmEnv()
	{
		super();

		meths = new ArrayList<MethodEnv>();
		syncMeths = new ArrayList<MethodEnv>();

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

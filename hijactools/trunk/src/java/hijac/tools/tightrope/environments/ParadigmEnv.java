package hijac.tools.tightrope.environments;

import java.util.ArrayList;

public class ParadigmEnv extends ObjectEnv
{
	private ClassEnv classEnv;

	public ParadigmEnv()
	{
		super();

		meths = new ArrayList<MethodEnv>();
		syncMeths = new ArrayList<MethodEnv>();
//		classEnv = new ClassEnv();

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

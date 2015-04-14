package hijac.tools.tightrope.environments;

public class ProgramEnv
{
	FrameworkEnv frameworkEnv;
	List<NonParadigmEnv> nonParadigmObjectEnvs;
	List<ClassTree> context;

	public ProgramEnv(FrameworkEnv frameworkEnv, List nonParadigmObjectEnvs, List<ClassTree> context)
	{
		this.frameworkEnv = frameworkEnv;
		this.nonParadigmObjectEnvs = nonParadigmObjectEnvs;
		this.context = context;
	}

		


}

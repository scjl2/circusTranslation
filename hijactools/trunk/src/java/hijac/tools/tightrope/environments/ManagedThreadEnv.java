package hijac.tools.tightrope.environments;

import java.util.Map;

public class ManagedThreadEnv extends ParadigmEnv
{
	private MethodEnv runMethod;

	public void addRunMethod(MethodEnv method)
	{
		runMethod = method;
	}

	public MethodEnv getRunMethod()
	{
		return runMethod;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map toMap()
	{
		Map map = super.toMap();

		Map runMethodMap = methodToMap(runMethod);

		map.put("Run", runMethodMap);

		return map;
	}
}

package hijac.tools.tightrope.environments;

import hijac.tools.tightrope.utils.TightRopeString.Name;

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

		Map runMethodMap = runMethod.toMap();

		map.put("Run", runMethodMap);

		return map;
	}

	@Override
	public void setId(String name)
	{
		super.setId(name + Name.SID);
		// TODO THis will need to be changed when I streamline the S+S sections
//		setObjectId(name);
		setThreadID(name);
	}
}

package hijac.tools.tightrope.environments;

import java.util.HashMap;
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
		
		Map runMethodMap = new HashMap();
		runMethodMap.put(METHOD_NAME, runMethod.getMethodName().toString());
		runMethodMap.put(RETURN_TYPE, runMethod.getReturnType());
		runMethodMap.put(PARAMETERS_STR, runMethod.getParameters());
		runMethodMap.put(BODY, runMethod.getBody());
		runMethodMap.put(ACCESS, runMethod.getAccessString());

		
		
		map.put("Run", runMethodMap);

		return map;
	}
}

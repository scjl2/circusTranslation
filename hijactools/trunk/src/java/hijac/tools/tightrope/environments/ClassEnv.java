package hijac.tools.tightrope.environments;

import java.util.HashMap;
import java.util.Map;

public class ClassEnv extends ParadigmEnv
{

	public ClassEnv()
	{
		super();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map toMap()
	{
		Map map = new HashMap();
		map.put(PROCESS_ID, getName().toString());
		// map.put("handlerType", "aperiodic");
		// map.put("importName", "Aperiodic");
		map.put(METHODS, methsList());
		map.put(SYNC_METHODS, syncMethsList());
		map.put(VARIABLES_STR, varsList());

		return map;
	}

}

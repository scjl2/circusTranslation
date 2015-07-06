package hijac.tools.tightrope.environments;

import java.util.HashMap;
import java.util.Map;

public class PeriodicEventHandlerEnv extends ParadigmEnv
{
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map toMap()
	{
		Map map = new HashMap();
		map.put("ProcessID", name.toString());
		map.put("handlerType", "periodic");
		map.put("importName", "Periodic");
		
		map.put("Variables", varsList());
		
		map.put("Methods", methsList());
		map.put("SyncMethods", syncMethsList());

		return map;
	}
}

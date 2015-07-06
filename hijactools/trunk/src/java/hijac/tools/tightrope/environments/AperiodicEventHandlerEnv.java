package hijac.tools.tightrope.environments;

import java.util.HashMap;
import java.util.Map;

public class AperiodicEventHandlerEnv extends ParadigmEnv
{
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map toMap()
	{
		Map map = new HashMap();
		map.put("SchedulableID", name.toString());
		map.put("handlerType", "aperiodic");
		map.put("importName", "Aperiodic");
		
		map.put("Variables", varsList());

		return map;
	}
}

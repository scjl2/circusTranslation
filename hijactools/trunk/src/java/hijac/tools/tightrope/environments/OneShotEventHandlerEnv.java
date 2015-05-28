package hijac.tools.tightrope.environments;

import java.util.HashMap;
import java.util.Map;

public class OneShotEventHandlerEnv extends ParadigmEnv
{
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map toMap()
	{
		Map map = new HashMap();
		map.put("SchedulableID", name.toString());
		map.put("handlerType", "oneShot");
		map.put("importName", "OneShot");

		return map;
	}
}

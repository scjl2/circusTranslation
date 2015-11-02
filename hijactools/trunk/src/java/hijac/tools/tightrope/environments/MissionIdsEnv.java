package hijac.tools.tightrope.environments;

import java.util.HashMap;
import java.util.Map;

public class MissionIdsEnv extends IdEnv
{
	private static final String MISSIONS_STR = "Missions";
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map toMap()
	{
		Map map = new HashMap();
		map.put(MISSIONS_STR, getIdNames());

		return map;
	}
	
	public void addIdNames(String idName)
	{
		idNames.add(idName+ID_STR);
	}
}
package hijac.tools.tightrope.environments;

import java.util.HashMap;
import java.util.Map;

public abstract class EventHandlerEnv extends ParadigmEnv
{
	
	public abstract String getHandlerType();
	
	public abstract String getImportName();
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map toMap()
	{
		Map map = new HashMap();
		map.put("ProcessID", name.toString());
		map.put("initializeApplicationMethod", "");

		// if (tlmsNames.length == 1)
		// {
		// map.put("SchedulableID", tlmsNames[0]);
		// }

		return map;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map toMapFromPEH()
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
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map toMapFromOneShot()
	{
		Map map = new HashMap();
		map.put("ProcessID", name.toString());
		map.put("handlerType", "oneShot");
		map.put("importName", "OneShot");
		
		map.put("Variables", varsList());
		
		map.put("Methods", methsList());
		map.put("SyncMethods", syncMethsList());

		return map;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map toMapFromAPEH()
	{
		Map map = new HashMap();
		map.put(PROCESS_ID, name.toString());
		map.put(HANDLER_TYPE, getHandlerType());
		map.put(IMPORT_NAME, getImportName());
		
		map.put(VARIABLES_STR, varsList());
		
		map.put(METHODS, methsList());
		map.put(SYNC_METHODS, syncMethsList());

		return map;
	}

}

package hijac.tools.tightrope.environments;

import java.util.Map;

public abstract class EventHandlerEnv extends ParadigmEnv
{
	
	public abstract String getHandlerType();
	
	public abstract String getImportName();
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map toMap()
	{
		Map map = super.toMap();
		
		map.put(HANDLER_TYPE, getHandlerType());
		map.put(IMPORT_NAME, getImportName());
		
	

		return map;
	}

}

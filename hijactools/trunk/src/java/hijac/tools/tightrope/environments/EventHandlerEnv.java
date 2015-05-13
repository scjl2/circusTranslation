package hijac.tools.tightrope.environments;

import java.util.HashMap;
import java.util.Map;



	
	public class EventHandlerEnv extends ParadigmEnv
	{		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public Map toMap()
		{
			Map map = new HashMap();
			map.put("ProcessName", name.toString());
			map.put("initializeApplicationMethod", "");
					
//			if (tlmsNames.length == 1)
//			{
//				map.put("SchedulableID", tlmsNames[0]);
//			}
				
			
			
			return map;
		}
	}

package hijac.tools.tightrope.environments;

import java.util.HashMap;
import java.util.Map;



	
	public class ManagedThreadEnv extends ParadigmEnv
	{		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public Map toMap()
		{
			Map map = new HashMap();
			map.put("ProcessID", name.toString());

				
			map.put("Variables", varsList());
			map.put("Parameters", paramsList());
			map.put("Methods", methsList());
			map.put("SyncMethods", syncMethsList());
			
			return map;
		}
	}
	
	
	
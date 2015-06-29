package hijac.tools.tightrope.environments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.Name;


	
	public class MissionEnv extends ParadigmEnv
	{		
		ArrayList<Name> schedulables = new ArrayList<Name>();
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public Map toMap()
		{
			Map map = new HashMap();
			map.put("MissionID", name.toString());
			map.put("RegisteredSchedulables", schedulables);
			map.put("Variables", varsList());
			map.put("Methods", methsList());
			map.put("SyncMethods", syncMethsList());
			
//			map.put("initializeApplicationMethod", "");
					
//			if (tlmsNames.length == 1)
//			{
//				map.put("SchedulableID", tlmsNames[0]);
//			}
			
			
			return map;
		}
		
		public void addSchedulable(Name schedulable)
		{
			schedulables.add(schedulable);
		}
	}
	

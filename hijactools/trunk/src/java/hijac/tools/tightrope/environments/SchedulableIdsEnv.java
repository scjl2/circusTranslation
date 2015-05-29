package hijac.tools.tightrope.environments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.Name;



	public class SchedulableIdsEnv 
	{		
		
		ArrayList<Name> schedulables;
		
		public SchedulableIdsEnv(ArrayList<Name> schedulables)
		{
			this.schedulables = schedulables;
		}
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public Map toMap()
		{
			Map map = new HashMap();
			map.put("Schedulables", schedulables);
						
			return map;
		}		
	}	
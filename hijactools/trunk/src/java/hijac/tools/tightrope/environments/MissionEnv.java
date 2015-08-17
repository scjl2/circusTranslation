package hijac.tools.tightrope.environments;

import java.util.ArrayList;
import java.util.Map;

import javax.lang.model.element.Name;


	
	public class MissionEnv extends ParadigmEnv
	{		
		ArrayList<Name> schedulables = new ArrayList<Name>();
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public Map toMap()
		{
		Map map = super.toMap();
			
			map.put("RegisteredSchedulables", schedulables);
			
			
			return map;
		}
		
		public void addSchedulable(Name schedulable)
		{
			schedulables.add(schedulable);
		}
	}
	

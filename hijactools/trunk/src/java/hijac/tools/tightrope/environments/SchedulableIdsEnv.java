package hijac.tools.tightrope.environments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.Name;



	public class SchedulableIdsEnv 
	{		
		
		ArrayList<Name> schedulables;
		Name topLevelSequencer;
		
		public SchedulableIdsEnv()
		{
			schedulables = new ArrayList<Name>();
		}
		
		public SchedulableIdsEnv(ArrayList<Name> schedulables)
		{
			this.schedulables = schedulables;
		}
		
		public void addSchedulable(Name schedulableName)
		{
			schedulables.add(schedulableName);
		}
		
		public void addTopLevelSequencer(Name topLevelSequencer)
		{
			this.topLevelSequencer = topLevelSequencer;
		}
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public Map toMap()
		{
			Map map = new HashMap();
			map.put("toplevelSequencer", topLevelSequencer);
			map.put("Schedulables", schedulables);
						
			return map;
		}		
	}	
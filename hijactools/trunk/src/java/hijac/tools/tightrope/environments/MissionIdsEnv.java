package hijac.tools.tightrope.environments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.Name;



	public class MissionIdsEnv extends ParadigmEnv
	{		
		ArrayList<Name> missions;
		
		public MissionIdsEnv(ArrayList<Name> missions)
		{
			this.missions = missions;
		}
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public Map toMap()
		{
			Map map = new HashMap();
			map.put("Missions", missions);
						
			return map;
		}		
	}	
package hijac.tools.tightrope.environments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.Name;


	
	public class TopLevelMissionSequencerEnv extends ParadigmEnv
	{		
		ArrayList<Name> missions = new ArrayList<Name>();
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public Map toMap()
		{
			Map map = super.toMap();
							
			for(Name n : missions)
			{
				//TODO THIS WONT WORK WITH MORE THAN ONE MISSION.
				map.put("MissionID", n);
			}
			
			
			
			return map;
		}
		
		public void addMission(Name mission)
		{
			missions.add(mission);
		}
	}
	

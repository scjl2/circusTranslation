package hijac.tools.tightrope.environments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.Name;

public class NestedMissionSequencerEnv extends ParadigmEnv
{
	ArrayList<Name> missions = new ArrayList<Name>();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map toMap()
	{
		Map map = new HashMap();
		map.put("ProcessID", name.toString());
	
		for (Name n : missions)
		{
			map.put("MissionID", n);
		}

		map.put("Variables", varsList());

		map.put("Methods", methsList());
		map.put("SyncMethods", syncMethsList());

		return map;
	}

	public void addMission(Name mission)
	{
		missions.add(mission);
	}
}

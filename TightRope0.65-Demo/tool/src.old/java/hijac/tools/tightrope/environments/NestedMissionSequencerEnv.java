package hijac.tools.tightrope.environments;

import java.util.ArrayList;
import java.util.Map;

import javax.lang.model.element.Name;

public class NestedMissionSequencerEnv extends ParadigmEnv
{
	ArrayList<Name> missions = new ArrayList<Name>();

	@SuppressWarnings({ "rawtypes" })
	public Map toMap()
	{
		Map map = super.toMap();

		// for (Name n : missions)
		// {
		// //TODO ALSO WONT WORK
		// map.put("MissionID", n);
		// }

		return map;
	}

	public void addMission(Name mission)
	{
		missions.add(mission);
	}
}

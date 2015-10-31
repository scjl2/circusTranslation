package hijac.tools.tightrope.environments;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Name;

public class MissionEnv extends ParadigmEnv
{
	private ArrayList<Name> schedulables;
	// TODO remove this stop-gap in favour of using the API
	private static final List<MethodEnv> MISSION_API_METHODS = new ArrayList<MethodEnv>();

	public MissionEnv()
	{
		super();

		schedulables = new ArrayList<Name>();

		MISSION_API_METHODS.add(new MethodEnv("getMission", "Mission", true));
		MISSION_API_METHODS.add(new MethodEnv("getSequencer",
				"MissionSequencer", true));
		// MISSION_API_METHODS
		// .add(new MethodEnv("missionMemorySize", "int", true));
		MISSION_API_METHODS.add(new MethodEnv("requestTermination", "boolean",
				true));
		MISSION_API_METHODS.add(new MethodEnv("terminationPending", "boolean",
				true));
	}

	@Override
	public List<MethodEnv> getMeths()
	{
		List<MethodEnv> missionMeths = super.getMeths();
		missionMeths.addAll(MISSION_API_METHODS);

		return missionMeths;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public List<Map> methsList()
	{
		List<Map> missionMethodsList = super.methsList();

//		for (MethodEnv me : MISSION_API_METHODS)
//		{
//			Map methodMap = methodToMap(me);
//
//			missionMethodsList.add(methodMap);
//		}

		return missionMethodsList;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map toMap()
	{
		Map map = super.toMap();

		map.put("RegisteredSchedulables", schedulables);
		
//		System.out.println("MissionEnv toMap = " + map);
		System.out.println();
		System.out.println("MissionEnv for "+getName()+" Map Methos = " + map.get("Methods"));
		System.out.println();

		return map;
	}

	public void addSchedulable(Name schedulable)
	{
		schedulables.add(schedulable);
	}
}

package hijac.tools.tightrope.environments;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Name;

public class MissionEnv extends ParadigmEnv
{
	private ArrayList<Name> schedulables;
	// TODO remove this stop-gap in favour of using the API
	private final List<MethodEnv> MISSION_API_METHODS = new ArrayList<MethodEnv>();

	public MissionEnv()
	{
		super();

		schedulables = new ArrayList<Name>();

		MISSION_API_METHODS.add(new MethodEnv("getSequencer",
				"MissionSequencer", true));

		MISSION_API_METHODS.add(new MethodEnv("requestTermination", "boolean",
				true));
		MISSION_API_METHODS.add(new MethodEnv("terminationPending", "boolean",
				true));

	}

	@Override
	public List<MethodEnv> getMeths()
	{
		List<MethodEnv> missionMeths = new ArrayList<MethodEnv>();

		missionMeths.addAll(super.getMeths());
		missionMeths.addAll(MISSION_API_METHODS);

		return missionMeths;
	}

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

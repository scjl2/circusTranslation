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
	public List<MethodEnv> getAllMeths()
	{
		List<MethodEnv> missionMeths = new ArrayList<MethodEnv>();

		missionMeths.addAll(super.getAllMeths());
		missionMeths.addAll(MISSION_API_METHODS);

		return missionMeths;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map toMap()
	{
		Map map = super.toMap();

		ArrayList<String> schedulableIds = new ArrayList<String>();
		for(Name n : schedulables)
		{
			schedulableIds.add(n.toString() + "SID");
		}
		
		map.put("RegisteredSchedulables", schedulableIds);

		return map;
	}

	public void addSchedulable(Name schedulable)
	{
		schedulables.add(schedulable);
	}
	
	@Override
	public void setId(String name)
	{
		super.setId(name+hijac.tools.tightrope.utils.TightRopeString.Name.MID);
		//TODO THis will need to be changed when I streamline the S+S sections
//				setObjectId(name);
	}

}

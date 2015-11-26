package hijac.tools.tightrope.environments;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Name;

public class MissionSequencerEnv extends ParadigmEnv
{
	private ArrayList<String> missions;

	private final List<MethodEnv> MISSION_SEQUENCER_API_METHODS = new ArrayList<MethodEnv>();

	public MissionSequencerEnv()
	{
		super();
		
		missions = new ArrayList<String>();

		MISSION_SEQUENCER_API_METHODS.add(new MethodEnv("signalTermination",
				"void", true));
	}

//	@SuppressWarnings({ "rawtypes" })
//	public Map toMap()
//	{
//		Map map = super.toMap();
//
//		return map;
//	}

	public void addMission(String mission)
	{
		missions.add(mission);
	}
}

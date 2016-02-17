package hijac.tools.tightrope.environments;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Name;

public class MissionSequencerEnv extends ParadigmEnv
{
	private ArrayList<Name> missions;

	private final List<MethodEnv> MISSION_SEQUENCER_API_METHODS = new ArrayList<MethodEnv>();

	public MissionSequencerEnv()
	{
		super();

		missions = new ArrayList<Name>();

		MISSION_SEQUENCER_API_METHODS.add(new MethodEnv("signalTermination",
				"void", true));
	}

	public void addMission(Name mission)
	{
		missions.add(mission);
	}
}

package hijac.tools.tightrope.environments;

import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.Name;

/**
 * Environment class for the Schedulable Ids. 
 * @author Matt Luckcuck
 */
public class SchedulableIdsEnv extends IdEnv
{


	private static final String SCHEDULABLES_STR = "Schedulables";
	private static final String TOPLEVEL_SEQUENCER = "toplevelSequencer";
	
	private String topLevelSequencer;


	public void addTopLevelSequencer(String topLevelMissionSequencer)
	{
		this.topLevelSequencer = topLevelMissionSequencer;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map toMap()
	{
		Map map = new HashMap();
		map.put(TOPLEVEL_SEQUENCER, topLevelSequencer);
		map.put(SCHEDULABLES_STR, getIdNames());

		return map;
	}
	
	public void addIdNames(String idName)
	{
		idNames.add(idName+ID_STR);
	}
}
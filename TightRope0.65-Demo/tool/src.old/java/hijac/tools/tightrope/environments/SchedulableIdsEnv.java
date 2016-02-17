package hijac.tools.tightrope.environments;

import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.Name;

/**
 * Environment class for the Schedulable Ids. 

 */
public class SchedulableIdsEnv extends IdEnv
{

	private static final String SCHEDULABLES_STR = "Schedulables";
	private static final String TOPLEVEL_SEQUENCER = "toplevelSequencer";
	
	private Name topLevelSequencer;


	public void addTopLevelSequencer(Name topLevelSequencer)
	{
		this.topLevelSequencer = topLevelSequencer;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map toMap()
	{
		Map map = new HashMap();
		map.put(TOPLEVEL_SEQUENCER, topLevelSequencer);
		map.put(SCHEDULABLES_STR, getIdNames());

		return map;
	}
}

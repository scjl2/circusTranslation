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
	
	//TODO This needs to be a list and be the SOLE owner of this information
	private String topLevelSequencer;


	public void addTopLevelSequencer(Name topLevelSequencer)
	{
		this.topLevelSequencer = topLevelSequencer + SID;
	}
	
	public String getTopLevelSequencer()
	{
		return topLevelSequencer;
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
		idNames.add(idName+SID);
	}
}
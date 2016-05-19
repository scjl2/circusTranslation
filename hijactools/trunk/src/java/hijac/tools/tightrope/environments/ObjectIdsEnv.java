package hijac.tools.tightrope.environments;

import hijac.tools.tightrope.utils.TightRopeString.Name;

import java.util.HashMap;
import java.util.Map;

/**
 * Environment class for the Thread Ids.
 * 
 * @author Matt Luckcuck
 */

// TODO This currently over optimistically takes eveything
// This outputs object ids that might not be needed.
public class ObjectIdsEnv extends IdEnv
{

	private static final String OBJECT_STR = "Objects";

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map toMap()
	{
		HashMap map = new HashMap();


		map.put(OBJECT_STR, idNames);

		return map;

	}

	@Override
	public void addIdNames(String idName)
	{
		if (!idNames.contains(idName+ Name.OBJ_ID))
		{
			idNames.add(idName + Name.OBJ_ID);
		}
	}



}
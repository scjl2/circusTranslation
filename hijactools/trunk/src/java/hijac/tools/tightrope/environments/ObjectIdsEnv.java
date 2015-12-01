package hijac.tools.tightrope.environments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Environment class for the Thread Ids.
 * 
 * @author Matt Luckcuck
 */

// This currently over optimistically takes eveything
// This outputs object ids that might not be needed.
public class ObjectIdsEnv extends IdEnv
{

	private static final String OBJECT_ID_STR = "Object" + ID_STR;
	private static final String OBJECT_STR = "Objects";

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map toMap()
	{
		HashMap map = new HashMap();

		// ArrayList<String> list = new ArrayList<String>();

		map.put(OBJECT_STR, getIdNames());

		return map;

	}

	public void addIdNames(String idName)
	{
		idNames.add(idName + OBJECT_ID_STR);
	}

}
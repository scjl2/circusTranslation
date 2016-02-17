package hijac.tools.tightrope.environments;

import java.util.HashMap;
import java.util.Map;

/**
 * Environment class for the Thread Ids. 

 */

//This currently over optimistically takes all the schedulable names, plus the Safelet.
//This outputs thread ids that might not be needed.
public class ThreadIdsEnv extends IdEnv
{

	private static final String THREADS_STR = "Threads";

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map toMap()
	{
		Map map = new HashMap();
		map.put(THREADS_STR, getIdNames());

		return map;
	}

}

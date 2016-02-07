package hijac.tools.tightrope.environments;

import hijac.tools.tightrope.utils.TightRopeString;
import hijac.tools.tightrope.utils.TightRopeString.Name;

import java.util.HashMap;
import java.util.Map;

/**
 * Environment class for the Thread Ids. 
 * @author Matt Luckcuck
 */

//This currently over optimistically takes all the schedulable names, plus the Safelet.
//This outputs thread ids that might not be needed.
public class ThreadIdsEnv extends IdEnv
{

//	private static final String THREAD_ID_STR = "Thread"+Name.ID_STR;
	private Map<String, String> threadMap = new HashMap<String, String>();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map toMap()
	{
		Map map = new HashMap();

		for(String id : threadMap.keySet())
		{
			String threadID = id ;
			String priority = threadMap.get(id); 			
			
			map.put(threadID, priority);
		}

		return map;
	}
	 
	public void addIdNames(String idName)
	{
		final String threadId = idName+TightRopeString.Name.Thread_ID;
		idNames.add(threadId);
		threadMap.put(threadId, "");
	}

	public void setThreadPriority(String threadName, String priority)
	{
		System.out.println("putting " + threadName + " -> " + priority);
		
			threadMap.put(threadName+TightRopeString.Name.Thread_ID, priority);	
		
	}

}
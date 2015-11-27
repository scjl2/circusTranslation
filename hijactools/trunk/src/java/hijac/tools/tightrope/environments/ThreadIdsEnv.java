package hijac.tools.tightrope.environments;

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

	private static final String THREAD_ID_STR = "Thread"+ID_STR;
	private static final String THREADS_STR = "Threads";
	
	private Map<String, String> threadMap = new HashMap<String, String>();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map toMap()
	{
		Map map = new HashMap();
//		map.put(THREADS_STR, getIdNames());
//		assert(getIdNames() != null);
		for(String id : threadMap.keySet())
		{
			String priority = threadMap.get(id); 
			String threadID = id +THREAD_ID_STR;
			System.out.println("Adding* " + threadID + " -> " + priority);
			map.put(threadID, priority);
		}

		return map;
	}
	
	public void addIdNames(String idName)
	{
		idNames.add(idName);
		threadMap.put(idName, "");
	}

	public void setThreadPriority(String threadName, String priority)
	{
		System.out.println("putting " + threadName + " -> " + priority);
		threadMap.put(threadName, priority);		
	}

}
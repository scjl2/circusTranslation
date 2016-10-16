package hijac.tools.tightrope.environments;

import hijac.tools.tightrope.utils.Debugger;
import hijac.tools.tightrope.utils.TightRopeString;

import java.util.HashMap;
import java.util.Map;

/**
 * Environment class for the Thread Ids.
 * 
 * @author Matt Luckcuck
 */

// This currently over optimistically takes all the schedulable names, plus the
// Safelet.
// This outputs thread ids that might not be needed.
public class ThreadIdsEnv extends IdEnv
{

  // private static final String THREAD_ID_STR = "Thread"+Name.ID_STR;
  private Map<String, String> threadMap = new HashMap<String, String>();
  private final String DEFAULT_PRIORITY = "NormPriority";

  @SuppressWarnings({ "rawtypes", "unchecked" })
  public Map toMap()
  {
    Map map = new HashMap();

    for (String id : threadMap.keySet())
    {
      String threadID = id;
      String priority = threadMap.get(id);
      
      map.put(threadID, priority);
    }

    return map;
  }

  public void addIdNames(String idName)
  {
    final String threadId = idName + TightRopeString.Name.Thread_ID;
    idNames.add(threadId);
    threadMap.put(threadId, DEFAULT_PRIORITY);
  }

  public void setThreadPriority(String threadName, String priority)
  {
    if (threadMap.containsKey(threadName + TightRopeString.Name.Thread_ID))
    {
      Debugger.log("putting " + threadName + " -> " + priority);

      threadMap.put(threadName + TightRopeString.Name.Thread_ID, priority);
    }
    else
    {
      Debugger.log("Thread:" + threadName + " Not Found");

    }

  }

}
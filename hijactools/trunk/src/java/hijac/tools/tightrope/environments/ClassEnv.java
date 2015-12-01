package hijac.tools.tightrope.environments;

import java.util.HashMap;
import java.util.Map;

public class ClassEnv extends ParadigmEnv
{

	public ClassEnv()
	{
		super();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map toMap()
	{
		Map map = new HashMap();
		map.put(PROCESS_ID, getName().toString());

		map.put(METHODS, methsList());
		map.put(SYNC_METHODS, syncMethsList());
		map.put(VARIABLES_STR, varsList());
		map.put(INITED_VARIABLES, initedVarsList());

		return map;
	}
	
	public boolean isEmpty()
	{
		if(meths.isEmpty() && syncMeths.isEmpty() && variables.isEmpty())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public String toString()
	{
		return "ClassEnv: " + "Name=" + getName() + " methods="
				+ meths.toString() + " synch methods=" + syncMeths.toString()
				+ " variables=" + variables.toString();
	}
}
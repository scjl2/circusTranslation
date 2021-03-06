package hijac.tools.tightrope.environments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class ParadigmEnv extends ObjectEnv
{
	protected static final String HAS_CLASS = "HasClass";
	protected ClassEnv classEnv;
	

	public ParadigmEnv()
	{
		super();

		meths = new ArrayList<MethodEnv>();
		syncMeths = new ArrayList<MethodEnv>();
//		classEnv = new ClassEnv();
		classEnv = null;
		

	}

	public void addClassEnv(ClassEnv classEnv)
	{
		this.classEnv = classEnv;
//		this.setHasClass(true);
	}

	public ClassEnv getClassEnv()
	{
		return classEnv;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map toMap()
	{
		Map map = new HashMap();
		map.put(PROCESS_ID, getName().toString());
		map.put(PARAMETERS_STR, paramsList());
		map.put(VARIABLES_STR, varsList());
		map.put(PARENTS_STR, getParents());
		map.put(METHODS, methsList());
		map.put(SYNC_METHODS, syncMethsList());
		map.put(HAS_CLASS, isHasClass());
		
		return map;
	}

	public boolean isHasClass()
	{
		if (classEnv != null)
		{
			if (!classEnv.getMeths().isEmpty()
					|| !classEnv.getSyncMeths().isEmpty())
			{
				return true;
			}
		}
		return false;
	}
//
//	public void setHasClass(boolean hasClass)
//	{
//		this.hasClass = hasClass;
//	}
}

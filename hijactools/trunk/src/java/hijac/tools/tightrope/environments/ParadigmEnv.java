package hijac.tools.tightrope.environments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class ParadigmEnv extends ObjectEnv
{
	protected static final String HAS_CLASS = "HasClass";
	protected ClassEnv classEnv;
	
	private final static ArrayList<String> GENERIC_PARADIGM_TYPES = new ArrayList<String>();
	

	public ParadigmEnv()
	{
		super();

//		classEnv = new ClassEnv();
		classEnv = null;
		
		

	}
	
	//
	//	public void setHasClass(boolean hasClass)
	//	{
	//		this.hasClass = hasClass;
	//	}
	
		private static void initGenericParadigmTypes()
		{
		
			GENERIC_PARADIGM_TYPES.add("Safelet");
			GENERIC_PARADIGM_TYPES.add("Mission");
			GENERIC_PARADIGM_TYPES.add("MissionSequencer");
			GENERIC_PARADIGM_TYPES.add("AperiodicEventHandler");
			GENERIC_PARADIGM_TYPES.add("OneShotEventHandler");
			GENERIC_PARADIGM_TYPES.add("PeriodicEventHandler");
			GENERIC_PARADIGM_TYPES.add("ManagedThread");
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

	public static ArrayList<String> getGenericParadigmTypes()
	{
		if(GENERIC_PARADIGM_TYPES.isEmpty())
		{
			initGenericParadigmTypes();
		}
		
		return GENERIC_PARADIGM_TYPES;
	}
	
	
}

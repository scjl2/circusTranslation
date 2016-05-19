package hijac.tools.tightrope.environments;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BinderMethodEnv //extends MethodEnv
{
	private Set<String> locations;
	private Set<String> callers;
	private String locationType = "";
	private String callerType = "";

	public BinderMethodEnv(String name)
	{
		locations = new HashSet<String>();
		callers = new HashSet<String>();

	}

	public BinderMethodEnv(String name, String location, String caller)
	{
		this(name);

		locations.add(location);
		callers.add(caller);
	}

	public Set<String> getLocations()
	{
		return locations;
	}

	public Set<String> getCallers()
	{
		return callers;
	}

	public void addLocation(String loc)
	{
		locations.add(loc);
	}

	public void addCaller(String caller)
	{
		callers.add(caller);
	}

	public String getLocationType()
	{		
		assert(this.locationType!= null );
		return this.locationType;
	}
	
	public void setLocationType(String locType)
	{
		assert(locType != null);
		this.locationType = locType;
		assert(this.locationType.equals(locType));
	}

	public String getCallerType()
	{		
		return callerType;
	}

	public void setCallerType(String callerType)
	{
		this.callerType = callerType;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map toMap()
	{
		Map returnMap = new HashMap();
		
		returnMap.put("Locs", locations);
		returnMap.put("Callers", callers);
		returnMap.put("CallerType", callerType);
		returnMap.put("LocationType", locationType);
		
		return returnMap;
	}
}
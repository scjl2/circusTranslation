package hijac.tools.tightrope.environments;

import java.util.HashSet;
import java.util.Set;

public class BinderMethodEnv extends MethodEnv
{
	private Set<String> locations;
	private Set<String> callers;
	private String locatioNType = "";
	private String callerType = "";

	public BinderMethodEnv(String name)
	{
		super(name);
		locations = new HashSet<String>();
		callers = new HashSet<String>();
		
		setReturnType("");
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

	public boolean hasReturn()
	{
		if (getReturnValue().equals("null") )//|| getReturnValue() != null)
		{
			return false;
		}
		else
		{
			return true;
		}

	}

	public String getLocationType()
	{		
		return locatioNType;
	}
	
	public void setLocationType(String locType)
	{
		locatioNType = locType;
	}

	public String getCallerType()
	{		
		return callerType;
	}

	public void setCallerType(String callerType)
	{
		this.callerType = callerType;
	}
}
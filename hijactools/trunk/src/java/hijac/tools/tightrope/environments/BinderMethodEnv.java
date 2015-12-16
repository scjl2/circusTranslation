package hijac.tools.tightrope.environments;

import java.util.ArrayList;
import java.util.List;

public class BinderMethodEnv extends MethodEnv
{
	private List<String> locations;
	private List<String> callers;

	public BinderMethodEnv(String name)
	{
		super(name);
		locations = new ArrayList<String>();
		callers = new ArrayList<String>();
	}

	public List<String> getLocations()
	{
		return locations;
	}

	public List<String> getCallers()
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
		if (getReturnValue().equals("null"))
		{
			return false;
		}
		else
		{
			return true;
		}

	}

}
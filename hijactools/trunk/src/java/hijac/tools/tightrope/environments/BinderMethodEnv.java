package hijac.tools.tightrope.environments;

import java.util.List;

import javax.lang.model.element.Name;

public class BinderMethodEnv extends MethodEnv
{
	private List<String> locations;
	private List<String> callers;

	public BinderMethodEnv(Name name)
	{
		super(name);
		// TODO Auto-generated constructor stub
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

}
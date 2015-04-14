package hijac.tools.tightrope.environments;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Name;

public class ObjectEnv
{
	Name name;
	List<Name> vars;

	public ObjectEnv()
	{
		vars = new ArrayList<Name>();
	}

	public Name getName()
	{
		return name;
	}

	public void setName(Name safelet)
	{
		this.name = safelet;
	}

	public List<Name> getVars()
	{
		return vars;
	}

	public void setVars(List<Name> vars)
	{
		this.vars = vars;
	}

}

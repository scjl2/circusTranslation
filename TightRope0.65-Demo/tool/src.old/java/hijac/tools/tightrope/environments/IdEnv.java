package hijac.tools.tightrope.environments;

import java.util.ArrayList;
import java.util.Map;

import javax.lang.model.element.Name;

/**
 * Super class for the all the Id environments. Holds a list of id names to
 * output the appropriate id file.
 * 

 */

public abstract class IdEnv
{
	protected ArrayList<Name> idNames = new ArrayList<Name>();
			
	@SuppressWarnings("rawtypes")
	public abstract Map toMap();

	public ArrayList<Name> getIdNames()
	{
		return idNames;
	}

	public void addIdNames(Name idName)
	{
		idNames.add(idName);
	}

}

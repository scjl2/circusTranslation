package hijac.tools.tightrope.environments;

import java.util.ArrayList;
import java.util.Map;

/**
 * Super class for the all the Id environments. Holds a list of id names to
 * output the appropriate id file.
 * 
 * @author Matt Luckcuck
 */

public abstract class IdEnv
{
	protected ArrayList<String> idNames = new ArrayList<String>();
	protected static final String ID_STR = "ID";

	@SuppressWarnings("rawtypes")
	public abstract Map toMap();

	public ArrayList<String> getIdNames()
	{
		return idNames;
	}

	public abstract void addIdNames(String idName);

}
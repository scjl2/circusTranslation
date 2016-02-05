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
	protected static final String MID = "M"+ID_STR;
	protected static final String SID = "S"+ID_STR;

	@SuppressWarnings("rawtypes")
	public abstract Map toMap();

	/**
	 * Output the id names
	 * 
	 * @return a list of id Names
	 */
	public ArrayList<String> getIdNames()
	{
		return idNames;
	}

	public abstract void addIdNames(String idName);

	public boolean contains(String id)
	{
		return idNames.contains(id);
	}
}
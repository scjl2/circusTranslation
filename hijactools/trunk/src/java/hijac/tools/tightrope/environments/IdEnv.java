package hijac.tools.tightrope.environments;


import java.util.ArrayList;
import java.util.Map;

/**
 * Super class for the all the Id Environments. Holds a list of id names to
 * output the appropriate id file.
 * 
 * @author Matt Luckcuck
 */

public abstract class IdEnv
{
	protected ArrayList<String> idNames = new ArrayList<String>();
	
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


}
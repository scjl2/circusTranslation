package hijac.tools.tightrope.environments;

import hijac.tools.tightrope.utils.TightRopeString;

import java.util.ArrayList;
import java.util.Map;

import javax.lang.model.element.Name;

public class SafeletEnv extends ParadigmEnv
{

	private MethodEnv initMethod, getSequencerMeth;
	
	private ArrayList<String> tlmsNames = new ArrayList<String>();

	public void addInitMethod(MethodEnv initMethod)
	{
		this.initMethod = initMethod;
	}

	public MethodEnv getInitMethod()
	{
		return initMethod;
	}

	public void addGetSequencerMethod(MethodEnv getSequencerMethod)
	{
		this.getSequencerMeth = getSequencerMethod;
	}

	public MethodEnv getGetSequencerMethod()
	{
		return getSequencerMeth;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map toMap()
	{
		Map map = super.toMap();

		// map.put("initializeApplicationMethod", methodToMap(initMethod));
		map.put("initializeApplicationMethod", "");

		//Should this only have one element?
		for (String name : tlmsNames)
		{
			map.put("SchedulableID", name);
		}

		return map;
	}

	public void addTopLevelMissionSequencer(Name name)
	{
		tlmsNames.add(name+TightRopeString.Name.SID);
	}

}

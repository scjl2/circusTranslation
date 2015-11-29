package hijac.tools.tightrope.environments;

import java.util.ArrayList;
import java.util.Map;

import javax.lang.model.element.Name;

public class SafeletEnv extends ParadigmEnv
{

	private MethodEnv initMethod, getSequencerMeth;

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

	// temp
	ArrayList<Name> tlmsNames = new ArrayList<Name>();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map toMap()
	{
		Map map = super.toMap();

		// map.put("initializeApplicationMethod", methodToMap(initMethod));
		map.put("initializeApplicationMethod", "");

		for (Name n : tlmsNames)
		{
			map.put("SchedulableID", n);
		}

		return map;
	}

	public void addTopLevelMissionSequencer(Name name)
	{
		tlmsNames.add(name);
	}

	
}

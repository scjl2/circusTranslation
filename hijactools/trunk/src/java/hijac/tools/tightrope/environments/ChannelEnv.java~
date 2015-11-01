package hijac.tools.tightrope.environments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.Name;

public class SafeletEnv extends ParadigmEnv
{
	// temp
	ArrayList<Name> tlmsNames = new ArrayList<Name>();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map toMap()
	{
		Map map = new HashMap();
		map.put("ProcessID", name.toString());
		map.put("initializeApplicationMethod", "");

		for (Name n : tlmsNames)
		{
			map.put("SchedulableID", n);
		}

		map.put("Variables", varsList());
		map.put("Methods", methsList());
		map.put("SyncMethods", syncMethsList());

		return map;
	}

	public void addTopLevelMissionSequencer(Name name)
	{
		tlmsNames.add(name);
	}
}

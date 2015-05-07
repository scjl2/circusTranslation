package hijac.tools.tightrope.environments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Name;

import com.sun.source.tree.MethodTree;



	public class SafeletEnv extends ParadigmEnv
	{		
		//temp
		ArrayList<Name> tlmsNames = new ArrayList<Name>();
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public Map toMap()
		{
			Map map = new HashMap();
			map.put("ProcessName", name.toString());
			map.put("initializeApplicationMethod", "");
			
			for(Name n : tlmsNames)
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
	
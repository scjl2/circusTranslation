package hijac.tools.tightrope.environments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Name;

import com.sun.source.tree.MethodTree;



	
	public class EventHandlerEnv extends ParadigmEnv
	{		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public Map toMap()
		{
			Map map = new HashMap();
			map.put("ProcessName", name.toString());
			map.put("initializeApplicationMethod", "");
					
//			if (tlmsNames.length == 1)
//			{
//				map.put("SchedulableID", tlmsNames[0]);
//			}
				
			
			
			return map;
		}
	}

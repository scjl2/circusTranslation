package hijac.tools.tightrope.environments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ApplicationEnv
{
	String safelet;
	Set<String> safeletSync;
	String missionSequencer;
	Set<String> controlTierSync;
	
	ArrayList<ApplicationTierEnv> tiers;
	
	@SuppressWarnings("unused")
	private class ApplicationTierEnv
	{
		
		ArrayList<ApplicationClusterEnv> clusters;
	}
	
	@SuppressWarnings("unused")
	private class ApplicationClusterEnv
	{
		
		Set<String> interfaceSync;
		String mission;
		Set<String> missionSync;
		ArrayList<String> schedulables; 
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map toMap()
	{
		Map returnmap = new HashMap();
		
		returnmap.put("Safelet", safelet);
		returnmap.put("SafeletSync", safeletSync);
		returnmap.put("MissionSequencer", safeletSync);
		
		return returnmap;
	}
}
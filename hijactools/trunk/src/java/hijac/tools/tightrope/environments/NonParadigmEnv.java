package hijac.tools.tightrope.environments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Name;

public class NonParadigmEnv extends ObjectEnv
{
	
	List<Name> nonPMeths;

	public NonParadigmEnv()
  {
    super();
    nonPMeths = new ArrayList<Name>();
    classEnv = new ClassEnv();
    classEnv.setName(getName());
  }
	
	public NonParadigmEnv(Name name, List<Name> vars, List<Name> nonPMeths)
	{
		super();
		nonPMeths = new ArrayList<Name>();
	}

	public void setname(Name objectName)
	{
	  super.setName(objectName);
	  classEnv.setName(objectName);
	}
	
	public List<Name> getNonPMeths()
	{
		return nonPMeths;
	}

	public void addNonPMeth(Name nonPMeth)
	{
		nonPMeths.add(nonPMeth);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
  public Map toMap()
  {
	  Map map = new HashMap();
    map.put(PROCESS_NAME, getName().toString());
    
    
    map.put(PROCESS_ID, getId());
    map.put("ProcObjectID", getObjectId());
    

//    map.put(FW_PARAMETERS, fwParamsList());
//    map.put(APP_PARAMETERS, appParamsList());
//    map.put(PROC_PARAMETERS, procParamsList());

    map.put(VARIABLES_STR, varsList());
    map.put(PARENTS_STR, getParents());
    map.put(METHODS, methsList());
    map.put(SYNC_METHODS, syncMethsList());

    map.put(HAS_CLASS, hasClass());

    

   

    return map;
  }
	
	


}

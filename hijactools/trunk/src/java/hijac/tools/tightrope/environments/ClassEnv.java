package hijac.tools.tightrope.environments;

import hijac.tools.tightrope.utils.TightRopeString.ObjectString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Name;

//TODO this is terrible. Need to refactor so that this is not extending ParadigmEnv
public class ClassEnv // extends ParadigmEnv
{
  private ObjectEnv objectEnv = new ObjectEnv();

  public ClassEnv()
  {
    super();
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  public Map toMap()
  {
    Map map = new HashMap();

    if (objectEnv.getName() != null)
    {
      map.put(ObjectString.PROCESS_NAME, objectEnv.getName().toString());
    }
    else
    {
      map.put(ObjectString.PROCESS_NAME, "");
    }

    map.put(ObjectString.METHODS, objectEnv.methsList());
    map.put(ObjectString.SYNC_METHODS, objectEnv.syncMethsList());
    map.put(ObjectString.VARIABLES_STR, objectEnv.varsList());
    map.put(ObjectString.PARENTS_STR, objectEnv.getParents());
    map.put(ObjectString.INITED_VARIABLES, objectEnv.initedVarsList());

    return map;
  }

  public boolean isEmpty()
  {
    if (objectEnv.meths.isEmpty() && objectEnv.syncMeths.isEmpty()
        && objectEnv.variables.isEmpty())
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  public String toString()
  {
    return "ClassEnv: " + "Name=" + objectEnv.getName() + " methods="
        + objectEnv.meths.toString() + " synch methods=" + objectEnv.syncMeths.toString()
        + " variables=" + objectEnv.variables.toString();
  }

  public List<MethodEnv> getMeths()
  {
    return objectEnv.getMeths();
  }

  public List<MethodEnv> getSyncMeths()
  {
    return objectEnv.getSyncMeths();
  }

  public void setName(Name name)
  {
    objectEnv.setName(name);
  }

  public void addVariableInit(String string, String variableInitAndInput, boolean b)
  {
    objectEnv.addVariableInit(string, variableInitAndInput, b);
  }

  public void addVariable(VariableEnv v)
  {
    objectEnv.addVariable(v);
  }

  public String getName()
  {
    return objectEnv.getName().toString();
  }

  public void addVariable(String string, String string2, String string3, boolean b)
  {
    objectEnv.addVariable(string, string2, string3, b);
  }

  public VariableEnv getVariable(Name varName)
  {
    return objectEnv.getVariable(varName);
  }

  public VariableEnv getVariable(String nodeVariableString)
  {
    return objectEnv.getVariable(nodeVariableString);
  }

  public void addSyncMeth(MethodEnv m)
  {
    objectEnv.addSyncMeth(m);
  }

  public void addMeth(MethodEnv m)
  {
    objectEnv.addMeth(m);
  }

  public void addParent(String missionId)
  {
    objectEnv.addParent(missionId);

  }
}
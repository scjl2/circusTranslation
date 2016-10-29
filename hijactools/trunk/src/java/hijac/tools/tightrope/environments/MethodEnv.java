package hijac.tools.tightrope.environments;

import hijac.tools.tightrope.utils.MethodDestinationE;
import hijac.tools.tightrope.utils.TightRopeString;
import hijac.tools.tightrope.utils.TightRopeString.LATEX;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Name;
import javax.lang.model.type.TypeKind;

public class MethodEnv
{
 	public static enum AccessModifier
	{
		PUBLIC, PRIVATE, PROTECTED
	};

	private String methodName = "";
	private String eventName = "";
	private String returnType = "";
	private Map<String, Type> parameters = new HashMap<String, Type>();
	protected ArrayList<Name> returnValues = new ArrayList<Name>();
	private Object body = LATEX.SKIP;
	private boolean synchronised = false;
	private AccessModifier accesMod = null;
	private boolean APIMethod;
	private MethodDestinationE methodDestination;
	
	private boolean externalAppMethod = false;
//	private BinderMethodEnv methodCallBinding = null;
	
	
	private Set<String> locations;
	private Set<String> callers;
	private String locationType = "";
	private String callerType = "";
	private ObjectEnv objectLocation;

	public MethodEnv(String name)
	{
		this.methodName = name;
		setEventName(name);
//		this.methodCallBinding = new BinderMethodEnv(name);
		locations = new HashSet<String>();
		callers = new HashSet<String>();
//		locations.add(location);
//		callers.add(caller);
	}

	public MethodEnv(Name name)
	{	
		this.methodName = name.toString();
		setEventName(name.toString());
//		this.methodCallBinding = new BinderMethodEnv(name.toString());
		locations = new HashSet<String>();
		callers = new HashSet<String>();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public MethodEnv(Name name, TypeKind returnType, ArrayList<Name> returnValues,
			Map params)
	{
		this.methodName = name.toString();
		setEventName(name.toString());
		this.returnType = returnType.toString();
		this.parameters = params;
		this.returnValues = returnValues;
//		this.methodCallBinding = new BinderMethodEnv(name.toString());
		locations = new HashSet<String>();
		callers = new HashSet<String>();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public MethodEnv(Name name, TypeKind returnType, ArrayList<Name> returnValues,
			Map params, Object body)
	{
		this.methodName = name.toString();
		setEventName(name.toString());
		this.returnType = returnType.toString();
		this.parameters = params;
		this.returnValues = returnValues;
		this.setBody(body);
//		this.methodCallBinding = new BinderMethodEnv(name.toString());
		locations = new HashSet<String>();
		callers = new HashSet<String>();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public MethodEnv(Name name, String returnType, ArrayList<Name> returnValues,
			Map params, Object body)
	{
		this.methodName = name.toString();
		setEventName(name.toString());
		this.returnType = returnType;
		this.parameters = params;
		this.returnValues = returnValues;
		this.setBody(body);
//		this.methodCallBinding = new BinderMethodEnv(name.toString());
		locations = new HashSet<String>();
		callers = new HashSet<String>();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public MethodEnv(Name name, TypeKind returnType, Map params)
	{
		this.methodName = name.toString();
		setEventName(name.toString());
		this.returnType = returnType.toString();
		this.parameters = params;
//		this.methodCallBinding = new BinderMethodEnv(name.toString());
		locations = new HashSet<String>();
		callers = new HashSet<String>();
	}

	public MethodEnv(String name, String returnType, boolean APIMethod)
	{
		methodName = name;
		setEventName(name.toString());
		this.returnType = returnType;
		this.APIMethod = APIMethod;
//		this.methodCallBinding = new BinderMethodEnv(name);
		locations = new HashSet<String>();
		callers = new HashSet<String>();
	}

	public String getName()
	{
		return methodName;
	}

	public String getReturnType()
	{

		if (returnType != null)
		{
			return returnType;
		}
		else
		{
			return "null";
		}
	}

	public void setReturnType(TypeKind returnType)
	{
		this.returnType = returnType.toString();
	}

	public void setReturnType(String returnType)
	{
		this.returnType = returnType;
	}

	public Map<String, Type> getParameters()
	{
		return parameters;
	}

	public void setParameters(Map<String, Type> parameters)
	{
		this.parameters = parameters;
	}

	public String getReturnValue()
	{
		// TODO MORE HACKERY
		if (returnValues != null)
		{
			if (returnValues.size() == 1)
			{
				return returnValues.get(0).toString();
			}
			else
			{
				return returnValues.toString();
			}
		}
		else
		{
			return "null";
		}
	}

	public void setBody(String body)
	{
		this.body = body;
	}

	public void setSynchronised(boolean sync)
	{
		synchronised = sync;
	}

	public boolean isSynchronised()
	{
		return synchronised;
	}

	public void setAccess(AccessModifier access)
	{
		accesMod = access;
	}

	public String getAccessString()
	{
		if (accesMod != null)
		{
			return accesMod.toString().toLowerCase();
		}
		else
		{
			return "";
		}
	}

	public Object getBody()
	{
		return body;
	}

	public void setBody(Object body)
	{
		this.body = body;
	}

	public boolean isAPIMethod()
	{
		return APIMethod;
	}

	public void setAPIMethod(boolean aPIMethod)
	{
		APIMethod = aPIMethod;
	}

	public String toString()
	{
		return "Method Env: name=" + methodName + ", returnType=" + returnType
				+ ", parmeters=" + parameters.toString() + ", return values="
				+ returnValues.toString() + ", body=" + body + ", synchronised="
				+ synchronised + ", access modifier=" + accesMod + ", API method="
				+ APIMethod + " Location Type=" + getLocationType() ;

	}

	public boolean isExternalAppMethod()
	{
		return externalAppMethod;
	}

	public void setExternalAppMethod(boolean externalAppMethod)
	{
		this.externalAppMethod = externalAppMethod;
		
		if(isExternalAppMethod())
		{
		}
	}

	public Set<String> getLocations()
	{
		return locations;
	}

	public Set<String> getCallers()
	{
		return callers;
	}

	public void addLocation(String loc)
	{
		locations.add(loc);
	}

	public void addCaller(String caller)
	{
		callers.add(caller);
	}

	@Deprecated
	public boolean hasReturn()
	{
		return returnValues.isEmpty();
		

	}

	public String getLocationType()
	{		
		return locationType;
	}
	
	public void setLocationType(String locType)
	{		
		locationType = locType;
	}

	public String getCallerType()
	{		
		return callerType;
	}

	public void setCallerType(String callerType)
	{
		this.callerType = callerType;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map toMap()
	{
		Map methodMap = new HashMap();

		String s = getName();
	
		methodMap.put(TightRopeString.Name.METHOD_NAME, s);
		methodMap.put(TightRopeString.Name.CHANNEL_NAME, eventName);
		methodMap.put(TightRopeString.Name.RETURN_TYPE, getReturnType());
		methodMap.put(TightRopeString.Name.RETURN_VALUE, getReturnValue());
		methodMap.put(TightRopeString.Name.PARAMETERS_STR, getParameters());
		methodMap.put(TightRopeString.Name.ACCESS, getAccessString());
		methodMap.put(TightRopeString.Name.BODY, getBody());
		methodMap.put(TightRopeString.Name.EXTERNAL_APPMETH, isExternalAppMethod());
		methodMap.put(TightRopeString.Name.LOC_TYPE, getLocationType());

		methodMap.put(TightRopeString.Name.LOCS, getLocations());
		methodMap.put(TightRopeString.Name.CALLERS_STR, getCallers());
		methodMap.put(TightRopeString.Name.CALLER_TYPE, getCallerType());
		
		return methodMap;
	}

	public void setMethodLocation(ObjectEnv object)
	{
		this.objectLocation = object;		
	}
	
	public ObjectEnv getMethodLocation()
	{
		return objectLocation;
	}

	public String getEventName()
	{
		return eventName;
	}

	public void setEventName(String eventName)
	{
		this.eventName = eventName;
	}

  public MethodDestinationE getMethodDestination()
  {
    return methodDestination;
  }

  public void setMethodDestination(MethodDestinationE methodDestination)
  {
    this.methodDestination = methodDestination;
  }



}
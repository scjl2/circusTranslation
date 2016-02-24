package hijac.tools.tightrope.environments;

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

	public static enum AccessMod
	{
		PUBLIC, PRIVATE, PROTECTED
	};

	private static final String METHOD_NAME = "MethodName";
	private static final String RETURN_TYPE = "ReturnType";
	private static final String RETURN_VALUE = "ReturnValue";
	private static final String ACCESS = "Access";
	private static final String BODY = "Body";
	private static final String PARAMETERS_STR = "Parameters";
	private static final String EXTERNAL_APPMETH = "ExternalAppmeth";
	
	private String methodName = "";
	private String returnType = "";
	private Map<String, Type> parameters = new HashMap<String, Type>();
	protected ArrayList<Name> returnValues = new ArrayList<Name>();
	private Object body = "";
	private boolean synchronised = false;
	private AccessMod accesMod = null;
	private boolean APIMethod;
	
	private boolean externalAppMethod = false;
//	private BinderMethodEnv methodCallBinding = null;
	
	
	private Set<String> locations;
	private Set<String> callers;
	private String locationType = "";
	private String callerType = "";

	public MethodEnv(String name)
	{
		this.methodName = name;
//		this.methodCallBinding = new BinderMethodEnv(name);
		locations = new HashSet<String>();
		callers = new HashSet<String>();
//		locations.add(location);
//		callers.add(caller);
	}

	public MethodEnv(Name name)
	{	
		this.methodName = name.toString();
//		this.methodCallBinding = new BinderMethodEnv(name.toString());
		locations = new HashSet<String>();
		callers = new HashSet<String>();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public MethodEnv(Name name, TypeKind returnType, ArrayList<Name> returnValues,
			Map params)
	{
		this.methodName = name.toString();
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
		this.returnType = returnType.toString();
		this.parameters = params;
//		this.methodCallBinding = new BinderMethodEnv(name.toString());
		locations = new HashSet<String>();
		callers = new HashSet<String>();
	}

	public MethodEnv(String name, String returnType, boolean APIMethod)
	{
		methodName = name;
		this.returnType = returnType;
		this.APIMethod = APIMethod;
//		this.methodCallBinding = new BinderMethodEnv(name);
		locations = new HashSet<String>();
		callers = new HashSet<String>();
	}

	public String getMethodName()
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

	public void setAccess(AccessMod access)
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
//			methodCallBinding = new BinderMethodEnv(methodName);
		}
	}

	public Set<String> getLocations()
	{
//		return methodCallBinding.getLocations();
		return locations;
	}

	public Set<String> getCallers()
	{
//		return methodCallBinding.getCallers();
		return callers;
	}

	public void addLocation(String loc)
	{
//		methodCallBinding.addLocation(loc);
		locations.add(loc);
	}

	public void addCaller(String caller)
	{
//		methodCallBinding.addCaller(caller);
		callers.add(caller);
	}

	@Deprecated
	public boolean hasReturn()
	{
		return returnValues.isEmpty();
		
//		if (getReturnValue().equals("null") )//|| getReturnValue() != null)
//		{
//			return false;
//		}
//		else
//		{
//			return true;
//		}

	}

	public String getLocationType()
	{		
//		return methodCallBinding.getLocationType();
		return locationType;
	}
	
	public void setLocationType(String locType)
	{
//		assert(locType != null);
//		methodCallBinding.setLocationType(locType);
//		assert(methodCallBinding.getLocationType().equals(locType));
//		assert(getLocationType().equals(locType));
		
		locationType = locType;
	}

	public String getCallerType()
	{		
//		return methodCallBinding.getCallerType();
		return callerType;
	}

	public void setCallerType(String callerType)
	{
//		methodCallBinding.setCallerType(callerType);
		this.callerType = callerType;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map toMap()
	{
		Map methodMap = new HashMap();

		String s = getMethodName();

		methodMap.put(METHOD_NAME, s);
		methodMap.put(RETURN_TYPE, getReturnType());
		methodMap.put(RETURN_VALUE, getReturnValue());
		methodMap.put(PARAMETERS_STR, getParameters());
		methodMap.put(ACCESS, getAccessString());
		methodMap.put(BODY, getBody());
		methodMap.put(EXTERNAL_APPMETH, isExternalAppMethod());
		methodMap.put("LocType", getLocationType());
//		methodMap.put("LocType", "LocTest");
		methodMap.put("Locs", getLocations());
		methodMap.put("Callers", getCallers());
		methodMap.put("CallerType", getCallerType());
//		methodMap.put("CallerType", "CallerTest");
		
		
		
		return methodMap;
	}
	
	public boolean equals(MethodEnv m)
	{
		return this.getMethodName().equals(m.getMethodName());
	}

//	public BinderMethodEnv getMethodCallBinding()
//	{
//		return methodCallBinding;
//	}
}
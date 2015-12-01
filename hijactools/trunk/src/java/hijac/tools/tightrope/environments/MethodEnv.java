package hijac.tools.tightrope.environments;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.Name;
import javax.lang.model.type.TypeKind;

public class MethodEnv
{
	public static enum AccessMod
	{
		PUBLIC, PRIVATE, PROTECTED
	};

	private String methodName = "";
	private String returnType = "";
	private Map<String, Type> parameters = new HashMap<String, Type>();
	private ArrayList<Name> returnValues = new ArrayList<Name>();
	private Object body = "";
	private boolean synchronised;
	private AccessMod accesMod = null;
	private boolean APIMethod;

	public MethodEnv(Name name)
	{
		this.methodName = name.toString();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public MethodEnv(Name name, TypeKind returnType,
			ArrayList<Name> returnValues, Map params)
	{
		this.methodName = name.toString();
		this.returnType = returnType.toString();
		this.parameters = params;
		this.returnValues = returnValues;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public MethodEnv(Name name, TypeKind returnType,
			ArrayList<Name> returnValues, Map params, Object body)
	{
		this.methodName = name.toString();
		this.returnType = returnType.toString();
		this.parameters = params;
		this.returnValues = returnValues;
		this.setBody(body);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public MethodEnv(Name name, String returnType,
			ArrayList<Name> returnValues, Map params, Object body)
	{
		this.methodName = name.toString();

		this.returnType = returnType;
		this.parameters = params;
		this.returnValues = returnValues;
		this.setBody(body);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public MethodEnv(Name name, TypeKind returnType, Map params)
	{
		this.methodName = name.toString();
		this.returnType = returnType.toString();
		this.parameters = params;
	}

	public MethodEnv(String name, String returnType, boolean APIMethod)
	{
		methodName = name;
		this.returnType = returnType;
		this.APIMethod = APIMethod;
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
		System.out.println("/// returnType.toString()" + returnType.toString());
		this.returnType = returnType.toString();
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
				+ returnValues.toString() + ", body=" + body
				+ ", synchronised=" + synchronised + ", access modifier="
				+ accesMod + ", API method=" + APIMethod;

	}
}
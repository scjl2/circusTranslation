package hijac.tools.tightrope.environments;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

import javax.lang.model.element.Name;
import javax.lang.model.type.TypeKind;


public class MethodEnv
{
	private Name methodName;
	private TypeKind returnType;
	private Map<String, Type> parameters;
	private ArrayList<Name> returnValues;
	Object body;
	
	
	public MethodEnv(Name name)
	{
		this.methodName = name;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public MethodEnv(Name name, TypeKind returnType, ArrayList<Name> returnValues ,Map params)
	{
		this.methodName = name;
		this.returnType = returnType;
		this.parameters = params;
		this.returnValues = returnValues;
	}
	
	//TODO Find out what object the body should be
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public MethodEnv(Name name, TypeKind returnType, ArrayList<Name> returnValues ,Map params, Object body)
	{
		this.methodName = name;
		this.returnType = returnType;
		this.parameters = params;
		this.returnValues = returnValues;
		this.body = body;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public MethodEnv(Name name, TypeKind returnType, Map params)
	{
		this.methodName = name;
		this.returnType = returnType;
		this.parameters = params;
	}

	public Name getMethodName()
	{
		return methodName;
	}

	public String getReturnType()
	{
		if(returnType != null)
		{
			return returnType.toString();
		}
		else
		{
			return "null";
		}
	}

	public void setReturnType(TypeKind returnType)
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
		//TODO MORE HACKERY
		if(returnValues != null)
		{
			if(returnValues.size() == 1)
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
}
package hijac.tools.tightrope.environments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Name;
import javax.lang.model.type.TypeKind;

import com.sun.source.tree.Tree;

public class ObjectEnv
{
	private static final String ACCESS = "access";
	private static final String BODY = "body";
	private static final String PARAMETERS_STR = "parameters";
	private static final String RETURN_TYPE = "returnType";
	private static final String METHOD_NAME = "methodName";
	private static final String VAR_INPUT = "VarInput";
	private static final String VAR_INIT = "VarInit";
	private static final String VAR_TYPE = "VarType";
	private static final String VAR_NAME = "VarName";
	protected static final String VARIABLES_STR = "Variables";
	protected static final String SYNC_METHODS = "SyncMethods";
	protected static final String METHODS = "Methods";
	protected static final String PROCESS_ID = "ProcessID";
	protected static final String IMPORT_NAME = "importName";
	protected static final String HANDLER_TYPE = "handlerType";
	// ClassTree classTree;
	Name name;
	List<VariableEnv> variables;
	List<VariableEnv> parameters;
	protected List<MethodEnv> meths;
	protected List<MethodEnv> syncMeths;

	public ObjectEnv()
	{
		variables = new ArrayList<VariableEnv>();
		parameters = new ArrayList<VariableEnv>();
	}

	public Name getName()
	{
		return name;
	}

	public void setName(Name safelet)
	{
		this.name = safelet;
	}

	public void addVariable(String variableName, String variableType,
			Object variableInit, boolean primitive)
	{
		variables.add(new VariableEnv(variableName, variableType, variableInit,
				primitive));
	}

	public VariableEnv getVariable(Name name)
	{
		return this.getVariable(name.toString());
	}

	public VariableEnv getVariable(String name)
	{
		for (VariableEnv v : variables)
		{
			if (v.getVariableName().equals(name))
			{
				return v;
			}
		}
		return null;
	}

	public List<VariableEnv> getVariables()
	{
		return variables;
	}

	public void addVariableInit(Name varName, Tree init)
	{
		for (VariableEnv varEnv : getVariables())
		{
			if (varEnv.getVariableName().contentEquals(varName))
			{
				varEnv.setVariableInit(init);
			}
		}
	}

	public void addVariable(String variableName, String variableType,
			Object variableInit, String variableInput, boolean primitive)

	{
		variables.add(new VariableEnv(variableName, variableType, variableInit,
				variableInput, primitive));

	}

	public void addVariableInit(String varName, String init,
			boolean variableInput)
	{
		for (VariableEnv varEnv : getVariables())
		{
			if (varEnv.getVariableName().contentEquals(varName))
			{
				varEnv.setVariableInit(init);
				varEnv.setVariableInput(variableInput);
			}
		}

	}

	public List<VariableEnv> getParameters()
	{
		return parameters;
	}

	public void addParameter(VariableEnv parameter)
	{
		parameters.add(parameter);
	}

	public void addParameter(String variableName, String variableType,
			Object variableInit, String variableInput, boolean primitive)

	{
		parameters.add(new VariableEnv(variableName, variableType,
				variableInit, variableInput, primitive));

	}

	@SuppressWarnings("rawtypes")
	public void addMeth(Name methName, TypeKind returnType,
			ArrayList<Name> returnValues, Map params)
	{
		meths.add(new MethodEnv(methName, returnType, returnValues, params));
	}

	@SuppressWarnings("rawtypes")
	public void addSyncMeth(Name methName, TypeKind returnType,
			ArrayList<Name> returnValues, Map params)
	{
		syncMeths
				.add(new MethodEnv(methName, returnType, returnValues, params));
	}

	public void addMeth(MethodEnv me)
	{
		meths.add(me);
	}

	public void addSyncMeth(MethodEnv me)
	{
		syncMeths.add(me);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> varsList()
	{
		List<Map> returnList = new ArrayList<>();

		for (VariableEnv v : variables)
		{
			Map varMap = new HashMap();
			varMap.put(VAR_NAME, v.getVariableName().toString());
			varMap.put(VAR_TYPE, v.getVariableType());
			varMap.put(VAR_INIT, v.getVariableInit().toString());
			varMap.put(VAR_INPUT, v.getVariableInput());

			returnList.add(varMap);
		}

		return returnList;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> paramsList()
	{
		List<Map> returnList = new ArrayList<>();

		for (VariableEnv v : parameters)
		{
			Map varMap = new HashMap();
			varMap.put(VAR_NAME, v.getVariableName().toString());
			varMap.put(VAR_TYPE, v.getVariableType());
			varMap.put(VAR_INIT, v.getVariableInit().toString());
			varMap.put(VAR_INPUT, v.getVariableInput());

			returnList.add(varMap);
		}

		return returnList;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> methsList()
	{
		List<Map> returnList = new ArrayList<>();

		for (MethodEnv me : meths)
		{
			Map methodMap = new HashMap();
			methodMap.put(METHOD_NAME, me.getMethodName().toString());
			methodMap.put(RETURN_TYPE, me.getReturnType());
			methodMap.put(PARAMETERS_STR, me.getParameters());
			methodMap.put(BODY, me.getBody());
			methodMap.put(ACCESS, me.getAccessString());

			returnList.add(methodMap);
		}

		return returnList;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> syncMethsList()
	{
		List<Map> returnList = new ArrayList<>();

		for (MethodEnv me : syncMeths)
		{
			Map methodMap = new HashMap();
			methodMap.put(METHOD_NAME, me.getMethodName().toString());
			methodMap.put(RETURN_TYPE, me.getReturnType());
			methodMap.put("returnValue", me.getReturnValue());
			methodMap.put(PARAMETERS_STR, me.getParameters());
			methodMap.put(ACCESS, me.getAccessString());
			methodMap.put(BODY, me.getBody());
			returnList.add(methodMap);
		}

		return returnList;
	}

}

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
	protected static final String ACCESS = "Access";
	protected static final String BODY = "Body";
	protected static final String RETURN_TYPE = "ReturnType";
	protected static final String METHOD_NAME = "MethodName";
	private static final String VAR_INPUT = "VarInput";
	private static final String VAR_INIT = "VarInit";
	private static final String VAR_TYPE = "VarType";
	private static final String VAR_NAME = "VarName";
	protected static final String VARIABLES_STR = "Variables";
	protected static final String SYNC_METHODS = "SyncMethods";
	protected static final String METHODS = "Methods";
	protected static final String PROCESS_ID = "ProcessID";
	protected static final String IMPORT_NAME = "ImportName";
	protected static final String HANDLER_TYPE = "HandlerType";
	protected static final String PARAMETERS_STR = "Parameters";
	// ClassTree classTree;
	/**
	 * The name of the entity this environment represents
	 */
	Name name;
	List<VariableEnv> variables;
	List<VariableEnv> parameters;
	protected List<MethodEnv> meths;
	protected List<MethodEnv> syncMeths;
	/**
	 * A list of custom channels required by this object. I.E. the new channels that need creating for it.
	 */	
	private List<ChannelEnv> newChannels;

	public ObjectEnv()
	{
		variables = new ArrayList<VariableEnv>();
		parameters = new ArrayList<VariableEnv>();
		newChannels = new ArrayList<ChannelEnv>();
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

	/**
	 * Gets a variable environment from this object which shares the same name as <code>name</code>. 
	 * If the variable doesn't exist in this object environment, then this method will return <code>null</code>
	 * This method use <code>getVariable(String name)</code> internally
	 * 
	 * @param name The name of the variable you're looking for, as a <code>Name</code>
	 * @return The <code>VariableEnv</code> representing the variable you're looking for, or <code>null</code>
	 */
	public VariableEnv getVariable(Name name)
	{
		return this.getVariable(name.toString());
	}

	
	/**
	 * Gets a variable environment from this object which shares the same name as <code>name</code>. 
	 * If the variable doesn't exist in this object environment, then this method will return <code>null</code>
	 * 
	 * @param name The name of the variable you're looking for, as a <code>String</code>
	 * @return The <code>VariableEnv</code> representing the variable you're looking for, or <code>null</code>
	 */
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

	/**
	 * Gets the <code>VariableEnv</code> representing the parameter that shares its name with the parameter.
	 * May return <code>null</code>
	 * 
	 * @param name The name of the parameter we're looking for
	 * @return The <code>VariableEnv</code> representing the parameter we're looking for, or <code>null</code> 
	 */
	public VariableEnv getParameter(String name)
	{
		for(VariableEnv v : parameters)
		{
			if(v.getVariableName().contentEquals(name))
			{
				return v;
			}
		}
		
		return null;
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
		me.setSynchronised(false);
		meths.add(me);
	}

	public void addSyncMeth(MethodEnv me)
	{
		me.setSynchronised(true);
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

	@SuppressWarnings({ "rawtypes" })
	public List<Map> methsList()
	{
		List<Map> returnList = new ArrayList<>();

		for (MethodEnv me : meths)
		{
			Map methodMap = methodToMap(me);


			returnList.add(methodMap);
		}

		return returnList;
	}

	@SuppressWarnings({ "rawtypes" })
	public List<Map> syncMethsList()
	{
		List<Map> returnList = new ArrayList<>();

		for (MethodEnv me : syncMeths)
		{
			Map methodMap = methodToMap(me);
			returnList.add(methodMap);
		}

		return returnList;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected Map methodToMap(MethodEnv me)
	{
		Map methodMap = new HashMap();
		
		
		String s = me.getMethodName();
	
		methodMap.put( METHOD_NAME, s);
		methodMap.put(RETURN_TYPE, me.getReturnType());
		methodMap.put("returnValue", me.getReturnValue());
		methodMap.put(PARAMETERS_STR, me.getParameters());
		methodMap.put(ACCESS, me.getAccessString());
		methodMap.put(BODY, me.getBody());
		
		return methodMap;
	}

	public List<MethodEnv> getMeths()
	{
		return meths;
	}
	
	public List<MethodEnv> getSyncMeths()
	{
		return syncMeths;
	}

	public List<ChannelEnv> getNewChannels()
	{
		return newChannels;
	}
	
	public void addNewChannel(String channelName, String channelType)
	{
		newChannels.add(new ChannelEnv(channelName, channelType));
	}
	

}

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
	private static final String VAR_INPUT = "VarInput";
	private static final String VAR_INIT = "VarInit";
	private static final String VAR_TYPE = "VarType";
	private static final String VAR_NAME = "VarName";
	protected static final String VARIABLES_STR = "Variables";
	protected static final String INITED_VARIABLES = "InitedVariables";
	protected static final String SYNC_METHODS = "SyncMethods";
	protected static final String METHODS = "Methods";
	protected static final String PROCESS_ID = "ProcessID";
	protected static final String IMPORT_NAME = "ImportName";
	protected static final String HANDLER_TYPE = "HandlerType";

	protected static final String PARENTS_STR = "Parents";
	protected static final String ID = "ID";
	// ClassTree classTree;
	/**
	 * The name of the entity this environment represents
	 */
	private Name name;
	/**
	 * The variables of this object
	 */
	protected List<VariableEnv> variables;
	/**
	 * /** The parameters of this object, separated by where they will be output
	 * to.
	 */
	protected List<VariableEnv> fwParameters, appParameters, threadParameters,
			procParameters;

	/**
	 * The non-synchronised methods of this object
	 */
	protected List<MethodEnv> meths;
	/**
	 * The synchronised methods of this object
	 */
	protected List<MethodEnv> syncMeths;
	/**
	 * The custom channels required by this object. I.E. the new channels that
	 * need creating for it.
	 */
	private List<ChannelEnv> newChannels;
	/**
	 * The required parents of this object. I.E. the files that this object's
	 * file will depend on to parse
	 */
	private List<String> parents;

	/**
	 * >>>>>>> deferredParameterGathering The standard constructor, which simply
	 * instantiates the lists
	 */
	public ObjectEnv()
	{

		meths = new ArrayList<MethodEnv>();
		syncMeths = new ArrayList<MethodEnv>();

		variables = new ArrayList<VariableEnv>();

		fwParameters = new ArrayList<VariableEnv>();
		appParameters = new ArrayList<VariableEnv>();
		threadParameters = new ArrayList<VariableEnv>();
		procParameters = new ArrayList<VariableEnv>();

		newChannels = new ArrayList<ChannelEnv>();
		parents = new ArrayList<String>();
	}
	
	public String getIdType()
	{
		return "";
	}

	public Name getName()
	{
		return name;
	}

	public void setName(Name objectName)

	{
		this.name = objectName;
	}

	public void addVariable(String variableName, String variableType,
			Object variableInit, boolean primitive)
	{
		variables
				.add(new VariableEnv(variableName, variableType, variableInit, primitive));
	}

	/**
	 * Gets a variable environment from this object which shares the same name
	 * as <code>name</code>. If the variable doesn't exist in this object
	 * environment, then this method will return <code>null</code> This method
	 * use <code>getVariable(String name)</code> internally
	 * 
	 * @param name
	 *            The name of the variable you're looking for, as a
	 *            <code>Name</code>
	 * @return The <code>VariableEnv</code> representing the variable you're
	 *         looking for, or <code>null</code>
	 */
	public VariableEnv getVariable(Name name)
	{
		return this.getVariable(name.toString());
	}

	/**
	 * Gets a variable environment from this object which shares the same name
	 * as <code>name</code>. If the variable doesn't exist in this object
	 * environment, then this method will return <code>null</code>
	 * 
	 * @param name
	 *            The name of the variable you're looking for, as a
	 *            <code>String</code>
	 * @return The <code>VariableEnv</code> representing the variable you're
	 *         looking for, or <code>null</code>
	 */
	public VariableEnv getVariable(String name)
	{
		System.out.println(this.name + ".getVariable:" + name);
		System.out.println("All the Variables = " + variables.toString());
		for (VariableEnv v : variables)
		{
			System.out.println("variable name = " + v.getName());

			String vName = v.getName();
			if (vName.contains("\\"))
			{
				// Matches \
				String[] vNameSplit = vName.split("\\\\");

				StringBuilder sb = new StringBuilder();
				for (String s : vNameSplit)
				{
					sb.append(s);
				}
				vName = sb.toString();

				System.out.println("\t Var After Split = " + vName);
			}

			System.out.println("name = " + name);
			System.out.println("vName = " + vName);
			if (vName.equals(name))
			{
				System.out.println("Returning " + v.toString());
				return v;
			}
		}
		System.out.println("retruning Null");
		return null;
	}

	public List<VariableEnv> getVariables()
	{
		return variables;
	}

	public void addVariableInit(String varName, Tree init)
	{
		System.out.println("addVariableInit(" + varName + "," + init);
		for (VariableEnv varEnv : getVariables())
		{
			System.out.println("\tvarEnv.getName=" + varEnv.getName());
			if (varEnv.getName().contentEquals(varName))
			{
				System.out.println("Setting Var Init: name=" + varName + " init=" + init);
				varEnv.setInit(init);
			}
		}
	}

	public void addVariable(VariableEnv v)
	{
		variables.add(v);

	}

	public void addVariable(String variableName, String variableType,
			Object variableInit, String variableInput, boolean primitive)

	{

		addVariable(new VariableEnv(variableName, variableType, variableInit,
				variableInput, primitive));

	}

	public void addVariableInit(String varName, String init, boolean variableInput)
	{
		for (VariableEnv varEnv : getVariables())
		{
			if (varEnv.getName().contentEquals(varName))
			{
				varEnv.setInit(init);
				varEnv.setInput(variableInput);
			}
		}

	}

	/**
	 * Gets the <code>VariableEnv</code> representing the parameter that shares
	 * its name with the parameter. May return <code>null</code>
	 * 
	 * @param name
	 *            The name of the parameter we're looking for
	 * @return The <code>VariableEnv</code> representing the parameter we're
	 *         looking for, or <code>null</code>
	 */
	public VariableEnv getParameter(String name)
	{

		for (VariableEnv v : getParameters())
		{
			if (v.getName().contentEquals(name))
			{
				return v;
			}
		}

		return null;
	}

	private List<VariableEnv> getParameters()
	{
		List<VariableEnv> parameters = new ArrayList<VariableEnv>();
		parameters.addAll(fwParameters);
		parameters.addAll(appParameters);
		parameters.addAll(threadParameters);

		return parameters;
	}

	public List<VariableEnv> getAllParameters()
	{
		return getParameters();
	}

	public List<VariableEnv> getFWParameters()
	{
		return fwParameters;
	}

	public List<VariableEnv> getAppParameters()
	{
		return appParameters;
	}

	public List<VariableEnv> getProcParameters()
	{
		return procParameters;
	}

	public List<VariableEnv> getThreadParameters()
	{
		return threadParameters;
	}

	public void addFWdParameter(VariableEnv parameter)
	{
		fwParameters.add(parameter);

	}

	public void addAppParameter(VariableEnv parameter)
	{
		appParameters.add(parameter);

	}

	public void addThreadParameter(VariableEnv parameter)
	{
		threadParameters.add(parameter);

	}

	public void addProcParameter(VariableEnv parameter)
	{
		System.out.println("adding " + parameter.toString());
		procParameters.add(parameter);
	}

	protected void addFWParameter(String variableName, String variableType,
			String programType, boolean primitive, String init)

	{
		boolean isNew = true;

		for (VariableEnv v : getParameters())
		{
			if (v.getName().equals(variableName))
			{
				// v.setVariableName(variableName);
				// v.setVariableType(variableType);
				// v.setProgramType(programType);
				// v.setPrimitive(primitive);
				v.setInit(init);

				isNew = false;
			}
		}

		if (isNew)
		{
			VariableEnv e = new VariableEnv(variableName, variableType, programType,
					primitive);
			e.setInit(init);
			addFWdParameter(e);
		}

	}

	protected void addProcParameter(String variableName, String variableType,
			String programType, boolean primitive, String init)

	{

		VariableEnv e = new VariableEnv(variableName, variableType, programType,
				primitive);
		e.setInit(init);
		addProcParameter(e);

	}

	protected void addAppParameter(String variableName, String variableType,
			String programType, boolean primitive, String init)

	{
		boolean isNew = true;

		for (VariableEnv v : getParameters())
		{
			if (v.getName().equals(variableName))
			{
				// v.setVariableName(variableName);
				// v.setVariableType(variableType);
				// v.setProgramType(programType);
				// v.setPrimitive(primitive);
				v.setInit(init);

				isNew = false;
			}
		}

		if (isNew)
		{
			VariableEnv e = new VariableEnv(variableName, variableType, programType,
					primitive);
			e.setInit(init);
			addAppParameter(e);
		}

	}

	protected void addThreadParameter(String variableName, String variableType,
			String programType, boolean primitive, String init)

	{
		boolean isNew = true;

		for (VariableEnv v : getParameters())
		{
			if (v.getName().equals(variableName))
			{
				// v.setVariableName(variableName);
				// v.setVariableType(variableType);
				// v.setProgramType(programType);
				// v.setPrimitive(primitive);
				v.setInit(init);

				isNew = false;
			}
		}

		if (isNew)
		{
			VariableEnv e = new VariableEnv(variableName, variableType, programType,
					primitive);
			e.setInit(init);
			addThreadParameter(e);
		}

	}

	@SuppressWarnings("rawtypes")
	public void addSyncMeth(Name methName, TypeKind returnType,
			ArrayList<Name> returnValues, Map params)
	{
		syncMeths.add(new MethodEnv(methName, returnType, returnValues, params));
	}

	public void addMeth(MethodEnv me)
	{
		System.out.println("Adding Meth " + me.getMethodName());
		me.setSynchronised(false);
		meths.add(me);
	}

	public void addSyncMeth(MethodEnv me)
	{
		System.out.println("Adding Sync Meth " + me.toString());
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

			varMap.put(VAR_NAME, v.getName().toString());
			varMap.put(VAR_TYPE, v.getType());
			if (v.getInit() != null)
			{
				varMap.put(VAR_INIT, v.getInit().toString());
			}
			varMap.put(VAR_INPUT, v.getInput());

			returnList.add(varMap);
		}

		return returnList;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> initedVarsList()
	{
		List<Map> returnList = new ArrayList<>();

		for (VariableEnv v : variables)
		{
			if (v.getInit() != null)
			{
				if (v.getInit().toString() != "")
				{
					Map varMap = new HashMap();
					varMap.put(VAR_NAME, v.getName().toString());
					varMap.put(VAR_TYPE, v.getType());
					varMap.put(VAR_INIT, v.getInit().toString());
					varMap.put(VAR_INPUT, v.getInput());

					returnList.add(varMap);
				}
			}
		}

		return returnList;
	}

	private enum ParamsType
	{
		FW, APP, PROC
	};

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<Map> paramsList(ParamsType type)
	{
		List<Map> returnList = new ArrayList<>();
		List<VariableEnv> params;

		switch (type)
		{
			case FW:
				params = getFWParameters();
				break;
			case APP:
				params = getAppParameters();
				break;
			case PROC:
				params = getProcParameters();
				break;
			default:
				params = new ArrayList<VariableEnv>();
		}

		for (VariableEnv v : params)
		{
			Map varMap = new HashMap();
			varMap.put(VAR_NAME, v.getName().toString());
			varMap.put(VAR_TYPE, v.getType());

			varMap.put("ProgramType", v.getProgramType());

			returnList.add(varMap);
		}

		return returnList;
	}

	@SuppressWarnings({ "rawtypes" })
	public List<Map> fwParamsList()
	{
		return paramsList(ParamsType.FW);
	}

	@SuppressWarnings({ "rawtypes" })
	public List<Map> appParamsList()
	{
		return paramsList(ParamsType.APP);
	}

	@SuppressWarnings({ "rawtypes" })
	public List<Map> procParamsList()
	{
		List<Map> procParams = paramsList(ParamsType.PROC);

		return procParams;
	}

	private enum MethsType
	{
		SYNC, NOT_SYNC
	};

	@SuppressWarnings({ "rawtypes" })
	private List<Map> listMethods(MethsType type)
	{
		List<Map> returnList = new ArrayList<>();
		List<MethodEnv> methods;

		switch (type)
		{
			case NOT_SYNC:
				methods = meths;
				break;
			case SYNC:
				methods = syncMeths;
				break;
			default:
				methods = new ArrayList<MethodEnv>();
		}

		for (MethodEnv me : methods)
		{
			Map methodMap = methodToMap(me);

			returnList.add(methodMap);
		}

		return returnList;
	}

	@SuppressWarnings({ "rawtypes" })
	public List<Map> methsList()
	{
		return listMethods(MethsType.NOT_SYNC);
	}

	@SuppressWarnings({ "rawtypes" })
	public List<Map> syncMethsList()
	{
		return listMethods(MethsType.SYNC);

	}

	@SuppressWarnings({ "rawtypes" })
	protected Map methodToMap(MethodEnv me)
	{
		return me.toMap();
	}

	public List<MethodEnv> getMeths()
	{
		return meths;
	}

	public MethodEnv getConstructor()
	{
		for (MethodEnv me : getMeths())
		{
			if (me.getMethodName().equals("<init>"))
			{
				return me;
			}
		}
		return null;
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

	public void addParent(String parentName)
	{
		boolean exists = false;
		for (String s : parents)
		{
			if (s.contains(parentName))
			{
				exists = true;
			}
		}
		if (!exists)
		{
			parents.add(parentName);
		}
	}

	public List<String> getParents()
	{
		return parents;
	}

	public void addParameterInit(String paramName, String paramInit)
	{

		for (VariableEnv v : getParameters())
		{
			if (v.getName().equals(paramName))
			{
				v.setInit(paramInit);
			}
		}

	}

}

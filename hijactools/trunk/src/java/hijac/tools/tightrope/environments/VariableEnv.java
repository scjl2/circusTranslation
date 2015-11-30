package hijac.tools.tightrope.environments;

public class VariableEnv
{

	private String name;
	private String type;
	private Object init;
	private boolean input;
	private boolean primitive;
	private String programType;

	public VariableEnv()
	{
		super();

		name = "";
		type = "";
		init = "";

		programType = "";
	}

	public VariableEnv(String variableName, String variableType,
			Object variableInit, boolean primitive)
	{
		super();
		this.name = variableName;
		this.type = variableType;
		this.primitive = primitive;

		if (variableInit == null)
		{
			this.init = "init\\_placeholder";
		}
		else
		{
			this.init = variableInit;
		}

		this.input = false;
	}

	public VariableEnv(String variableName, String variableType,
			String programType, boolean primitive)
	{
		this.name = variableName;
		this.type = variableType;
		this.programType = programType;
		this.primitive = primitive;
	}

	public VariableEnv(String variableName, String variableType,
			Object variableInit, String variableInput, boolean primitive)
	{
		this(variableName, variableType, variableInit, primitive);

		this.input = true;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String variableName)
	{
		this.name = variableName;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String variableType)
	{
		this.type = variableType;
	}

	public Object getInit()
	{
		return init;
	}

	public void setInit(Object variableInit)
	{
		this.init = variableInit;
	}

	public Boolean getInput()
	{
		return input;
	}

	public boolean isPrimitive()
	{
		return primitive;
	}

	public void setPrimitive(boolean primitive)
	{
		this.primitive = primitive;
	}

	public void setInput(boolean variableInput)
	{
		this.input = variableInput;
	}

	public String getProgramType()
	{
		return programType;
	}

	public void setProgramType(String programType)
	{
		this.programType = programType;
	}

	public String toString()
	{
		return "VarEnv: name = " + name + " type = " + type
				+ " init = " + init + " input = " + input
				+ " programType = " + programType + " primitive = " + primitive;
	}
}
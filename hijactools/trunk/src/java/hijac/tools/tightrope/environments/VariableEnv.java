package hijac.tools.tightrope.environments;

public class VariableEnv
{
	private String variableName;
	private String variableType;
	private Object variableInit;
	private boolean variableInput;
	private boolean primitive;
	private String programType;

	public VariableEnv()
	{
		super();
	}

	public VariableEnv(String variableName, String variableType,
			Object variableInit, boolean primitive)
	{
		super();
		this.variableName = variableName;
		this.variableType = variableType;
		this.primitive = primitive;

		if (variableInit == null)
		{
			this.variableInit = "init\\_placeholder";
		}
		else
		{
			this.variableInit = variableInit;
		}

		this.variableInput = false;
	}

	public VariableEnv(String variableName, String variableType,
			String programType, boolean primitive)
	{
		this.variableName = variableName;
		this.variableType = variableType;
		this.programType = programType;
		this.primitive = primitive;
	}

	public VariableEnv(String variableName, String variableType,
			Object variableInit, String variableInput, boolean primitive)
	{
		this(variableName, variableType, variableInit, primitive);

		this.variableInput = true;
	}

	public String getVariableName()
	{
		return variableName;
	}

	public void setVariableName(String variableName)
	{
		this.variableName = variableName;
	}

	public String getVariableType()
	{
		return variableType;
	}

	public void setVariableType(String variableType)
	{
		this.variableType = variableType;
	}

	public Object getVariableInit()
	{
		return variableInit;
	}

	public void setVariableInit(Object variableInit)
	{
		this.variableInit = variableInit;
	}

	public Boolean getVariableInput()
	{
		return variableInput;
	}

	public boolean isPrimitive()
	{
		return primitive;
	}

	public void setPrimitive(boolean primitive)
	{
		this.primitive = primitive;
	}

	public void setVariableInput(boolean variableInput)
	{
		this.variableInput = variableInput;
	}

	public String getProgramType()
	{
		return programType;
	}

	public void setProgramType(String programType)
	{
		this.programType = programType;
	}
}
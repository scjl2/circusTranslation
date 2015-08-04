package hijac.tools.tightrope.environments;

public class VariableEnv
{
	private String variableName;
	private String variableType;
	private Object variableInit;
	private boolean variableInput;

	public VariableEnv()
	{
		super();		
	}

	public VariableEnv(String variableName, String variableType,
			Object variableInit)
	{
		super();
		this.variableName = variableName;
		this.variableType = variableType;
		if(variableInit == null)
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
			Object variableInit, String variableInput)
	{
		this(variableName, variableType, variableInit );
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

	

}
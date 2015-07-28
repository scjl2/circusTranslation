package hijac.tools.tightrope.environments;

import javax.lang.model.element.Name;
import javax.lang.model.type.TypeKind;

import com.sun.source.tree.Tree;

public class VariableEnv
{
	private String variableName;
	private String variableType;
	private Object variableInit;

	public VariableEnv()
	{
		super();
		// TODO Auto-generated constructor stub
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

}
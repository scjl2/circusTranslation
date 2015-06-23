package hijac.tools.tightrope.environments;

import javax.lang.model.element.Name;
import javax.lang.model.type.TypeKind;

public class VariableEnv
{
	private Name variableName;
	private TypeKind variableType;
	private Object variableInit;

	public VariableEnv()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public VariableEnv(Name variableName, TypeKind variableType,
			Object variableInit)
	{
		super();
		this.variableName = variableName;
		this.variableType = variableType;
		this.variableInit = variableInit;
	}

	public Name getVariableName()
	{
		return variableName;
	}

	public void setVariableName(Name variableName)
	{
		this.variableName = variableName;
	}

	public TypeKind getVariableType()
	{
		return variableType;
	}

	public void setVariableType(TypeKind variableType)
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
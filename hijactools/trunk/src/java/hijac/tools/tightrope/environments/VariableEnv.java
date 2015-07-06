package hijac.tools.tightrope.environments;

import javax.lang.model.element.Name;
import javax.lang.model.type.TypeKind;

import com.sun.source.tree.Tree;

public class VariableEnv
{
	private Name variableName;
	private Tree variableType;
	private Object variableInit;

	public VariableEnv()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public VariableEnv(Name variableName, Tree variableType,
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

	public Name getVariableName()
	{
		return variableName;
	}

	public void setVariableName(Name variableName)
	{
		this.variableName = variableName;
	}

	public Tree getVariableType()
	{
		return variableType;
	}

	public void setVariableType(Tree variableType)
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
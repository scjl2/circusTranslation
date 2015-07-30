package hijac.tools.tightrope.environments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Name;
import com.sun.source.tree.Tree;

public class ObjectEnv
{
//	ClassTree classTree;
	Name name;
	List<VariableEnv> variables;
	protected List<MethodEnv> meths;
	protected List<MethodEnv> syncMeths;

	public ObjectEnv()
	{
		variables = new ArrayList<VariableEnv>();
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
			Object variableInit)
	{
		variables.add(new VariableEnv(variableName, variableType, variableInit));		
	}
	
	public VariableEnv getVariable(Name name)
	{
		return this.getVariable(name.toString());
	}
	
	public VariableEnv getVariable(String name)
	{
		for(VariableEnv v : variables)
		{
			if(v.getVariableName().equals(name))
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
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> varsList()
	{
		List<Map> returnList = new ArrayList<>();
		
		for(VariableEnv v : variables)
		{
			Map varMap = new HashMap();
			varMap.put("VarName", v.getVariableName().toString());
			varMap.put("VarType", v.getVariableType());
			varMap.put("VarInit", v.getVariableInit().toString());
			
			returnList.add(varMap);
		}
		
		return returnList;
	}

	public void addVariableInit(Name varName, Tree init)
	{
		for(VariableEnv varEnv : getVariables())
		{
			if(varEnv.getVariableName().contentEquals(varName))
			{
				varEnv.setVariableInit(init);
			}
		}
		
	}

//	public ClassTree getClassTree()
//	{
//		return classTree;
//	}
//
//	public void setClassTree(ClassTree classTree)
//	{
//		this.classTree = classTree;
//	}
	
	
	

}

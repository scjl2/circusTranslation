package hijac.tools.tightrope.environments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Name;
import javax.lang.model.type.TypeKind;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;

public class ObjectEnv
{
//	ClassTree classTree;
	Name name;
	List<VariableEnv> variables;
	List<MethodTree> meths;

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

	public void addVariable(Name variableName, TypeKind variableType,
			Object variableInit)
	{
		variables.add(new VariableEnv(variableName, variableType, variableInit));		
	}
	
	public List<VariableEnv> getVariables()
	{
		return variables;
	}
	
	public List<Map> varsList()
	{
		List<Map> returnList = new ArrayList<>();
		
		for(VariableEnv v : variables)
		{
			Map varMap = new HashMap();
			varMap.put("VarName", v.getVariableName());
			varMap.put("VarType", v.getVariableType());
			varMap.put("VarInit", v.getVariableInit());
		}
		
		return returnList;
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

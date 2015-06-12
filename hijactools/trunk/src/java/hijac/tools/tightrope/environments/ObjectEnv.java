package hijac.tools.tightrope.environments;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Name;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;

public class ObjectEnv
{
//	ClassTree classTree;
	Name name;
	List<Name> vars;
	List<MethodTree> meths;

	public ObjectEnv()
	{
		vars = new ArrayList<Name>();
	}

	public Name getName()
	{
		return name;
	}
	
	public void setName(Name safelet)
	{
		this.name = safelet;
	}

	public List<Name> getVars()
	{
		return vars;
	}

	public void addVar(Name var)
	{
		vars.add(var);
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

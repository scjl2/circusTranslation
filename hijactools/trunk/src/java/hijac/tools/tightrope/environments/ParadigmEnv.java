package hijac.tools.tightrope.environments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Name;

import com.sun.source.tree.MethodTree;


public class ParadigmEnv extends ObjectEnv
{

	List<Name> nonPMeths;
	List<MethodTree> meths;
	
	

	public ParadigmEnv()
	{
		super();
		nonPMeths = new ArrayList<Name>();
		meths = new ArrayList<MethodTree>();
	}

	public List<Name> getNonPMeths()
	{
		return nonPMeths;
	}

	public void addNonPMeth(Name nonPMeth)
	{
		nonPMeths.add(nonPMeth);
	}

	public List<MethodTree> getMeths()
	{
		return meths;
	}

	public void addMeth(MethodTree meth)
	{
		meths.add(meth);
	}

	
	
	
	
	
	
	



	// public Name getmethodName(String methodName)
	// {
	// for(Name n : meths)
	// {
	// if (n.contentEquals(methodName))
	// {
	// return n;
	// }
	// }
	// return null;
	// }

}

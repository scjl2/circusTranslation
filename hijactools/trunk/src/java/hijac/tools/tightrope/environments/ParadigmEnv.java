package hijac.tools.tightrope.environments;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Name;

import com.sun.source.tree.MethodTree;


public class ParadigmEnv extends ObjectEnv
{	
	List<MethodTree> meths;
	List<Name> syncMeths;	

	public ParadigmEnv()
	{
		super();
//		nonPMeths = new ArrayList<Name>();
		meths = new ArrayList<MethodTree>();
		syncMeths = new ArrayList<Name>();
	}

//	public List<Name> getNonPMeths()
//	{
//		return nonPMeths;
//	}
//
//	public void addNonPMeth(Name nonPMeth)
//	{
//		nonPMeths.add(nonPMeth);
//	}

	public List<MethodTree> getMeths()
	{
		return meths;
	}

	public void addMeth(MethodTree meth)
	{
		meths.add(meth);
	}

	public void addSyncMeth(Name name)
	{
		syncMeths.add(name);		
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

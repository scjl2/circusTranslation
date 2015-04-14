package hijac.tools.tightrope.environments;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Name;

public class ParadigmEnv extends ObjectEnv
{

	List<Name> nonPMeths;
	List<Name> meths;

	public ParadigmEnv()
	{
		super();
		nonPMeths = new ArrayList<Name>();
		meths = new ArrayList<Name>();
	}

	public List<Name> getNonPMeths()
	{
		return nonPMeths;
	}

	public void addNonPMeth(Name nonPMeth)
	{
		nonPMeths.add(nonPMeth);
	}

	public List<Name> getMeths()
	{
		return meths;
	}

	public void addMeth(Name meth)
	{
		meths.add(meth);
	}
	
	public Name getmethodName(String methodName)
	{
		for(Name n : meths)
		{
			if (n.contentEquals(methodName))
			{
				return n;
			}
		}
		return null;
	}
	
	

}

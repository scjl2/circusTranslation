package hijac.tools.tightrope.environments;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Name;

public class NonParadigmEnv extends ObjectEnv
{
	
	List<Name> nonPMeths;

	public NonParadigmEnv(Name name, List<Name> vars, List<Name> nonPMeths)
	{
		super();
		nonPMeths = new ArrayList<Name>();
	}

	public List<Name> getNonPMeths()
	{
		return nonPMeths;
	}

	public void addNonPMeth(Name nonPMeth)
	{
		nonPMeths.add(nonPMeth);
	}
	
	


}

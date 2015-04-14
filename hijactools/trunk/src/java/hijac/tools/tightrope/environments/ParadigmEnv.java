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
	public void setNonPMeths(List<Name> nonPMeths)
	{
		this.nonPMeths = nonPMeths;
	}
	public List<Name> getMeths()
	{
		return meths;
	}
	public void setMeths(List<Name> meths)
	{
		this.meths = meths;
	}
	


}

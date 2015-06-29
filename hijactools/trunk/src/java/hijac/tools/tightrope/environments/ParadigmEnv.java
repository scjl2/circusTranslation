package hijac.tools.tightrope.environments;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Name;
import javax.lang.model.type.TypeKind;

import com.sun.source.tree.MethodTree;

public class ParadigmEnv extends ObjectEnv
{
	public ParadigmEnv()
	{
		super();
		// nonPMeths = new ArrayList<Name>();
		meths = new ArrayList<MethodEnv>();
		syncMeths = new ArrayList<MethodEnv>();
		
	}

	// public List<Name> getNonPMeths()
	// {
	// return nonPMeths;
	// }
	//
	// public void addNonPMeth(Name nonPMeth)
	// {
	// nonPMeths.add(nonPMeth);
	// }

	
	
	public List<MethodEnv> getMeths()
	{
		return meths;
	}

	public void addMeth(Name meth)
	{
		meths.add(new MethodEnv(name));
	}

	public void addSyncMeth(Name name, TypeKind returnType, ArrayList<Name> returnValues ,Map params)
	{
		syncMeths.add(new MethodEnv(name, returnType, returnValues, params));
	}
	
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> methsList()
	{
		List<Map> returnList = new ArrayList<>();

		// Map<String, String> returnMap = new HashMap<String, String>();

		for (MethodEnv me : meths)
		{
			Map methodMap = new HashMap();
			methodMap.put("methodName", me.getMethodName().toString());
			methodMap.put("returnType", me.getReturnType());
			methodMap.put("parameters", me.getParameters());

			returnList.add(methodMap);
		}

		return returnList;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> syncMethsList()
	{
		List<Map> returnList = new ArrayList<>();

		// Map<String, String> returnMap = new HashMap<String, String>();

		for (MethodEnv me : syncMeths)
		{
			Map methodMap = new HashMap();
			methodMap.put("methodName", me.getMethodName().toString());
			methodMap.put("returnType", me.getReturnType());
			methodMap.put("returnValue", me.getReturnValue());
			methodMap.put("parameters", me.getParameters());

			returnList.add(methodMap);
		}

		return returnList;
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

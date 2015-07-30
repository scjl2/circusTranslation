package hijac.tools.tightrope.environments;

import hijac.tools.tightrope.environments.EnvironmentBuilder.ClassEnv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Name;
import javax.lang.model.type.TypeKind;

public class ParadigmEnv extends ObjectEnv
{
	private ClassEnv classEnv;

	public ParadigmEnv()
	{
		super();

		meths = new ArrayList<MethodEnv>();
		syncMeths = new ArrayList<MethodEnv>();

	}

	public List<MethodEnv> getMeths()
	{
		return meths;
	}

	@SuppressWarnings("rawtypes")
	public void addMeth(Name methName, TypeKind returnType,
			ArrayList<Name> returnValues, Map params)
	{
		meths.add(new MethodEnv(methName, returnType, returnValues, params));
	}

	@SuppressWarnings("rawtypes")
	public void addSyncMeth(Name methName, TypeKind returnType,
			ArrayList<Name> returnValues, Map params)
	{
		syncMeths
				.add(new MethodEnv(methName, returnType, returnValues, params));
	}

	public void addMeth(MethodEnv me)
	{
		meths.add(me);
	}

	public void addSyncMeth(MethodEnv me)
	{		
		syncMeths.add(me);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> methsList()
	{
		List<Map> returnList = new ArrayList<>();

		for (MethodEnv me : meths)
		{
			Map methodMap = new HashMap();
			methodMap.put("methodName", me.getMethodName().toString());
			methodMap.put("returnType", me.getReturnType());
			methodMap.put("parameters", me.getParameters());
			methodMap.put("body", me.getBody());
			methodMap.put("access", me.getAccessString());

			returnList.add(methodMap);
		}

		return returnList;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> syncMethsList()
	{
		List<Map> returnList = new ArrayList<>();

		for (MethodEnv me : syncMeths)
		{
			Map methodMap = new HashMap();
			methodMap.put("methodName", me.getMethodName().toString());
			methodMap.put("returnType", me.getReturnType());
			methodMap.put("returnValue", me.getReturnValue());
			methodMap.put("parameters", me.getParameters());
			methodMap.put("access", me.getAccessString());
			methodMap.put("body", me.getBody());
			returnList.add(methodMap);
		}

		return returnList;
	}

	public void addClassEnv(ClassEnv classEnv)
	{
		this.classEnv = classEnv;
	}

	public ClassEnv getClassEnv()
	{
		return classEnv;
	}
}

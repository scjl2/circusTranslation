package hijac.tools.tightrope.environments;

import hijac.tools.tightrope.utils.TightRopeString.Name;

import java.util.Map;

public abstract class EventHandlerEnv extends ParadigmEnv
{

	public abstract String getHandlerType();

	public abstract String getImportName();

	public static final String HANDLE_ASYNC = "HandleAsync";

	private MethodEnv handleAsync;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map toMap()
	{
		Map map = super.toMap();

		map.put(HANDLER_TYPE, getHandlerType());
		map.put(IMPORT_NAME, getImportName());

		Map handleAsyncMap = handleAsync.toMap();
		map.put(HANDLE_ASYNC, handleAsyncMap);

		return map;
	}

	public MethodEnv getHandleAsync()
	{
		return handleAsync;
	}

	public void addHandleAsyncMethod(MethodEnv method)
	{
		handleAsync = method;
	}

	@Override
	public void setId(String name)
	{
		super.setId(name + Name.SID);
		// TODO THis will need to be changed when I streamline the S+S sections
//		setObjectId(name);
		setThreadID(name);
	}
}
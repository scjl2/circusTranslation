package hijac.tools.tightrope.environments;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OneShotEventHandlerEnv extends EventHandlerEnv
{
	private static final String HANDLER_TYPE = "oneShot";
	private static final String IMPORT_NAME = "OneShot";
	// TODO remove this stop-gap in favour of using the API
	private static final List<MethodEnv> EVENT_HANDLER_API_METHODS = new ArrayList<MethodEnv>();

	public OneShotEventHandlerEnv()
	{
		EVENT_HANDLER_API_METHODS
				.add(new MethodEnv("deschedule", "void", true));
		EVENT_HANDLER_API_METHODS.add(new MethodEnv("scheduleNextReleaseTime",
				"void", true));
	}

	@Override
	public String getHandlerType()
	{
		return HANDLER_TYPE;
	}

	@Override
	public String getImportName()
	{
		return IMPORT_NAME;
	}

	@Override
	public List<MethodEnv> getAllMeths()
	{
		List<MethodEnv> osehMeths = new ArrayList<MethodEnv>();						
				
		osehMeths.addAll(super.getAllMeths());
		osehMeths.addAll(EVENT_HANDLER_API_METHODS);

		return osehMeths;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public List<Map> methsList()
	{
		List<Map> osehMethodsList = super.methsList();

//		for (MethodEnv me : EVENT_HANDLER_API_METHODS)
//		{
//			Map methodMap = methodToMap(me);
//
//			apehMethodsList.add(methodMap);
//		}

		return osehMethodsList;
	}

}

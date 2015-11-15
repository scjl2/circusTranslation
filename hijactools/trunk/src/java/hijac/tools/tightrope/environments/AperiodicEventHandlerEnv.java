package hijac.tools.tightrope.environments;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AperiodicEventHandlerEnv extends EventHandlerEnv
{

	private static final String IMPORT_NAME = "Aperiodic";
	private static final String APERIODIC_HANDLER_TYPE = "aperiodic";
	private static final String APERIODIC_LONG_HANDLER_TYPE = "aperiodicLong";
	
	private enum HandlerType {aperiodic, aperiodicLong};
	private HandlerType handlerType;
	
	// TODO remove this stop-gap in favour of using the API
	private final List<MethodEnv> EVENT_HANDLER_API_METHODS = new ArrayList<MethodEnv>();

	public AperiodicEventHandlerEnv()
	{
		EVENT_HANDLER_API_METHODS.add(new MethodEnv("release", "void", true));
	}

	@Override
	public String getHandlerType()
	{
		if(handlerType ==  HandlerType.aperiodic)
		{
			return APERIODIC_HANDLER_TYPE;
		}
		else
			if (handlerType ==  HandlerType.aperiodicLong)
			{
				return APERIODIC_LONG_HANDLER_TYPE;
			}
		return null;
	}

	@Override
	public String getImportName()
	{
		return IMPORT_NAME;
	}

	@Override
	public List<MethodEnv> getMeths()
	{
		List<MethodEnv> apehMeths = new ArrayList<MethodEnv>();
		
		apehMeths.addAll(super.getMeths());
		apehMeths.addAll(EVENT_HANDLER_API_METHODS);

		return apehMeths;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public List<Map> methsList()
	{
		List<Map> apehMethodsList = super.methsList();

//		for (MethodEnv me : EVENT_HANDLER_API_METHODS)
//		{
//			Map methodMap = methodToMap(me);
//
//			apehMethodsList.add(methodMap);
//		}

		return apehMethodsList;
	}

}

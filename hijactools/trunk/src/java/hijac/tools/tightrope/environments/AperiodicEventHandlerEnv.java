package hijac.tools.tightrope.environments;

public class AperiodicEventHandlerEnv extends EventHandlerEnv
{

	private static final String IMPORT_NAME = "Aperiodic";
	private static final String HANDLER_TYPE = "aperiodic";

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

}

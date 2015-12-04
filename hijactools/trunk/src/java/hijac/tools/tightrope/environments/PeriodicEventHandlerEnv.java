package hijac.tools.tightrope.environments;

public class PeriodicEventHandlerEnv extends EventHandlerEnv
{
	private static final String HANDLER_TYPE = "periodic";
	private static final String IMPORT_NAME = "Periodic";

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
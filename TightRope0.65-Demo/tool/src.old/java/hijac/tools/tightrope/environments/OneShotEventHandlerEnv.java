package hijac.tools.tightrope.environments;

public class OneShotEventHandlerEnv extends EventHandlerEnv
{
	private static final String HANDLER_TYPE =	"oneShot";
	private static final String IMPORT_NAME = 	"OneShot";
	
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

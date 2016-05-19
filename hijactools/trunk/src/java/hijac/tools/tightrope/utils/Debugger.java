package hijac.tools.tightrope.utils;

public class Debugger
{

	public static boolean enabled;

	public static void setEnabled(boolean isEnabled)
	{
		enabled = isEnabled;
	}

	public static boolean isEnabled()
	{
		return enabled;
	}

	public static void log(Object o)
	{
		if (Debugger.isEnabled())
		{
			System.out.println(o.toString());
		}
	}
}

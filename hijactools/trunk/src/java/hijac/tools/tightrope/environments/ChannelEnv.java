package hijac.tools.tightrope.environments;

public class ChannelEnv
{
<<<<<<< HEAD
	String channelName;
	String channelType;

	public ChannelEnv()
	{
		super();
		// TODO Auto-generated constructor stub
	}
=======
	private String channelName;
	private String channelType;
>>>>>>> RULES

	public ChannelEnv(String channelName, String channelType)
	{
		super();
		this.channelName = channelName;
		this.channelType = channelType;
	}

	public String getChannelName()
	{
		return channelName;
	}

	public void setChannelName(String channelName)
	{
		this.channelName = channelName;
	}

	public String getChannelType()
	{
		return channelType;
	}

	public void setChannelType(String channelType)
	{
		this.channelType = channelType;
	}
}

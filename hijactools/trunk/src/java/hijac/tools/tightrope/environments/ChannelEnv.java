package hijac.tools.tightrope.environments;

public class ChannelEnv
{

	private String channelName;
	private String channelType;


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

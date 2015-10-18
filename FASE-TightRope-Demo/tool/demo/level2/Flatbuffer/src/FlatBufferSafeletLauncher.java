package scjlevel2examples.flatbuffer;

import javax.safetycritical.*;



import devices.Console;

//Application entry point, runs the Safelet
public class FlatBufferSafeletLauncher extends Launcher	
{

	public FlatBufferSafeletLauncher(Safelet<Mission> safelet)
	{
		super(safelet, 2);
	}

	public static void main(String[] args)
	{
		Console.println("FlatBufferSafeletExecuter ");
		// Run the safelet which starts the whole application
		new FlatBufferSafeletLauncher(new FlatBuffer()).run();
		
		//new LaunchLevel2(new FlatBuffer()).run();
	}
}

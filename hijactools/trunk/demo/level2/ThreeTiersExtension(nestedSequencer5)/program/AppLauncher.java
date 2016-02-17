package nestedSequencer5;

import javax.safetycritical.LaunchLevel2;

import devices.Console;


//Application entry point, runs the Safelet
public class AppLauncher
{
	public static void main(String[] args)
	{
		Console.println("App Launcher ");
				
		new LaunchLevel2(new MyApp());		
		
		Console.println("AppLauncher Finished");
	}
}
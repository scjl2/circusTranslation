package hijac.tools.application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import hijac.tools.compiler.SCJCompilationConfig;
import hijac.tools.config.Config;
import hijac.tools.tightrope.gui.TightRopeFrame;

public class TightRopeGUI
{
	public static void main(String[] args) throws IOException 
	{
		 TightRope.init();
		 
		 TightRopeFrame gui = new TightRopeFrame();
			
			SCJCompilationConfig config = TightRope.intiConfig();
			
			String[] scjSrcList = Config.getSCJSrc();
			//Assuming there is only one...
			
			gui.populateTextFeilds(Config.getRTSJLib(),Config.getSCJLib(),scjSrcList[0]);
			
			gui.displayFrame();
			
			
			List<File> classpath = new ArrayList<File>();
			String text = gui.getRtsjLibText();
			if(! text.isEmpty())
			{
				classpath.add(new File(text));
			}
			
			text = gui.getScjLibText();
			
			if (! text.isEmpty())
			{
				classpath.add(new File(text));
			}
			
			List<File> sourcepath = new ArrayList<File>();
			sourcepath.add(new File(gui.getProgramSourceText()));			
			
			config = new  SCJCompilationConfig(classpath, sourcepath);
			
			
			
			translate(config);

//			TightRope.exit();
		
		
		
		
		
	}

	public static void translate(SCJCompilationConfig config) throws IOException,
			FileNotFoundException
	{
		TightRope.compilePhase(config);

		TightRope.buildPhase();

		TightRope.generatePhase();
	}
}
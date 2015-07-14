package hijac.tools.application;

import hijac.tools.analysis.SCJAnalysis;
import static java.nio.file.StandardCopyOption.*;
import hijac.tools.compiler.SCJCompilationConfig;
import hijac.tools.compiler.SCJCompilationException;
import hijac.tools.compiler.SCJCompilationTask;
import hijac.tools.config.Config;
import hijac.tools.config.Statics;

import hijac.tools.application.UncaughtExceptionHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import hijac.tools.tightrope.environments.EnvironmentBuilder;
import hijac.tools.tightrope.environments.ProgramEnv;
import hijac.tools.tightrope.generators.CircusGenerator;

public class TightRopeTest
{
	public static SCJAnalysis ANALYSIS;
	
	private static ProgramEnv programEnv;

	private static final String VERSION = "v0.5";
	private static final boolean SILENT = true;
	private static final boolean LATEX = true;
	private static final boolean FREEMARKER = true;

	private static String customName ="";
	
	static
	{
		Statics.kickstart();
	}

	protected static void setUncaughtExceptionHandler()
	{
		Thread.currentThread().setUncaughtExceptionHandler(
				new UncaughtExceptionHandler());
	}

	public static void main(String[] args) throws IOException
	{

		final long startTime = System.nanoTime();
		
		System.out.println();
		System.out.println("+++ Tight Rope - SCJ to Circus translator +++");
		System.out.println("+++ " + VERSION + " +++");
		System.out.println("+++ Matt Luckcuck +++");
		System.out.println();

		setUncaughtExceptionHandler();
		
		setCustomName();

		SCJCompilationConfig config = SCJCompilationConfig.getDefault();
		
		SCJCompilationTask compiler = new SCJCompilationTask(config);

		System.out.println("Compiling SCJ sources...");

		try
		{
			ANALYSIS = SCJAnalysis.create(compiler);
		} catch (SCJCompilationException e)
		{
			System.out.println("Failed to compile...");
			e.getDiagnostics().print(System.out);
			System.exit(-1);
		}

		EnvironmentBuilder environmentBuilder = new EnvironmentBuilder(ANALYSIS);

		programEnv = environmentBuilder.explore();

		System.out.println();
		System.out.println("+++ Structure +++ ");
		programEnv.output();
		
		System.out.println("Network = " + programEnv.geNetworkMap());
		
		if(FREEMARKER)
		{
			CircusGenerator circGen = new CircusGenerator(customName, programEnv);
			circGen.translate();
		}

		if(LATEX)
		{
			runLatex(customName);
		}

		final long duration = System.nanoTime() - startTime;

		System.out.println();
		System.out.println("+++ Finished +++");
		System.out.println("Total Time: " + duration / 1000000000 + "."
				+ (duration % 1000000000) + " seconds ");
		System.out.println(duration + " nanoseconds");

		System.exit(0);
	}

	private static void setCustomName()
	{
		String[] source = Config.getSCJSrc();
		String sourceString ="";
		if(source.length == 1)
		{
			String[] srouceSplit = source[0].split("/");
			sourceString=srouceSplit[srouceSplit.length-2];
		}
		else
		{
			System.out.println("*** CircusGenerator: multiple sources ***");
		}
		
		
		
		customName=sourceString;
	}

	/** 
	 * Generate the report and styles.
	 * Then run PDFLatex on them
	 * 
	 * @param customName - the name of the program being translated
	 */	 
	private static void runLatex(String customName)
	{
		try
		{
			String s = null;

			/*
			 * This executes pdflatex with the output directory of our
			 * translation on the latex file we've just written
			 */
			System.out.println("+++ Generating PDF +++");

			String latexLocation = "/usr/bin/pdflatex";
			String outputDirectory = "/home/matt/Documents/Translation/test/output/"+customName+"/";
			String reportLocation = "/home/matt/Documents/Translation/test/output/"+customName+"/"+customName+"-Report.tex";
			String space = " ";

			
			Path source = Paths.get("/home/matt/Documents/Translation/test/packages/circus.sty");
			Path destination = Paths.get("/home/matt/Documents/Translation/test/output/"+customName+"/circus.sty");
			
			Files.copy(source, destination, REPLACE_EXISTING);
			
			source = Paths.get("/home/matt/Documents/Translation/test/packages/czt.sty");
			destination = Paths.get("/home/matt/Documents/Translation/test/output/"+customName+"/czt.sty");
			
			Files.copy(source, destination, REPLACE_EXISTING);
			
			source = Paths.get("/home/matt/Documents/Translation/test/packages/hijac.sty");
			destination = Paths.get("/home/matt/Documents/Translation/test/output/"+customName+"/hijac.sty");
			
			Files.copy(source, destination, REPLACE_EXISTING);
			
			
			
			
			Process p = Runtime.getRuntime().exec(latexLocation + space + "-output-directory" + space
							+ outputDirectory + space + reportLocation);

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(
					p.getInputStream()));

			BufferedReader stdError = new BufferedReader(new InputStreamReader(
					p.getErrorStream()));

			if (!SILENT)
			{
				// read the output from the command
				System.out
						.println("Here is the standard output of the command:\n");
				while ((s = stdInput.readLine()) != null)
				{
					System.out.println(s);
				}
			

			
				// read any errors from the attempted command
				System.out
						.println("Here is the standard error of the command (if any):\n");
				while ((s = stdError.readLine()) != null)
				{
					System.out.println(s);
				}
			}

		} catch (Exception e)
		{
			System.out.println("exception happened - here's what I know: ");
			e.printStackTrace();
			System.exit(-1);
		}
	}
}

package hijac.tools.application;

import hijac.tools.analysis.SCJAnalysis;
import hijac.tools.compiler.SCJCompilationConfig;
import hijac.tools.compiler.SCJCompilationException;
import hijac.tools.compiler.SCJCompilationTask;
import hijac.tools.config.Statics;

import hijac.tools.application.UncaughtExceptionHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import hijac.tools.tightrope.environments.EnvironmentBuilder;
import hijac.tools.tightrope.environments.ProgramEnv;
import hijac.tools.tightrope.generators.CircusGenerator;

public class TightRopeTest
{
	public static SCJAnalysis ANALYSIS;
	//
	// private static Trees trees;
	// private static Set<CompilationUnitTree> units;
	// private static Set<TypeElement> type_elements;

	private static ProgramEnv programEnv;

	private static String version = "v0.4";

	private static boolean silent = true;
	private static boolean latex = false;
	private static boolean freemarker = false;

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
		System.out.println("+++" + version + "+++");
		System.out.println("+++ Matt Luckcuck +++");
		System.out.println();

		setUncaughtExceptionHandler();

		SCJCompilationTask compiler = new SCJCompilationTask(
				SCJCompilationConfig.getDefault());

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

		System.out.println("+++ Structure +++ ");
		programEnv.output();
		
		System.out.println("Network = " + programEnv.geNetworkMap());
		
		if(freemarker)
		{
			CircusGenerator circGen = new CircusGenerator(programEnv);
			circGen.translate();
		}

		if(latex)
		{
			runLatex();
		}

		final long duration = System.nanoTime() - startTime;

		System.out.println();
		System.out.println("+++ Finished +++");
		System.out.println("Total Time: " + duration / 1000000000 + "."
				+ (duration % 1000000000) + " seconds ");
		System.out.println(duration + " nanoseconds");

		System.exit(0);
	}

	public static void runLatex()
	{
		/*
		 * +++++++++++++++++++++++++++ Execute pdflatex on the output
		 * ++++++++++++++++++++++++++
		 */
		try
		{
			String s = null;

			/*
			 * This executes pdflatex with the output directory of our
			 * translation on the latex file we've just written
			 */
			System.out.println("+++ Generating PDF +++");

			String latexLocation = "/usr/bin/pdflatex";
			String outputDirectory = "/home/matt/Documents/Translation/test/output/";
			String reportLocation = "/home/matt/Documents/Translation/test/output/Report.tex";
			String space = " ";

			Process p = Runtime.getRuntime().exec(
			// "/usr/bin/pdflatex -output-directory /home/matt/Documents/Translation/test/output/ /home/matt/Documents/Translation/test/output/Report.tex");
					latexLocation + space + "-output-directory" + space
							+ outputDirectory + space + reportLocation);

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(
					p.getInputStream()));

			BufferedReader stdError = new BufferedReader(new InputStreamReader(
					p.getErrorStream()));

			if (!silent)
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

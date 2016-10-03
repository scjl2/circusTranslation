package hijac.tools.application;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import hijac.tools.analysis.SCJAnalysis;
import hijac.tools.compiler.SCJCompilationConfig;
import hijac.tools.compiler.SCJCompilationException;
import hijac.tools.compiler.SCJCompilationTask;
import hijac.tools.config.Statics;
import hijac.tools.tightrope.builders.EnvironmentBuilder;
import hijac.tools.tightrope.environments.ProgramEnv;
import hijac.tools.tightrope.generators.CircusGenerator;
import hijac.tools.tightrope.generators.NewSCJApplication;
import hijac.tools.tightrope.utils.Debugger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TightRope
{
	private static final String VERSION = "v0.7";
	private static final boolean QUIET_LATEX_OUTPUT = true;
	private static final boolean RUN_LATEX = true;
	private static final boolean RUN_FREEMARKER = true;
	private static final boolean USE_ANNOTATIONS = false;
	private static final boolean DEBUG_OUTPUT = true;

	public static SCJAnalysis ANALYSIS;
	private static EnvironmentBuilder environmentBuilder = null;
	private static ProgramEnv programEnv;
	private static NewSCJApplication scjApplication;

	private static String programName = "";
	private static long startTime;

	static
	{
		Statics.kickstart();
	}

	/**
	 * Main, for the command line version. Simply accepts the locations in the config file and generates
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException
	{

		 init();
		
		SCJCompilationConfig config = intiConfig();
		
		compilePhase(config);

		buildPhase();

		generatePhase();

		exit();
	}

	public static void init()
	{
		startTime = System.nanoTime();
		
		Debugger.setEnabled(DEBUG_OUTPUT);

		System.out.println();
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("+++ Tight Rope - SCJ to Circus translator +++");
		System.out.println("+++++++++++++++++++ " + VERSION + " +++++++++++++++++++");
		System.out.println("+++++++++++++++ Matt Luckcuck +++++++++++++++");
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println();

		setUncaughtExceptionHandler();
	}

	public static SCJCompilationConfig intiConfig()
	{		
		return SCJCompilationConfig.getDefault();
	}

	public static void compilePhase(SCJCompilationConfig config) throws IOException
	{
		System.out.println("+++++++++++++++++++++");
		System.out.println("+++ Compile Phase +++");
		System.out.println("+++++++++++++++++++++");
		
		SCJCompilationTask compiler = new SCJCompilationTask(config);	
	
		try
		{
			ANALYSIS = SCJAnalysis.create(compiler);
		}
		catch (SCJCompilationException e)
		{
			System.out.println("Failed to compile...");
			e.getDiagnostics().print(System.out);
			System.exit(-1);
		}
	}

	public static void buildPhase()
	{
		System.out.println("+++++++++++++++++++");
		System.out.println("+++ Build Phase +++");
		System.out.println("+++++++++++++++++++");
		
		scjApplication = new NewSCJApplication(ANALYSIS);
		environmentBuilder = new EnvironmentBuilder(ANALYSIS);
	
		programEnv = environmentBuilder.explore();
		
		setProgramName(environmentBuilder.getProgramName());
	
		System.out.println();
		System.out.println("+++ Structure +++ ");
		System.out.println();
		System.out.println("+++ Program Name: " + programName + " +++");
		programEnv.output();
	
		System.out.println("Network = " + programEnv.geNetworkMap());
	}

	public static void generatePhase() throws IOException, FileNotFoundException
	{
		if (RUN_FREEMARKER)
		{
			System.out.println("++++++++++++++++++++++");
			System.out.println("+++ Generate Phase +++");
			System.out.println("++++++++++++++++++++++");
			runFreemarker();
		}
	
		if (RUN_LATEX)
		{
			runLatex(programName);
		}
		
		final long duration = System.nanoTime() - startTime;
		
		System.out.println();
		System.out.println("+++ Finished +++");
		System.out.println("Total Time: " + duration / 1000000000 + "."
				+ (duration % 1000000000) + " seconds ");
		System.out.println(duration + " nanoseconds");
	}

	public static void exit()
	{
		
	
		System.exit(0);
	}

	private static void runFreemarker() throws IOException,
			FileNotFoundException
	{
		CircusGenerator circGen = new CircusGenerator(programName, programEnv);
		circGen.generate();
	}

	private static void setProgramName(String programName)
	{
		TightRope.programName = programName;
	}

	/**
	 * Generate the report and styles. Then run PDFLatex on them
	 * 
	 * @param customName
	 *            - the name of the program being translated
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
			String outputDirectory = "../output/" + customName + "/";
			String reportLocation = "../output/" + customName + "/"
					+ customName + "-Report.tex";
			String space = " ";

			File dir = new File("../output/" + customName);
			dir.mkdir();

			Path source = Paths.get("../packages/circus.sty");
			Path destination = Paths.get("../output/" + customName
					+ "/circus.sty");

			Files.copy(source, destination, REPLACE_EXISTING);

			source = Paths.get("../packages/czt.sty");
			destination = Paths.get("../output/" + customName + "/czt.sty");

			Files.copy(source, destination, REPLACE_EXISTING);

			source = Paths.get("../packages/hijac.sty");
			destination = Paths.get("../output/" + customName + "/hijac.sty");

			Files.copy(source, destination, REPLACE_EXISTING);

			Process p = Runtime.getRuntime().exec(
					latexLocation + space + "-output-directory" + space
							+ outputDirectory + space + reportLocation);

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(
					p.getInputStream()));

			BufferedReader stdError = new BufferedReader(new InputStreamReader(
					p.getErrorStream()));

			if (!QUIET_LATEX_OUTPUT)
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
			else
			{
				if (stdError.readLine() != null)
				{
					System.out.println();
					System.out.println("+++ Latex error! +++");
					System.out.println();
				}
				else
				{
					System.out.println();
					System.out.println("+++ Latex complete +++");
					System.out.println();
				}
			}

		}
		catch (Exception e)
		{
			System.out.println("An exception happened - here's what I know: ");
			e.printStackTrace();
			System.exit(-1);
		}
	}

	protected static void setUncaughtExceptionHandler()
	{
		Thread.currentThread().setUncaughtExceptionHandler(
				new UncaughtExceptionHandler());
	}

	public static ProgramEnv getProgramEnv()
	{
		return environmentBuilder.getProgramEnv();
	}

	public static NewSCJApplication getSCJApplication()
	{
		return scjApplication;
	}

	public static String getVersion()
	{
		return VERSION;
	}

	public static boolean useAnnotations()
	{
		return USE_ANNOTATIONS;
	}
}

package hijac.tools.application;

import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import javax.lang.model.element.TypeElement;
import hijac.tools.analysis.SCJAnalysis;
import hijac.tools.compiler.SCJCompilationConfig;
import hijac.tools.compiler.SCJCompilationException;
import hijac.tools.compiler.SCJCompilationTask;
import hijac.tools.config.Statics;

import hijac.tools.application.UncaughtExceptionHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import hijac.tools.tightrope.environments.EnvironmentBuilder;
import hijac.tools.tightrope.environments.ProgramEnv;
import java.util.Map;
import java.util.Set;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.Trees;

public class TightRopeTest
{
	public static SCJAnalysis ANALYSIS;

	private static Trees trees;
	private static Set<CompilationUnitTree> units;
	private static Set<TypeElement> type_elements;

	private static ProgramEnv programEnv;
	
	

	static
	{
		Statics.kickstart();
	}

	// private static HashMap<String, Name> framework = new HashMap<String,
	// Name>();

	

	protected static void setUncaughtExceptionHandler()
	{
		Thread.currentThread().setUncaughtExceptionHandler(
				new UncaughtExceptionHandler());
	}

	/* Main Program */

	public static void main(String[] args) throws IOException
	{
		
		final long startTime = System.nanoTime();
		
		
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
				
				
				
		System.out.println("Framework Printing");
		programEnv.output();
		
		
//		programEnv.getMethod("getSequencer");
		
		
//		  translateLatexAndExit();
		final long duration = System.nanoTime() - startTime;
		
		System.out.println("Total Time: " + duration/1000000000+ "." + (duration % 1000000000)  + " seconds "   );
		System.out.println(duration + " nanoseconds");
		
			
		
		System.exit(0);
	}

	private static void translateLatexAndExit() throws IOException,
			FileNotFoundException
	{
		/* ------------------------------------------------------------------------ */    
        /* You should do this ONLY ONCE in the whole application life-cycle:        */    
    
        /* Create and adjust the configuration singleton */
        freemarker.template.Configuration cfg = new freemarker.template.Configuration();
//        cfg.setDirectoryForTemplateLoading(new File("/home/matt/Uni/Translation/test/templates"));
        cfg.setDirectoryForTemplateLoading(new File("/home/matt/Documents/Translation/test/templates"));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);

        /* ------------------------------------------------------------------------ */    
        

        /* Create a data-model */
        Map root = programEnv.getSafelet().toMap();
//        root.put("author", "Matt");      
//        root.put("title", "Matt's Awesome PDF");
        

        /* Get the template (uses cache internally) */
        freemarker.template.Template temp = cfg.getTemplate("SafeletApp-Template.ftl");

        
        
        /* Variables for writing output to a file */
        File file = new File("/home/matt/Documents/Translation/test/output/SafeletApp.circus");
        FileOutputStream fop = new FileOutputStream(file);
		
			
        
        /* Merge data-model with template */
        Writer out = new OutputStreamWriter(fop);
        try
		{
			temp.process(root, out);
		} catch (TemplateException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        // Note: Depending on what `out` is, you may need to call `out.close()`.
        // This is usually the case for file output, but not for servlet output.
        
        /* Execute pdflatex on the output */
        try {
        String s = null;
        
        
        /* This executes pdflatex with the output directory of our translation on the latex file we've just written */
        System.out.println("Generating PDF...");
//        Process p = Runtime.getRuntime().exec("/usr/bin/pdflatex -output-directory /home/matt/Uni/Translation/test/output/ /home/matt/Uni/Translation/test/output/latexTest.tex");
        Process p = Runtime.getRuntime().exec("/usr/bin/pdflatex -output-directory /home/matt/Documents/Translation/test/output/ /home/matt/Documents/Translation/test/output/SafeletApp.tex");

        
        BufferedReader stdInput = new BufferedReader(new
        InputStreamReader(p.getInputStream()));

        BufferedReader stdError = new BufferedReader(new
        InputStreamReader(p.getErrorStream()));

        // read the output from the command
        System.out.println("Here is the standard output of the command:\n");
        while ((s = stdInput.readLine()) != null) {
        System.out.println(s);
        }

        // read any errors from the attempted command
        System.out.println("Here is the standard error of the command (if any):\n");
        while ((s = stdError.readLine()) != null) 
        {
        	System.out.println(s);
        }

   

		
	
		
		// Level2Translator circus_trans = new Level2Translator();
		//
		// circus_trans.setTarget((Target)t);
		//
		//
		//
		//
		// circus_trans.setOutput(new Output());
		//
		// circus_trans.execute(new SCJApplication(ANALYSIS));
		//
		// PostProcessor post = new PostProcessor();
		//
		// post.execute();

		System.exit(0);
		
        } catch (Exception e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            System.exit(-1);
        }
	}
}

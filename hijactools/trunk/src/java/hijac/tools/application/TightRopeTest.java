package hijac.tools.application;

import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;

import hijac.tools.analysis.AnalysisError;
import hijac.tools.analysis.SCJAnalysis;
import hijac.tools.analysis.TaskProcessor;
import hijac.tools.analysis.SCJDataKey;
import hijac.tools.analysis.AnalysisTask;
import hijac.tools.analysis.tasks.MethodCallDepTask;
import hijac.tools.collections.RelationImpl;
import hijac.tools.compiler.SCJCompilationConfig;
import hijac.tools.compiler.SCJCompilationException;
import hijac.tools.compiler.SCJCompilationTask;
import hijac.tools.config.Hijac;
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

import hijac.tools.modelgen.Output;
import hijac.tools.modelgen.Target;
import hijac.tools.modelgen.circus.PostProcessor;
import hijac.tools.modelgen.circus.SCJApplication;
import hijac.tools.modelgen.circus.datamodel.ClassModel;
import hijac.tools.modelgen.circus.datamodel.SafeletModel;
import hijac.tools.modelgen.circus.translators.CircusTranslator;
import hijac.tools.modelgen.circus.translators.SafeletTranslator;
import hijac.tools.modelgen.circus.visitors.AMethodVisitor;
import hijac.tools.modelgen.targets.ClassTarget;
import hijac.tools.scjmsafe.translation.*;
import hijac.tools.scjmsafe.checker.*;
import hijac.tools.tightrope.environments.EnvironmentBuilder;
import hijac.tools.tightrope.environments.ProgramEnv;
import hijac.tools.tightrope.translators.Level2Translator;
import hijac.tools.tightrope.visitors.ReturnVisitor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import javax.lang.model.element.TypeElement;

import javax.lang.model.type.TypeMirror;

import com.sun.source.tree.AnnotatedTypeTree;
import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.ArrayAccessTree;
import com.sun.source.tree.ArrayTypeTree;
import com.sun.source.tree.AssertTree;
import com.sun.source.tree.AssignmentTree;
import com.sun.source.tree.BinaryTree;
import com.sun.source.tree.BlockTree;
import com.sun.source.tree.BreakTree;
import com.sun.source.tree.CaseTree;
import com.sun.source.tree.CatchTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.CompoundAssignmentTree;
import com.sun.source.tree.ConditionalExpressionTree;
import com.sun.source.tree.ContinueTree;
import com.sun.source.tree.DoWhileLoopTree;
import com.sun.source.tree.EmptyStatementTree;
import com.sun.source.tree.EnhancedForLoopTree;
import com.sun.source.tree.ErroneousTree;
import com.sun.source.tree.ExpressionStatementTree;
import com.sun.source.tree.ForLoopTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.IfTree;
import com.sun.source.tree.ImportTree;
import com.sun.source.tree.InstanceOfTree;
import com.sun.source.tree.IntersectionTypeTree;
import com.sun.source.tree.LabeledStatementTree;
import com.sun.source.tree.LambdaExpressionTree;
import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.MemberReferenceTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.ModifiersTree;
import com.sun.source.tree.NewArrayTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.ParameterizedTypeTree;
import com.sun.source.tree.ParenthesizedTree;
import com.sun.source.tree.PrimitiveTypeTree;
import com.sun.source.tree.ReturnTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.SwitchTree;
import com.sun.source.tree.SynchronizedTree;
import com.sun.source.tree.ThrowTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.TreeVisitor;

import com.sun.source.tree.TryTree;
import com.sun.source.tree.TypeCastTree;
import com.sun.source.tree.TypeParameterTree;
import com.sun.source.tree.UnaryTree;
import com.sun.source.tree.UnionTypeTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.tree.WhileLoopTree;
import com.sun.source.tree.WildcardTree;
import com.sun.source.util.SimpleTreeVisitor;
import com.sun.source.util.Trees;
import com.sun.tools.javac.tree.JCTree;

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
		final Calendar startTimeCal = Calendar.getInstance();
		
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

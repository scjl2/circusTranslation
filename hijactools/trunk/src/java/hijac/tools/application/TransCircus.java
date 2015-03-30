package hijac.tools.application;

import freemarker.template.TemplateException;

import hijac.tools.analysis.AnalysisError;
import hijac.tools.analysis.SCJAnalysis;
import hijac.tools.compiler.SCJCompilationConfig;
import hijac.tools.compiler.SCJCompilationException;
import hijac.tools.compiler.SCJCompilationTask;
import hijac.tools.config.Statics;
import hijac.tools.modelgen.Output;
import hijac.tools.modelgen.Translator;
import hijac.tools.modelgen.circus.PostProcessor;
import hijac.tools.modelgen.circus.SCJApplication;
import hijac.tools.modelgen.circus.analysis.CircusAnalyser;
import hijac.tools.modelgen.circus.translators.CircusTranslator;

import java.io.IOException;

public class TransCircus {
   public static SCJAnalysis ANALYSIS;

   static {
      Statics.kickstart();
   }

   protected static void setUncaughtExceptionHandler() {
      Thread.currentThread().setUncaughtExceptionHandler(
         new UncaughtExceptionHandler());
   }

   /* Main Program */

   public static void main(String[] args) throws IOException {
      setUncaughtExceptionHandler();

      SCJCompilationTask compiler =
         new SCJCompilationTask(SCJCompilationConfig.getDefault());

      System.out.println("Compiling SCJ sources...");

      try {
         ANALYSIS = SCJAnalysis.create(compiler);
      }
      catch(SCJCompilationException e) {
    	  System.out.println("Failed to compile sources...");
         e.getDiagnostics().print(System.out);
         System.exit(-1);
      }

      System.out.println("Analyising SCJ sources...");

      CircusAnalyser analyser = new CircusAnalyser();

      if (analyser.invoke(ANALYSIS)) {
         System.out.println("All tasks succeeded.");
      }
      else {
         System.out.println("Task execution failed.");
         for (AnalysisError error : analyser.getErrors()) {
            System.out.println(error);
         }
         System.exit(-1);
      }

      CircusTranslator circus_trans = new CircusTranslator();

      circus_trans.setOutput(new Output());

      circus_trans.execute(new SCJApplication(ANALYSIS));

      PostProcessor post = new PostProcessor();

      post.execute();

      System.exit(0);
   }
}

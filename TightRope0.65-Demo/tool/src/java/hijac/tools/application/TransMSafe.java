package hijac.tools.application;

import freemarker.template.TemplateException;

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
import hijac.tools.config.Statics;

import java.io.IOException;

import hijac.tools.scjmsafe.translation.*;
import hijac.tools.scjmsafe.checker.*;

import java.util.Set;
import java.util.HashSet;

public class TransMSafe {
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
         e.getDiagnostics().print(System.out);
         System.exit(-1);
      }

      SCJmSafeTranslator translation = new SCJmSafeTranslator(ANALYSIS);

      Set<AnalysisTask> tasks = new HashSet<AnalysisTask>(0);
      tasks.add(new MethodCallDepTask());
      TaskProcessor taskProcessor = new TaskProcessor(tasks);
      taskProcessor.invoke(ANALYSIS);
      SCJDataKey[] keys = taskProcessor.generates();
      RelationImpl methodDeps = null;
      for (SCJDataKey dk : keys) {
          if (dk.toString().equals("hijac.MethodCallDep")) {
              methodDeps = ANALYSIS.get(dk);
          }
      }

      SCJmSafeChecker checker = new SCJmSafeChecker(translation.getTranslatedProgram(), methodDeps);

      System.exit(0);
   }
}

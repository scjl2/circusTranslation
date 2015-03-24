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

import hijac.tools.application.UncaughtExceptionHandler;

import java.io.IOException;

import hijac.tools.modelgen.Output;
import hijac.tools.modelgen.circus.PostProcessor;
import hijac.tools.modelgen.circus.SCJApplication;
import hijac.tools.modelgen.circus.translators.CircusTranslator;
import hijac.tools.modelgen.circus.translators.SafeletTranslator;
import hijac.tools.scjmsafe.translation.*;
import hijac.tools.scjmsafe.checker.*;
import hijac.tools.tightrope.translators.Level2Translator;

import java.util.Set;
import java.util.HashSet;

import javax.lang.model.element.TypeElement;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.Trees;

public class TightRopeTest {
   public static SCJAnalysis ANALYSIS;

   private static Trees trees;
   private static Set<CompilationUnitTree> units;
   private static Set<TypeElement> type_elements;

   
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

     
      trees = ANALYSIS.TREES;
      units = ANALYSIS.getCompilationUnits();
      type_elements = ANALYSIS.getTypeElements();
      
     
      
//      for(TypeElement elem : type_elements) 
//      {
////    	  System.out.println(elem.toString());
//    	  String elemID = elem.toString();
//    	  if (elemID.equalsIgnoreCase("accs.ACCMission"))
//    	  {
//    		  ClassTree tree = trees.getTree(elem);
//    		  System.out.println(tree.toString());
//    		  
////    		  SafeletTranslator st  = new SafeletTranslator();
////    		//  st.setTarget(elem);
////    		  st.invoke();
//    		  
//    		 
////              return super.visitClass(tree, p);
//    		  
////    		  
//    	  }
//    	  
//      }
      
      Level2Translator circus_trans = new Level2Translator();

      circus_trans.setOutput(new Output());

      circus_trans.execute(new SCJApplication(ANALYSIS));

      PostProcessor post = new PostProcessor();

      post.execute();

            
    
      
      System.exit(0);
   }
}

package hijac.tools.compiler;

import hijac.tools.utils.FileUtils;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.TaskListener;
import com.sun.source.util.Trees;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import javax.annotation.processing.Processor;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import javax.tools.JavaFileObject;
import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

/**
 * @author Frank Zeyda
 * @version $Revision: 230 $
 */
public class SCJCompilationTask extends JavacTask {
   protected final JavaCompiler
      COMPILER = ToolProvider.getSystemJavaCompiler();

   protected final StandardJavaFileManager
      FILE_MANAGER = COMPILER.getStandardFileManager(null, null, null);

   protected final JavacDiagnostics
      DIAGNOSTICS = new JavacDiagnostics();

   protected final JavacTask task;

   /* Configuration of the compilation task. */
   protected final SCJCompilationConfig config;

   public SCJCompilationTask(SCJCompilationConfig config) {
      this.config = config;
      task = createTask();
   }

   protected Collection<String> createOptions() {
      Collection<String> options = new ArrayList<String>();
      String SEP = System.getProperty("path.separator");
      options.add("-classpath");
      options.add(FileUtils.fileListToString(config.getClassPath(), SEP));
      options.add("-sourcepath");
      options.add(FileUtils.fileListToString(config.getSourcePath(), SEP));
      return options;
   }

   protected JavacTask createTask() {
      return (JavacTask) COMPILER.getTask(
         null, FILE_MANAGER, DIAGNOSTICS, createOptions(), null,
         FILE_MANAGER.getJavaFileObjectsFromFiles(config.getSourceFiles()));
   }

   /* Implementation of JavaCompiler.CompilerTask. */

   public Boolean call() {
      DIAGNOSTICS.reset();
      return task.call();
   }

   public void setLocale(Locale locale) {
      task.setLocale(locale);
   }

   public void setProcessors(Iterable<? extends Processor> processors) {
      task.setProcessors(processors);
   }

   public void addTaskListener(TaskListener listener) {
      task.addTaskListener(listener);
   }

   public void removeTaskListener(TaskListener listener) {
      task.removeTaskListener(listener);
   }

   /* Implementation of JavacTask. */

   public Iterable<? extends CompilationUnitTree> parse() throws IOException {
      DIAGNOSTICS.reset();
      return task.parse();
   }

   public Iterable<? extends Element> analyze() throws IOException {
      DIAGNOSTICS.reset();
      return task.analyze();
   }

   /* Maybe support the following operation as well. */

   public Iterable<? extends JavaFileObject> generate() throws IOException {
      throw new UnsupportedOperationException();
   }

   public Elements getElements() {
      return task.getElements();
   }

   public Types getTypes() {
      return task.getTypes();
   }

   public TypeMirror getTypeMirror(Iterable<? extends Tree> path) {
      return task.getTypeMirror(path);
   }

   public void setTaskListener(TaskListener taskListener) {
      task.setTaskListener(taskListener);
   }

   /* Supplementary Methods. */

   public Trees getTrees() {
      return Trees.instance(task);
   }

   public boolean success() {
      return !DIAGNOSTICS.hasError();
   }

   public void raiseError() throws SCJCompilationException {
      if (!success()) {
         throw new SCJCompilationException(this);
      }
   }

   public JavacDiagnostics getDiagnostics() {
      return DIAGNOSTICS;
   }


}

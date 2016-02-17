package hijac.tools.analysis.tasks;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.Tree;

import hijac.tools.analysis.AbstractAnalysisTask;
import hijac.tools.analysis.SCJAnalysis;
import hijac.tools.analysis.SCJDataKey;
import hijac.tools.collections.Relation;
import hijac.tools.collections.RelationFactory;
import hijac.tools.collections.Relations;
import hijac.tools.comparators.Comparators;
import hijac.tools.config.Hijac;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

/**
 * @author Frank Zeyda
 * @version $Revision: 198 $
 */
public class MethodCallDepTask extends AbstractAnalysisTask {
   public static final SCJDataKey KEY = Hijac.key("MethodCallDep");
   public static final SCJDataKey[] REQUIRES = { };
   public static final SCJDataKey[] GENERATES = { KEY };

   public MethodCallDepTask() {
      super("MethodCallDepTask", REQUIRES, GENERATES);
   }

   public void doTask() {
      Relation<ExecutableElement, ExecutableElement> call_dep =
         RelationFactory.newRelation(Comparators.ExecutableElement);
      MethodCallDepVisitor visitor = new MethodCallDepVisitor(ANALYSIS);
      for (CompilationUnitTree unit : ANALYSIS.getCompilationUnits()) {
         visitor.reset();
         visitor.scan(unit, call_dep);
      }
      /* None of the following work; I am slightly puzzled by this. */
      /*for (CompilationUnitTree unit : ANALYSIS.getCompilationUnits()) {
         visitor.reset();
         unit.accept(visitor, call_dep);
      }*/
      /*for (TypeElement e : ANALYSIS.getTypeElements()) {
         Tree tree = ANALYSIS.TREES.getTree(e);
         visitor.reset();
         visitor.scan(tree, call_dep);
      }*/
      /*for (TypeElement e : ANALYSIS.getTypeElements()) {
         visitor.reset();
         Tree tree = ANALYSIS.TREES.getTree(e);
         tree.accept(visitor, call_dep);
      }*/
      /*System.out.println("[Call Relation]");
      System.out.println(call_dep);*/
      //System.out.println(call_dep.size() + " maplets before closure.");
      call_dep = Relations.closure(call_dep);
      //System.out.println(call_dep.size() + " maplets after closure.");
      ANALYSIS.put(KEY, call_dep);
   }
}

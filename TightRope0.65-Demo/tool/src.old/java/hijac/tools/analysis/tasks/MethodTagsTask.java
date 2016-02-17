package hijac.tools.analysis.tasks;

import com.sun.source.tree.CompilationUnitTree;

import hijac.tools.analysis.AbstractAnalysisTask;
import hijac.tools.analysis.SCJDataKey;
import hijac.tools.analysis.SCJMethodTag;
import hijac.tools.collections.Relation;
import hijac.tools.config.Hijac;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.ExecutableElement;

/* For now we do not record tags for static and instance initialisers. */

/**
 * @author Frank Zeyda
 * @version $Revision: 198 $
 */
public class MethodTagsTask extends AbstractAnalysisTask {
   public static final SCJDataKey KEY1 = Hijac.key("MethodTags");
   public static final SCJDataKey KEY2 = Hijac.key("Methods");
   public static final SCJDataKey[] REQUIRES = { MethodCallDepTask.KEY };
   public static final SCJDataKey[] GENERATES = { KEY1, KEY2 };

   public MethodTagsTask() {
      super("MethodTagsTask", REQUIRES, GENERATES);
   }

   public void doTask() {
      Map<ExecutableElement, SCJMethodTag>
         method_tags = new HashMap<ExecutableElement, SCJMethodTag>();
      for (CompilationUnitTree unit : ANALYSIS.getCompilationUnits()) {
         MethodTagsVisitor visitor = new MethodTagsVisitor(ANALYSIS);
         visitor.scan(unit, method_tags);
      }
      Set<ExecutableElement> methods = method_tags.keySet();
      /* Augment method tags with transitive dependencies. */
      Relation<ExecutableElement, ExecutableElement> call_rel =
         ANALYSIS.get(MethodCallDepTask.KEY);
      for (ExecutableElement element : methods) {
         SCJMethodTag method_tag = method_tags.get(element);
         for (ExecutableElement callee : call_rel.image(element)) {
            assert method_tags.containsKey(callee);
            SCJMethodTag callee_tag = method_tags.get(callee);
            method_tag.getCallDomains().add(callee_tag.getDomain());
            if (callee_tag.allocates()) { method_tag.setAllocates(); }
            if (callee_tag.maySuspend()) { method_tag.setMaySuspend(); }
         }
      }
      ANALYSIS.put(KEY1, method_tags);
      ANALYSIS.put(KEY2, methods);
      /*System.out.println("[All Methods]");
      System.out.println(methods);*/
   }
}

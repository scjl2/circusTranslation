package hijac.tools.analysis.tasks;

import javacutils.TreeUtils;


import com.sun.source.tree.AssertTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.NewArrayTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.SynchronizedTree;

import hijac.tools.analysis.SCJAnalysis;
import hijac.tools.analysis.SCJMethodTag;
import hijac.tools.analysis.utils.SCJTreePathScanner;

import java.util.Map;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;

/**
 * @author Frank Zeyda
 * @version $Revision: 230 $
 */
public class MethodTagsVisitor extends SCJTreePathScanner<
      Map<ExecutableElement, SCJMethodTag>,
      Map<ExecutableElement, SCJMethodTag>> {

   public MethodTagsVisitor(SCJAnalysis analysis) {
      super(analysis);
   }

   @Override
   public Map<ExecutableElement, SCJMethodTag>
      visitMethod(MethodTree node,
         Map<ExecutableElement, SCJMethodTag> method_tags) {
      ExecutableElement method = (ExecutableElement) getElement();
      if (!method_tags.containsKey(method)) {
         method_tags.put(method, new SCJMethodTag(method));
      }
      return super.visitMethod(node, method_tags);
   }

   @Override
   public Map<ExecutableElement, SCJMethodTag>
      visitMethodInvocation(MethodInvocationTree node,
         Map<ExecutableElement, SCJMethodTag> method_tags) {
      ExecutableElement target = TreeUtils.elementFromUse(node);
      assert target != null;
      if (!method_tags.containsKey(target)) {
         method_tags.put(target, new SCJMethodTag(target));
      }
      ExecutableElement context = getEnclosingMethod();
      /* assert context != null; */
      if (context != null) {
         if (!method_tags.containsKey(context)) {
            method_tags.put(context, new SCJMethodTag(context));
         }
         /* Are we calling a synchronised method? */
         if (target.getModifiers().contains(Modifier.SYNCHRONIZED)) {
            method_tags.get(context).setMaySuspend();
         }
         /* We we calling wait()? */
         String class_name = target.getEnclosingElement().toString();
         String method_name = target.getSimpleName().toString();
         if (class_name.equals("java.lang.Object") &&
            method_name.equals("wait")) {
            method_tags.get(context).setMaySuspend();
         }
      }
      else {
         /* We may be in an initialiser or field initialisation here. */
      }
      return super.visitMethodInvocation(node, method_tags);
   }

   @Override
   public Map<ExecutableElement, SCJMethodTag>
      visitNewClass(NewClassTree node,
         Map<ExecutableElement, SCJMethodTag> method_tags) {
      ExecutableElement target = TreeUtils.elementFromUse(node);
      assert target != null;
      if (!method_tags.containsKey(target)) {
         method_tags.put(target, new SCJMethodTag(target));
      }
      ExecutableElement context = getEnclosingMethod();
      /* assert context != null; */
      if (context != null) {
         if (!method_tags.containsKey(context)) {
            method_tags.put(context, new SCJMethodTag(context));
         }
         method_tags.get(context).setAllocates();
      }
      else {
         /* We may be in an initialiser or field initialisation here. */
      }
      return super.visitNewClass(node, method_tags);
   }

   @Override
   public Map<ExecutableElement, SCJMethodTag>
      visitNewArray(NewArrayTree node,
         Map<ExecutableElement, SCJMethodTag> method_tags) {
      ExecutableElement context = getEnclosingMethod();
      /* assert context != null; */
      if (context != null) {
         if (!method_tags.containsKey(context)) {
            method_tags.put(context, new SCJMethodTag(context));
         }
         method_tags.get(context).setAllocates();
      }
      else {
         /* We may be in an initialiser or field initialisation here. */
      }
      return super.visitNewArray(node, method_tags);
   }

   @Override
   public Map<ExecutableElement, SCJMethodTag>
      visitSynchronized(SynchronizedTree node,
         Map<ExecutableElement, SCJMethodTag> method_tags) {
      ExecutableElement context = getEnclosingMethod();
      /* assert context != null; */
      if (context != null) {
         if (!method_tags.containsKey(context)) {
            method_tags.put(context, new SCJMethodTag(context));
         }
         method_tags.get(context).setMaySuspend();
      }
      else {
         /* We may be in an instance or static initialiser here. */
      }
      return super.visitSynchronized(node, method_tags);
   }

   @Override
   public Map<ExecutableElement, SCJMethodTag>
      visitAssert(AssertTree node,
         Map<ExecutableElement, SCJMethodTag> method_tags) {
      /* Do not traverse assert statements. */
      return method_tags;
   }
}

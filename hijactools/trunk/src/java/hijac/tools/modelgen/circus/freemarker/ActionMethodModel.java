package hijac.tools.modelgen.circus.freemarker;

import hijac.tools.modelgen.circus.SCJApplication;
import hijac.tools.modelgen.circus.visitors.AMethodVisitor;
import hijac.tools.modelgen.circus.visitors.MethodVisitorContext;

/**
 * @author Frank Zeyda
 * @version $Revision: 213 $
 */
public class ActionMethodModel extends
   VisitorMethodModel<MethodVisitorContext> {

   public ActionMethodModel(SCJApplication context) {
      super("ACTION",
         new AMethodVisitor(context),
         new MethodVisitorContext());
   }
}

package hijac.tools.modelgen.circus.freemarker;

import hijac.tools.modelgen.circus.SCJApplication;
import hijac.tools.modelgen.circus.visitors.AMethodVisitor;
/*import hijac.tools.modelgen.circus.visitors.DMethodVisitor;*/
import hijac.tools.modelgen.circus.visitors.MethodVisitorContext;

/**
 * @author Frank Zeyda
 * @version $Revision: 213 $
 */
public class DataOpMethodModel extends
   VisitorMethodModel<MethodVisitorContext> {

   public DataOpMethodModel(SCJApplication context) {
      super("DATAOP",
         new AMethodVisitor(context),
         new MethodVisitorContext());
   }
}

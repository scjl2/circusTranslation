package hijac.tools.modelgen.circus.datamodel;

import hijac.tools.modelgen.circus.SCJApplication;

/**
 * Data model for translating {@code Safelet} application classes.
 *
 * @author Frank Zeyda
 * @version $Revision$
 */
public class SafeletModel extends ActiveClassModel {
   /**
    * Constructs a data model for a {@code Safelet} application class.
    *
    * @param context SCJ application context of the data model.
    * @param target Safelet class for whose translation the data model is
    * used.
    */
   public SafeletModel(SCJApplication context, ClassModel target) {
      super(context, target);
   }
}

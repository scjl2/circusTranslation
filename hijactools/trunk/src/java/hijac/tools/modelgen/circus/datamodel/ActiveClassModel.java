package hijac.tools.modelgen.circus.datamodel;

import hijac.tools.modelgen.circus.SCJApplication;

/**
 * Data model for all active classes of an SCJ application.
 *
 * @author Frank Zeyda
 * @version $Revision: 213 $
 */
public class ActiveClassModel extends DataModel {
   /**
    * Constructs a data model for an active class.
    *
    * @param context SCJ application context of the data model.
    * @param target Class for which the data model is constructed.
    */
   public ActiveClassModel(SCJApplication context, ClassModel target) {
      super(context, target);
   }

   /**
    * Returns the the associated class target.
    *
    * @return Associated class target of the data model.
    */
   public ClassModel getclass() {
      return (ClassModel) TARGET;
   }
}

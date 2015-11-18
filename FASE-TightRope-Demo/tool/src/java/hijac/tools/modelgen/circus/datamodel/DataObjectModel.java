package hijac.tools.modelgen.circus.datamodel;

import hijac.tools.modelgen.circus.SCJApplication;

/**
 * Data model for passive classes (data objects) of an SCJ application.
 *
 * @author Frank Zeyda
 * @version $Revision: 213 $
 */
public class DataObjectModel extends DataModel {
   /**
    * Constructs a data model for a passive class / data object.
    *
    * @param context SCJ application context in which the class resides.
    * @param target Class for which the data model is constructed.
    */
   public DataObjectModel(SCJApplication context, ClassModel target) {
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

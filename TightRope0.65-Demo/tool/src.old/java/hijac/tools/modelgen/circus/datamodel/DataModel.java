package hijac.tools.modelgen.circus.datamodel;

import hijac.tools.modelgen.Target;
import hijac.tools.modelgen.circus.SCJApplication;

import hijac.tools.utils.ModelUtils;

/**
 * Base class of all data models used by the translation templates.
 *
 * @author Frank Zeyda
 * @version $Revision$
 */
public class DataModel {
   /**
    * SCJ application context of the data model.
    */
   protected final SCJApplication CONTEXT;

   /**
    * Translation target of the data model if applicable.
    */
   protected final Target TARGET;

   /**
    * Utility object for working with JDK model elements.
    */
   protected final ModelUtils MODEL;

   /**
    * Constructs a data model for a given SCJ application context.
    *
    * @param context SCJ application context of the data model.
    */
   public DataModel(SCJApplication context) {
      assert context != null;
      CONTEXT = context;
      TARGET = null;
      MODEL = new ModelUtils(CONTEXT);
   }

   /**
    * Constructs a data model for a given SCJ application context and
    * translation target.
    *
    * @param context SCJ application context of the data model.
    * @param target Target for which the data model is constructed.
    */
   public DataModel(SCJApplication context, Target target) {
      assert context != null;
      assert target != null;
      CONTEXT = context;
      TARGET = target;
      MODEL = new ModelUtils(CONTEXT);
   }

   /**
    * Returns the SCJ application context of the data model.
    *
    * @return SCJ application context of the data model.
    */
   public SCJApplication getContext() {
      return CONTEXT;
   }

   /**
    * Returns the utility objects for JDK model elements.
    *
    * @return Utility objects for JDK model elements.
    */
   public ModelUtils getModelUtils() {
      return MODEL;
   }

   /**
    * Determines whether this data model has a translation target associated
    * with it.
    *
    * @return True if the data model has a translation target.
    */
   public boolean hasTarget() {
      return TARGET != null;
   }

   /**
    * Returns the translation {@link Target} for which the data model was
    * constructed, if applicable.
    *
    * @return Translation target of the data model.
    */
   public Target gettarget() {
      return TARGET;
   }
}

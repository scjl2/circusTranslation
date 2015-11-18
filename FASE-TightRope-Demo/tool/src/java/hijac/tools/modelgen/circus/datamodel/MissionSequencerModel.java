package hijac.tools.modelgen.circus.datamodel;

import hijac.tools.modelgen.circus.SCJApplication;

/**
 * Data model for translating {@code MissionSequencer} application classes.
 *
 * @author Frank Zeyda
 * @version $Revision: 213 $
 */
public class MissionSequencerModel extends ActiveClassModel {
   /**
    * Constructs a data model for a {@code MissionSequencer} class.
    *
    * @param context SCJ application context of the data model.
    * @param target Mission sequencer class for whose translation the data
    * model is used.
    */
   public MissionSequencerModel(SCJApplication context, ClassModel target) {
      super(context, target);
   }
}

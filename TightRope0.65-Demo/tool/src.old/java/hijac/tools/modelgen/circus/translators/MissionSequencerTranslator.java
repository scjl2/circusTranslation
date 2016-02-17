package hijac.tools.modelgen.circus.translators;

import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
/*import freemarker.template.TemplateException;*/

import hijac.tools.config.Hijac;
import hijac.tools.modelgen.AbstractTranslator;
import hijac.tools.modelgen.circus.SCJApplication;
import hijac.tools.modelgen.circus.templates.CircusObjectWrapper;
import hijac.tools.modelgen.circus.datamodel.ClassModel;
import hijac.tools.modelgen.circus.datamodel.MissionSequencerModel;
import hijac.tools.modelgen.targets.ClassTarget;

import javax.lang.model.type.TypeMirror;

/**
 * @author Frank Zeyda
 * @version $Revision: 213 $
 */
class MissionSequencerTranslator extends AbstractTranslator<SCJApplication> {
   public MissionSequencerTranslator() {
      super("MissionSequencerTranslator");
   }

   @Override
   public boolean isApplicable() {
      if (TARGET instanceof ClassTarget) {
         ClassTarget class_target = (ClassTarget) TARGET;
         if (!class_target.isAPIClass()) {
            TypeMirror safelet_type = (TypeMirror)
               ANALYSIS.get(Hijac.key("MissionSequencerType"));
            return ANALYSIS.TYPES.isSubtype(
               class_target.getTypeElement().asType(), safelet_type);
         }
      }
      return false;
   }

   @Override
   public void execute() {
      ClassTarget class_target = (ClassTarget) TARGET;
      ClassModel class_model =
         new ClassModel(CONTEXT, class_target.getTypeElement());
      MissionSequencerModel sequencer_model =
         new MissionSequencerModel(CONTEXT, class_model);
      write(CONTEXT.TEMPLATES.MISSION_SEQUENCER, sequencer_model,
         class_model.getSectionName() + "App" + ".circus");
   }
}

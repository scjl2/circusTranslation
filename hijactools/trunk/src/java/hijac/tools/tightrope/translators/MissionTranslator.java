package hijac.tools.tightrope.translators;

import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
/*import freemarker.template.TemplateException;*/

import hijac.tools.config.Hijac;
import hijac.tools.modelgen.AbstractTranslator;
import hijac.tools.modelgen.circus.SCJApplication;
import hijac.tools.modelgen.circus.templates.CircusObjectWrapper;
import hijac.tools.modelgen.circus.datamodel.ClassModel;
import hijac.tools.modelgen.circus.datamodel.MissionModel;
import hijac.tools.modelgen.targets.ClassTarget;

import javax.lang.model.type.TypeMirror;

/**
 * @author Frank Zeyda
 * @version $Revision: 213 $
 */
class MissionTranslator extends AbstractTranslator<SCJApplication> {
   public MissionTranslator() {
      super("MissionTranslator");
   }

   @Override
   public boolean isApplicable() {
      if (TARGET instanceof ClassTarget) {
         ClassTarget class_target = (ClassTarget) TARGET;
         if (!class_target.isAPIClass()) {
            TypeMirror mission_type = (TypeMirror)
               ANALYSIS.get(Hijac.key("MissionType"));
            return ANALYSIS.TYPES.isSubtype(
               class_target.getTypeElement().asType(), mission_type);
         }
      }
      return false;
   }

   @Override
   public void execute() {
      ClassTarget class_target = (ClassTarget) TARGET;
      ClassModel class_model =
         new ClassModel(CONTEXT, class_target.getTypeElement());
      MissionModel mission_model =
         new MissionModel(CONTEXT, class_model);
      write(CONTEXT.TEMPLATES.MISSION, mission_model,
         class_model.getSectionName() + "App" + ".circus");
   }
}

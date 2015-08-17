package hijac.tools.tightrope.translators;

import hijac.tools.config.Hijac;
import hijac.tools.modelgen.AbstractTranslator;
import hijac.tools.modelgen.circus.SCJApplication;
import hijac.tools.modelgen.circus.datamodel.ClassModel;
import hijac.tools.modelgen.circus.datamodel.HandlerModel;
import hijac.tools.modelgen.targets.ClassTarget;

import javax.lang.model.type.TypeMirror;
/*import freemarker.template.TemplateException;*/

/**
 * @author Frank Zeyda
 * @version $Revision: 207 $
 */
class AperiodicHandlerTranslator extends AbstractTranslator<SCJApplication> {
   public AperiodicHandlerTranslator() {
      super("AperiodicHandlerTranslator");
   }

   @Override
   public boolean isApplicable() {
      if (TARGET instanceof ClassTarget) {
         ClassTarget class_target = (ClassTarget) TARGET;
         if (!class_target.isAPIClass()) {
            TypeMirror handler_type1 = (TypeMirror)
                ANALYSIS.get(Hijac.key("AperiodicEventHandlerType"));
            TypeMirror handler_type2 = (TypeMirror)
                ANALYSIS.get(Hijac.key("AperiodicLongEventHandlerType"));
            return ANALYSIS.TYPES.isSubtype(
               class_target.getTypeElement().asType(), handler_type1) ||
                   ANALYSIS.TYPES.isSubtype(
               class_target.getTypeElement().asType(), handler_type2);
         }
      }
      return false;
   }

   @Override
   public void execute() {
      ClassTarget class_target = (ClassTarget) TARGET;
      ClassModel class_model =
         new ClassModel(CONTEXT, class_target.getTypeElement());
      HandlerModel handler_model =
         new HandlerModel(CONTEXT, class_model);
      write(CONTEXT.TEMPLATES.APERIODIC_HANDLER, handler_model,
         class_model.getSectionName() + "App" + ".circus");
   }
}

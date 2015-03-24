package hijac.tools.tightrope.translators;

import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
/*import freemarker.template.TemplateException;*/

import hijac.tools.config.Hijac;
import hijac.tools.modelgen.AbstractTranslator;
import hijac.tools.modelgen.circus.SCJApplication;
import hijac.tools.modelgen.circus.templates.CircusObjectWrapper;
import hijac.tools.modelgen.circus.datamodel.ClassModel;
import hijac.tools.modelgen.circus.datamodel.HandlerModel;
import hijac.tools.modelgen.targets.ClassTarget;

import javax.lang.model.type.TypeMirror;

/**
 * @author Frank Zeyda
 * @version $Revision: 207 $
 */
class PeriodicHandlerTranslator extends AbstractTranslator<SCJApplication> {
   public PeriodicHandlerTranslator() {
      super("PeriodicHandlerTranslator");
   }

   @Override
   public boolean isApplicable() {
      if (TARGET instanceof ClassTarget) {
         ClassTarget class_target = (ClassTarget) TARGET;
         if (!class_target.isAPIClass()) {
            TypeMirror handler_type = (TypeMirror)
                ANALYSIS.get(Hijac.key("PeriodicEventHandlerType"));
            return ANALYSIS.TYPES.isSubtype(
               class_target.getTypeElement().asType(), handler_type);
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
      write(CONTEXT.TEMPLATES.PERIODIC_HANDLER, handler_model,
         class_model.getSectionName() + "App" + ".circus");
   }
}

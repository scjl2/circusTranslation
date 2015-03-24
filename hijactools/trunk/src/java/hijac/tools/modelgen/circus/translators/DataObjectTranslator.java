package hijac.tools.modelgen.circus.translators;

import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
/*import freemarker.template.TemplateException;*/

import hijac.tools.config.Hijac;
import hijac.tools.modelgen.AbstractTranslator;
import hijac.tools.modelgen.circus.SCJApplication;
import hijac.tools.modelgen.circus.datamodel.ClassModel;
import hijac.tools.modelgen.circus.datamodel.DataObjectModel;
import hijac.tools.modelgen.circus.templates.CircusObjectWrapper;
import hijac.tools.modelgen.targets.ClassTarget;

import javax.lang.model.type.TypeMirror;

/**
 * @author Frank Zeyda
 * @version $Revision: 213 $
 */
class DataObjectTranslator extends AbstractTranslator<SCJApplication> {
   public DataObjectTranslator() {
      super("DataObjectTranslator");
   }

   @Override
   public boolean isApplicable() {
      if (TARGET instanceof ClassTarget) {
         ClassTarget class_target = (ClassTarget) TARGET;
         ClassModel class_model =
            new ClassModel(CONTEXT, class_target.getTypeElement());
         return !class_target.isAPIClass() &&
            !class_model.isInteractionClass() &&
            (class_model.hasFields() || class_model.hasDataOperations());
      }
      return false;
   }

   @Override
   public void execute() {
      ClassTarget class_target = (ClassTarget) TARGET;
      ClassModel class_model =
         new ClassModel(CONTEXT, class_target.getTypeElement());
      DataObjectModel data_object_model =
         new DataObjectModel(CONTEXT, class_model);
      write(CONTEXT.TEMPLATES.DATA_OBJECT, data_object_model,
         class_model.getSectionName() + "Class" + ".circus");
   }
}

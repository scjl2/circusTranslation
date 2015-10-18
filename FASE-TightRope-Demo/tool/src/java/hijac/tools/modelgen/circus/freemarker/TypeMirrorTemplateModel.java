package hijac.tools.modelgen.circus.freemarker;

import freemarker.template.AdapterTemplateModel;
import freemarker.template.TemplateScalarModel;

import hijac.tools.modelgen.circus.utils.TransUtils;

import javax.lang.model.type.TypeMirror;

/**
 * Template model for Tree objects of the JDK Compiler API.
 *
 * @author Frank Zeyda
 * @version $Revision$
 */
public class TypeMirrorTemplateModel implements AdapterTemplateModel,
      TemplateScalarModel {
   protected final TypeMirror type;

   public TypeMirrorTemplateModel(TypeMirror type) {
      assert type != null;
      this.type = type;
   }

   @Override
   public Object getAdaptedObject(Class c) {
      return type;
   }

   @Override
   public String getAsString() {
      return TransUtils.encodeType(type);
   }
}

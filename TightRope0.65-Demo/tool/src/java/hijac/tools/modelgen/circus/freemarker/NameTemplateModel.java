package hijac.tools.modelgen.circus.freemarker;

import freemarker.template.AdapterTemplateModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;

import hijac.tools.modelgen.circus.utils.TransUtils;

import javax.lang.model.element.Name;

/**
 * Template model for Name objects of the JDK model API.
 *
 * @author Frank Zeyda
 * @version $Revision$
 */
public class NameTemplateModel implements AdapterTemplateModel,
      TemplateScalarModel {
   protected final Name name;

   public NameTemplateModel(Name name) {
      assert name != null;
      this.name = name;
   }

   @Override
   public Object getAdaptedObject(Class c) {
      return name;
   }

   @Override
   public String getAsString() {
      return TransUtils.encodeName(name);
   }
}

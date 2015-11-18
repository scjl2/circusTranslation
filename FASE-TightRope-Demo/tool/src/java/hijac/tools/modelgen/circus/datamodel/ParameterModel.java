package hijac.tools.modelgen.circus.datamodel;

import hijac.tools.modelgen.circus.SCJApplication;
import hijac.tools.modelgen.circus.freemarker.NameTemplateModel;
import hijac.tools.modelgen.circus.utils.TransUtils;

import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * Data model for the parameter of a method.
 *
 * @author Frank Zeyda
 * @version $Revision$
 */
public class ParameterModel extends DataModel {
   /**
    * JDK model element representing the parameter in the data model.
    */
   public final VariableElement param;

   public ParameterModel(SCJApplication context, VariableElement param) {
      super(context);
      assert param != null;
      this.param = param;
   }

   /**
    * Returns the name of the parameter.
    *
    * @return Name of the parameter as a {@link Name} object.
    */
   public Name getName() {
      return param.getSimpleName();
   }

   /**
    * Returns the type of the parameter.
    *
    * @return Type of the parameter as a {@link TypeMirror} object.
    */
   public TypeMirror getType() {
      return param.asType();
   }

   /**
    * <p>Returns a string encoding of the parameter, escaping symbols as
    * necessary to ensure compatibility with Z LaTeX mark-up.</p>
    *
    * <p>This method suitably escapes symbols such as underscores so
    * that they can be parsed by the <i>Circus</i> parser of CZT.</p>
    *
    * @return String representation of the field that can be used directly
    * in the generated <i>Circus</i> model.
    */
   public String toString() {
      return new NameTemplateModel(getName()).getAsString();
   }
}

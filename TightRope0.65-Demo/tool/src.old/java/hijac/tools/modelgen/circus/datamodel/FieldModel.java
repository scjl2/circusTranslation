package hijac.tools.modelgen.circus.datamodel;

import hijac.tools.modelgen.circus.SCJApplication;
import hijac.tools.modelgen.circus.freemarker.NameTemplateModel;
import hijac.tools.modelgen.circus.utils.TransUtils;

import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * Data model for a class field exposed within the templates.
 *
 * @author Frank Zeyda
 * @version $Revision$
 */
public class FieldModel extends DataModel {
   /**
    * JDK model element representing the field in the data model.
    */
   public final VariableElement field;

   /**
    * Constructs a data model for a class field.
    *
    * @param context SCJ application context of the data model.
    * @param field JDK model element representing the field.
    */
   public FieldModel(SCJApplication context, VariableElement field) {
      super(context);
      assert field != null;
      this.field = field;
   }

   /**
    * Returns the name of the field.
    *
    * @return Simple name of the field as a {@link Name} object.
    */
   public Name getName() {
      return field.getSimpleName();
   }

   /**
    * Returns the type of the field.
    *
    * @return Type of the field as a {@link TypeMirror} object.
    */
   public TypeMirror getType() {
      return field.asType();
   }

   /**
    * Returns the modifiers of the field.
    *
    * @return Modifiers of the field as a {@link ModifiersModel} data model.
    */
   public ModifiersModel getModifiers() {
      return new ModifiersModel(CONTEXT, field.getModifiers());
   }

   /**
    * <p>Returns a string encoding of the field, escaping symbols as
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
